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
import java.io.IOException;

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

        // Título "tu resto"
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20);
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 750);
        contentStream.showText("Tu Resto");
        contentStream.endText();

        // Línea horizontal
        contentStream.setLineWidth(1);
        contentStream.moveTo(25, 730);
        contentStream.lineTo(575, 730);
        contentStream.stroke();

        // Eliminar caracteres de control del texto
        text = text.replaceAll("[\\u0000-\\u001F]", "");

        // Texto de la promoción
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        String[] lines = text.split("\n");
        int yPosition = 700; // Comenzamos desde una posición vertical

        for (String line : lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(25, yPosition);
            contentStream.showText(line);
            contentStream.endText();
            yPosition -= 15; // Bajamos la posición vertical para la siguiente línea
        }

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
