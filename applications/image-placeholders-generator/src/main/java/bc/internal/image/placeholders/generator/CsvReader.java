package bc.internal.image.placeholders.generator;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

public class CsvReader {

    static final private int NUMMARK = 10;
    static final private char COMMA = ',';
    static final private char DQUOTE = '"';
    static final private char CRETURN = '\r';
    static final private char LFEED = '\n';
    static final private char SQUOTE = '\'';
    static final private char COMMENT = '#';

    /**
     * Should we ignore multiple carriage-return/newline characters at the end
     * of the record?
     */
    private boolean stripMultipleNewlines = true;

    /**
     * What should be used as the separator character?
     */
    private char separator = COMMA;
    private List<String> fields = new ArrayList<>();
    private boolean eofSeen = false;
    private Reader in;

    private int lineNumberCounter = 0;
    private boolean skipFirstRow = false;

    public boolean isStripMultipleNewlines() {
        return stripMultipleNewlines;
    }

    public void setStripMultipleNewlines(boolean stripMultipleNewlines) {
        this.stripMultipleNewlines = stripMultipleNewlines;
    }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public boolean isEofSeen() {
        return eofSeen;
    }

    public void setEofSeen(boolean eofSeen) {
        this.eofSeen = eofSeen;
    }

    static public Reader stripBom(InputStream in)
            throws java.io.IOException,
            java.io.UnsupportedEncodingException {
        PushbackInputStream pin = new PushbackInputStream(in, 3);
        byte[] b = new byte[3];
        int len = pin.read(b, 0, b.length);
        if ((b[0] & 0xFF) == 0xEF && len == 3) {
            if ((b[1] & 0xFF) == 0xBB
                    && (b[2] & 0xFF) == 0xBF) {
                return new InputStreamReader(pin, "UTF-8");
            } else {
                pin.unread(b, 0, len);
            }
        } else if (len >= 2) {
            if ((b[0] & 0xFF) == 0xFE
                    && (b[1] & 0xFF) == 0xFF) {
                return new InputStreamReader(pin, "UTF-16BE");
            } else if ((b[0] & 0xFF) == 0xFF
                    && (b[1] & 0xFF) == 0xFE) {
                return new InputStreamReader(pin, "UTF-16LE");
            } else {
                pin.unread(b, 0, len);
            }
        } else if (len > 0) {
            pin.unread(b, 0, len);
        }
        return new InputStreamReader(pin, "UTF-8");
    }

    public CsvReader(InputStream input) throws IOException {
        this.lineNumberCounter = 0;
        this.in = new BufferedReader(stripBom(input));
    }

    public boolean hasNext() throws java.io.IOException {
        // Skip the first row.
        if (skipFirstRow && lineNumberCounter == 0) {
            lineNumberCounter += 1;
            hasNext();
        } else {
            lineNumberCounter += 1;
        }

        if (eofSeen) {
            return false;
        }
        fields.clear();
        eofSeen = split(in, fields);
        if (eofSeen) {
            return !fields.isEmpty();
        } else {
            return true;
        }
    }

    public List<String> next() {
        return fields;
    }

    // Returns true if EOF seen.
    static private boolean discardLinefeed(Reader in,
            boolean stripMultiple)
            throws java.io.IOException {
        if (stripMultiple) {
            in.mark(NUMMARK);
            int value = in.read();
            while (value != -1) {
                char c = (char) value;
                if (c != CRETURN && c != LFEED) {
                    in.reset();
                    return false;
                } else {
                    in.mark(NUMMARK);
                    value = in.read();
                }
            }
            return true;
        } else {
            in.mark(NUMMARK);
            int value = in.read();
            if (value == -1) {
                return true;
            } else if ((char) value != LFEED) {
                in.reset();
            }
            return false;
        }
    }

    private boolean skipComment(Reader in)
            throws java.io.IOException {
        /* Discard line. */
        int value;
        while ((value = in.read()) != -1) {
            char c = (char) value;
            if (c == CRETURN) {
                return discardLinefeed(in, stripMultipleNewlines);
            }
        }
        return true;
    }

