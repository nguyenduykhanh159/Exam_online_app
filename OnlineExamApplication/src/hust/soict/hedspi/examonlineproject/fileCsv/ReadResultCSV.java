/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.hedspi.examonlineproject.fileCsv;

import hust.soict.hedspi.examonlineproject.entity.ResultEntity;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author DuyKhanh
 */
public class ReadResultCSV {
    public static final char COMMA_DELIMITER = ',';
    
    public static ArrayList getQuestionInfor(String line) {
        ArrayList<String> result = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '\"') {
                if (str.length() > 0 && stack.size() % 2 == 0)
                    str.append(ch);
                    stack.push(ch);
            } else if (ch == COMMA_DELIMITER && stack.size() % 2 == 0) {
                result.add(str.toString());
                stack.clear();
                str = new StringBuilder();
            } else if (ch == COMMA_DELIMITER && stack.size() % 2 != 0) {
                str.append(ch);
            } else {
                str.append(ch);
            }
        }
            result.add(str.toString());
        return result;
    }
    
    public static ArrayList<ResultEntity> readCSV(String fileName) {
        ArrayList<ResultEntity> list = new ArrayList<>();
        try (BufferedReader bir = new BufferedReader(new FileReader(fileName))) {
            String line = bir.readLine();
            while (line != null) {
                ArrayList<String> result = getQuestionInfor(line);
                list.add(new ResultEntity(result.get(0), result.get(1), result.get(2), result.get(3)));
                line = bir.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
