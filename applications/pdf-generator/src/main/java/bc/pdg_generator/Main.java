package bc.pdg_generator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private final CsvReader csvReader = new CsvReader();
    private final PdfDocumentsGenerator pdfDocumentsGenerator = new PdfDocumentsGenerator();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        validateConfigFile();
        List<Document> documents = csvReader.readCsvFile();
        pdfDocumentsGenerator.generate(documents);
    }

    private void validateConfigFile() {
        if (Config.SPECIFICATIONS_ITEM_SEPARATOR.isEmpty()) {
            System.err.println("ERROR: Specifications item separator is empty.");
            System.exit(1);
        }

        if (Config.SPECIFICATIONS_VALUE_SEPARATOR.isEmpty()) {
            System.err.println("ERROR: Specifications value separator is empty.");
            System.exit(1);
        }

        if (!Files.isReadable(Paths.get(Config.PATH_TO_DATA_CSV_FILE))) {
            System.err.println("ERROR: Data .csv file is not readable.");
            System.exit(1);
        }

        if (!Files.isDirectory(Paths.get(Config.PATH_TO_DOCUMENTS_DIRECTORY))) {
            System.err.println("ERROR: Directory with Documents is not available.");
            System.exit(1);
        }

        if (!Files.isDirectory(Paths.get(Config.PATH_TO_DRAWINGS_DIRECTORY))) {
            System.err.println("ERROR: Directory with Drawings is not available.");
            System.exit(1);
        }
    }

}
