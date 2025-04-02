# Call Me - Sistema de Gestão de Chamados

## Visão do Produto

O **Call Me** é um sistema de gestão de chamados desenvolvido para otimizar e organizar o atendimento de solicitações em diferentes contextos, como suporte técnico e atendimento ao cliente. Ele permite o registro, acompanhamento e resolução de chamados de forma eficiente, garantindo transparência e melhorando a comunicação entre usuários e administradores.

## Descrição do Projeto

Este projeto foi desenvolvido como parte da disciplina de **Engenharia de Software**, utilizando metodologias ágeis para garantir um desenvolvimento eficiente e colaborativo. A solução foi projetada para ser escalável, segura e de fácil utilização, proporcionando uma experiência intuitiva para seus usuários.

## Tecnologias Utilizadas

O projeto utiliza Java Spring Boot para o backend e Angular para o frontend. O banco de dados escolhido foi o PostgreSQL, garantindo robustez e confiabilidade. A containerização é feita com Docker, permitindo facilidade na implantação. Para documentação e testes de API, são utilizados (*a definir). O controle de versão é feito através do GitHub, e o gerenciamento de tarefas é realizado por meio do GitHub Projects.

## Funcionalidades Principais

O sistema permite o cadastro e autenticação de usuários, a abertura e acompanhamento de chamados, além de notificações em tempo real. Conta com um painel administrativo para gerenciamento dos chamados e funcionalidades que possibilitam a análise do histórico e a geração de relatórios de atendimento.

## Como Rodar o Projeto

### Clone o Repositório

```cmd
git clone https://github.com/ifmt-cba/callme.git
```
### Configurar e Rodar o Backend

```cmd
docker-compose up -d
```

### Configurar e Rodar no frontend

```cmd
npm install
ng serve
```
