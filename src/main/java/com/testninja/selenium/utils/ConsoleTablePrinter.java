package com.testninja.selenium.utils;

import java.util.ArrayList;
import java.lang.StringBuilder;

public class ConsoleTablePrinter {

    /*
     * Modify these to suit your use
     */
    private final int TABLEPADDING = 4;
    private final char SEPERATOR_CHAR = '-';

    private String[] headers;
    private ArrayList<String[]> table;
    private ArrayList<Integer> maxLength;
    private int rows,cols;

    /*
     * Constructor that needs two arraylist
     * 1: The headersIs is one list containing strings with the columns headers
     * 2: The content is an matrix of Strings build up with ArrayList containing the content
     *
     * Call the printTable method to print the table
     */

    public ConsoleTablePrinter(String[] headersIn, ArrayList<String[]> content){
        this.headers = headersIn;
        this.maxLength =  new ArrayList<Integer>();
        //Set headers length to maxLength at first
        for(int i = 0; i < headers.length; i++){
            maxLength.add(headers[i].length());
        }
        this.table = content;
        calcMaxLengthAll();
    }
    /*
     * To update the matrix
     */
    public void updateField(int row, int col, String input){
        //Update the value
        table.get(row)[col] = input;
        //Then calculate the max length of the column
        calcMaxLengthCol(col);
    }
    /*
     * Prints the content in table to console
     */
    public void printTable(){
        //Take out the
        StringBuilder sb = new StringBuilder();
        StringBuilder sbRowSep = new StringBuilder();
        String padder = "";
        int rowLength = 0;
        String rowSeperator = "";

        //create padding string containing just containing spaces
        for(int i = 0; i < TABLEPADDING; i++){
            padder += " ";
        }

        //create the rowSeperator
        for(int i = 0; i < maxLength.size(); i++){
            sbRowSep.append("|");
            for(int j = 0; j < maxLength.get(i)+(TABLEPADDING*2); j++){
                sbRowSep.append(SEPERATOR_CHAR);
            }
        }
        sbRowSep.append("|");
        rowSeperator = sbRowSep.toString();

        sb.append(rowSeperator);
        sb.append("\n");
        //HEADERS
        sb.append("|");
        for(int i = 0; i < headers.length; i++){
            sb.append(padder);
            sb.append(headers[i]);
            //Fill up with empty spaces
            for(int k = 0; k < (maxLength.get(i)-headers[i].length()); k++){
                sb.append(" ");
            }
            sb.append(padder);
            sb.append("|");
        }
        sb.append("\n");
        sb.append(rowSeperator);
        sb.append("\n");

        //BODY
        for(int i = 0; i < table.size(); i++){
            String[] tempRow = table.get(i);
            //New row
            sb.append("|");
            for(int j = 0; j < tempRow.length; j++){
                sb.append(padder);
                sb.append(tempRow[j]);
                //Fill up with empty spaces
                for(int k = 0; k < (maxLength.get(j)-tempRow[j].length()); k++){
                    sb.append(" ");
                }
                sb.append(padder);
                sb.append("|");
            }
            sb.append("\n");
            sb.append(rowSeperator);
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
    /*
     * Fills maxLenth with the length of the longest word
     * in each column
     *
     * This will only be used if the user dont send any data
     * in first init
     */
    private void calcMaxLengthAll(){
        for(int i = 0; i < table.size(); i++){
            String[] temp = table.get(i);
            for(int j = 0; j < temp.length; j++){
                //If the table content was longer then current maxLength - update it
                if(temp[j].length() > maxLength.get(j)){
                    maxLength.set(j, temp[j].length());
                }
            }
        }
    }
    /*
     * Same as calcMaxLength but instead its only for the column given as inparam
     */
    private void calcMaxLengthCol(int col){
        for(int i = 0; i < table.size(); i++){
            if(table.get(i)[col].length() > maxLength.get(col)){
                maxLength.set(col, table.get(i)[col].length());
            }
        }
    }
}
