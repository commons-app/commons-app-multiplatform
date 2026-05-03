package app.multiplatform.commons.upload.domain.model

import app.multiplatform.commons.upload.ui.UploadLicense

data class Contribution(
    val caption: String,
    val description: String,
    val license: UploadLicense,
    val username: String,
    val fileKey: String? = null
)
