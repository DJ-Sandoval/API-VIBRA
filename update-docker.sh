#!/bin/bash

# Script para actualizar el contenedor Docker de vibra-api
# Autor: Generado por IA
# Fecha: $(date +%Y-%m-%d)

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para mostrar mensajes
print_message() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Función para verificar si el comando fue exitoso
check_success() {
    if [ $? -eq 0 ]; then
        print_success "$1 completado exitosamente"
    else
        print_error "$1 falló"
        exit 1
    fi
}

# Función principal
main() {
    print_message "==================== INICIANDO ACTUALIZACIÓN DE VIBRA-API ===================="
    print_message "Fecha y hora: $(date)"

    # 1. Verificar que estamos en el directorio correcto
    if [ ! -f "pom.xml" ] && [ ! -f "build.gradle" ]; then
        print_error "No se encontró pom.xml o build.gradle. ¿Estás en el directorio raíz del proyecto?"
        exit 1
    fi

    # 2. Construir el proyecto con Maven o Gradle
    print_message "Paso 1/4: Construyendo el proyecto..."
    if [ -f "pom.xml" ]; then
        print_message "Detectado proyecto Maven, ejecutando 'mvn clean package'..."

        # Preguntar si quiere saltar los tests
        read -p "¿Saltar tests? (s/n): " skip_tests

        if [ "$skip_tests" = "s" ] || [ "$skip_tests" = "S" ]; then
            mvn clean package -DskipTests
            check_success "Construcción Maven (con tests saltados)"
        else
            mvn clean package
            check_success "Construcción Maven"
        fi

    elif [ -f "build.gradle" ]; then
        print_message "Detectado proyecto Gradle, ejecutando 'gradle build'..."
        gradle clean build
        check_success "Construcción Gradle"
    else
        print_error "No se encontró ni pom.xml ni build.gradle"
        exit 1
    fi

    # 3. Verificar que el JAR existe
    print_message "Paso 2/4: Verificando el archivo JAR..."
    if ! ls target/*.jar >/dev/null 2>&1 && ! ls build/libs/*.jar >/dev/null 2>&1; then
        print_error "No se encontró el archivo JAR en target/ o build/libs/"
        exit 1
    fi

    # 4. Detener el contenedor actual (si existe)
    print_message "Paso 3/4: Deteniendo el contenedor actual..."
    if docker ps | grep -q "vibra-api"; then
        print_message "Deteniendo contenedor vibra-api..."
        docker stop vibra-api
        check_success "Detención del contenedor"
    else
        print_warning "El contenedor vibra-api no está corriendo"
    fi

    # 5. Remover el contenedor (si existe)
    if docker ps -a | grep -q "vibra-api"; then
        print_message "Eliminando contenedor vibra-api..."
        docker rm vibra-api
        check_success "Eliminación del contenedor"
    fi

    # 6. Reconstruir la imagen
    print_message "Paso 4/4: Reconstruyendo imagen y levantando contenedor..."
    docker-compose build
    check_success "Construcción de imagen"

    # 7. Levantar el contenedor en modo detached
    docker-compose up -d
    check_success "Inicio del contenedor"

    # 8. Mostrar logs por unos segundos
    print_message "==================== CONTENEDOR INICIADO ===================="
    print_message "Mostrando logs de los últimos 10 segundos..."
    sleep 2
    docker logs vibra-api --tail 20

    print_success "¡Actualización completada!"
    print_message "Para ver los logs en tiempo real: docker logs -f vibra-api"
    print_message "Para ver el estado: docker ps"

    # 9. Preguntar si quiere ver los logs en tiempo real
    read -p "¿Ver logs en tiempo real? (s/n): " show_logs
    if [ "$show_logs" = "s" ] || [ "$show_logs" = "S" ]; then
        docker logs -f vibra-api
    fi
}

# Función de ayuda
show_help() {
    echo "Uso: ./update-docker.sh [opciones]"
    echo ""
    echo "Opciones:"
    echo "  -h, --help    Mostrar esta ayuda"
    echo "  -s, --skip-tests  Saltar los tests durante la construcción"
    echo "  -l, --logs    Ver logs después de la actualización"
    echo ""
    echo "Ejemplos:"
    echo "  ./update-docker.sh          # Actualizar con tests"
    echo "  ./update-docker.sh -s      # Actualizar saltando tests"
    echo "  ./update-docker.sh -sl     # Saltar tests y ver logs"
}

# Procesar argumentos
SKIP_TESTS=false
SHOW_LOGS=false

while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -s|--skip-tests)
            SKIP_TESTS=true
            shift
            ;;
        -l|--logs)
            SHOW_LOGS=true
            shift
            ;;
        *)
            print_error "Opción desconocida: $1"
            show_help
            exit 1
            ;;
    esac
done

# Ejecutar script
main