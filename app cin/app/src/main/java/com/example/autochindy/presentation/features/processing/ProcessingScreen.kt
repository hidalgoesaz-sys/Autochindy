package com.example.autochindy.presentation.features.processing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ProcessingScreen(
    statusText: String = "Iniciando procesamiento...",
    progressPercent: Float? = null,
    onProcessingComplete: () -> Unit
) {
    // MOCK: Auto-complete for demonstration purposes since we don't have the real ViewModel wired yet.
    // In a real app, the ViewModel would trigger onProcessingComplete() when flows emit Finished.
    LaunchedEffect(Unit) {
        delay(6000)
        onProcessingComplete()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (progressPercent != null) {
            CircularProgressIndicator(
                progress = progressPercent,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${(progressPercent * 100).toInt()}%", style = MaterialTheme.typography.titleMedium)
        } else {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Procesando contenido", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = statusText, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Esto puede tomar un tiempo para archivos largos. El proceso se realiza de forma segmentada para proteger la memoria.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
