package app.multiplatform.commons.upload.data

import app.multiplatform.commons.upload.domain.model.Contribution
import app.multiplatform.commons.utils.getVersionNameWithSha
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class PageContentsCreator {
    fun createFrom(contribution: Contribution): String = buildString {
        append("== {{int:filedesc}} ==\n")
        append("{{Information\n")

        // Only add the {{en|1=...}} wrapper when there is actual text; an empty
        // wrapper renders the "Template:Description/i18n" error on Commons.
        if (contribution.description.isNotBlank()) {
            append("|description={{en|1=").append(contribution.description).append("}}\n")
        } else {
            append("|description=\n")
        }

        append("|source={{own}}\n")
        append("|author=[[User:").append(contribution.username).append("|")
        append(contribution.username).append("]]\n")

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val dateString = "${now.year}-${now.month.number.toString().padStart(2, '0')}-${now.day.toString().padStart(2, '0')}"
        append("|date=").append(dateString).append("\n")

        append("}}\n\n")

        append("== {{int:license-header}} ==\n")
        // wikiTemplate lives on UploadLicense — single source of truth
        append(contribution.license.wikiTemplate).append("\n\n")

        append("{{Uploaded from Mobile|platform=Multiplatform|version=")
        append(getVersionNameWithSha()).append("}}\n")

        append("{{subst:unc}}")
    }
}
