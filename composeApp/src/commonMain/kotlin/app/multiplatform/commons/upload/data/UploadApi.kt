package app.multiplatform.commons.upload.data

import app.multiplatform.commons.upload.data.dto.UploadResponseDto
import app.multiplatform.commons.utils.Constants.MW_API_PREFIX
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.content.ProgressListener
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import kotlinx.serialization.json.JsonObject

class UploadApi(private val client: HttpClient) {

    suspend fun uploadChunkToStash(
        filename: String,
        totalFileSize: Long,
        offset: Long,
        fileKey: String,
        csrfToken: String,
        chunkBytes: ByteArray,
        mimeType: String?,
        onProgress: ProgressListener
    ): UploadResponseDto {
        return client.post {
            url("${MW_API_PREFIX}&action=upload&stash=1&ignorewarnings=1")
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("filename", filename)
                        append("filesize", totalFileSize.toString())
                        append("offset", offset.toString())
                        append("filekey", fileKey)
                        append("token", csrfToken)
                        append("chunk", chunkBytes, Headers.build {
                            append(HttpHeaders.ContentType, mimeType ?: "image/octet-stream")
                            append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                        })
                    }
                )
            )
            onUpload(onProgress)
        }.body()
    }

    suspend fun uploadFileToCommons(
        filename: String,
        token: String,
        fileBytes: ByteArray,
        text: String,
        comment: String,
        mimeType: String?,
        onProgress: ProgressListener
    ): UploadResponseDto {
        return client.post {
            url("${MW_API_PREFIX}&action=upload&ignorewarnings=1")
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("token", token)
                        append("text", text)
                        append("filename", filename)
                        append("comment", comment)
                        append("file", fileBytes, Headers.build {
                            append(HttpHeaders.ContentType, mimeType ?: "image/octet-stream")
                            append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                        })
                    }
                )
            )
            onUpload(onProgress)
        }.body()
    }

    /**
     * Uploads a file from stash.
     * Equivalent to UploadInterface.uploadFileFromStash in Retrofit.
     */
    suspend fun uploadFileFromStash(
        token: String,
        text: String,
        comment: String,
        filename: String,
        fileKey: String,
    ): JsonObject {
        return client.submitForm(
            url = "${MW_API_PREFIX}&action=upload&ignorewarnings=1",
            formParameters = parameters {
                append("token", token)
                append("text", text)
                append("comment", comment)
                append("filename", filename)
                append("filekey", fileKey)
            }
        ) {
            header("Cache-Control", "no-cache")
        }.body()
    }

    /**
     * Sets the structured-data caption (WikiBase label) for a file on Commons.
     * Equivalent to PageEditInterface.postCaptions in the Android app.
     * [title] must be the canonical form, e.g. "File:Example.jpg".
     */
    suspend fun setCaption(
        token: String,
        title: String,
        language: String,
        value: String,
    ): JsonObject {
        return client.submitForm(
            url = "${MW_API_PREFIX}&action=wbsetlabel&site=commonswiki",
            formParameters = parameters {
                append("token", token)
                append("title", title)
                append("language", language)
                append("value", value)
                append("summary", "Adding caption")
            }
        ) {
            header("Cache-Control", "no-cache")
        }.body()
    }
}
