package com.example.autochindy.presentation.features.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
        ) {
            Text("Ajustes de PIN", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { /* TODO */ }, modifier = Modifier.padding(top = 8.dp)) {
                Text("Cambiar PIN de acceso")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("API Keys Externas (Whisper/GPT)", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("OpenAI API Key (para uso local)") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }
    }
}
