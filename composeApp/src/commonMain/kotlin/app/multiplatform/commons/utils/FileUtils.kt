package app.multiplatform.commons.utils

import app.multiplatform.commons.model.LatLng
import okio.FileSystem
import okio.Path
import okio.Source
import okio.buffer
import okio.HashingSource
import okio.use

expect val platformFileSystem: FileSystem
expect val platformTempDirectory: Path

object FileUtils {
    fun getSHA1(source: Source): String {
        val hashingSource = HashingSource.sha1(source)
        hashingSource.buffer().use {
            while (it.read(ByteArray(8192)) != -1) {
                // Read everything to compute hash
            }
        }
        return hashingSource.hash.hex()
    }

    fun getFileInputStream(path: Path): Source {
        return platformFileSystem.source(path)
    }

    fun getFileExt(fileName: String): String {
        return fileName.substringAfterLast('.', "")
    }
}

expect fun getMimeType(path: Path): String?
expect fun getGeolocationOfFile(path: Path, inAppPictureLocation: LatLng?): String?
expect fun getVersionNameWithSha(): String
