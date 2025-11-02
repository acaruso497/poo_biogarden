package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ControlloData {
	
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean isDataValida(String dataStr) { //prende la data come stringa e controlla che sia nel formato dd/MM/yyyy
        try {
            LocalDate.parse(dataStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    public static boolean isPrimaDataMinore(String dataStr1, String dataStr2) { //prende due date e controlla che la prima sia minore della seconda
        try {
            LocalDate data1 = LocalDate.parse(dataStr1, formatter);
            LocalDate data2 = LocalDate.parse(dataStr2, formatter);
            return data1.isBefore(data2);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
