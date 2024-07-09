package com.tallerwebi.dominio;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;


import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.TIMES_ROMAN;

@Component
public class Email {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mateo.fortu@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void generarEmailPromocionPDF(String to, String subject, String text) throws IOException, MessagingException{
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 700);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("mateo.fortu@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Adjunto encontrarás la promoción en formato PDF.");

        helper.addAttachment("promocion.pdf", new ByteArrayResource(byteArrayOutputStream.toByteArray()));

        emailSender.send(message);
    }
}
