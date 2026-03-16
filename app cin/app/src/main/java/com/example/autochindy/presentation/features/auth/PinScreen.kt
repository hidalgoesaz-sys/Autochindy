package com.example.autochindy.presentation.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PinScreen(
    onPinSuccess: () -> Unit,
    pinManager: com.example.autochindy.core.security.PinManager? = null
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val manager = pinManager ?: remember { com.example.autochindy.core.security.PinManager(context) }
    
    var pin by remember { mutableStateOf("") }
    var isSetupMode by remember { mutableStateOf(!manager.hasPin()) }
    var errorText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val title = if (isSetupMode) "Crea un nuevo PIN" else "Introduce tu PIN"
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = pin,
            onValueChange = { 
                if (it.length <= 6) pin = it 
                errorText = ""
            },
            label = { Text("PIN numérico (4-6 dígitos)") },
            singleLine = true,
            isError = errorText.isNotEmpty()
        )
        
        if (errorText.isNotEmpty()) {
            Text(text = errorText, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { 
            if (pin.length < 4) {
                errorText = "El PIN debe tener al menos 4 dígitos"
                return@Button
            }
            
            if (isSetupMode) {
                manager.savePin(pin)
                onPinSuccess()
            } else {
                if (manager.validatePin(pin)) {
                    onPinSuccess()
                } else {
                    errorText = "PIN Incorrecto"
                }
            }
        }) {
            Text(if (isSetupMode) "Guardar y Entrar" else "Desbloquear")
        }
    }
}
