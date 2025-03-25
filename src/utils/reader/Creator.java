package utils.reader;

public interface Creator<T, C> {

    T create(C data);

}
