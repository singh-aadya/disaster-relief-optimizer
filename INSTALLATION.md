# SupplyMate Installation Guide

## Prerequisites

### Java Development Kit (JDK)
SupplyMate requires Java JDK 8 or higher to compile and run.

#### Installing Java on Windows:
1. Download OpenJDK from https://adoptium.net/
2. Choose the latest LTS version (Java 17 or 21 recommended)
3. Install the MSI package
4. Verify installation by opening Command Prompt and running:
   ```
   java -version
   javac -version
   ```

#### Alternative Installation Methods:
- **Chocolatey**: `choco install openjdk`
- **Winget**: `winget install Eclipse.Temurin.17.JDK`
- **Manual**: Download from Oracle or OpenJDK official sites

## Compilation and Execution

### Method 1: Using the Batch Script (Recommended)
1. Navigate to the project directory
2. Double-click `compile_and_run.bat`
3. The script will automatically compile and run the application

### Method 2: Manual Compilation
1. Open Command Prompt
2. Navigate to the `src` directory:
   ```
   cd "C:\Users\Aadya\Workspace\disaster_relief_optimizer\src"
   ```
3. Compile all Java files:
   ```
   javac *.java
   ```
4. Run the application:
   ```
   java SupplyMate
   ```

### Method 3: Running Tests
To run the test script:
```
cd src
javac test_script.java *.java
java test_script
```

## Project Structure
```
disaster_relief_optimizer/
├── README.md              # Project overview
├── INSTALLATION.md        # This file
├── compile_and_run.bat    # Windows compilation script
├── test_script.java       # Test runner
└── src/                   # Source code directory
    ├── SupplyMate.java           # Main application class
    ├── Family.java              # Family entity class
    ├── Supply.java              # Supply entity class
    ├── Inventory.java           # Inventory management
    ├── AllocationResult.java    # Allocation result container
    ├── SupplyAllocator.java     # Core allocation algorithms
    ├── ReportGenerator.java     # Report generation and export
    └── SampleDataGenerator.java # Test data generation
```

## Troubleshooting

### Common Issues:

1. **"javac is not recognized"**
   - Java JDK is not installed or not in PATH
   - Install JDK and restart Command Prompt

2. **"java.lang.ClassNotFoundException"**
   - Make sure you're in the src directory when running
   - Compile all .java files before running

3. **Permission Denied**
   - Run Command Prompt as Administrator
   - Check file permissions

4. **Out of Memory Errors**
   - Increase JVM heap size: `java -Xmx2g SupplyMate`

### Environment Variables:
If Java is installed but not recognized, add to PATH:
1. Open System Properties → Environment Variables
2. Add Java bin directory to PATH (e.g., `C:\Program Files\Eclipse Adoptium\jdk-17.0.8.101-hotspot\bin`)
3. Restart Command Prompt

## IDE Setup (Optional)

### Eclipse:
1. Create new Java project
2. Import existing source files
3. Set JRE to JDK 8+

### IntelliJ IDEA:
1. Open project from existing sources
2. Configure SDK to JDK 8+
3. Mark src as source root

### VS Code:
1. Install Java Extension Pack
2. Open project folder
3. Configure Java runtime

## Performance Notes
- Default heap size should be sufficient for normal usage
- For large datasets (>100 families), consider increasing heap size
- CSV export files are created in the src directory