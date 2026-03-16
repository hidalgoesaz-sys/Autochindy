package com.example.autochindy.presentation.features.result

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(onBackToHome: () -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Boletín", "Transcripción")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados") },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Export to DOCX logic here */ }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Exportar a Word")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            
            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                if (selectedTabIndex == 0) {
                    // Boletin View
                    Column {
                        Text("Título Generado", style = MaterialTheme.typography.headlineMedium)
                        Text("Subtítulo atractivo", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Este es el contenido resumido del boletín generado estrictamente basado en la transcripción. Sin inventar datos.", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    // Transcripcion View
                    Text("Aquí se mostrará la transcripción cruda completa del audio, segmento por segmento...", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
