package com.eg.blps1.delegate.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateConverter {
    public static LocalDate convertToLocalDate(Object dateObj, DateTimeFormatter formatter) {
        if (dateObj instanceof LocalDate) {
            return (LocalDate) dateObj;
        } else if (dateObj instanceof Date) {
            return ((Date) dateObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (dateObj instanceof String) {
            return LocalDate.parse((String) dateObj, formatter);
        } else {
            throw new DateTimeParseException("Неподдерживаемый формат даты", dateObj.toString(), 0);
        }
    }
}
