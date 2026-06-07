package com.a.s.APIVibraBike.service.impl;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.a.s.APIVibraBike.model.entity.Alumno;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PDFService {

    private final QRService qrService;

    public byte[] generarTarjetaIndividual(Alumno alumno) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);

            PageSize cardSize = new PageSize(242, 152);
            pdfDoc.setDefaultPageSize(cardSize);

            Document document = new Document(pdfDoc);

            dibujarTarjeta(pdfDoc, document, alumno);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar tarjeta", e);
        }

        return baos.toByteArray();
    }

    private void dibujarTarjeta(
            PdfDocument pdfDoc,
            Document document,
            Alumno alumno) throws Exception {

        PdfPage page = pdfDoc.addNewPage();
        PdfCanvas canvas = new PdfCanvas(page);

        Color azul = new DeviceRgb(30, 144, 255);
        Color blanco = ColorConstants.WHITE;

        float width = page.getPageSize().getWidth();
        float height = page.getPageSize().getHeight();

        // Fondo
        canvas.saveState();
        canvas.setFillColor(azul);
        canvas.roundRectangle(0, 0, width, height, 20);
        canvas.fill();
        canvas.restoreState();

        // Logo círculo
        canvas.setFillColor(blanco);
        canvas.circle(30, 120, 15);
        canvas.fill();

        Canvas textCanvas = new Canvas(
                canvas,
                page.getPageSize()
        );

        textCanvas.add(
                new Paragraph("♡")
                        .setFontSize(16)
                        .setFontColor(azul)
                        .setFixedPosition(24, 112, 20)
        );

        textCanvas.add(
                new Paragraph("VIBRA")
                        .setBold()
                        .setFontSize(18)
                        .setFontColor(blanco)
                        .setFixedPosition(50, 115, 100)
        );

        textCanvas.add(
                new Paragraph("BIKE CENTER")
                        .setBold()
                        .setFontSize(8)
                        .setFontColor(blanco)
                        .setFixedPosition(50, 103, 100)
        );

        // QR
        byte[] qrBytes = qrService.generarQR(
                alumno.getQrUuid(),
                150,
                150
        );

        Image qrImage = new Image(
                ImageDataFactory.create(qrBytes)
        );

        qrImage.scaleToFit(80, 80);
        qrImage.setFixedPosition(80, 40);

        document.add(qrImage);

        // Marco QR
        canvas.setStrokeColor(blanco);
        canvas.setLineWidth(2);
        canvas.roundRectangle(
                75,
                35,
                90,
                90,
                12
        );
        canvas.stroke();

        // Nombre
        textCanvas.add(
                new Paragraph(
                        alumno.getNombre()
                                + " "
                                + alumno.getApellido()
                )
                        .setBold()
                        .setFontSize(10)
                        .setFontColor(blanco)
                        .setFixedPosition(15, 15, 200)
        );

        // Matrícula
        textCanvas.add(
                new Paragraph(
                        "Matrícula: "
                                + alumno.getMatricula()
                )
                        .setFontSize(8)
                        .setFontColor(blanco)
                        .setFixedPosition(15, 5, 200)
        );

        textCanvas.close();
    }

}
