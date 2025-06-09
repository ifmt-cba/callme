# Sistema de Gerenciamento de Chamados - Arquitetura

## Diagrama de Classes

```mermaid
classDiagram
    direction LR
    
    %% Entidades principais
    User "1" <|--|> "*" Role : possui
    User "1" <|--|> "*" ChamadoInterno : cria
    
    %% Controllers e suas dependências
    UserController "1" ..> "*" User : gerencia
    UserController "1" ..> "1" UserRepository : usa
    UserController "1" ..> "1" RoleRepository : usa
    
    TokenController "1" ..> "*" User : autentica
    TokenController "1" ..> "1" UserRepository : consulta
    
    ChamadoInternoController "1" ..> "*" ChamadoInterno : gerencia
    ChamadoInternoController "1" ..> "1" ChamadoInternoRepository : usa
    ChamadoInternoController "1" ..> "1" UserRepository : consulta
    
    ChamadoExternoController "1" ..> "*" ChamadoExterno : gerencia
    ChamadoExternoController "1" ..> "1" ChamadoExternoService : usa
    ChamadoExternoController "1" ..> "1" EmailReceiverService : usa
    
    %% Services e suas dependências
    ChamadoExternoService "1" ..> "1" ChamadoExternoRepository : usa
    ChamadoExternoService "1" ..> "*" ChamadoExterno : manipula
    EmailReceiverService "1" ..> "*" ChamadoExterno : cria
    
    %% Repositories e suas entidades
    UserRepository "1" ..> "*" User : persiste
    RoleRepository "1" ..> "*" Role : persiste
    ChamadoInternoRepository "1" ..> "*" ChamadoInterno : persiste
    ChamadoExternoRepository "1" ..> "*" ChamadoExterno : persiste
    
    class User {
        UUID userid
        String username
        String email
        String password
        String token
        LocalDateTime resetTokenExpiration
        getters()
        setters()
    }

    class Role {
        Long roleId
        String name
        Values RT
        Values ADMIN
        getters()
    }

    class ChamadoInterno {
        Long chamadoID
        String content
        Instant creationTimestamp
        getters()
        setters()
    }

    class ChamadoExterno {
        Long id
        String remetente
        String assunto
        String descricao
        String dataHora
        String tokenEmail
        String messageId
        StatusChamado status
    }

    class UserController {
        BCryptPasswordEncoder passwordEncoder
        newUser(CreateUserDto)
        getAllUsers()
        newUserAdmin(CreateUserDto)
        deleteUser(UUID)
    }

    class TokenController {
        JwtEncoder jwtEncoder
        BCryptPasswordEncoder passwordEncoder
        login(LoginRequestDTO)
    }

    class ChamadoInternoController {
        feed(page, pageSize)
        createChamado(ChamadoInternoDto)
        deleteChamado(Long)
    }

    class ChamadoExternoController {
        criarChamados()
        listarChamados()
        buscarChamadoPorToken(String)
        editarChamadoPorToken(String, ChamadoExterno)
    }

    class EmailReceiverController {
        listarEmailsCompletos()
        listarResumos()
    }

    class AnexoEmailController {
        visualizarAnexo(Long)
    }

    class UserRepository {
        findByEmail(String)
        findByEmailIgnoreCase(String)
        findByToken(String)
        findByUsername(String)
    }

    class RoleRepository {
        findByName(String)
    }

    class ChamadoInternoRepository {
        JpaRepository methods
    }

    class ChamadoExternoRepository {
        findByMessageId(String)
        findByTokenEmail(String)
    }

    class ChamadoExternoService {
        criarChamadosAPartirDeEmails(List~EmailResumoDTO~)
        listarChamados()
        buscarChamadoPorToken(String)
        editarChamadoPorToken(String, ChamadoExterno)
    }

    class EmailReceiverService {
        checkInbox()
        listarResumosEmails()
    }
```

## Dicionário de Dados

### Tabela: tb_users
| Campo | Tipo | Descrição |
|-------|------|-----------|
| user_id | UUID | Identificador único do usuário |
| username | VARCHAR | Nome de usuário (único) |
| email | VARCHAR | Email do usuário (único) |
| password | VARCHAR | Senha criptografada |
| token | VARCHAR | Token de autenticação |
| reset_token_expiration | TIMESTAMP | Data de expiração do token de reset |

### Tabela: tb_roles
| Campo | Tipo | Descrição |
|-------|------|-----------|
| role_id | BIGINT | Identificador único do papel |
| name | VARCHAR | Nome do papel (ADMIN ou RT) |

### Tabela: tb_users_roles (Relacionamento)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| user_id | UUID | Referência ao ID do usuário |
| role_id | BIGINT | Referência ao ID do papel |

