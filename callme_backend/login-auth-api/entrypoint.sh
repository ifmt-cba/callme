#!/bin/sh

# Função para parar o Tailscale de forma limpa ao sair
cleanup() {
    echo "Parando o Tailscale..."
    # Tenta parar o daemon do tailscaled se ele ainda estiver rodando
    PID_TAILSCALED=$(pidof tailscaled)
    if [ -n "$PID_TAILSCALED" ]; then
        kill "$PID_TAILSCALED"
    fi
    echo "Tailscale parado."
}
# Executa a função cleanup quando o script receber um sinal de EXIT, INT ou TERM
trap cleanup EXIT INT TERM

# Inicia o daemon do Tailscale em background
echo "Iniciando o Tailscale daemon..."
tailscaled --tun=userspace-networking --socks5-server=localhost:1055 --outbound-http-proxy-listen=localhost:1055 --statedir=/var/lib/tailscale &

# Aguarda um pouco para o daemon iniciar
sleep 3

# Conecta o Tailscale à sua rede
echo "Conectando ao Tailscale..."
tailscale up \
    --authkey="${TAILSCALE_AUTHKEY}" \
    --hostname="${TAILSCALE_HOSTNAME:-callme-backend-$(hostname)}" \
    --accept-routes \
    --advertise-exit-node="${TAILSCALE_ADVERTISE_EXIT_NODE:-false}"

# Aguarda um pouco para o Tailscale conectar e as rotas serem estabelecidas
echo "Aguardando conexão do Tailscale..."
sleep 10 # Pode aumentar se necessário

# Verifica o status do Tailscale (para debug)
echo "Status do Tailscale:"
tailscale status || echo "Falha ao obter status do Tailscale (pode não estar totalmente conectado ainda)"

# Lista os IPs do Tailscale (para debug)
echo "IPs do Tailscale:"
tailscale ip -4 || echo "Nenhum IP v4 do Tailscale atribuído ainda"
tailscale ip -6 || echo "Nenhum IP v6 do Tailscale atribuído ainda"

# --- Adicionar Tailscale Netcheck para Diagnóstico ---
echo "Executando Tailscale netcheck..."
tailscale netcheck
# ----------------------------------------------------

# Configura as propriedades de sistema Java para usar o proxy HTTP do Tailscale
# para acessar o MinIO (e outras URLs HTTP se necessário)
JAVA_OPTS=""
# O proxy HTTP está escutando em localhost:1055 dentro do container
JAVA_OPTS="$JAVA_OPTS -Dhttp.proxyHost=localhost -Dhttp.proxyPort=1055"
# Se você precisar que localhost e 127.0.0.1 NÃO passem pelo proxy:
JAVA_OPTS="$JAVA_OPTS -Dhttp.nonProxyHosts=localhost|127.0.0.1"
# Se você também fosse acessar MinIO via HTTPS, adicionaria:
# JAVA_OPTS="$JAVA_OPTS -Dhttps.proxyHost=localhost -Dhttps.proxyPort=1055"
# JAVA_OPTS="$JAVA_OPTS -Dhttps.nonProxyHosts=localhost|127.0.0.1"

echo "Configurações Java (JAVA_OPTS): ${JAVA_OPTS}"

# Finalmente, executa sua aplicação Spring Boot.
echo "Iniciando a aplicação Spring Boot..."
cd /app
exec java ${JAVA_OPTS} -jar app.jar
