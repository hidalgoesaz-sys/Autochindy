# Autochindy - Generador Strict de Boletines Periodísticos 🎙️📰

Autochindy es una aplicación Android enfocada en la privacidad y la precisión diseñada para convertir audios y videos largos en boletines periodísticos en español altamente estructurados. Su núcleo tecnológico se basa en garantizar exactitud ("Zero-Hallucination") mediante prompts de temperatura cero hacia LLMs locales o remotos, apoyados en procesamiento en streaming segmentado.

---

## 1. Manual de Instalación de la App (Para Usuarios/QA)

1. Descarga el archivo `.apk` provisto en la sección de *Releases* del proyecto o compila el proyecto directamente desde el código fuente a tu teléfono.
2. Si tu teléfono solicita permisos para **"Instalar aplicaciones desconocidas"**, acéptalos desde la configuración de Android.
3. Abre Autochindy.
4. Concede los permisos a la aplicación para **"Acceder a Fotos, Contenido Multimedia y Archivos"** de tu dispositivo (necesario para leer los videos y audios que quieras resumir).

## 2. Manual de Uso

### 🔐 Iniciar Sesión (Acceso con PIN)
- Al abrir la app por primera vez, se te pedirá que **crees un PIN numérico de 4 a 6 dígitos**.
- Este PIN asegura que solo tú puedas ver tu historial offline.
- En futuros ingresos, ingresa ese mismo PIN para desbloquear el Dashboard.

### 🔗 Cómo pegar una URL o Subir un Archivo
- En la pantalla principal verás dos botones centrales.
- **Pegar URL:** Pega un enlace público válido (ej. YouTube, Facebook Video). La app se encargará internamente de validar y extraer únicamente la pista de audio sin descargar video pesado.
- **Subir Archivo:** Toca el botón para abrir la galería de Android y selecciona cualquier grabación de voz o video largo MP4 que tengas guardado. No hay límite de tiempo.

### ✍️ La Instrucción Editorial (Boletín Resumido o Completo)
1. Antes de empezar el proceso, verás una configuración.
2. Elige si quieres un boletín de **Cobertura Completa** o **Extra Resumido**.
3. **Instrucción Libre:** Escribe cómo quieres que narre la noticia (Ej. *"Quiero que el enfoque sea muy crítico sobre las declaraciones financieras del final"*).
4. **Priorización de Personas:** Puedes escribir nombres clave separados por coma. La IA los pondrá en el Título o Subtítulo **solamente** si logran ser detectados en la transcripción. Si no lo dijeron en el video, la app no lo inventará.

### ⚙️ Generar el Boletín
- Toca **"Procesar"**.
- Verás una pantalla de progreso en vivo indicando el "Segmento activo". Puedes dejar el teléfono sobre la mesa; la app segmentará en trozos pequeños para no sobrecargar la memoria.

### 📜 Resultados y Exportación a Word
- Al terminar, se abrirán dos pestañas: **[Boletín]** y **[Transcripción]**.
- La pestaña de transcripción tiene el texto crudo exacto con "Marcas de Tiempo" (ej. `[01:20]`).
- Toca el botón **Exportar** (ícono de compartir 📤 en la parte superior derecha) para generar un archivo `.docx`. Puedes enviarlo por WhatsApp, Gmail, o guardarlo localmente.

### 🗃️ Consultar el Historial
- Desde la pantalla principal, toca en **"Ver Historial"**.
- Accederás a todos tus boletines pasados incluso si no tienes acceso a internet (guardados offline).

---

## 3. README Técnico del Proyecto
El proyecto está construido nativamente para Android usando un patrón estricto de **Clean Architecture** que previene el acoplamiento de plataformas y facilita el testing. Se implementó una política *Offline-First* con persistencia, delegando al hardware del teléfono la segmentación inicial para asegurar que audios masivos no rompan el heap de Android (`OutOfMemoryError`). El módulo extra de captura de URLs usa un patrón **Strategy** (Providers injectables) que permite añadir nuevas redes sociales sin tocar el núcleo de la App.

---

## 4. Instrucciones para Compilar el Proyecto Android

