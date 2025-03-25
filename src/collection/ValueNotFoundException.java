package collection;

public class ValueNotFoundException extends RuntimeException {
    public ValueNotFoundException(String value) {
        super("Value \"" + value + "\" is not found.");
    }
}
