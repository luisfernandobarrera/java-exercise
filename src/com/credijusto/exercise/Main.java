package com.credijusto.exercise;

import com.google.gson.*;

import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Main {

    public static void main(String[] args) {
        final String FILE_NAME = "/Users/lbarrera/Code/java-exercise/src/payments.json";

        ArrayList<TransactionRecord> records;
        records = new ArrayList<TransactionRecord>();

        try {
            FileReader jsonFile = new FileReader(FILE_NAME);
            JsonArray objects = new JsonParser().parse(jsonFile).getAsJsonArray();


            for (JsonElement obj : objects) {
                JsonObject object = (JsonObject) obj;
                TransactionRecord record = TransactionRecord.getRecordFromJson(object);
                records.add(record);
            }

            System.out.println("Records Loaded");

            System.out.println(records.size());

            printRecords(records);

            System.out.println(getTotal(records));
            SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

            Date startDate = ISO8601DATEFORMAT.parse("2021-05-01");
            Date limitDate = ISO8601DATEFORMAT.parse("2021-05-21");

            System.out.println(getTotalAtDate(records, limitDate));
            System.out.println(getAverageBetweenDates(records, startDate, limitDate));


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }


    }

    public static TransactionRecord sendRecord(JsonObject record) {
        TransactionRecord tr = TransactionRecord.getRecordFromJson(record);
        System.out.println(tr.date + " " + tr.payee + " " + tr.amount.toString() + "\n");
        return tr;
    }

    public static void printRecords(ArrayList<TransactionRecord> records) {
        for (TransactionRecord record: records) {
            System.out.println(record);
        }
    }

    public static BigDecimal getTotal(ArrayList<TransactionRecord> records) {
        BigDecimal result = new BigDecimal("0.0");

        for (TransactionRecord t : records) {
            result = result.add(t.amount);
        }

        return result;
    }

    public static BigDecimal getTotalAtDate(ArrayList<TransactionRecord> records, Date date) {
        BigDecimal result = new BigDecimal("0.0");

        for (TransactionRecord t : records) {
            if (t.date.before(date) || t.date.equals(date)) {
                result = result.add(t.amount);
            }
        }

        return result;
    }

    public static BigDecimal getAverageBetweenDates(ArrayList<TransactionRecord> records, Date startDate, Date endDate) {
        ArrayList<Date> dates = getListOfDates(startDate, endDate);

        BigDecimal sum = new BigDecimal("0.0");
        int recordCount = 0;

        for (Date date : dates) {
            sum = sum.add(getTotalAtDate(records, date));
            recordCount += 1;
        }
        return sum.divide(BigDecimal.valueOf(recordCount));
    }

    public static ArrayList<Date> getListOfDates(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        ArrayList<Date> dates = new ArrayList<>();

        Date date = cal.getTime();

        do {
            dates.add(date);
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        } while (date.before(endDate));

        return dates;
    }
}