package bc.pdg_generator;

public class Config {

    public static final String PATH_TO_DRAWINGS_DIRECTORY = "/home/dima/projects/contracts/mro/tasks-done/8-august/deublin-pdf-generator/drawings";
    public static final String PATH_TO_DOCUMENTS_DIRECTORY = "/home/dima/projects/contracts/mro/tasks-done/8-august/deublin-pdf-generator/documents";
    public static final String PATH_TO_DATA_CSV_FILE = "/home/dima/projects/contracts/mro/tasks-done/8-august/deublin-pdf-generator/data-to-generate-pdf-files.csv";
    
    public static final String SPECIFICATIONS_ITEM_SEPARATOR = "◙";
    public static final String SPECIFICATIONS_VALUE_SEPARATOR = "●";
    
    public static final String[] CSV_FILE_HEADERS = {"id", "document-file-name", "specifications", "drawing-file-name"};
}
