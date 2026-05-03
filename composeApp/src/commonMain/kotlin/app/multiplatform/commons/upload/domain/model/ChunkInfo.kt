package app.multiplatform.commons.upload.domain.model

data class ChunkInfo(
    val uploadResult: UploadResult?,
    val indexOfNextChunkToUpload: Int,
    val totalChunks: Int,
)
