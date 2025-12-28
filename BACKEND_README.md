# SimplySocial Backend Startup Script

This script automatically installs dependencies and starts the SimplySocial backend server with real-time log monitoring and periodic screen clearing.

## Features

- ✅ **Cross-platform**: Works on both macOS and Linux
- ✅ **Auto dependency installation**: Installs Java and Maven if not present
- ✅ **Real-time logs**: Displays colorized logs with periodic screen clearing
- ✅ **Clean interface**: Clears screen every 30 seconds for better readability
- ✅ **Graceful shutdown**: Properly stops the backend when interrupted

## Prerequisites

The script will automatically install missing dependencies, but you may need:

### Linux
- `apt-get`, `yum`, or `dnf` package manager (for auto-installation)
- `sudo` privileges (for installing Java/Maven)

### macOS
- [Homebrew](https://brew.sh/) (for auto-installation)

## Usage

1. **Make the script executable** (if not already):
   ```bash
   chmod +x start-backend.sh
   ```

2. **Run the script** from the SimplySocial root directory:
   ```bash
   ./start-backend.sh
   ```

## What the script does

1. **Environment Check**: Detects your operating system
2. **Dependency Installation**: 
   - Checks for Java 21+ and installs if missing
   - Checks for Maven or uses Maven wrapper (`mvnw`)
3. **Build Process**: Runs `mvn clean install -DskipTests`
4. **Server Startup**: Starts the Spring Boot application in background
5. **Log Monitoring**: Displays real-time logs with color coding:
   - 🔴 **Red**: ERROR messages
   - 🟡 **Yellow**: WARN messages  
   - 🟢 **Green**: INFO messages
   - 🔵 **Blue**: DEBUG messages
6. **Screen Management**: Clears screen every 30 seconds for clean viewing

## Stopping the server

Press `Ctrl+C` to stop the script and the backend server.

## Log Files

Logs are also saved to `server/logs/application.log` for persistent storage.

## Troubleshooting

### Permission Issues
```bash
chmod +x start-backend.sh
```

### Java Installation Issues
If auto-installation fails, manually install Java 21:

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install openjdk-21-jdk
```

**macOS:**
```bash
brew install openjdk@21
```

### Maven Issues
The script prefers the Maven wrapper (`mvnw`) if available, otherwise uses system Maven.

### Port Already in Use
If port 8080 is busy, check for other Spring Boot instances:
```bash
lsof -i :8080
kill -9 <PID>
```

## Configuration

The script uses these default settings:
- **Log clear interval**: 30 seconds
- **Server port**: 8080 (Spring Boot default)
- **Log file**: `server/logs/application.log`

To modify the screen clear interval, edit the `clear_interval` variable in the script.