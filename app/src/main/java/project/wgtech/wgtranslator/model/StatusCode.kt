package project.wgtech.wgtranslator.model

enum class StatusCode(val code: Int) {
    IDLE(0),
    INITIALIZE(1),
    INITIALIZE_COMPLETE_MODEL(2),
    DOWNLOAD_MODEL(3),
    DOWNLOAD_COMPLETE_MODEL(4),
    DOWNLOAD_FAILURE_MODEL(5)
}