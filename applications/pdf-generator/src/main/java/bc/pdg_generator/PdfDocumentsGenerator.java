package bc.pdg_generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;
import be.quodlibet.boxable.line.LineStyle;
import java.awt.Color;

import java.util.List;

public class PdfDocumentsGenerator {

    public void generate(List<Document> documents) throws IOException {
        for (Document document : documents) {
            generateDocument(document);
        }
    }

    private void generateDocument(Document document) throws IOException {
        System.out.println("Generating document: " + document.getDocumentFileName());
        
        try (PDDocument pdfDocument = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            pdfDocument.addPage(page);

            addImage(document.getDrawingFilePath(), pdfDocument, page);

            addTable(document.getSpecifications(), pdfDocument, page);

            pdfDocument.save(document.getDocumentFilePath().toFile());
        }
    }

    private void addImage(final Path imagePath, final PDDocument document, PDPage page) throws IOException {
        PDImageXObject image = PDImageXObject.createFromFile(imagePath.toString(), document);
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.drawImage(image,
                    10, // X offest.
                    page.getBBox().getHeight() - 250, // Y offset.
                    600, // Image width.
                    200); // Image height.
        }
    }

    private void addTable(List<SpecificationsItem> specifications, PDDocument document, PDPage page) throws IOException {
        // Initialize table
        float margin = 10;
        float tableWidth = page.getMediaBox().getWidth() - (6 * margin);
        float yStartNewPage = page.getMediaBox().getHeight() - 270;
        boolean drawContent = true;
        boolean drawLines = true;
        float yStart = yStartNewPage;
        float bottomMargin = 70;
        BaseTable table = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, 3 * margin, document, page, drawLines,
                drawContent);

        int counter = 0;
        for (SpecificationsItem specificationsItem : specifications) {
            counter++;
            if (counter > 21) {
                break;
            }

            addRow(specificationsItem.getName(), specificationsItem.getValue(), table);
        }

        table.draw();
    }

    private void addRow(String text1, String text2, BaseTable table) {
        int fontSize = 10;
        LineStyle lineStyle = new LineStyle(Color.GRAY, 1);

        Row<PDPage> row = table.createRow(15f);

        Cell<PDPage> cell1 = row.createCell((100 / 2f), text1, HorizontalAlignment.get("left"), VerticalAlignment.get("top"));
        cell1.setFontSize(fontSize);
        cell1.setBorderStyle(lineStyle);

        Cell<PDPage> cell2 = row.createCell((100 / 2f), text2, HorizontalAlignment.get("left"), VerticalAlignment.get("top"));
        cell2.setFontSize(fontSize);
        cell2.setBorderStyle(lineStyle);
        // Remove left border for left cell. In order to remove double board in place where 2 cells touched.
        cell2.setLeftBorderStyle(new LineStyle(Color.WHITE, 0));
    }
}
