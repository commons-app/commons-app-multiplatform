package app.multiplatform.commons.upload.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    uiState: UploadUiState,
    onEvent: (UploadEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (uiState.phase) {
                            UploadPhase.DETAILS -> "Upload to Commons"
                            UploadPhase.UPLOADING -> "Uploading…"
                            UploadPhase.COMPLETE -> "Upload Complete"
                        }
                    )
                },
                navigationIcon = {
                    if (uiState.phase == UploadPhase.DETAILS) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.phase,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it } + fadeOut())
            },
            modifier = Modifier.padding(innerPadding),
            label = "phase_transition"
        ) { phase ->
            when (phase) {
                UploadPhase.DETAILS -> DetailsContent(uiState = uiState, onEvent = onEvent)
                UploadPhase.UPLOADING, UploadPhase.COMPLETE -> ProgressContent(
                    uiState = uiState,
                    onEvent = onEvent,
                    onNavigateBack = onNavigateBack,
                )
            }
        }
    }
}

@Composable
private fun DetailsContent(
    uiState: UploadUiState,
    onEvent: (UploadEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ImagePreviewPlaceholder()

        OutlinedTextField(
            value = uiState.caption,
            onValueChange = { onEvent(UploadEvent.OnCaptionChanged(it)) },
            label = { Text("Caption") },
            placeholder = { Text("What is shown in this file?") },
            supportingText = {
                uiState.captionError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            isError = uiState.captionError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = uiState.description,
            onValueChange = { onEvent(UploadEvent.OnDescriptionChanged(it)) },
            label = { Text("Description") },
            placeholder = { Text("Location, date, context… (optional)") },
            minLines = 3,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth(),
        )

        LicensePicker(
            selected = uiState.selectedLicense,
            expanded = uiState.isLicenseMenuOpen,
            onToggle = { onEvent(UploadEvent.OnToggleLicenseMenu) },
            onSelect = { onEvent(UploadEvent.OnLicenseSelected(it)) },
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = { onEvent(UploadEvent.OnStartUpload) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Start Upload", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun ImagePreviewPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Image preview",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LicensePicker(
    selected: UploadLicense,
    expanded: Boolean,
    onToggle: () -> Unit,
    onSelect: (UploadLicense) -> Unit,
) {
    Box {
        OutlinedCard(
            onClick = onToggle,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "License",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = selected.label,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Choose license",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = onToggle) {
            UploadLicense.entries.forEach { license ->
                DropdownMenuItem(
                    text = { Text(license.label) },
                    onClick = { onSelect(license) },
                )
            }
        }
    }
}

@Composable
private fun ProgressContent(
    uiState: UploadUiState,
    onEvent: (UploadEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.uploadProgress,
        animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic),
        label = "upload_progress",
    )

    val isComplete = uiState.phase == UploadPhase.COMPLETE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        UploadProgressCard(
            caption = uiState.caption,
            progress = animatedProgress,
            isComplete = isComplete,
        )

        Spacer(modifier = Modifier.weight(1f))

        if (isComplete) {
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("Done", style = MaterialTheme.typography.labelLarge)
            }
        } else {
            FilledTonalButton(
                onClick = { onEvent(UploadEvent.OnCancelUpload) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("Cancel", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun UploadProgressCard(
    caption: String,
    progress: Float,
    isComplete: Boolean,
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = caption.ifBlank { "Untitled" },
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 2,
                    )
                    Text(
                        text = if (isComplete) "Uploaded to Wikimedia Commons"
                               else "Uploading to Wikimedia Commons…",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                if (isComplete) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Complete",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    strokeCap = StrokeCap.Round,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = if (isComplete) "Complete" else "In progress",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${(progress * 100).roundToInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isComplete) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (isComplete) {
                Text(
                    text = "Your file is now live on Wikimedia Commons. Thank you for your contribution!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
