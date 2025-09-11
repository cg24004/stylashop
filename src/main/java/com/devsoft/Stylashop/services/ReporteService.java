package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.IngresoDTO;
import com.devsoft.Stylashop.dto.MetodoPagoDTO;
import com.devsoft.Stylashop.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final PagoRepository pagoRepository;

    public byte[] generarReporte(LocalDate inicio, LocalDate fin, boolean detallado) throws Exception {
        List<IngresoDTO> data = pagoRepository.obtenerIngresosRangoFechas(inicio, fin);

        Document document = new Document(PageSize.LETTER, 36, 36, 72, 36);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        Element.ALIGN_CENTER,
                        new Phrase(String.format("Página %d", writer.getPageNumber()),
                                FontFactory.getFont(FontFactory.HELVETICA, 9, Font.ITALIC)),
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 20,
                        0
                );
            }
        });

        document.open();

        // Encabezado
        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{1, 4});

        try {
            Image logo = Image.getInstance("src/main/resources/static/logo.jpeg");
            logo.scaleToFit(70, 70);
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            header.addCell(logoCell);
        } catch (Exception e) {
            PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            header.addCell(emptyCell);
        }

        PdfPCell datosEmpresa = new PdfPCell();
        datosEmpresa.setBorder(Rectangle.NO_BORDER);
        datosEmpresa.addElement(new Paragraph("Stylashop - Boutique de Ropa",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        datosEmpresa.addElement(new Paragraph("Dirección: Chalatenango, El Salvador",
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        datosEmpresa.addElement(new Paragraph("Tel: 2301-3333",
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        header.addCell(datosEmpresa);

        document.add(header);
        document.add(new Paragraph(" "));

        Paragraph titulo = new Paragraph("Reporte de Ingresos",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph rango = new Paragraph("Del " + inicio + " al " + fin,
                FontFactory.getFont(FontFactory.HELVETICA, 11));
        rango.setAlignment(Element.ALIGN_CENTER);
        document.add(rango);
        document.add(new Paragraph(" "));

        // Contenido
        if (detallado) {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 3f, 2f, 2f});

            String[] headers = {"No", "Cliente", "Fecha Pago", "Monto"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            int i = 1;
            for (IngresoDTO dto : data) {
                table.addCell(String.valueOf(i));
                table.addCell(dto.getClienteNombre());
                table.addCell(dto.getFechaPago().toString());
                table.addCell("$ " + dto.getSumaPagos().setScale(2, RoundingMode.HALF_UP));
                i++;
            }

            document.add(table);
        } else {
            Map<LocalDate, BigDecimal> resumen = data.stream()
                    .collect(Collectors.groupingBy(
                            IngresoDTO::getFechaPago,
                            Collectors.reducing(BigDecimal.ZERO, IngresoDTO::getSumaPagos, BigDecimal::add)
                    ));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(50);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setWidths(new float[]{2f, 2f});

            String[] headers = {"Fecha", "Total Día"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (Map.Entry<LocalDate, BigDecimal> entry : resumen.entrySet()) {
                table.addCell(entry.getKey().toString());
                table.addCell("$ " + entry.getValue().setScale(2, RoundingMode.HALF_UP));
            }

            document.add(table);
        }

        // Total general
        BigDecimal totalGeneral = data.stream()
                .map(IngresoDTO::getSumaPagos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        document.add(new Paragraph(" "));
        Paragraph total = new Paragraph("TOTAL GENERAL: $ " + totalGeneral.setScale(2, RoundingMode.HALF_UP),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK));
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        document.close();
        return baos.toByteArray();
    }


    public byte[] generarReporteMetodosPago(LocalDate inicio, LocalDate fin) throws Exception {
        try {
            System.out.println("Generando reporte desde: " + inicio + " hasta: " + fin);

            List<MetodoPagoDTO> data = pagoRepository.obtenerResumenPorMetodoPago(inicio, fin);
            System.out.println("Datos obtenidos: " + data.size() + " registros");

            Document document = new Document(PageSize.LETTER, 36, 36, 72, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();

            // Encabezado (logo y nombre empresa)
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            header.setWidths(new int[]{1, 4});

            try {
                Image logo = Image.getInstance("src/main/resources/static/logo.jpeg");
                logo.scaleToFit(70, 70);
                PdfPCell logoCell = new PdfPCell(logo, false);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                header.addCell(logoCell);
            } catch (Exception e) {
                PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
                emptyCell.setBorder(Rectangle.NO_BORDER);
                header.addCell(emptyCell);
            }

            PdfPCell datosEmpresa = new PdfPCell();
            datosEmpresa.setBorder(Rectangle.NO_BORDER);
            datosEmpresa.addElement(new Paragraph("Stylashop - Boutique de Ropa",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            datosEmpresa.addElement(new Paragraph("Dirección: Chalatenango, El Salvador",
                    FontFactory.getFont(FontFactory.HELVETICA, 10)));
            datosEmpresa.addElement(new Paragraph("Tel: 2301-3333",
                    FontFactory.getFont(FontFactory.HELVETICA, 10)));
            header.addCell(datosEmpresa);
            document.add(header);

            // Título y rango
            Paragraph titulo = new Paragraph("Reporte de Métodos de Pago",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph rango = new Paragraph("Del " + inicio + " al " + fin,
                    FontFactory.getFont(FontFactory.HELVETICA, 11));
            rango.setAlignment(Element.ALIGN_CENTER);
            document.add(rango);
            document.add(new Paragraph(" "));

            // Tabla de métodos de pago
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(80);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setWidths(new float[]{3f, 2f, 2f});

            String[] headers = {"Método de Pago", "Transacciones", "Total"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (MetodoPagoDTO dto : data) {
                table.addCell(dto.getMetodo() != null ? dto.getMetodo().name() : "SIN MÉTODO");
                table.addCell(String.valueOf(dto.getCantidadTransacciones()));
                table.addCell("$ " + dto.getTotal().setScale(2, RoundingMode.HALF_UP));
            }

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            System.err.println("Error en generarReporteMetodosPago: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}
