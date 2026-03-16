$ErrorActionPreference = "Stop"

$AndroidSdkPath = "C:\AndroidDK"
$CmdLineToolsZip = "$AndroidSdkPath\cmdline-tools.zip"
$CmdLineToolsUrl = "https://dl.google.com/android/repository/commandlinetools-win-11479360_latest.zip"

if (-not (Test-Path "$AndroidSdkPath\cmdline-tools\latest\bin\sdkmanager.bat")) {
    Write-Host "Creando directorio del SDK..."
    New-Item -ItemType Directory -Force -Path "$AndroidSdkPath\cmdline-tools"

    Write-Host "Descargando Android Command Line Tools..."
    Invoke-WebRequest -Uri $CmdLineToolsUrl -OutFile $CmdLineToolsZip

    Write-Host "Extrayendo herramientas..."
    Expand-Archive -Path $CmdLineToolsZip -DestinationPath "$AndroidSdkPath\cmdline-tools" -Force
    
    # Mover a la carpeta 'latest' como lo requiere sdkmanager
    Rename-Item -Path "$AndroidSdkPath\cmdline-tools\cmdline-tools" -NewName "latest"
}

$env:ANDROID_HOME = $AndroidSdkPath
$SdkManager = "$AndroidSdkPath\cmdline-tools\latest\bin\sdkmanager.bat"

Write-Host "Aceptando licencias..."
Invoke-Command -ScriptBlock {
    echo y | & $SdkManager --licenses
}

Write-Host "Instalando plataformas y build-tools..."
& $SdkManager "platforms;android-34" "build-tools;34.0.0" "platform-tools"

$GradleZip = "C:\gradle.zip"
$GradleExtract = "C:\gradle_extracted"
$GradleUrl = "https://services.gradle.org/distributions/gradle-8.4-bin.zip"

if (-not (Test-Path "$GradleExtract\gradle-8.4\bin\gradle.bat")) {
    Write-Host "Descargando Gradle..."
    Invoke-WebRequest -Uri $GradleUrl -OutFile $GradleZip

    Write-Host "Extrayendo Gradle..."
    Expand-Archive -Path $GradleZip -DestinationPath $GradleExtract -Force
}

Write-Host "Listo. Dependencias instaladas en C:\AndroidDK y C:\gradle_extracted."
