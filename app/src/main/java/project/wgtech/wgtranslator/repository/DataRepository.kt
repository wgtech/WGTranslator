package project.wgtech.wgtranslator.repository

import project.wgtech.wgtranslator.model.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor (
    private val languageModels : ArrayList<Language>) {

    fun getLanguageModels() = languageModels
}