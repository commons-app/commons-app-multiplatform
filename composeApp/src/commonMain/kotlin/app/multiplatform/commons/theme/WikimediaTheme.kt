package app.multiplatform.commons.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Codex colour tokens — https://doc.wikimedia.org/codex/latest/design-tokens/color.html
private val Blue700  = Color(0xFF3366CC) // progressive / primary
private val Blue800  = Color(0xFF3056A9)
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

private val WikimediaColorScheme = lightColorScheme(
    primary             = Blue700,
    onPrimary           = Color.White,
    primaryContainer    = BlueLight,
    onPrimaryContainer  = Blue900,
    secondary           = Gray600,
    onSecondary         = Color.White,
    secondaryContainer  = Gray100,
    onSecondaryContainer = Gray900,
    background          = Color.White,
    onBackground        = Gray900,
    surface             = Color.White,
    onSurface           = Gray900,
    surfaceVariant      = Gray50,
    onSurfaceVariant    = Gray700,
    error               = Red700,
    onError             = Color.White,
    errorContainer      = Color(0xFFFDE8E6),
    onErrorContainer    = Red700,
    outline             = Gray400,
    outlineVariant      = Gray100,
)

// Codex font tokens — sizes map to the 7-tier scale (0.75–1.75rem → 12–28sp)
private val WikimediaTypography = Typography(
    headlineLarge  = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp),
    headlineSmall  = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
    bodyLarge      = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium     = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 22.sp),
    bodySmall      = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 20.sp),
    labelLarge     = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp),
    labelMedium    = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
    labelSmall     = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 18.sp),
)

@Composable
fun WikimediaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WikimediaColorScheme,
        typography = WikimediaTypography,
        content = content
    )
}
