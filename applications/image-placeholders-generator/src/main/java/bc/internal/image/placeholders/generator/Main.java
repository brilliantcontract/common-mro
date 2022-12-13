package bc.internal.image.placeholders.generator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main {

    private final PlaceholderGenerator placeholderGenerator = new PlaceholderGenerator();

    /**
     * .csv file has to have 3 columns: ID, CATALOG_NUMBER, LOGO_PATH.
     *
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length != 1) {
            System.out.println("You have to specify path to .csv file as application argument.");
        }
        if (Files.isRegularFile(Paths.get(args[0]))) {
            System.out.println("Path to .csv file is incorrect.");
        }

        new Main().run(Paths.get(args[0]));
    }

    private void run(Path csv) throws FileNotFoundException, IOException {
        CsvReader reader = null;
        try (InputStream in = new FileInputStream(csv.toFile());) {
            reader = new CsvReader(in);
            reader.setSkipFirstRow(true);
            while (reader.hasNext()) {
                List<String> row = reader.next();
                System.out.printf("Processing:\t%-15s %-25s %s%n", row.get(0), row.get(1), row.get(2));

                Product product = new Product(row.get(0), row.get(1), row.get(2));
                placeholderGenerator.generate(product);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}
