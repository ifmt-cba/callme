# Estágio 1: Build da Aplicação Java com Maven
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
# Roda o build do Maven. Se houver erros aqui, o JAR não será criado.
RUN mvn clean package -DskipTests

# Comando de diagnóstico: Lista o conteúdo da pasta target APÓS o build do Maven.
# Procure por esta saída nos LOGS DO BUILD da imagem.
RUN echo "--- Conteúdo de /app/target/ no estágio de build ---" && ls -l /app/target/ && echo "-------------------------------------------------------"

# Estágio 2: Imagem Final com OpenJDK, Tailscale e a Aplicação
FROM openjdk:21-jdk-bookworm
WORKDIR /app

# Copia o JAR da aplicação do estágio de build para o estágio final.
# O *.jar deve encontrar o JAR produzido pelo Maven e copiá-lo como app.jar
COPY --from=build /app/target/*.jar app.jar

# --- Início da Seção de Instalação do Tailscale ---
USER root

# Comandos de Diagnóstico (para ver nos logs do build se algo falhar)
RUN echo "--- Diagnostic Info (Pre-Tailscale Install) ---" && \
    echo "Running as: $(whoami)" && \
    echo "PATH is: $PATH" && \
    echo "Verifying apt-get presence..." && \
    which apt-get && \
    ls -l /usr/bin/apt-get && \
    echo "--- End Diagnostic Info (Pre-Tailscale Install) ---"

# Instalar dependências e Tailscale usando apt-get
RUN echo "Updating package lists..." && \
    apt-get update && \
    echo "Installing dependencies: curl, gnupg, ca-certificates, wget..." && \
    apt-get install -y --no-install-recommends curl gnupg ca-certificates wget && \
    echo "Adding Tailscale repository..." && \
    curl -fsSL https://pkgs.tailscale.com/stable/debian/bookworm.noarmor.gpg | tee /usr/share/keyrings/tailscale-archive-keyring.gpg >/dev/null && \
    curl -fsSL https://pkgs.tailscale.com/stable/debian/bookworm.tailscale-keyring.list | tee /etc/apt/sources.list.d/tailscale.list && \
    echo "Updating package lists again after adding Tailscale repo..." && \
    apt-get update && \
    echo "Installing Tailscale..." && \
    apt-get install -y --no-install-recommends tailscale && \
    echo "Cleaning up apt cache..." && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    echo "Tailscale installation complete."
# --- Fim da Seção de Instalação do Tailscale ---

# Comando de diagnóstico: Lista o conteúdo da pasta /app APÓS copiar o JAR.
# Procure por esta saída nos LOGS DO BUILD da imagem.
RUN echo "--- Conteúdo de /app no estágio final (após copiar app.jar) ---" && ls -l /app && echo "------------------------------------------------------------"

# Copia o script de inicialização para dentro da imagem
COPY entrypoint.sh /entrypoint.sh
# Dá permissão de execução para o script
RUN chmod +x /entrypoint.sh

# Define que o entrypoint.sh será executado quando o contêiner iniciar
ENTRYPOINT ["/entrypoint.sh"]

# A aplicação Java (e o entrypoint.sh) rodará como root por enquanto.