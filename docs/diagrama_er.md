```mermaid
---
title: CallMe - Class UML
---
classDiagram
    chamado -- comentarios
    chamado -- funcionario
    funcionario -- administrador
    administrador -- chamado
    class comentarios{
        -uuid | id
        -int  | pk_id
        -fk | id_chamado
        -str | email -> quem respondeu a mensagem
        -str | autor
    }
    class chamado{
        -uuid | id
        -int | pk_id
        -uuid - a decidir | token
        -int | tier/1 - 5 -> definido pelo gerente
        -fk | funcionario_id
        -str | status/default: aberto
        -str | email/usuario que abriu o chamado
    }
    class funcionario{
        -uuid | id
        -int | pk_id
        -str | email
        -str | senha -> hasheada
        -int | tier/1 - 5
        -fk | created_by -> Administrador que criou a conta
    }
    class administrador{
        -uuid | id
        -int | pk_id
        -str | nome
        -str | email
        -str | senha/hasheada
    }
```