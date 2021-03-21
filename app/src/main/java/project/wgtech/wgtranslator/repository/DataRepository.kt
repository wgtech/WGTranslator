package project.wgtech.wgtranslator.repository

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.withContext
import project.wgtech.wgtranslator.model.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor (
    private val languageModels : ArrayList<Language>) {
    private val TAG = DataRepository::class.java.simpleName

    fun getLanguageModels() = languageModels
    fun getTranslateData(sourceLanguage: Language, targetLanguage: Language, targetText: String): String {
        var result = ""

        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage.code)
            .setTargetLanguage(targetLanguage.code)
            .build()

        Translation.getClient(translatorOptions)
            .downloadModelIfNeeded(DownloadConditions.Builder().requireWifi().build())

        Translation.getClient(translatorOptions)
            .translate(targetText)
            .addOnSuccessListener {
                Log.d("wgtech", "provideTranslateData (addOnSuccessListener) --> $it")
                result = it.toString()
            }
            .addOnFailureListener {
                Log.e("wgtech", "provideTranslateData (addOnFailureListener) --> ${it.message}")
                result = ""
            }

        Log.d("wgtech", "result= $result")

        return result
    }
}