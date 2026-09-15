package service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFService {

    public static void exportarConDialogo(Component parent, JTable tabla, String tituloReporte) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte PDF");
        String nombreArchivo = tituloReporte.replace(" ", "_").toLowerCase() + "_" + System.currentTimeMillis() + ".pdf";
        fileChooser.setSelectedFile(new File(nombreArchivo));

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try {
                generarReporte(tabla, tituloReporte, fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(parent, "Reporte PDF exportado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Error al generar PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private static void generarReporte(JTable tabla, String titulo, String rutaArchivo) throws Exception {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph parrafoTitulo = new Paragraph(titulo, fuenteTitulo);
        parrafoTitulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(parrafoTitulo);

        Font fuenteFecha = FontFactory.getFont(FontFactory.HELVETICA, 10);
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Paragraph parrafoFecha = new Paragraph("Generado el: " + fechaHora, fuenteFecha);
        parrafoFecha.setAlignment(Paragraph.ALIGN_RIGHT);
        parrafoFecha.setSpacingAfter(20);
        documento.add(parrafoFecha);

        PdfPTable pdfTable = new PdfPTable(tabla.getColumnCount());
        pdfTable.setWidthPercentage(100);

        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            PdfPCell cell = new PdfPCell(new Phrase(tabla.getColumnName(i), fuenteEncabezado));
            cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
            pdfTable.addCell(cell);
        }

        Font fuenteDatos = FontFactory.getFont(FontFactory.HELVETICA, 11);
        for (int row = 0; row < tabla.getRowCount(); row++) {
            for (int col = 0; col < tabla.getColumnCount(); col++) {
                Object val = tabla.getValueAt(row, col);
                PdfPCell cell = new PdfPCell(new Phrase(val != null ? val.toString() : "", fuenteDatos));
                pdfTable.addCell(cell);
            }
        }

        documento.add(pdfTable);
        documento.close();
    }
}