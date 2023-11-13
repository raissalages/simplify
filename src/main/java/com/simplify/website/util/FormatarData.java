package com.simplify.website.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatarData {
    private static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public static Date formatarData(String dataString) {
        try {
            return formato.parse(dataString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
