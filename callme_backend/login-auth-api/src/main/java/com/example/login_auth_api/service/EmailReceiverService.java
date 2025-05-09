package com.example.login_auth_api.service;

import com.example.login_auth_api.dto.EmailLeituraCompletaDTO;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailReceiverService {

    public List<EmailLeituraCompletaDTO> checkInbox() {
        List<EmailLeituraCompletaDTO> emails = new ArrayList<>();

        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(properties);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "callmegerencia@gmail.com", "pvgn peru emdw nvyl");

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            System.out.println("Você tem " + messages.length + " mensagens.\n");

            List<String> comprovantes = new ArrayList<>();

            for (Message message : messages) {
                String assunto = message.getSubject();
                String remetente = message.getFrom()[0].toString();
                String destinatarios = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
                String conteudo = extrairConteudo(message);
                String dataHora = message.getReceivedDate() != null
                        ? message.getReceivedDate().toInstant().atZone(java.time.ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy 'às' HH:mm"))
                        : "[Data indisponível]";
                String messageId = message.getHeader("Message-ID") != null
                        ? message.getHeader("Message-ID")[0]
                        : "[Sem ID]";

                // Autenticação
                String spf = getHeaderValue(message, "Received-SPF");
                String authResults = getHeaderValue(message, "Authentication-Results");

                // Gerar comprovante detalhado estilo Gmail
                StringBuilder comprovante = new StringBuilder();
                comprovante.append("======== MENSAGEM ORIGINAL (COMPROVANTE) ========\n");
                comprovante.append("ID da mensagem : ").append(messageId).append("\n");
                comprovante.append("Criado em      : ").append(dataHora).append(" (entregue após poucos segundos)\n");
                comprovante.append("De             : ").append(remetente).append("\n");
                comprovante.append("Para           : ").append(destinatarios).append("\n");
                comprovante.append("Assunto        : ").append(assunto).append("\n");
                comprovante.append("SPF            : ").append(obterResultado(spf, "spf")).append("\n");
                comprovante.append("DKIM           : ").append(obterResultado(authResults, "dkim")).append("\n");
                comprovante.append("DMARC          : ").append(obterResultado(authResults, "dmarc")).append("\n");
                comprovante.append("Conteúdo:\n----------------------------------------\n");
                comprovante.append(conteudo).append("\n");
                comprovante.append("============================================\n");

                String comprovanteStr = comprovante.toString();
                comprovantes.add(comprovanteStr);

                // Impressão resumida
                System.out.println("----- E-mail Resumido -----");
                System.out.println("Remetente: " + remetente);
                System.out.println("Assunto: " + assunto);
                System.out.println("Conteúdo: " + conteudo);
                System.out.println("----------------------------\n");

                // Adiciona ao DTO
                emails.add(new EmailLeituraCompletaDTO(remetente, assunto, conteudo, comprovanteStr));
            }

            // Impressão dos comprovantes
            System.out.println("========= COMPROVANTES COMPLETOS =========\n");
            for (String comprovante : comprovantes) {
                System.out.println(comprovante);
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
            } else {
                return "[Conteúdo não suportado]";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "[Erro ao extrair conteúdo]";
        }
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
}
