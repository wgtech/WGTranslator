package project.wgtech.wgtranslator.repository

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import project.wgtech.wgtranslator.R
import project.wgtech.wgtranslator.Util
import project.wgtech.wgtranslator.di.IoDispatcher
import project.wgtech.wgtranslator.model.Language
import project.wgtech.wgtranslator.model.Status
import project.wgtech.wgtranslator.model.StatusCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor (
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val util: Util,
        private val languageModels : ArrayList<Language>,
        private val modelManager: RemoteModelManager,
        private val downloadConditions: DownloadConditions
) {

    var initializedStatus = Status(StatusCode.INITIALIZE, null, "", "")

    fun getLanguageModels() = languageModels

    @ExperimentalCoroutinesApi
    fun flowInitializeAllModels() : Flow<Status> = callbackFlow { // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/callback-flow.html

        val booleans = mutableListOf<Boolean>().apply {
            for (i in 0 until languageModels.size) { add(false) }
        }

        for (i in 0 until languageModels.size) {
            val lang = languageModels[i]
            val model = TranslateRemoteModel.Builder(lang.code).build()
            modelManager.isModelDownloaded(model)
                    .addOnCompleteListener { res ->
                        val checkResult = res.result
                        util.logd("isModelDownloaded? ${lang.code} $checkResult")

                        if (checkResult == false) {
                            runBlocking {
                                sendBlocking(Status(StatusCode.DOWNLOAD_MODEL, null,
                                    "${util.getString(R.string.message_now_download)} (${lang.code})",
                                    null))
                                modelManager.download(model, downloadConditions)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                util.logd("Download queue: ${lang.code} ... Done")
                                                sendBlocking(Status(StatusCode.DOWNLOAD_COMPLETE_MODEL, null,
                                                        "${util.getString(R.string.message_download_complete)} (${lang.code})",
                                                        null))

                                                if (booleans.all { true }) {
                                                    sendBlocking(Status(StatusCode.INITIALIZE_COMPLETE_MODEL, null,
                                                            util.getString(R.string.message_initialize_complete),
                                                            null))
                                                }
                                            }
                                        }
                                        .addOnFailureListener {
                                            sendBlocking(Status(StatusCode.DOWNLOAD_FAILURE_MODEL, null,
                                                "${util.getString(R.string.message_download_fail)} (${lang.code})",
                                                null))
                                            util.loge("Here! because of ${it.message}")
                                            it.printStackTrace()
                                        }
                            }

                        } else if (i == languageModels.size - 1 && booleans.all { true }) {
                            sendBlocking(Status(StatusCode.INITIALIZE_COMPLETE_MODEL, null,
                                    util.getString(R.string.message_initialize_complete),
                                    null))
                        }
                    }
        }

        awaitClose { }
    }


    fun getTranslateData(sourceLanguage: Language, targetLanguage: Language, targetText: String) : String? {

        var result : String? = ""
        runBlocking {
            withContext(ioDispatcher) {
                Translation.getClient(TranslatorOptions.Builder()
                        .setSourceLanguage(sourceLanguage.code)
                        .setTargetLanguage(targetLanguage.code)
                        .build())
                    .translate(targetText)
                    .addOnSuccessListener {
                        result = it
                        util.logd("provideTranslateData (addOnSuccessListener) --> $result")
                    }
                    .addOnFailureListener {
                        result = it.message
                        util.loge("provideTranslateData (addOnFailureListener) --> $result")
                    }
            }
        }
        util.logd("result= $result")
        return result
    }
}