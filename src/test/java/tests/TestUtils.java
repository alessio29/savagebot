package tests;

public class TestUtils {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    public static String normalize(String string) {
        if (!LINE_SEPARATOR.equals("\n")) {
            string = string.replace(LINE_SEPARATOR, "\n");
        }
        return string.trim();
    }
}