    // Returns true when EOF has been seen.
    private boolean split(Reader in, List<String> fields)
            throws java.io.IOException {
        StringBuilder sbuf = new StringBuilder();
        int value;
        while ((value = in.read()) != -1) {
            char c = (char) value;
            switch (c) {
                case CRETURN:
                    if (sbuf.length() > 0) {
                        fields.add(sbuf.toString());
                        sbuf.delete(0, sbuf.length());
                    }
                    return discardLinefeed(in, stripMultipleNewlines);

                case LFEED:
                    if (sbuf.length() > 0) {
                        fields.add(sbuf.toString());
                        sbuf.delete(0, sbuf.length());
                    }
                    if (stripMultipleNewlines) {
                        return discardLinefeed(in, stripMultipleNewlines);
                    } else {
                        return false;
                    }

                case DQUOTE: {
                    // Processing double-quoted string ..
                    while ((value = in.read()) != -1) {
                        c = (char) value;
                        if (c == DQUOTE) {
                            // Saw another double-quote. Check if
                            // another char can be read.
                            in.mark(NUMMARK);
                            if ((value = in.read()) == -1) {
                                // Nope, found EOF; means End of
                                // field, End of record and End of
                                // File
                                if (sbuf.length() > 0) {
                                    fields.add(sbuf.toString());
                                    sbuf.delete(0, sbuf.length());
                                }
                                return true;
                            } else if ((c = (char) value) == DQUOTE) {
                                // Found a second double-quote
                                // character. Means the double-quote
                                // is included.
                                sbuf.append(DQUOTE);
                            } else if (c == CRETURN) {
                                // Found End of line. Means End of
                                // field, and End of record.
                                if (sbuf.length() > 0) {
                                    fields.add(sbuf.toString());
                                    sbuf.delete(0, sbuf.length());
                                }
                                // Read and discard a line-feed if we
                                // can indeed do so.
                                return discardLinefeed(in,
                                        stripMultipleNewlines);
                            } else if (c == LFEED) {
                                // Found end of line. Means End of
                                // field, and End of record.
                                if (sbuf.length() > 0) {
                                    fields.add(sbuf.toString());
                                    sbuf.delete(0, sbuf.length());
                                }
                                // No need to check further. At this
                                // point, we have not yet hit EOF, so
                                // we return false.
                                if (stripMultipleNewlines) {
                                    return discardLinefeed(in, stripMultipleNewlines);
                                } else {
                                    return false;
                                }
                            } else {
                                // Not one of EOF, double-quote,
                                // newline or line-feed. Means end of
                                // double-quote processing. Does NOT
                                // mean end-of-field or end-of-record.
                                // System.err.println("EOR on '" + c +
                                // "'");
                                in.reset();
                                break;
                            }
                        } else {
                            // Not a double-quote, so no special meaning.
                            sbuf.append(c);
                        }
                    }
                    // Hit EOF, and did not see the terminating double-quote.
                    if (value == -1) {
                        // We ignore this error, and just add whatever
                        // left as the next field.
                        if (sbuf.length() > 0) {
                            fields.add(sbuf.toString());
                            sbuf.delete(0, sbuf.length());
                        }
                        return true;
                    }
                }
                break;

                default:
                    if (c == separator) {
                        fields.add(sbuf.toString());
                        sbuf.delete(0, sbuf.length());
                    } else {
                        /* A comment line is a line starting with '#' with
               * optional whitespace at the start. */
                        if (c == COMMENT && fields.isEmpty()
                                && sbuf.toString().trim().isEmpty()) {
                            boolean eof = skipComment(in);
                            if (eof) {
                                return eof;
                            } else {
                                sbuf.delete(0, sbuf.length());
                            }
                            /* Continue with next line if not eof. */
                        } else {
                            sbuf.append(c);
                        }
                    }
            }
        }
        if (sbuf.length() > 0) {
            fields.add(sbuf.toString());
            sbuf.delete(0, sbuf.length());
        }
        return true;
    }

    public boolean isSkipFirstRow() {
        return skipFirstRow;
    }

    public void setSkipFirstRow(boolean skipFirstRow) {
        this.skipFirstRow = skipFirstRow;
    }

    public void close() throws IOException {
        in.close();
    }
}
