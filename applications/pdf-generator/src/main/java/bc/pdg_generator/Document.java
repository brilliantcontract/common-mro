package bc.pdg_generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Document {

    private final Integer id;
    private final String documentFileName;
    private final List<SpecificationsItem> specifications = new ArrayList<>();
    private final String drawingFileName;

    Document(Integer id, String documentFileName, String textualSpecifications, String drawingFileName) {
        this.id = id;
        this.documentFileName = documentFileName;
        this.drawingFileName = drawingFileName;
        this.specifications.addAll(unserializeTextualSpecifications(textualSpecifications));
    }

    public Integer getId() {
        return id;
    }

    public String getDocumentFileName() {
        return documentFileName;
    }

    public List<SpecificationsItem> getSpecifications() {
        return specifications;
    }

    public String getDrawingFileName() {
        return drawingFileName;
    }

    private List<SpecificationsItem> unserializeTextualSpecifications(String textualSpecifications) {
        List<SpecificationsItem> specifications = new ArrayList<>();

        String[] chunks = textualSpecifications.split(Config.SPECIFICATIONS_ITEM_SEPARATOR + "|" + Config.SPECIFICATIONS_VALUE_SEPARATOR);
        for (int index = 0; index < chunks.length; index += 2) {
            specifications.add(new SpecificationsItem(chunks[index], chunks[index + 1]));
        }

        return specifications;
    }

    public Path getDocumentFilePath() {
        Path documentPath = Paths.get(Config.PATH_TO_DOCUMENTS_DIRECTORY + "/" + documentFileName);
        return documentPath;
    }

    public Path getDrawingFilePath() {
        Path drawingPath = Paths.get(Config.PATH_TO_DRAWINGS_DIRECTORY + "/" + drawingFileName);
        return drawingPath;
    }

}
