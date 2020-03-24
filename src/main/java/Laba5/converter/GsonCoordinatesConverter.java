package Laba5.converter;

import com.google.gson.*;

import java.time.format.DateTimeFormatter;

public class GsonCoordinatesConverter {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

}
