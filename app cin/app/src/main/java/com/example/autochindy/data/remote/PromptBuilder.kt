package com.example.autochindy.data.remote

import com.example.autochindy.domain.model.EditorialConfig
import com.example.autochindy.domain.model.PriorityPreference

/**
 * Builds strict, low-temperature prompt instructions for the LLM
 * to avoid hallucinations and ensure editorial fidelity.
 */
object PromptBuilder {

    private const val SYSTEM_ROLE_INSTRUCTION = """
        Eres un periodista experto en la redacción de boletines de noticias.
        Tu trabajo principal es extraer información de transcripciones de audio y video
        para formular un Título, un Subtítulo y el Cuerpo del artículo.
        
        REGLAS ESTRICTAS E INQUEBRANTABLES:
        1. NO INVENTES NADA. Todo dato, cifra o hecho debe estar presente en el texto original.
        2. NO agregues contexto externo que no se mencione en el texto fuente.
        3. NO deduzcas nombres de personas. Solo incluye nombres explícitos de la transcripción.
        4. NO asignes declaraciones a personas si la transcripción no lo deja absolutamente claro.
        5. Tono: Periodístico, neutral e informativo (español).
        6. Si la información fuente es confusa o no está clara, indícalo o evítalo.
        7. Si te doy una 'Instrucción Editorial', respétala siempre que no incumpla las reglas anteriores.
    """

    fun buildSystemPrompt(): String {
        return SYSTEM_ROLE_INSTRUCTION.trimIndent()
    }

    fun buildUserPrompt(
        transcriptionText: String,
        config: EditorialConfig,
        regenerateTitleOnly: Boolean = false,
        regenerateSubtitleOnly: Boolean = false
    ): String {
        val promptBuilder = StringBuilder()

        promptBuilder.append("Transcripción Original:\n")
        promptBuilder.append("\"\"\"\n$transcriptionText\n\"\"\"\n\n")

        if (!config.customInstructions.isNullOrBlank()) {
            promptBuilder.append("Instrucción Editorial:\n")
            promptBuilder.append("- ${config.customInstructions}\n\n")
        }

        if (config.peopleToPrioritize.isNotEmpty()) {
            val peopleStr = config.peopleToPrioritize.joinToString(", ")
            promptBuilder.append("Preferencia de Personas:\n")
            promptBuilder.append("- Si estas personas aparecen CLARAMENTE en la transcripción, intenta incluirlas prioritariamente en la sección indicada, pero NO lo fuerces si no aparecen. Personas a buscar: [$peopleStr]\n")
            
            val locationInst = when(config.priorityPreference) {
                PriorityPreference.TITLE -> "solo en el TÍTULO."
                PriorityPreference.SUBTITLE -> "solo en el SUBTÍTULO."
                PriorityPreference.BOTH -> "tanto en el TÍTULO como en el SUBTÍTULO."
                PriorityPreference.BODY_ONLY -> "únicamente en el CUERPO del texto."
                PriorityPreference.NONE -> "sin preferencia particular."
            }
            promptBuilder.append("- Aplicar prioridad $locationInst\n\n")
        }

        val verbosityCmd = if (config.bulletinType == com.example.autochindy.domain.model.BulletinType.SUMMARIZED) {
            "Genera un cuerpo EXTRA RESUMIDO, destacando únicamente los 3 puntos más cruciales. Obligatorio que sea breve."
        } else {
            "Genera un cuerpo COMPLETO Y DETALLADO abarcando toda la transcripción, pero manteniendo una estructura periodística directa."
        }

        promptBuilder.append("TAREA FINAL:\n")
        promptBuilder.append("- $verbosityCmd\n\n")
        
        if (regenerateTitleOnly) {
            promptBuilder.append("Devuelve ÚNICAMENTE un nuevo TÍTULO (sin formato markdown).")
        } else if (regenerateSubtitleOnly) {
            promptBuilder.append("Devuelve ÚNICAMENTE un nuevo SUBTÍTULO (sin formato markdown).")
        } else {
            promptBuilder.append("""
                Devuelve exactamente el siguiente formato JSON sin envolturas de markdown ni explicaciones adicionales:
                {
                   "title": "Un título atractivo",
                   "subtitle": "Un subtítulo que resuma el contexto",
                   "content": "El cuerpo periodístico completo del boletín...",
                   "confidence": "HIGH" (opciones: HIGH, MEDIUM, LOW)
                }
                El campo 'confidence' debe ser HIGH si el texto es claro. MEDIUM si hay partes un poco confusas. LOW si la transcripción parece cortada, tiene muchos errores reconocibles o hay grave riesgo de interpretación y no se entiende de qué hablan.
            """.trimIndent())
        }

        return promptBuilder.toString()
    }
}
