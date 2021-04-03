package project.wgtech.wgtranslator.model

import android.graphics.drawable.Drawable

data class Status(
    var code: StatusCode,
    var drawable: Drawable?,
    var message: String,
    var comment: String?
)
