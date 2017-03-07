package com.ufcg.es.loanalert;

import org.joda.time.DateTime;

import java.util.Locale;

class Utils {

    static String formatDate(DateTime dateTime) {
        return String.format(Locale.US, "%02d/%s/%d", dateTime.getDayOfMonth(),
            formatMonth(dateTime.getMonthOfYear()), dateTime.getYear());
    }

    private static String formatMonth(int monthOfYear) {
        switch (monthOfYear) {
            case 1:
            default:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
    }

}
