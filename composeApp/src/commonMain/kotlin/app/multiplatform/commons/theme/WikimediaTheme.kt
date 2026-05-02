@file:OptIn(ExperimentalAdaptiveApi::class)

package app.multiplatform.commons.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTheme
import io.github.alexzhirkevich.cupertino.adaptive.CupertinoThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.MaterialThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.Theme
import io.github.alexzhirkevich.cupertino.theme.Typography as CupertinoTypography
import io.github.alexzhirkevich.cupertino.theme.lightColorScheme as cupertinoLightColorScheme

// Codex colour tokens — https://doc.wikimedia.org/codex/latest/design-tokens/color.html
private val Blue700  = Color(0xFF3366CC) // progressive / primary
private val Blue800  = Color(0xFF3056A9) // hover / active state
private val Blue900  = Color(0xFF233566)
private val BlueLight = Color(0xFFEAF3FB)

private val Gray1000 = Color(0xFF101418)
private val Gray900  = Color(0xFF202122) // text base
private val Gray700  = Color(0xFF404244) // text neutral
private val Gray600  = Color(0xFF54595D) // text subtle
private val Gray400  = Color(0xFFA2A9B1) // border / disabled
private val Gray100  = Color(0xFFEAECF0) // background neutral
private val Gray50   = Color(0xFFF8F9FA) // background subtle

private val Red700   = Color(0xFFBF3C2C) // destructive
private val Red500   = Color(0xFFF54739) // error

private val WikimediaMaterialThemeSpec = MaterialThemeSpec(
    colorScheme = lightColorScheme(
        primary = Blue700,
        onPrimary = Color.White,
        primaryContainer = BlueLight,
        onPrimaryContainer = Blue900,
        secondary = Gray600,
        onSecondary = Color.White,
        secondaryContainer = Gray100,
        onSecondaryContainer = Gray900,
        background = Color.White,
        onBackground = Gray900,
        surface = Color.White,
        onSurface = Gray900,
        surfaceVariant = Gray50,
        onSurfaceVariant = Gray700,
        error = Red700,
        onError = Color.White,
        errorContainer = Color(0xFFFDE8E6),
        onErrorContainer = Red700,
        outline = Gray400,
        outlineVariant = Gray100,
    ),
    typography = Typography(
        headlineLarge  = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp),
        headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp),
        headlineSmall  = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
        bodyLarge      = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        bodyMedium     = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 22.sp),
        bodySmall      = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 20.sp),
        labelLarge     = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp),
        labelMedium    = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
        labelSmall     = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 18.sp),
    ),
)

private val WikimediaCupertinoThemeSpec = CupertinoThemeSpec(
    colorScheme = cupertinoLightColorScheme(
        accent = Blue700,
        label = Gray900,
        secondaryLabel = Gray700,
        tertiaryLabel = Gray600,
        quaternaryLabel = Gray400,
        systemFill = Gray100,
        secondarySystemFill = Gray100,
        tertiarySystemFill = Gray50,
        quaternarySystemFill = Gray50,
        placeholderText = Gray600,
        separator = Gray100,
        opaqueSeparator = Gray100,
        link = Blue700,
        systemGroupedBackground = Gray50,
        secondarySystemGroupedBackground = Color.White,
        tertiarySystemGroupedBackground = Color.White,
        systemBackground = Color.White,
        secondarySystemBackground = Gray50,
        tertiarySystemBackground = Color.White,
    ),
    typography = CupertinoTypography(
        largeTitle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 36.sp),
        title1 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp),
        title2 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp),
        title3 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
        headline = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp),
        body = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        callout = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        subhead = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 22.sp),
        footnote = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp),
        caption1 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp),
        caption2 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp),
    ),
)

@Composable
fun WikimediaTheme(
    target: Theme = platformThemeTarget(),
    content: @Composable () -> Unit,
) {
    AdaptiveTheme(
        target = target,
        material = WikimediaMaterialThemeSpec,
        cupertino = WikimediaCupertinoThemeSpec,
        content = content,
    )
}
