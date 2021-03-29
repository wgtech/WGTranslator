package project.wgtech.wgtranslator.repository

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.*
import project.wgtech.wgtranslator.Util
import project.wgtech.wgtranslator.model.Language
import project.wgtech.wgtranslator.model.Status
import project.wgtech.wgtranslator.model.StatusCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor (
    private val util: Util,
    private val languageModels : ArrayList<Language>,
    private val modelManager: RemoteModelManager,
    private val downloadConditions: DownloadConditions
) {

    var initializedStatus = Status(StatusCode.INITIALIZE, null, "", "")

    fun getLanguageModels() = languageModels

    fun initializeAllModels() : Status {
        var result = initializedStatus

        for (i in 0 until languageModels.size) {
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                .addOnSuccessListener {
                    // TODO : Notify that it is already downloaded to SplashActivity
                }
                .addOnFailureListener {
                    util.logw("Can't find ${languageModels[i].name} model")
                    modelManager.download(TranslateRemoteModel.Builder(languageModels[i].code).build(), downloadConditions)
                        .addOnSuccessListener {
                            util.logd("initializeAllModels: ${languageModels[i].code} ... Done")
                        }
                        .addOnFailureListener {
                            result = Status(StatusCode.DOWNLOAD_FAILURE_MODEL, null, "Initialize failure", "initializeAllModels: ${languageModels[i].code} ... Fail")
                        }
                }

        }

        return result
    }

    fun getTranslateData(sourceLanguage: Language, targetLanguage: Language, targetText: String) : String? {

        var result : String? = ""
        runBlocking {
            withContext(Dispatchers.IO) {
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