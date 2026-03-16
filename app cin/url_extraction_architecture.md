# Diseño Módulo de Extracción por URL Pública

## Resumen de Arquitectura
Para evitar el acoplamiento a una sola plataforma o implementación (como un Web Scraper puro o un único backend monolítico), el sistema de entrada de URLs usará un patrón **`Strategy`** apoyado en un sistema central de **Conectores (Providers)**.

## 1. Entidades y Contratos Base

### Estados de Disponibilidad
Cualquier intento de analizar un enlace responderá a nivel de dominio con uno de estos estados:
- `COMPATIBLE`: Extracción 100% soportada y permitida.
- `COMPATIBLE_WITH_LIMITATIONS`: Ejemplo: YouTube permite el video, pero por restricciones de región o edad no podemos asegurar la descarga HD completa, o Facebook Reels extrae audio mono en lugar de estéreo.
- `TEMPORARILY_UNAVAILABLE`: La API o red subyacente impuso un rate-limit o un ban temporal por IP.
- `ERROR`: Link roto, formato irreconocible, o plataforma no implementada.

### Contrato Principal: `UrlAudioProvider`
Cada red social o servicio que soportemos debe tener su propia clase que implemente esta interfaz:
```kotlin
interface UrlAudioProvider {
    val providerName: String // ej. "YOUTUBE", "FACEBOOK", "GENERIC"
    
    // 1. Detección
    fun canHandle(url: String): Boolean
    
    // 2. Comprobación de salud / estado
    suspend fun checkAvailability(url: String): UrlSupportStatus
    
    // 3. Extracción y Normalización
    suspend fun extractAudio(url: String): Flow<ExtractionProgress>
}
```

## 2. Componentes del Diseño Modular

1. **`UrlModuleDetector (Factory/Router)`**
   - **Responsabilidad:** Recibir la URL bruta del usuario (`https://youtu.be/...`).
   - Iterar sobre una lista inyectada (vía Dagger/Hilt) de `UrlAudioProvider`s disponibles.
   - Detenerse en el primero cuyo método `canHandle` devuelva `true`.

2. **`YouTubeProvider`** (Prioritario)
   - Implementa `UrlAudioProvider`.
   - Utiliza expresiones regulares (`Regex`) exclusivas para dominios `.youtube.com` o `.youtu.be`.
   - Lógica de extracción: Mapea la petición hacia un Microservicio Externo (`yt-dlp wrapper`) que nos devuelve flujo de bytes de audio, ignorando completamente el canal de video.

3. **`FacebookProvider`** (Prioritario)
   - Implementa `UrlAudioProvider`.
   - Detecta dominios de facebook o fb.watch.
   - Lógica de extracción particular adaptada al Graph/Scraping necesario de FB.

4. **`GenericFallbackProvider`** (Opcional Futuro)
   - Intenta buscar la etiqueta `<audio>` o `<video>` en el DOM nativo si es una web libre, devolviendo `ERROR` si falla de inmediato.

## 3. Manejo Claro de Fallos

Si el `UrlModuleDetector` no encuentra un Provider que coincida (o si el `checkAvailability` devuelve un Ban IP o Error temporal), la capa de presentación de Compose (`InputSelectionScreen`) recibe un `Estado de Error`.

- Nunca intentará procesar la descarga.
- Informará visualmente al usuario: "La plataforma X está sufriendo bloqueos de extracción. Sube el video desde tu galería manualmente.".

## 4. Beneficios frente a otras alternativas

- **Aislamiento:** Si mañana Facebook cambia su algoritmo de protección de videos integrados, la clase `YouTubeProvider` no necesita recompilarse ni se rompe.
- **Microservicios (Recomendado):** Al implementarlo así, si la librería local nativa se vuelve obsoleta, un Provider simplemente cambia la URL interna a `https://tu-api.cloud/api/v1/yt/extract` sin que la app principal ni el UI general de *Autochindy* se entere.
- **Normalización de Audio delegada:** Cada conector es responsable de asegurar que el audio que escupe al final de `extractAudio` sea, por ejemplo, un MP3 o WAV estandarizado para la capa de IA (el transcriptor).
