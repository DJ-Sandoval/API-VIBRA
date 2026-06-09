package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.entity.Usuario;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PDFService {

    private final QRService qrService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generarTarjetaDigital(Usuario usuario) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            // === CORRECCIÓN PRINCIPAL ===
            PageSize cardSize = new PageSize(242, 152);
            pdfDoc.setDefaultPageSize(cardSize);
            PdfPage page = pdfDoc.addNewPage();   // ← Agregamos la página primero

            dibujarTarjeta(pdfDoc, page, document, usuario);

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de tarjeta digital", e);
        }

        return baos.toByteArray();
    }

    private void dibujarTarjeta(PdfDocument pdfDoc, PdfPage page, Document document, Usuario usuario) throws Exception {
        PdfCanvas canvas = new PdfCanvas(page);
        Canvas textCanvas = new Canvas(canvas, page.getPageSize());

        DeviceRgb azul = new DeviceRgb(30, 144, 255);
        float width = page.getPageSize().getWidth();
        float height = page.getPageSize().getHeight();

        // Fondo azul redondeado
        canvas.saveState();
        canvas.setFillColor(azul);
        canvas.roundRectangle(0, 0, width, height, 15);
        canvas.fill();
        canvas.restoreState();

        // Logo Vibra Bike
        textCanvas.add(new Paragraph("♡")
                .setFontSize(18)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(20, 118, 30));

        textCanvas.add(new Paragraph("VIBRA")
                .setBold()
                .setFontSize(19)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(48, 122, 120));

        textCanvas.add(new Paragraph("BIKE CENTER")
                .setBold()
                .setFontSize(7.5f)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(48, 112, 120));

        // ==================== QR MODIFICADO (JSON PAYLOAD) ====================
        // Ahora instanciamos el DTO de datos y lo convertimos a un String JSON compacto
        com.a.s.APIVibraBike.model.dto.UsuarioQRPayloadDTO payload = com.a.s.APIVibraBike.model.dto.UsuarioQRPayloadDTO.builder()
                .qrUuid(usuario.getQrUuid())
                .usuarioId(usuario.getId())
                .nombreCompleto(usuario.getNombre() + " " + usuario.getApellidos())
                .folio(usuario.getFolio())
                .plan(usuario.getPlan())
                .build();

        String textoQR = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(payload);

        byte[] qrBytes = qrService.generarQR(textoQR, 300, 300);

        Image qrImage = new Image(ImageDataFactory.create(qrBytes));
        qrImage.scaleToFit(78, 78);
        qrImage.setFixedPosition(148, 38);
        document.add(qrImage);

        // Marco del QR
        canvas.setStrokeColor(ColorConstants.WHITE);
        canvas.setLineWidth(2.5f);
        canvas.roundRectangle(145, 35, 84, 84, 10);
        canvas.stroke();

        // ==================== Datos Basicos en PDF ====================
        String nombreCompleto = usuario.getNombre() + " " + usuario.getApellidos();

        textCanvas.add(new Paragraph(nombreCompleto)
                .setBold()
                .setFontSize(11)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(18, 72, 125));

        textCanvas.add(new Paragraph("Tel: " + usuario.getTelefono())
                .setFontSize(8.5f)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(18, 58, 130));

        String cumple = usuario.getFechaCumple() != null
                ? usuario.getFechaCumple().format(DATE_FORMATTER)
                : "N/A";

        textCanvas.add(new Paragraph("Cumple: " + cumple)
                .setFontSize(8.5f)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(18, 48, 130));

        textCanvas.add(new Paragraph("Inicio: " + usuario.getFechaInicio().format(DATE_FORMATTER))
                .setFontSize(8.5f)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(18, 38, 130));

        textCanvas.add(new Paragraph("Plan: " + usuario.getPlan())
                .setFontSize(8.5f)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(18, 28, 130));

        textCanvas.add(new Paragraph("Folio: " + usuario.getFolio())
                .setFontSize(8f)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(18, 15, 130));

        textCanvas.close();
    }
}