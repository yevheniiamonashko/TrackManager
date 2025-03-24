package nl.saxion.cds.utils.reader;

public interface Creator<T, C> {

    T create(C data);

}
