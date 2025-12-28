#!/bin/bash

# Cross-platform backend startup script for SimplySocial
# Works on both macOS and Linux

set -e

# Color definitions for better output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[$(date '+%H:%M:%S')]${NC} ${GREEN}$1${NC}"
}

print_warning() {
    echo -e "${BLUE}[$(date '+%H:%M:%S')]${NC} ${YELLOW}$1${NC}"
}

print_error() {
    echo -e "${BLUE}[$(date '+%H:%M:%S')]${NC} ${RED}$1${NC}"
}

# Function to detect OS
detect_os() {
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        echo "linux"
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        echo "macos"
    else
        echo "unknown"
    fi
}

# Function to clear screen (cross-platform)
clear_screen() {
    if command -v clear >/dev/null 2>&1; then
        clear
    elif command -v cls >/dev/null 2>&1; then
        cls
    else
        printf '\033[2J\033[H'
    fi
}

# Function to check if Java is installed
check_java() {
    if command -v java >/dev/null 2>&1; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
        print_status "Java found: $JAVA_VERSION"
        return 0
    else
        return 1
    fi
}

# Function to install Java based on OS
install_java() {
    local os=$(detect_os)
    print_warning "Java not found. Installing Java..."
    
    case $os in
        "linux")
            if command -v apt-get >/dev/null 2>&1; then
                sudo apt-get update
                sudo apt-get install -y openjdk-21-jdk
            elif command -v yum >/dev/null 2>&1; then
                sudo yum install -y java-21-openjdk-devel
            elif command -v dnf >/dev/null 2>&1; then
                sudo dnf install -y java-21-openjdk-devel
            else
                print_error "Unable to install Java automatically. Please install Java 21 manually."
                exit 1
            fi
            ;;
        "macos")
            if command -v brew >/dev/null 2>&1; then
                brew install openjdk@21
                echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
                export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
            else
                print_error "Homebrew not found. Please install Java 21 manually or install Homebrew first."
                exit 1
            fi
            ;;
        *)
            print_error "Unsupported OS. Please install Java 21 manually."
            exit 1
            ;;
    esac
}

# Function to check if Maven is available (including wrapper)
check_maven() {
    if [[ -f "./mvnw" ]]; then
        print_status "Maven wrapper found"
        MAVEN_CMD="./mvnw"
        return 0
    elif command -v mvn >/dev/null 2>&1; then
        print_status "Maven found: $(mvn -version | head -n 1)"
        MAVEN_CMD="mvn"
        return 0
    else
        return 1
    fi
}

# Function to install Maven based on OS
install_maven() {
    local os=$(detect_os)
    print_warning "Maven not found and no wrapper available. Installing Maven..."
    
    case $os in
        "linux")
            if command -v apt-get >/dev/null 2>&1; then
                sudo apt-get update
                sudo apt-get install -y maven
            elif command -v yum >/dev/null 2>&1; then
                sudo yum install -y maven
            elif command -v dnf >/dev/null 2>&1; then
                sudo dnf install -y maven
            else
                print_error "Unable to install Maven automatically. Please install Maven manually."
                exit 1
            fi
            ;;
        "macos")
            if command -v brew >/dev/null 2>&1; then
                brew install maven
            else
                print_error "Homebrew not found. Please install Maven manually or install Homebrew first."
                exit 1
            fi
            ;;
        *)
            print_error "Unsupported OS. Please install Maven manually."
            exit 1
            ;;
    esac
    MAVEN_CMD="mvn"
}

# Function to monitor logs with periodic screen clearing
monitor_logs() {
    local log_file="$1"
    local clear_interval=30  # Clear screen every 30 seconds
    local last_clear=$(date +%s)
    
    print_status "Monitoring logs... (Press Ctrl+C to stop)"
    print_warning "Screen will clear every ${clear_interval} seconds"
    echo ""
    
    tail -f "$log_file" | while read line; do
        current_time=$(date +%s)
        
        # Clear screen periodically
        if (( current_time - last_clear >= clear_interval )); then
            clear_screen
            echo -e "${PURPLE}=== SimplySocial Backend Logs ===${NC}"
            echo -e "${CYAN}Last cleared: $(date)${NC}"
            echo -e "${YELLOW}Press Ctrl+C to stop${NC}"
            echo ""
            last_clear=$current_time
        fi
        
        # Color code log levels
        if [[ $line == *"ERROR"* ]]; then
            echo -e "${RED}$line${NC}"
        elif [[ $line == *"WARN"* ]]; then
            echo -e "${YELLOW}$line${NC}"
        elif [[ $line == *"INFO"* ]]; then
            echo -e "${GREEN}$line${NC}"
        elif [[ $line == *"DEBUG"* ]]; then
            echo -e "${BLUE}$line${NC}"
        else
            echo "$line"
        fi
    done
}

# Main script execution
main() {
    clear_screen
    echo -e "${PURPLE}================================${NC}"
    echo -e "${PURPLE}  SimplySocial Backend Startup  ${NC}"
    echo -e "${PURPLE}================================${NC}"
    echo ""
    
    # Detect OS
    local os=$(detect_os)
    print_status "Detected OS: $os"
    
    # Navigate to server directory
    if [[ ! -d "server" ]]; then
        print_error "Server directory not found. Please run this script from the SimplySocial root directory."
        exit 1
    fi
    
    cd server
    print_status "Changed to server directory"
    
    # Check and install Java if needed
    if ! check_java; then
        install_java
        if ! check_java; then
            print_error "Java installation failed"
            exit 1
        fi
    fi
    
    # Check and install Maven if needed
    if ! check_maven; then
        install_maven
        if ! check_maven; then
            print_error "Maven installation failed"
            exit 1
        fi
    fi
    
    # Install dependencies
    print_status "Installing dependencies..."
    $MAVEN_CMD clean install -DskipTests
    
    if [[ $? -ne 0 ]]; then
        print_error "Dependency installation failed"
        exit 1
    fi
    
    print_status "Dependencies installed successfully!"
    
    # Create logs directory if it doesn't exist
    mkdir -p logs
    LOG_FILE="logs/application.log"
    
    # Start the backend in background and capture logs
    print_status "Starting SimplySocial backend..."
    print_warning "Starting server in background..."
    
    # Start Spring Boot application with logging to file
    $MAVEN_CMD spring-boot:run -Dspring-boot.run.jvmArguments="-Dlogging.file.name=$LOG_FILE" > /dev/null 2>&1 &
    BACKEND_PID=$!
    
    # Wait a moment for the application to start
    sleep 5
    
    # Check if the process is still running
    if ! kill -0 $BACKEND_PID 2>/dev/null; then
        print_error "Backend failed to start"
        exit 1
    fi
    
    print_status "Backend started with PID: $BACKEND_PID"
    
    # Create a simple log file if it doesn't exist yet
    if [[ ! -f "$LOG_FILE" ]]; then
        echo "$(date): SimplySocial backend starting..." > "$LOG_FILE"
    fi
    
    # Set up trap to kill backend on script exit
    trap "print_warning 'Stopping backend...'; kill $BACKEND_PID 2>/dev/null; exit 0" INT TERM EXIT
    
    # Monitor logs with periodic clearing
    monitor_logs "$LOG_FILE"
}

# Run main function
main "$@"