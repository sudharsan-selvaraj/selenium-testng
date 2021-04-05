package com.testninja.selenium.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class JavaUtils {

    public static void sleep(long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sleep() {
        sleep(2000);
    }

    public static String generateRandomNumber(){
        Integer randomNumber = (int)Math.floor((Math.random() * 10000000) + 1);
        return randomNumber.toString();
    }

    public static String generateRandomNumber(Integer length){
        return generateRandomNumber().substring(0, length);
    }

    public static String generateRandomAlbhabets() {
        String alphabets = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * alphabets.length());
            stringBuilder.append(alphabets.charAt(index));
        }
        return stringBuilder.toString();

    }

    public static String getFolderPath(String folderName) {
        return new File(JavaUtils.class.getClassLoader().getResource(folderName).getFile()).getPath();
    }

    public static String getDayBeforeYesterdayDate() {
        Date date = new Date(System.currentTimeMillis()-2*24*60*60*1000);
        return new SimpleDateFormat("MMMM d, yyyy").format(date);
    }

    /**
     * Method to return the next date for the provided date argument
     * @param date
     * @return next date
     */
    public static String getNextDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            Date t = dateFormat.parse(date);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(t);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            return dateFormat.format(calendar.getTime());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTodayDate() {
        Date date = new Date();
        return new SimpleDateFormat("MMMM d, yyyy").format(date);
    }

    public static String formatDateToApiParam(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            Date dt = dateFormat.parse(date);
            return new SimpleDateFormat("YYYY-MM-dd").format(dt) + "T00:00:00.000Z";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
