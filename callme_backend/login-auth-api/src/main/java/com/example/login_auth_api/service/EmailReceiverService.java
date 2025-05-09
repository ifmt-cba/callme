package com.example.login_auth_api.service;

import com.example.login_auth_api.dto.EmailLeituraCompletaDTO;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
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
            System.out.println("Você tem " + messages.length + " mensagens.");

            for (Message message : messages) {
                String assunto = message.getSubject();
                String remetente = message.getFrom()[0].toString();
                String conteudo = extrairConteudo(message);

                // ✅ Geração do comprovante
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                message.writeTo(outputStream);
                String comprovante = outputStream.toString(StandardCharsets.UTF_8);

                // ✅ Impressão filtrada (resumo)
                System.out.println("----- E-mail Resumido -----");
                System.out.println("Remetente: " + remetente);
                System.out.println("Assunto: " + assunto);
                System.out.println("Conteúdo: " + conteudo);

                // ✅ Impressão de comprovante completo
                System.out.println("\n----- Comprovante Completo -----");
                System.out.println(comprovante);
                System.out.println("-----------------------------\n");

                // ✅ Adiciona à lista de retorno
                emails.add(new EmailLeituraCompletaDTO(remetente, assunto, conteudo, comprovante));
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

            if (bodyPart.isMimeType("text/html")) {
                html = (String) bodyPart.getContent(); // pega HTML formatado
            } else if (bodyPart.isMimeType("text/plain") && html == null) {
                return (String) bodyPart.getContent(); // fallback
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                return extrairTextoDeMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }

        return html != null ? html : "[Sem texto encontrado]";
    }
}
