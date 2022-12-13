package bc.internal.image.placeholders.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Product {

    private final String id;
    private final String catalogNumber;
    private final Path logoPath;

    public Product(String id, String catalogNumber, String logoPath) {
        this.id = id;
        this.catalogNumber = catalogNumber;
        this.logoPath = Paths.get(logoPath);
    }

    public String getId() {
        return id;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public Path getLogoPath() {
        return logoPath;
    }
    
}
