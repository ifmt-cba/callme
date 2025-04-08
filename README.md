> [!caution]
> .env no repositorio é provisorio

# Call Me - Sistema de Gestão de Chamados

## Visão do Produto

O **Call Me** é um sistema de gestão de chamados desenvolvido para otimizar e organizar o atendimento de solicitações em diferentes contextos, como suporte técnico e atendimento ao cliente. Ele permite o registro, acompanhamento e resolução de chamados de forma eficiente, garantindo transparência e melhorando a comunicação entre usuários e administradores.

---

## Descrição do Projeto

Este projeto foi desenvolvido como parte da disciplina de **Engenharia de Software**, utilizando metodologias ágeis. A arquitetura foi desenhada para ser **modular, escalável, segura e de fácil manutenção**, integrando tecnologias modernas e boas práticas de desenvolvimento.

---

## Tecnologias Utilizadas

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

## Funcionalidades Principais

O sistema permite o cadastro e autenticação de usuários, a abertura e acompanhamento de chamados, além de notificações em tempo real por meio de emails. Conta com um painel administrativo para gerenciamento dos chamados e funcionalidades que possibilitam a análise do histórico e a geração de relatórios de atendimento.

---

## Variáveis de Ambiente

As variáveis estão definidas no arquivo `.env` em `./callme_backend`. Certifique-se de criar esse arquivo antes de subir os containers:

```dotenv
POSTGRES_DB=callme
POSTGRES_USER=callme
POSTGRES_PASSWORD=callme
POSTGRES_HOST=localhost
POSTGRES_PORT=5433
```
> [!CAUTION]
> **Importante:** nunca commit esse arquivo `.env` com credenciais reais em repositórios públicos.

---

## Como Rodar o Projeto

### 1. Clone o Repositório

```bash
git clone https://github.com/ifmt-cba/callme.git
```

### 2. Suba os containers com Docker
> Certifique-se de que o Docker está instalado e em execução.
```bash
docker compose up -d
```

## Acesso ao pgAdmin (GUI para PostgreSQL)

O pgAdmin está rodando em um container Docker junto com o resto do ambiente. Para acessá‑lo via navegador, siga estes passos:

### **Verifique as variáveis de ambiente**  

No arquivo `.env` você deve ter configurado:
```dotenv
PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=admin
```
> Essas credenciais serão usadas para fazer login no pgAdmin.

### **Suba os containers**

Execute:
```bash
docker compose up -d
```
> Isso iniciará, entre outros, o serviço pgadmin, que por padrão está mapeado para a porta 5050 da sua máquina.

### **Abra o navegador**

Acesse:
```
http://localhost:5050
```
> Você verá a tela de login do pgAdmin.

### **Login no pgAdmin**

- E‑mail: use o valor de `PGADMIN_DEFAULT_EMAIL`, por exemplo: `admin@admin.com`.
- Senha: use o valor de `PGADMIN_DEFAULT_PASSWORD`, por exemplo: `admin`.

### **Adicionar o servidor PostgreSQL**
Após o login, clique com o botão direito em “Servers” (ou use o menu “Add New Server”).

- Name: ```callme```.</br>
- Connection > Host ```callme/address: db```</br>
- Port: ```5432```</br>
- Maintenance database: ```callme```</br>
- Username: ```callme```</br>
- Password: ```callme```</br>

Clique em Save para se conectar.

---

## Padrão de Commits & Fluxo de Contribuição 

### Commit Messages

> [!IMPORTANT]
> Para manter uma melhor organização é recomendado que se use o padrão dos coventional commits:
```cmd
<tipo>: <descrição-curta>
```
> [!TIP]
> **tipos:** </br></br>
> 1. **feat:** *Adição de novas funcionalidades* </br>
> 2. **fix:** *Correção de bugs* </br>
> 3. **docs:** *Commits refentes a mudanças na documentação* </br>
> 4. **style:** *Alterações referentes a formatações na apresentação do código que não afetam o significado do código* </br>
> 5. **refactor:** *Qualquer mudança no código que não altere a funcionalidade dele* </br>
> 6. **perf:** *Uma alteração de código que melhora o desempenho* </br>
> 7. **test:** *Adição ou correção de testes* </br>
> 8. **chore:** *Atualização de tarefas que não ocasionam alteração no código, mas mudanças de ferramentas, mudanças de configuração e bibliotecas* </br>

## Fluxo de Trabalho

### 3. Dar pull

```bash
git stash
git pull origin master --rebase
git stash apply
```
> [!TIP]
> `git stash` e `git stash apply`, só se aplicam caso você já tenha feito mudanças em algum arquivo antes de dar o pull

> [!WARNING]
> `sempre` que abrirem o código veriquem se não possui mudanças no repositorio `(pulls pendentes)`

### 2. Criar branch

```bash
git checkout -b <pequena-descrição>
```
> [!CAUTION]
> utilize somente `-` para separar palavras, não utilize `_` e nem `ESPAÇOS`!

### 3. Commitar

```bash
git add <diretorio do arquivo>
git commit -m "feat: adicionar endpoint de login"
```
> [!TIP]
> A descrição tem que ser preferencialmente em inglês!

### 4. Push da branch

```bash
git push origin <nome-da-branch>
```

### 5. Abrir Pull Request

No github abra um `pull request` e espere alguém revisar, evitem margear sem ter review de outra pessoa, isso pode ajudar caso tenha passado algum erro despercebido!

### 6. Revisão & Merge

Aguarde a aprovação, ajuste o que for solicitado e após alguém revisar faça o merge

### 7. Atualizar GitHub Projects

Mova o cartão da tarefa para `Done`

---

### Boas Práticas

* **Código Limpo:** *siga princípios de Clean Code e Object Calisthenics.*
* **Revisões de Código:** *todo PR deve ter ao menos 1 review aprovado.*
* **Testes Automatizados:** *escreva testes unitários e automatizados.*
* **Documentação:** *mantenha Swagger e README atualizados.*
* **Segurança:** *nunca exponha credenciais; use variáveis de ambiente.*
* **Modularidade:** *separe responsabilidades em pacotes/módulos claros.*
* **Comunicação:** *descreva mudanças relevantes no PR e atualize a equipe via GitHub Projects, Whatsapp ou Discord.*
