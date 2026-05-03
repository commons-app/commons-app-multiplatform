package app.multiplatform.commons.upload.data

import app.multiplatform.commons.upload.domain.model.Contribution
import app.multiplatform.commons.upload.ui.UploadLicense
import app.multiplatform.commons.utils.getVersionNameWithSha
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class PageContentsCreator {
    fun createFrom(contribution: Contribution): String = buildString {
        append("== {{int:filedesc}} ==\n")
        append("{{Information\n")
        append("|description={{en|1=").append(contribution.description).append("}}\n")
        append("|source={{own}}\n")
        append("|author=[[User:").append(contribution.username).append("|")
        append(contribution.username).append("]]\n")

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val dateString = "${now.year}-${now.month.number.toString().padStart(2, '0')}-${now.day.toString().padStart(2, '0')}"
        append("|date=").append(dateString).append("\n")

        append("}}").append("\n\n")

        append("== {{int:license-header}} ==\n")
        append(licenseTemplateFor(contribution.license)).append("\n\n")
        
        append("{{Uploaded from Mobile|platform=Multiplatform|version=")
        append(getVersionNameWithSha()).append("}}\n")
        
        // Bare minimum as requested: default to uncategorized if not provided
        append("{{subst:unc}}")
    }

    private fun licenseTemplateFor(license: UploadLicense) = when (license) {
        UploadLicense.CC_BY_SA_4 -> "{{self|cc-by-sa-4.0}}"
        UploadLicense.CC_BY_4 -> "{{self|cc-by-4.0}}"
        UploadLicense.CC0 -> "{{self|cc-zero}}"
    }
}
