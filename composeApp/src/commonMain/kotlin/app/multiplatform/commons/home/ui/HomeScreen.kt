package app.multiplatform.commons.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import commons.composeapp.generated.resources.Res
import commons.composeapp.generated.resources.ic_cdx_add
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToUpload: (ByteArray) -> Unit = {}) {
    val scope = rememberCoroutineScope()
    val pickerLauncher = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let { onNavigateToUpload(it) }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Commons") },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { pickerLauncher.launch() },
                icon = {Icon(painterResource(Res.drawable.ic_cdx_add), contentDescription = "Upload") },
                text = { Text("Upload") },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Welcome to Commons",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Tap + Upload to contribute a file",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
