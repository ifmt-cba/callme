package com.example.login_auth_api.service;

import com.example.login_auth_api.domain.user.AnexoEmail;
import com.example.login_auth_api.domain.user.SendEmail;
import com.example.login_auth_api.dto.EmailLeituraCompletaDTO;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.repositories.AnexoEmailRepository;
import com.example.login_auth_api.repositories.EmailRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EmailReceiverService {

    private final EmailRepository emailRepository;

    @Autowired
    private AnexoEmailRepository anexoEmailRepository;

    public EmailReceiverService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public List<EmailLeituraCompletaDTO> checkInbox() {
        List<EmailLeituraCompletaDTO> emails = new ArrayList<>();

        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(properties);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "callmegerencia@gmail.com", "pvgn peru emdw nvyl"); // Troque pela sua senha de app

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();

            for (Message message : messages) {
                String assunto = message.getSubject();
                String remetente = message.getFrom()[0].toString();
                String destinatarios = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
                String conteudo = extrairConteudo(message);

                String dataHora = message.getReceivedDate() != null
                        ? message.getReceivedDate().toInstant().atZone(java.time.ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy 'às' HH:mm", new Locale("pt", "BR")))
                        : "[Data indisponível]";

                String messageId = Optional.ofNullable(message.getHeader("Message-ID"))
                        .map(headers -> headers.length > 0 ? headers[0] : "[Sem ID]")
                        .orElse("[Sem ID]");

                String spf = getHeaderValue(message, "Received-SPF");
                String authResults = getHeaderValue(message, "Authentication-Results");

                String spfResultado = obterResultado(spf, "spf");
                String dkimResultado = obterResultado(authResults, "dkim");
                String dmarcResultado = obterResultado(authResults, "dmarc");

                StringBuilder comprovante = new StringBuilder();
                comprovante.append("======== MENSAGEM ORIGINAL (COMPROVANTE) ========\n");
                comprovante.append("ID da mensagem : ").append(messageId).append("\n");
                comprovante.append("Criado em      : ").append(dataHora).append("\n");
                comprovante.append("De             : ").append(remetente).append("\n");
                comprovante.append("Para           : ").append(destinatarios).append("\n");
                comprovante.append("Assunto        : ").append(assunto).append("\n");
                comprovante.append("SPF            : ").append(obterResultado(spf, "spf")).append("\n");
                comprovante.append("DKIM           : ").append(obterResultado(authResults, "dkim")).append("\n");
                comprovante.append("DMARC          : ").append(obterResultado(authResults, "dmarc")).append("\n");
                comprovante.append("Conteúdo:\n----------------------------------------\n");
                comprovante.append(conteudo).append("\n");
                comprovante.append("============================================\n");

                if (emailRepository.findByMessageId(messageId).isEmpty()) {
                    String token = UUID.randomUUID().toString();

                    SendEmail emailEntity = SendEmail.builder()
                            .remetente(remetente)
                            .destinatario(destinatarios)
                            .assunto(assunto)
                            .corpoSimples(conteudo)
                            .dataHora(dataHora)
                            .messageId(messageId)
                            .spf(obterResultado(spf, "spf"))
                            .dkim(obterResultado(authResults, "dkim"))
                            .dmarc(obterResultado(authResults, "dmarc"))
                            .comprovante(comprovante.toString())
                            .token(token)
                            .build();

                    emailRepository.save(emailEntity);

                    salvarAnexosDaMensagem(message, messageId);

                    EmailLeituraCompletaDTO dto = new EmailLeituraCompletaDTO(
                            token,
                            remetente,
                            destinatarios,
                            assunto,
                            conteudo,
                            dataHora,
                            spfResultado,
                            dkimResultado,
                            dmarcResultado,
                            comprovante.toString()
                    );

                    emails.add(dto);
                }


            }

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return emails;
    }

    private String extrairConteudo(Message message) {
        try {
            Object content = message.getContent();
            if (content instanceof String) {
                return (String) content;
            } else if (content instanceof MimeMultipart) {
                return extrairTextoDeMimeMultipart((MimeMultipart) content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[Erro ao extrair conteúdo]";
    }

    private String extrairTextoDeMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        String html = null;
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                return (String) bodyPart.getContent();
            } else if (bodyPart.isMimeType("text/html") && html == null) {
                html = (String) bodyPart.getContent();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                return extrairTextoDeMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return html != null ? html.replaceAll("\\<.*?\\>", "") : "[Sem texto encontrado]";
    }

    private String getHeaderValue(Message message, String headerName) {
        try {
            String[] headers = message.getHeader(headerName);
            return (headers != null && headers.length > 0) ? headers[0] : "[Não encontrado]";
        } catch (MessagingException e) {
            return "[Erro ao obter " + headerName + "]";
        }
    }

    private String obterResultado(String cabecalho, String tipo) {
        if (cabecalho == null) return "[Não encontrado]";
        tipo = tipo.toLowerCase();

        if (cabecalho.toLowerCase().contains(tipo + "=pass")) return "'PASS'";
        if (cabecalho.toLowerCase().contains(tipo + "=fail")) return "'FAIL'";
        if (cabecalho.toLowerCase().contains(tipo + "=neutral")) return "'NEUTRAL'";
        return "[Desconhecido]";
    }

    private void salvarAnexosDaMensagem(Message message, String messageId) {
        try {
            if (message.getContent() instanceof Multipart multipart) {
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart part = multipart.getBodyPart(i);

                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) ||
                            (part.getFileName() != null && !part.getFileName().isEmpty())) {

                        String fileName = part.getFileName();
                        String mimeType = part.getContentType();

                        InputStream is = part.getInputStream();
                        byte[] bytes = is.readAllBytes();

                        SendEmail emailEntity = emailRepository.findByMessageId(messageId)
                                .orElseThrow(() -> new RuntimeException("Email não encontrado"));

                        AnexoEmail anexo = new AnexoEmail();
                        anexo.setNomeArquivo(fileName);
                        anexo.setTipoMime(mimeType);
                        anexo.setConteudo(bytes);
                        anexo.setDataRecebimento(LocalDateTime.now());
                        anexo.setEmail(emailEntity);

                        anexoEmailRepository.save(anexo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<EmailResumoDTO> listarResumosEmails() {
        List<EmailResumoDTO> resumos = new ArrayList<>();

        List<SendEmail> emails = emailRepository.findAll();
        for (SendEmail email : emails) {
            resumos.add(new EmailResumoDTO(
                    email.getRemetente(),
                    email.getAssunto(),
                    email.getCorpoSimples(),
                    email.getDataHora(),
                    email.getToken()
            ));
        }

        return resumos;
    }

}