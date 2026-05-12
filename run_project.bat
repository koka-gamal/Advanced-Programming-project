@echo off
title Clinic Appointment System
cd /d "%~dp0"

if not exist build\classes mkdir build\classes

echo Compiling Clinic Appointment System...
javac -d build\classes src\clinic\app\Main.java src\clinic\data\DataStore.java src\clinic\model\*.java src\clinic\ui\*.java

if errorlevel 1 (
    echo.
    echo Compilation failed.
    pause
    exit /b 1
)

echo.
echo Starting Clinic Appointment System...
java -cp build\classes clinic.app.Main

echo.
pause
