package bc.internal.keywords.finder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CsvWriter {

    private char separator = ',';
    private char quote = '"';

    private BufferedWriter out;

    public CsvWriter(OutputStream out) throws IOException {
        this.out = new BufferedWriter(new OutputStreamWriter(out));
    }

    public void write(String... fields) throws IOException {
        StringBuilder row = new StringBuilder();
        for (String field : fields) {
            row
                    .append(quote)
                    .append(escape(field))
                    .append(quote)
                    .append(separator);
        }
        row.append('\n');
        
        out.append(row);
    }

    private String escape(String value) {
        if (value.indexOf(quote) > -1) {
            return value.replace("" + quote, "" + quote + quote);
        }

        return value;
    }

    public void close() throws IOException {
        out.close();
    }

}