1. Instala [Android Studio](https://developer.android.com/studio) (Giraffe o superior recomendado).
2. Clona el repositorio y abre la carpeta `app cin` en Android Studio.
3. Permite que **Gradle Sync** finalice la descarga de dependencias. (O usa `./gradlew sync`).
4. **Configuración de la API Key:** En el paquete `data/remote/openai/OpenAiBulletinGenerator.kt` se manejan llamadas REST. Necesitarás inyectar tu API Key de OpenAI para habilitar las transcripciones de Whisper/GPT en entornos reales.
5. Ejecuta el comando `./gradlew build` en la terminal para compilar el proyecto globalmente o pulsa **Run (Shift + F10)** en Android Studio con un Emulador funcionando (API > 26).

---

## 5. Lista de Dependencias Utilizadas

* **UI & Theming:** Jetpack Compose (BOM 2023.10.01), Material3, Accompanist (opcional para edge-to-edge).
* **Navegación:** `androidx.navigation:navigation-compose:2.7.4`
* **Concurrencia:** Kotlin Coroutines Android (`1.7.3`)
* **Base de Datos offline:** Room (`2.6.0` con KSP compiler).
* **Redes y Parsing (La capa I.A):** Retrofit (`2.9.0`), Gson (`2.10.1`).
* **Seguridad Biométrica/Local:** AndroidX Security Crypto `1.1.0-alpha06` (`EncryptedSharedPreferences`).
* **Generación de Archivos Word:** Apache POI (`org.apache.poi:poi-ooxml:5.2.3`).

---

## 6. Estructura Completa del Repositorio

El diseño respeta una separación semántica:
```text
com.example.autochindy
├── AutochindyApp.kt               # Entrypoint de inicialización global (DI/Context)
├── MainActivity.kt                # Host Activity de Jetpack Compose
├── core/
│   └── security/                  # Manejo de criptografía (PinManager)
├── data/
│   ├── local/                     # Room Database, DAOs y Entities
│   ├── remote/                    # OpenAI API, PromptBuilder, Models REST
│   ├── remote/url/                # UrlModuleDetector, Strategies Youtube/Facebook
│   └── repository/                # Implementación concreta de Repositories (Data Source)
├── domain/
│   ├── model/                     # Plain Kotlin Classes (Entities Puras sin logica framework)
│   └── repository/                # Contratos abstractos (Interfaces de puertos)
├── export/                        # Aislamiento orgánico de Apache POI (DocxExporter)
└── presentation/
    ├── features/                  # UI Packages (auth, history, home, processing, result)
    ├── navigation/                # Compose NavHost y Grafos de Rutas
    └── theme/                     # Color, Typos y Material Themes
```

---

## 7. Lista de Tareas Pendientes para la Versión 2 (V2)

- [ ] Sustitución de `MockMediaExtractor` por el SDK real de **FFmpegKit** para permitir la demultiplexación de mp4 robusta en offline puro.
- [ ] Incorporación de un Microservicio externo backend para delegar peticiones de redes sociales problemáticas (Bypass de validación en la nube para YouTube/FB).
- [ ] Módulo de Fact-Checking interno (Cruzar Transcripción vs Texto Redactado antes de devolver la vista final al usuario) para agregar al estado de `ConfidenceLevel`.
- [ ] Implementar Inyección de Dependencias automatizada pura usando Hilt/Dagger en toda la capa de repositorios.
- [ ] Tematización oscura nativa.

---

## 8. Riesgos Técnicos Identificados

1. **Memoria y Ciclo de Vida en Android (Extracción):** 
   Aunque tenemos segmentación a nivel código, procesar un MP4 de 3GB nativamente en un celular de gama baja sin un `ForegroundService` puede hacer que el Sistema Operativo mate la aplicación silenciosamente si el usuario la minimiza. Un servicio adjunto a notificaciones fijas soluciona esto en V2.
2. **Políticas Mutables de URLs Web:**
   Depender de Regex crudo y extractores `client-side` para redes públicas como YouTube o Facebook (Reels) es insostenible temporalmente. Las plataformas actualizan su DOM y técnicas Anti-Bot constantemente. El `UrlAudioProvider` debe evolucionar a un puente/wrapper de microservicios como `yt-dlp` en Nubes externas.
3. **Restricciones de Apache POI:**
   Escribir documentos `.docx` requiere crear estructuras complejas de XML/ZIP internamente. Apache POI inflará parcialmente el tamaño de la APK final de Android (Multidex) advirtiendo ligeros cuellos de botella al abrir / inicializar la librería en celulares muy viejos. Alternativa V2: `DOCX4J`.
