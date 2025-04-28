package com.example.login_auth_api.service;

import com.example.login_auth_api.dto.EmailDTO;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailReceiverService {

    public List<EmailDTO> checkInbox() {
        List<EmailDTO> emails = new ArrayList<>();

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

                EmailDTO emailDTO = new EmailDTO(assunto, remetente, conteudo);
                emails.add(emailDTO);
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
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);

            if (bodyPart.isMimeType("text/plain")) {
                return bodyPart.getContent().toString();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                return extrairTextoDeMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return "[Sem texto encontrado]";
    }
}
