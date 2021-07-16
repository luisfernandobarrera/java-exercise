package com.credijusto.exercise;

import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TransactionRecord {
    BigDecimal amount;
    String payee;
    Date date;

    static SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    TransactionRecord (JsonObject object) {
        amount = object.get("amount").getAsBigDecimal();
        payee = object.get("payee").getAsString();
        String dateString = object.get("date").getAsString();
        date = dateFromString(dateString);
    }

    public static Date dateFromString(String dateString) {
        try {
            Date date = ISO8601DATEFORMAT.parse(dateString);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static TransactionRecord getRecordFromJson(JsonObject object) {
        return new TransactionRecord(object);
    }

    public String toString() {
        String dateStr = ISO8601DATEFORMAT.format(date);
        return dateStr + " | " + payee + " | " + amount;
    }
}