### Tabela: tb_chamados_internos
| Campo | Tipo | Descrição |
|-------|------|-----------|
| chamado_id | BIGINT | Identificador único do chamado |
| user_id | UUID | Referência ao usuário que criou o chamado |
| content | VARCHAR | Conteúdo do chamado |
| creation_timestamp | TIMESTAMP | Data e hora de criação do chamado |

## DTOs (Data Transfer Objects)

### CreateUserDto
- username: String
- password: String
- email: String

### LoginRequestDTO
- username: String
- password: String

### RegisterRequestDTO
- name: String
- email: String
- password: String
- setor: String

## Fluxo de Funcionamento

1. **Criação de Usuários**
   - ➡️ Usuários podem ser criados como RT (padrão) ou ADMIN
   - ➡️ Apenas ADMINs podem criar outros ADMINs
   - ➡️ Senhas são criptografadas usando BCrypt

2. **Autenticação**
   - ➡️ Login via username/password
   - ➡️ Geração de token JWT
   - ➡️ Sistema de reset de senha com token temporário

3. **Gerenciamento de Chamados**
   - ➡️ Usuários podem criar chamados internos
   - ➡️ Cada chamado está vinculado ao usuário criador
   - ➡️ Registro automático de timestamp de criação

4. **Controle de Acesso**
   - ➡️ Dois níveis de acesso: ADMIN e RT
   - ➡️ ADMINs podem gerenciar outros usuários
   - ➡️ Sistema de autorização baseado em roles 


# 📁 Estrutura do Projeto Call Me

## 📊 Diagrama de Caso de Uso

O diagrama abaixo representa os principais atores e suas interações com o sistema Call Me:

```mermaid
graph TB
    subgraph "Call Me - Sistema de Gestão de Chamados"
        User(("👤 Usuário"))
        Admin(("👨‍💼 Administrador"))
        Tech(("👨‍🔧 Técnico"))
        
        %% Casos de uso do Usuário
        UC1["Cadastrar-se"]
        UC2["Fazer Login"]
        UC3["Abrir Chamado"]
        UC4["Acompanhar Chamado"]
        UC5["Receber Notificações"]
        UC6["Visualizar Histórico"]
        
        %% Casos de uso do Administrador
        UC7["Gerenciar Usuários"]
        UC8["Gerar Relatórios"]
        UC9["Configurar Sistema"]
        
        %% Casos de uso do Técnico
        UC10["Atender Chamados"]
        UC11["Atualizar Status"]
        UC12["Adicionar Comentários"]
        
        %% Relacionamentos do Usuário
        User --> UC1
        User --> UC2
        User --> UC3
        User --> UC4
        User --> UC5
        User --> UC6
        
        %% Relacionamentos do Administrador
        Admin --> UC7
        Admin --> UC8
        Admin --> UC9
        Admin --> UC2
        
        %% Relacionamentos do Técnico
        Tech --> UC10
        Tech --> UC11
        Tech --> UC12
        Tech --> UC2
        Tech --> UC5
    end
```

## 🏗️ Estrutura do Projeto

### 📂 Backend (Java + Spring Boot)

```
callme_backend/
├── login-auth-api/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── callme/
│   │   │   │           ├── config/
│   │   │   │           ├── controller/
│   │   │   │           ├── dto/
│   │   │   │           ├── model/
│   │   │   │           ├── repository/
│   │   │   │           ├── service/
│   │   │   │           └── security/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
└── .gitignore
```

### 📂 Frontend (Angular)

```
callme_frontend/
├── angular-frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   ├── services/
│   │   │   ├── models/
│   │   │   ├── guards/
│   │   │   └── shared/
│   │   ├── assets/
│   │   └── environments/
│   ├── package.json
│   └── angular.json
└── package-lock.json
```

## 🎯 Principais Funcionalidades por Ator

### 👤 Usuário Comum
- Cadastro e login no sistema
- Abertura de novos chamados
- Acompanhamento do status dos chamados
- Recebimento de notificações
- Visualização do histórico de chamados
- Atualização de dados pessoais

### 👨‍💼 Administrador
- Gerenciamento de usuários
- Geração de relatórios
- Configurações do sistema
- Monitoramento geral
- Definição de políticas de acesso

### 👨‍🔧 Técnico
- Atendimento aos chamados
- Atualização de status
- Adição de comentários e soluções
- Categorização de problemas
- Priorização de atendimentos

## 🔧 Tecnologias Utilizadas

### Backend
- Java 21
- Spring Boot 3
- Spring Security
- JWT Authentication
- PostgreSQL
- JPA/Hibernate
- Maven

### Frontend
- Angular 17+
- TypeScript
- Angular Material
- RxJS
- SCSS
- JWT Interceptor

### DevOps
- Docker
- Docker Compose
- Git
- GitHub Actions (CI/CD)

## 📡 Integrações

- Sistema de E-mail para notificações
- Autenticação JWT
- Banco de dados PostgreSQL
- Upload de arquivos
- Logs do sistema 
