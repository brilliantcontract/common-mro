package bc.pdg_generator;

public class DocumentBuilder {

    private Integer id;
    private String fileName;
    private String specifications;
    private String drawingFile;

    public DocumentBuilder() {
    }

    public DocumentBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public DocumentBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public DocumentBuilder setSpecifications(String specifications) {
        this.specifications = specifications;
        return this;
    }

    public DocumentBuilder setDrawingFile(String drawingFile) {
        this.drawingFile = drawingFile;
        return this;
    }

    public Document createDocument() {
        return new Document(id, fileName, specifications, drawingFile);
    }

}
