package app.multiplatform.commons.upload.data

import app.multiplatform.commons.auth.domain.models.MwQueryResponse
import app.multiplatform.commons.utils.Constants.MW_API_PREFIX
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MediaApi(private val client: HttpClient) {

    /**
     * This method retrieves a list of Media objects for a given user name
     *
     * @param username     user's Wikimedia Commons username.
     * @param itemLimit    how many images are returned
     * @param continuation the continuation string from the previous query or empty map
     */
    suspend fun getMediaListForUser(
        username: String?,
        itemLimit: Int,
        continuation: Map<String, String>,
    ): MwQueryResponse {
        return client.get(MW_API_PREFIX) {
            parameter("action", "query")
            parameter("generator", "allimages")
            parameter("gaisort", "timestamp")
            parameter("gaidir", "older")
            parameter("prop", MEDIA_PARAMS)
            
            username?.let { parameter("gaiuser", it) }
            parameter("gailimit", itemLimit.toString())
            
            // Append continuation parameters
            continuation.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }

    companion object {
        const val THUMB_HEIGHT_PX = 450

        const val MEDIA_PARAMS = "prop=imageinfo|coordinates&iiprop=url|extmetadata|user&&iiurlheight=" +
                    THUMB_HEIGHT_PX +
                    "&iiextmetadatafilter=DateTime|Categories|GPSLatitude|GPSLongitude|" +
                    "ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"
    }
}
