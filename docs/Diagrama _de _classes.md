```mermaid
---
title: CallMe - Diagrama de Classes
---

classDiagram

class User {
  +UUID userid
  +String username
  +String email
  +String token
  +String password
  +LocalDateTime resetTokenExpiration
  +Set<Role> roles
  +boolean isLoginCorrect()
  +String generateResetToken()
  +boolean isTokenExpired()
}

class Role {
  +long roleId
  +String name
}

class ChamadoExterno {
  +Long id
  +String remetente
  +String assunto
  +String descricao
  +String dataHora
  +String tokenEmail
  +String messageId
  +StatusChamado status
  +User tecnico
}

class ChamadoInterno {
  +long chamadoID
  +String content
  +Instant creationTimestamp
  +User user
}

class StatusChamado {
  <<enum>>
  +ABERTO
  +EM_ANDAMENTO
  +FECHADO
  +CANCELADO
}

class SendEmail {
  +Long id
  +String remetente
  +String destinatario
  +String assunto
  +String corpoSimples
  +String dataHora
  +String messageId
  +String dkim
  +String dmarc
  +String spf
  +String comprovante
  +String token
  +List<AnexoEmail> anexos
}

class AnexoEmail {
  +Long id
  +String nomeArquivo
  +String tipoMime
  +byte[] conteudo
  +LocalDateTime dataRecebimento
  +SendEmail email
}

User "1" <-- "0..*" ChamadoInterno : "criado por"
User "1" <-- "0..*" ChamadoExterno : "tem RT"
User "0..*" --> "0..*" Role : "possui"
SendEmail "1" -- "0..*" AnexoEmail : "contém"
ChamadoExterno --> StatusChamado : "usa"
