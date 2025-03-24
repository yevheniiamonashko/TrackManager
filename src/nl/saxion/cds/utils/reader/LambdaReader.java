package nl.saxion.cds.utils.reader;

import nl.saxion.cds.custom_data_structures.MyArrayList;


import java.io.IOException;


/**
 * A generic CSV file reader, which extends from the basic CSV reader,
 * uses a `Creator` interface to transform each line of the CSV file
 * into an object of type `T`.
 * Each line is split into columns based on a specified regex, and these
 * columns are passed to the `Creator` to generate objects.
 *
 * @param <T> The type of object to create from each line in the CSV file.
 */

public class LambdaReader<T> extends CSVReader {

    private final Creator<T, MyArrayList<String>> creator;


    /**
     * Constructs a new LambdaReader.
     *
     * @param filename The path to the CSV file.
     * @param regex The delimiter used to separate columns in each line of the CSV file.
     * @param creator An instance of `Creator` interface used to create objects of type `T` from each line's columns.
     * @param hasHeader Indicates if the CSV file has a header row. If true, skip the first line.
     * @throws IOException If an I/O error occurs while opening the file.
     */


    public LambdaReader(String filename, String regex, Creator<T, MyArrayList<String>> creator, boolean hasHeader) throws IOException {
        super(filename, regex, hasHeader);
        this.creator = creator;
    }

    /**
     * Reads each line of the CSV file, converts it into an object of type `T` using the provided `Creator` instance,
     * and collects these objects in a list.
     *
     * @return An objects' list of type `T`, each representing a line in the CSV file.
     */

    public MyArrayList<T> readObjects() {
        MyArrayList<T> result = new MyArrayList<>();

        while (readLine()) {
            MyArrayList<String> columnsList = getColumns();
            T temp = creator.create(columnsList);
            result.addLast(temp);
        }

        return result;
    }

    public void close() {
        super.close();
    }
}