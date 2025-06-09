# 📞 Call Me - Sistema de Gestão de Chamados

## 🧭 Visão do Produto

O **Call Me**  é um sistema web de gestão de chamados projetado para centralizar, organizar e otimizar o atendimento de solicitações em ambientes como suporte técnico, atendimento ao cliente e serviços internos. A plataforma permite o registro, acompanhamento e resolução estruturada de chamados, promovendo maior agilidade no atendimento, rastreabilidade das interações e transparência nos processos.

Por meio de uma interface intuitiva e recursos como notificações automáticas, atribuição de responsabilidades e histórico completo de cada chamado, o Call Me melhora significativamente a comunicação entre usuários e administradores, reduzindo falhas operacionais e aumentando a satisfação dos envolvidos.

---

## 🛠️ Descrição do Projeto

 Diagnóstico do Problema
Contextualização: Muitas empresas e instituições enfrentam dificuldades na gestão eficiente de chamados de suporte técnico, resultando em atrasos no atendimento e falta de controle sobre solicitações. Este projeto visa desenvolver um sistema que automatize a criação, organização e acompanhamento de chamados via e-mail.
## 
Público-Alvo: A SEMAS e outras instituições que necessitam de um sistema automatizado para registro e acompanhamento de chamados.
## 
Justificativa: A falta de um sistema estruturado pode levar a falhas na comunicação e na resolução de problemas técnicos. A extensão universitária permite o desenvolvimento e aplicação de soluções inovadoras para este cenário, beneficiando tanto a comunidade acadêmica quanto setores administrativos.


---

## ⚙️ Tecnologias Utilizadas

- **Backend:** Java 21 com Spring Boot 3  
- **Frontend:** Angular 17+  
- **Banco de Dados:** PostgreSQL (Alpine)  
- **Autenticação:** JWT com Spring Security  
- **Hash de Senha:** ?  
- **Gerenciamento de Containers:** Docker  
- **Versionamento:** Git + GitHub  
- **Gerenciamento de Tarefas (Scrum):** GitHub Projects  
- **Documentação e Testes da API:** (em definição)  

---

## ✨ Funcionalidades Principais

- Cadastro e autenticação de usuários  
- Abertura e acompanhamento de chamados  
- Notificações em tempo real via email  
- Painel administrativo completo  
- Histórico e relatórios de atendimento  

---

## 🔐 Variáveis de Ambiente

As variáveis estão definidas no arquivo `.env` em `./callme_backend`. Crie esse arquivo antes de subir os containers:

```dotenv
POSTGRES_DB=callme
POSTGRES_USER=callme
POSTGRES_PASSWORD=callme
POSTGRES_HOST=database
POSTGRES_PORT=5432
PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=admin
```

> ⚠️ **Importante:** nunca envie esse arquivo `.env` com credenciais reais para repositórios públicos.

> [!CAUTION]
> `.env` no repositório é **provisório** — **não use em produção!**


---

## 🚀 Como Rodar o Projeto

### 1. Clone o Repositório

```bash
git clone https://github.com/ifmt-cba/callme.git
```

### 2. Suba os containers com Docker

> Certifique-se de que o Docker está instalado e em execução.

```bash
docker compose up -d
```

---

## 🗃️ Acesso ao pgAdmin (GUI para PostgreSQL)

### 🔎 Verifique as variáveis de ambiente

```dotenv
PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=admin
```

### 🔼 Suba os containers

```bash
docker compose up -d
```

> Isso iniciará o serviço pgAdmin, mapeado na porta `5050` da sua máquina.

### 🌐 Acesse via navegador

```
http://localhost:5050
```

### 🔐 Login no pgAdmin

- Email: `admin@admin.com`  
- Senha: `admin`  

### ➕ Adicionar servidor PostgreSQL

1. Clique com o botão direito em **Servers > Add New Server**
2. Preencha os dados:

```
Name: callme
Connection > Host: db
Port: 5432
Maintenance database: callme
Username: callme
Password: callme
```

Clique em **Save**.

---

## 🧾 Padrão de Commits & Fluxo de Contribuição 

### ✅ Commit Messages

> Para manter a organização, siga o padrão dos **Conventional Commits**:

```bash
<tipo>: <descrição-curta>
```

**Tipos recomendados:**  

- **feat:** nova funcionalidade  
- **fix:** correção de bugs  
- **docs:** alterações na documentação  
- **style:** mudanças de formatação (semântica preservada)  
- **refactor:** refatoração de código  
- **perf:** melhoria de desempenho  
- **test:** testes adicionados/corrigidos  
- **chore:** tarefas auxiliares (configs, deps...)  

---

## 🔄 Fluxo de Trabalho

### 1. Verifique atualizações

```bash
git stash
git pull origin master --rebase
git stash apply
```

> Use `stash` apenas se houver mudanças locais.

### 2. Crie uma branch

```bash
git checkout -b minha-nova-feature
```

> Use `-` para separar palavras. Não use `_` nem espaços!

### 3. Faça commits

```bash
git add caminho/do/arquivo
git commit -m "feat: adicionar endpoint de login"
```

> Preferencialmente use **descrições em inglês.**

### 4. Envie sua branch

```bash
git push origin minha-nova-feature
```

### 5. Abra um Pull Request

No GitHub, crie um PR e aguarde revisão de outro colaborador.

### 6. Revisão & Merge

Após ajustes e aprovação, realize o merge.

### 7. Atualize o GitHub Projects

Mova o cartão da tarefa para **Done**.

---

## 🌟 Boas Práticas

- **Clean Code:** siga princípios de legibilidade e simplicidade  
- **Revisões:** todo PR deve ter ao menos 1 aprovação  
- **Testes:** escreva testes automatizados  
- **Documentação:** mantenha Swagger e README atualizados  
- **Segurança:** não exponha dados sensíveis  
- **Modularidade:** separe bem as responsabilidades  
- **Comunicação:** mantenha o time informado (GitHub, WhatsApp, Discord)
