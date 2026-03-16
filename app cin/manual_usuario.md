# Autochindy - Manual de Instalación y Uso

## Requisitos Previos
- **Android Studio** (Giraffe o superior recomendado).
- **Gradle 8.0+**
- **JDK 17**

## Instrucciones de Compilación

1. Clona o descarga el código fuente en tu entorno local.
2. Abre Android Studio y haz clic en **File > Open**, luego selecciona la carpeta `app cin`.
3. Permite que Gradle descargue las dependencias y sincronice el proyecto.
4. Si tienes errores sobre "gradlew", asegúrate de abrir una terminal de PowerShell dentro de la carpeta `app cin` y ejecuta `.\gradlew build` para comprobar la compilación local.
5. Haz clic en el botón de **Run (Play verde)** en Android Studio para instalarlo en el Emulador o Dispositivo Android conectado.

## Uso Básico

1. **Configuración Inicial:** Al iniciar por primera vez, deberás configurar (o validar) tu **PIN de Seguridad**.
2. **Dashboard (Home):** Verás las opciones para procesar medios.
   - **Pegar URL:** Inserta enlaces a contenido compatible (ej. YouTube, Facebook). El Extractor Modular descargará y aislará el audio.
   - **Subir Archivo:** Selecciona audios o videos de tu almacenamiento interno.
3. **Módulo Editorial:** 
   - Define si deseas un **Boletín Completo** o un **Boletín Resumido**.
   - Ingresa palabras clave o nombres de personas importantes a priorizar.
4. **Procesamiento:** El archivo se segmentará y se enviará por partes seguras a la API de Inteligencia Artificial (OpenAI) garantizando que no existan cortes de memoria.
5. **Resultados e Historial:** Visualiza la transcripción en crudo o el formato periodístico (Título, Subtítulo y Cuerpo) estrictamente verificado.
6. **Exportar:** Utiliza el botón superior derecho para compartir nativamente como archivo de Microsoft Word (`.docx`).

## Estructura de Módulos
El proyecto utiliza **Clean Architecture** y patrón **Strategy** fuertemente:
- Las llamadas de red están en `data/remote`.
- Si necesitas adherir otra red social (ej. Instagram o TikTok), crea una clase implementando [UrlAudioProvider](file:///C:/Users/Dell/OneDrive/Escritorio/app%20cin/app/src/main/java/com/example/autochindy/domain/repository/UrlAudioProvider.kt#6-27) y añádela al inyector en [UrlModuleDetector](file:///C:/Users/Dell/OneDrive/Escritorio/app%20cin/app/src/main/java/com/example/autochindy/data/remote/url/UrlModuleDetector.kt#10-30).
