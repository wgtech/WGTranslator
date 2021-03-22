package project.wgtech.wgtranslator.repository

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.*
import project.wgtech.wgtranslator.Util
import project.wgtech.wgtranslator.model.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor (
    private val languageModels : ArrayList<Language>,
    private val util: Util
) {
    private val TAG = DataRepository::class.java.simpleName

    fun getLanguageModels() = languageModels

    fun initializeAllModels() {
        for (i in 0 until languageModels.size) {
            val translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(languageModels[i].code)
                .setTargetLanguage(languageModels[languageModels.size - 1 - i].code)
                .build()

            Translation.getClient(translatorOptions).downloadModelIfNeeded(
                DownloadConditions.Builder().requireWifi().build()
            )

            Log.d("wgtech", "setUpAllModels: ${languageModels[i].code} --> ${languageModels[languageModels.size - 1 - i].code} ... Done")
        }
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
                        Log.d("wgtech", "provideTranslateData (addOnSuccessListener) --> $result")
                    }
                    .addOnFailureListener {
                        result = it.message
                        Log.e("wgtech", "provideTranslateData (addOnFailureListener) --> $result")
                    }
            }
        }
        Log.d("wgtech", "result= $result")
        return result
    }
}