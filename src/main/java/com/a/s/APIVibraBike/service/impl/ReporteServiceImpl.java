package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.dto.AsistenciaResponseDTO;
import com.a.s.APIVibraBike.model.enums.Horario;
import com.a.s.APIVibraBike.service.interfaces.AsistenciaService;
import com.a.s.APIVibraBike.service.interfaces.ReporteService;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder; // <- Importación correcta para bordes personalizados
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final AsistenciaService asistenciaService;

    // Paleta de Colores Corporativa
    private static final Color AZUL_CIELO = new DeviceRgb(0, 174, 239);    // #00AEEF
    private static final Color GRIS_OSCURO = new DeviceRgb(43, 43, 43);    // #2B2B2B
    private static final Color GRIS_CLARO = new DeviceRgb(240, 242, 245);  // Fondos de filas
    private static final Color BLANCO = new DeviceRgb(255, 255, 255);

    @Override
    public ByteArrayOutputStream generarReporteAsistenciaDia(LocalDate fecha) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(36, 36, 36, 36); // Márgenes limpios

            // === 1. ENCABEZADO (Logo + Título) ===
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{30, 70})).useAllAvailableWidth();
            headerTable.setBorder(null); // <- En iText 7 quitar bordes se hace pasando null

            // Cargar Logo desde resources de forma segura
            try {
                InputStream is = new ClassPathResource("/static/LogoVibra.jpg").getInputStream();
                byte[] bytes = is.readAllBytes();
                Image logo = new Image(ImageDataFactory.create(bytes));
                logo.setWidth(110);
                headerTable.addCell(new Cell().add(logo).setBorder(null));
            } catch (Exception e) {
                log.error("No se pudo cargar el logo del reporte en resources/LogoVibra.jpg: {}", e.getMessage());
                headerTable.addCell(new Cell().add(new Paragraph("VibraBike")).setBorder(null));
            }

            // Título del Reporte
            String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph infoHeader = new Paragraph()
                    .add(new Text("VibraBikeCenter\n").setBold().setFontSize(22).setFontColor(AZUL_CIELO))
                    .add(new Text("Reporte de Asistencia del día: " + fechaFormateada + "\n").setBold().setFontSize(14).setFontColor(GRIS_OSCURO))
                    .setTextAlignment(TextAlignment.RIGHT);

            headerTable.addCell(new Cell().add(infoHeader).setBorder(null));
            document.add(headerTable);
            document.add(new Paragraph("\n")); // Espaciador

            // === 2. PROCESAR CADA UNO DE LOS 3 HORARIOS ===
            for (Horario horario : Horario.values()) {
                // Obtener datos del servicio de asistencia
                List<AsistenciaResponseDTO> asistencias = asistenciaService.listarAsistenciasPorHorario(fecha, horario);

                // Nombre bonito del bloque de horario
                String tituloHorario = horario.name().replace("_", " "); // "PRIMER HORARIO", etc.

                // Subtítulo de Sección
                document.add(new Paragraph(tituloHorario)
                        .setBold()
                        .setFontSize(12)
                        .setFontColor(BLANCO)
                        .setBackgroundColor(GRIS_OSCURO)
                        .setPaddingLeft(10)
                        .setPaddingTop(4)
                        .setPaddingBottom(4)
                        .setMarginBottom(5));

                if (asistencias.isEmpty()) {
                    document.add(new Paragraph("No se registraron asistencias en este horario.")
                            .setFontSize(10)
                            .setItalic()
                            .setFontColor(GRIS_OSCURO)
                            .setMarginBottom(15)
                            .setPaddingLeft(10));
                } else {
                    // Estructura de Tabla: Folio, Nombre, Teléfono, Hora de Entrada, Clases Restantes
                    Table table = new Table(UnitValue.createPercentArray(new float[]{15, 40, 20, 13, 12})).useAllAvailableWidth();
                    table.setMarginBottom(15);

                    // Headers de la Tabla
                    String[] headers = {"Folio", "Cliente / Usuario", "Teléfono", "Hora", "Restantes"};
                    for (String h : headers) {
                        table.addHeaderCell(new Cell()
                                .add(new Paragraph(h).setBold().setFontSize(10).setFontColor(BLANCO))
                                .setBackgroundColor(AZUL_CIELO)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setPadding(5));
                    }

                    // Filas de Datos (Cebra o Alternadas)
                    boolean alternatingRow = false;
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                    for (AsistenciaResponseDTO ast : asistencias) {
                        Color rowBg = alternatingRow ? GRIS_CLARO : BLANCO;

                        table.addCell(createCell(ast.getFolio(), rowBg, TextAlignment.LEFT));
                        table.addCell(createCell(ast.getNombreUsuario(), rowBg, TextAlignment.LEFT));
                        table.addCell(createCell(ast.getTelefono(), rowBg, TextAlignment.LEFT));
                        table.addCell(createCell(ast.getHoraRegistro().format(timeFormatter), rowBg, TextAlignment.CENTER));
                        table.addCell(createCell(String.valueOf(ast.getClasesRestantes()), rowBg, TextAlignment.CENTER));

                        alternatingRow = !alternatingRow;
                    }
                    document.add(table);
                }
            }

            document.close();
        } catch (Exception e) {
            log.error("Error crítico generando PDF: ", e);
        }

        return out;
    }

    // Método helper para celdas estilizadas y limpias sin bordes toscos
    private Cell createCell(String text, Color bgColor, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(text == null ? "" : text).setFontSize(9).setFontColor(GRIS_OSCURO))
                .setBackgroundColor(bgColor)
                .setTextAlignment(alignment)
                .setPadding(4)
                .setBorder(null) // Quita el borde de caja completo por defecto
                .setBorderBottom(new SolidBorder(new DeviceRgb(220, 224, 230), 0.5f)); // Añade línea inferior fina
    }
}