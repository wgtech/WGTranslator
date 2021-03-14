package project.wgtech.wgtranslator.viewmodel

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import project.wgtech.wgtranslator.model.Language
import javax.inject.Inject

class LanguageViewModel @Inject constructor(
    var language: Language,
    val code: ObservableField<String> = ObservableField(language.code),
    val name: ObservableField<String> = ObservableField(language.name),
    val drawable: ObservableField<Drawable> = ObservableField(language.drawable)
) : ViewModel() {


}