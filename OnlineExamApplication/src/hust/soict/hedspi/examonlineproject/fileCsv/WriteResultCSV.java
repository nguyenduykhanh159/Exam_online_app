/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.hedspi.examonlineproject.fileCsv;

import hust.soict.hedspi.examonlineproject.entity.ResultEntity;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author DuyKhanh
 */
public class WriteResultCSV {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
 
    public static void writeCsvFile(String fileName, ResultEntity ketQua, ArrayList<ResultEntity> listKetQua) { 
        listKetQua.add(ketQua);
 
        FileWriter fileWriter = null;
 
        try {
            fileWriter = new FileWriter(fileName);
            for (int i = 0; i < listKetQua.size(); i++) {
                fileWriter.append(listKetQua.get(i).getIdStudent());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listKetQua.get(i).getNumberQuestion());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listKetQua.get(i).getQuestion());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listKetQua.get(i).getAnswerSelected());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
 
//            System.out.println("CSV file was created successfully !!!");
 
        } catch (IOException e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
    
    public static void writeCsvFile(String fileName, ArrayList<ResultEntity> listKetQua) { 
        FileWriter fileWriter = null;
 
        try {
            fileWriter = new FileWriter(fileName);
            for (int i = 0; i < listKetQua.size(); i++) {
                fileWriter.append(listKetQua.get(i).getIdStudent());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listKetQua.get(i).getNumberQuestion());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listKetQua.get(i).getQuestion());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listKetQua.get(i).getAnswerSelected());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
 
//            System.out.println("CSV file was created successfully !!!");
 
        } catch (IOException e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
}
