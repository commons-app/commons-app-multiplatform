package app.multiplatform.commons.utils

import app.multiplatform.commons.model.LatLng
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSBundle
import platform.Foundation.NSTemporaryDirectory
import platform.UniformTypeIdentifiers.UTType

actual val platformFileSystem: FileSystem = FileSystem.SYSTEM

actual val platformTempDirectory: Path = NSTemporaryDirectory().toPath()

actual fun getMimeType(path: Path): String? {
    val extension = path.name.substringAfterLast('.', "")
    return UTType.typeWithFilenameExtension(extension)?.preferredMIMEType
}

actual fun getGeolocationOfFile(path: Path, inAppPictureLocation: LatLng?): String? {
    return inAppPictureLocation?.let { "${it.latitude},${it.longitude}" }
}

actual fun getVersionNameWithSha(): String {
    val version = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
    val build = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleVersion") as? String
    return "${version ?: "unknown"} (${build ?: "unknown"})"
}
