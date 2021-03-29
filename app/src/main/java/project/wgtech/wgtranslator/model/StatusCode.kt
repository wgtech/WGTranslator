package project.wgtech.wgtranslator.model

enum class StatusCode(val code: Int) {
    IDLE(0),
    INITIALIZE(1),
    DOWNLOAD_MODEL(2),
    DOWNLOAD_COMPLETE_MODEL(3),
    DOWNLOAD_FAILURE_MODEL(4),
    ALREADY_STORED_MODEL(5)
}