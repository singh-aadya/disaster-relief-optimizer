@echo off
echo Compiling SupplyMate Java Application...
cd src

echo Compiling Java files...
javac *.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo Running SupplyMate...
echo.
java SupplyMate

pause