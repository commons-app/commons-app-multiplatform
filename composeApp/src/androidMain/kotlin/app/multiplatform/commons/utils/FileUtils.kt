package app.multiplatform.commons.utils

import android.webkit.MimeTypeMap
import androidx.exifinterface.media.ExifInterface
import app.multiplatform.commons.model.LatLng
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.context.GlobalContext
import android.content.Context

actual val platformFileSystem: FileSystem = FileSystem.SYSTEM

actual val platformTempDirectory: Path by lazy {
    val context = GlobalContext.get().get<Context>()
    context.cacheDir.absolutePath.toPath()
}

actual fun getMimeType(path: Path): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(path.toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

actual fun getGeolocationOfFile(path: Path, inAppPictureLocation: LatLng?): String? {
    return try {
        val exifInterface = ExifInterface(path.toString())
        val latLong = exifInterface.latLong
        if (latLong != null && latLong.size >= 2) {
            "${latLong[0]},${latLong[1]}"
        } else {
            inAppPictureLocation?.let { "${it.latitude},${it.longitude}" }
        }
    } catch (e: Exception) {
        inAppPictureLocation?.let { "${it.latitude},${it.longitude}" }
    }
}

actual fun getVersionNameWithSha(): String {
    return try {
        val context = GlobalContext.get().get<Context>()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "unknown"
    } catch (e: Exception) {
        "unknown"
    }
}
