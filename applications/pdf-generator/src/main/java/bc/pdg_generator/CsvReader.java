package bc.pdg_generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CsvReader {

    public List<Document> readCsvFile() {
        Reader reader = createReader();
        Iterable<CSVRecord> records = readRecordsFromCsvFile(reader);
        List<Document> documents = convertCsvRecordsToDocuments(records);

        return documents;
    }

    private List<Document> convertCsvRecordsToDocuments(Iterable<CSVRecord> records) throws NumberFormatException {
        List<Document> documents = new ArrayList<>();

        for (CSVRecord record : records) {
            Document document = new DocumentBuilder()
                    .setId(Integer.valueOf(record.get(Config.CSV_FILE_HEADERS[0])))
                    .setFileName(record.get(Config.CSV_FILE_HEADERS[1]))
                    .setSpecifications(record.get(Config.CSV_FILE_HEADERS[2]))
                    .setDrawingFile(record.get(Config.CSV_FILE_HEADERS[3]))
                    .createDocument();

            documents.add(document);
        }

        return documents;
    }

    private Iterable<CSVRecord> readRecordsFromCsvFile(Reader reader) {
        Iterable<CSVRecord> records = null;

        try {
            records = CSVFormat.DEFAULT
                    .withHeader(Config.CSV_FILE_HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(reader);
        } catch (IOException ex) {
            Logger.getLogger(CsvReader.class.getName()).log(Level.SEVERE, "", ex);
            System.exit(1);
        }

        return records;
    }

    private Reader createReader() {
        Reader reader = null;

        try {
            reader = new FileReader(Config.PATH_TO_DATA_CSV_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvReader.class.getName()).log(Level.SEVERE, "Specified .csv file with data in Config class was not found.", ex);
            System.exit(1);
        }

        return reader;
    }

}
