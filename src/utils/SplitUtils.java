package utils;

import java.util.ArrayList;
import java.util.Arrays;

public final class SplitUtils {
    private SplitUtils() {}

    // Stesso algoritmo: split su "," e ritorno di ArrayList<String>
    public static ArrayList<String> splitByCommaToArrayList(String usernameConcatenati) {
        String[] usernamesArray = usernameConcatenati.split(",");
        return new ArrayList<>(Arrays.asList(usernamesArray));
    }
}
