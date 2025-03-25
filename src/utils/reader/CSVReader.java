package utils.reader;

import custom_data_structures.MyArrayList;

import java.io.*;


/**
 * A basic CSV reader class that reads lines from a CSV file
 * and splits them into columns based on a specified delimiter.
 */

public class CSVReader {
    private final BufferedReader reader;


    private final String regex;
    private String currentLine;

    /**
     * Constructs a new reader.CSVReader for reading from a CSV file.
     *
     * @param filename The path to the CSV file.
     * @param regex The delimiter used to split each line into columns.
     * @param hasHeader Specifies whether the CSV file has a header row. If true, skip the first line.
     * @throws IOException If an I/O error occurs when opening the file.
     */

    public CSVReader(String filename, String regex,boolean hasHeader) throws IOException {
        this.reader = new BufferedReader(new FileReader(filename));
        this.regex = regex;

        if (hasHeader) {
            reader.readLine();
        }


    }

    /**
     * Reads the next line from the CSV file and stores it in `currentLine`.
     *
     * @return `true` if a line was successfully read; `false` if the end of the file has been reached.
     * @throws RuntimeException If an I/O error occurs while reading from the file.
     */

    public boolean readLine() {
        try {
            currentLine = reader.readLine();
            return currentLine != null;
        } catch (IOException e) {
            throw new RuntimeException("Error reading from the file", e);
        }
    }

    /**
     * Splits the current line into columns based on the specified delimiter and returns them as a list.
     *
     * @return A list of strings, where each string is a column from the current line.
     */


    public MyArrayList<String> getColumns() {
        MyArrayList<String> columns = new MyArrayList<>();
        if (currentLine != null) {

            String[] values = currentLine.split(regex);
            for (String value : values) {
                columns.addLast(value);
            }
        }
        return columns;

    }
    /**
     * Closes the CSV file reader.
     *
     * @throws RuntimeException If an I/O error occurs while closing the reader.
     */

    public void close() {
        try {

            reader.close();

        } catch (IOException e) {
            throw new RuntimeException("Error closing the reader", e);
        }

    }
}
