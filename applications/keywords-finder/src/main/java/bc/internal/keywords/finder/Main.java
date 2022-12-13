package bc.internal.keywords.finder;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Main {

    private static final Path PDF_FILE = Paths.get("pdf-to-scan.pdf");
    private static final Path CSV_KEYWORDS_FILE = Paths.get("keywords-to-search.csv");

    public static void main(String[] args) throws Exception {
        new Main().go();
    }

    private void go() throws IOException {
        String pdfText = extractTextFromPdfFile(PDF_FILE.toFile()).toUpperCase();

        CsvWriter csvWriter = null;
        CsvReader csvReader = null;
        try (InputStream in = new FileInputStream(CSV_KEYWORDS_FILE.toFile());) {
            csvWriter = new CsvWriter(System.out);
            csvReader = new CsvReader(in);
            List<String> row;

            while (csvReader.hasNext()) {
                row = csvReader.next();
                String keyword = row.get(0).toUpperCase();

                if (pdfText.contains(keyword)) {
                    csvWriter.write(row.toArray(new String[0]));
                }
            }
        } finally {
            if (csvWriter != null) {
                csvWriter.close();
            }
            if (csvReader != null) {
                csvReader.close();
            }
        }
    }

    private String extractTextFromPdfFile(File pdfFile) throws IOException {
        PDFTextStripper pdfStripper = null;
        PDDocument pdDocument = null;
        PDFParser parser = new PDFParser(new RandomAccessFile(pdfFile, "r"));
        parser.parse();
        String parsedText = "";
        try (COSDocument cosDocument = parser.getDocument()) {
            pdfStripper = new PDFTextStripper();
            pdDocument = new PDDocument(cosDocument);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(pdDocument.getNumberOfPages());
            parsedText = pdfStripper.getText(pdDocument);
        }

        return parsedText;
    }

}
