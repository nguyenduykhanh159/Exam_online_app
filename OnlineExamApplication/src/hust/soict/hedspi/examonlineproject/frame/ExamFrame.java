/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.hedspi.examonlineproject.frame;

import hust.soict.hedspi.examonlineproject.entity.GradeEntity;
import hust.soict.hedspi.examonlineproject.fileCsv.ReadQuestionCSV;
import hust.soict.hedspi.examonlineproject.fileCsv.ReadResultCSV;
import hust.soict.hedspi.examonlineproject.fileCsv.WriteResultCSV;
import hust.soict.hedspi.examonlineproject.entity.QuestionEntity;
import hust.soict.hedspi.examonlineproject.entity.ResultEntity;
import hust.soict.hedspi.examonlineproject.entity.Student;
import hust.soict.hedspi.examonlineproject.take_infor_std.TakeInfor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DuyKhanh
 */
public class ExamFrame extends javax.swing.JFrame implements Runnable{
    private static final String FILE_DE_THI = "E:\\Online_Exam_Project\\Exam\\exam.csv";
    private static final String FILE_GRADE = "D:\\Result_Of_Student\\List_Grade_Of_Students.csv";
    private static String fileResult = "D:/Result_Of_Student/" + LoginFrame.student_id + ".csv";
    public static ArrayList<QuestionEntity> result = ReadQuestionCSV.readCSV(FILE_DE_THI);
    private static ArrayList<ResultEntity> listKetQua = new ArrayList<>();
    private static ArrayList<GradeEntity> listGrade = new ArrayList<>();
    private static int numberQuestion = 1;
    private static ArrayList<Integer> listNumberQuestion = new ArrayList<>();
    private QuestionEntity currentQuestEntity;
    private static final int TOTAL_TIME = 1;
    private static Thread thread = new Thread();
    public static int count;
    private static int minute;
    private static int second;
    private static Comparator<ResultEntity> tangDanNumberQuestion = new Comparator<>() {
        @Override
        public int compare(ResultEntity o1, ResultEntity o2) {
            return Integer.parseInt(o1.getNumberQuestion()) - Integer.parseInt(o2.getNumberQuestion());
        }
    };
    
    /**
     * Creates new form ExamFrame
     */
    
    public ExamFrame() {
        initComponents();
        ExamFrame.count = 0;
        Student std = TakeInfor.takeInfor(LoginFrame.student_id);
        
        this.studentIdLabel.setText(std.getStudent_id());
        this.nameStdLabel.setText(std.getName());
        this.classStdLabel.setText(std.getClazz());
        
        Collections.shuffle(result);
        this.numberQuestionLabel.setText(String.valueOf(numberQuestion));
        ExamFrame.listNumberQuestion.add(numberQuestion);
        this.currentQuestEntity = result.get(0);
        load();
    }
    
    @Override
    public void run() {
        minute = TOTAL_TIME;
        
        do {
            ResultFrame.timeOutString = "";
            for (second = 59; second >= 0; second--) {
                if (second == 59) {
                    minute = minute - 1;
                    if (minute < 10) {
                        this.phutLabel.setText("0" + String.valueOf(minute));
                    } else {
                        this.phutLabel.setText(String.valueOf(minute));
                    }
                }
                if (second < 10) {
                    this.secLabel.setText("0" + String.valueOf(second));
                } else {
                    this.secLabel.setText(String.valueOf(second));
                }     
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ExamFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } while (minute != 0 && second != 0);
        writeResult();
        if (listKetQua.size() < result.size()) {
            writeResultNotSelected();
        }
        writeFinalResultToFile();
        showTestResult();
        this.setEnabled(false);
        ResultFrame.timeOutString = "TIME OUT";
        ResultFrame.runFrame();
    }
    
    public void load() {
        int index = result.indexOf(currentQuestEntity);
        this.submitBt.setVisible(false);
        this.backButton.setEnabled(true);
        this.nextButton.setEnabled(true);
        if (index == 0) {
            this.backButton.setEnabled(false);
        }
        if (index == result.size() - 1) {
            this.nextButton.setEnabled(false);
            this.submitBt.setVisible(true);
        }
        this.questionLabel.setText(currentQuestEntity.getQuestion());
        this.answer1Bt.setText(currentQuestEntity.getAnswer1());
        this.answer2Bt.setText(currentQuestEntity.getAnswer2());
        this.answer3Bt.setText(currentQuestEntity.getAnswer3());
        this.answer4Bt.setText(currentQuestEntity.getAnswer4());

        int chooseAnswer = currentQuestEntity.chooseAnswer;        
        switch(chooseAnswer) {
            case 0 -> {
                this.buttonGroup1.clearSelection();
            }
            case 1 -> {
                this.answer1Bt.setSelected(true);
            }
            case 2 -> {
                this.answer2Bt.setSelected(true);
            }
            case 3 -> {
                this.answer3Bt.setSelected(true);
            }
            case 4 -> {
                this.answer4Bt.setSelected(true);
            }
        }
    }
    
    public void loadQuestionWithQuestionBt(String textOfButton) {
        numberQuestion = Integer.parseInt(textOfButton);
        if (listNumberQuestion.contains(numberQuestion)) {
            numberQuestionLabel.setText(String.valueOf(numberQuestion));
            currentQuestEntity = result.get(numberQuestion - 1);
            load();
        } else {
            listNumberQuestion.add(numberQuestion);
            numberQuestionLabel.setText(String.valueOf(numberQuestion));
            currentQuestEntity = result.get(numberQuestion - 1);
            load();
        }
    }
    
    public void loadAnswerToFile(ResultEntity ketQua, int index) {
        ketQua.setIdStudent(studentIdLabel.getText());
        ketQua.setNumberQuestion(String.valueOf(ExamFrame.listNumberQuestion.get(index)));
        ketQua.setQuestion(result.get(listNumberQuestion.get(index) - 1).getQuestion());
        ketQua.setAnswerSelected(String.valueOf(result.get(listNumberQuestion.get(index) - 1).chooseAnswer));
    }
    
    public void writeResult() {
        ResultEntity ketQua = new ResultEntity();
        int index = 0;
        loadAnswerToFile(ketQua, index);
        WriteResultCSV.writeCsvFile(fileResult, ketQua, listKetQua);
        for (index = 1; index < listNumberQuestion.size(); index++) {
            loadAnswerToFile(ketQua, index);
            listKetQua = ReadResultCSV.readCSV(fileResult);
            WriteResultCSV.writeCsvFile(fileResult, ketQua, listKetQua);
        }
    }
    
    public void writeResultNotSelected() {
        for (int i = 0; i < result.size(); i++) {
            if (listNumberQuestion.contains(i + 1) == false) {
                listNumberQuestion.add(i + 1);
                ResultEntity ketQua = new ResultEntity();
                ketQua.setIdStudent(studentIdLabel.getText());
                ketQua.setNumberQuestion(String.valueOf(i + 1));
                ketQua.setQuestion(result.get(i).getQuestion());
                ketQua.setAnswerSelected(String.valueOf(result.get(i).chooseAnswer));
                WriteResultCSV.writeCsvFile(fileResult, ketQua, listKetQua);
            }
        }
    }
    
    public void writeFinalResultToFile() {
        listKetQua.sort(tangDanNumberQuestion);
        WriteResultCSV.writeCsvFile(fileResult, listKetQua);
    }
    
    public void showTestResult() {
        for (int i = 0; i < listKetQua.size(); i++) {
            for (int j = 0; j < result.size(); j++) {
                if (listKetQua.get(i).getQuestion().equals(result.get(j).getQuestion()) == true && 
                    listKetQua.get(i).getAnswerSelected().equals(result.get(j).getCorrectAnswer()) == true) {
                    count += 1;
                }
            }
        }
//        System.out.println(count);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        studentIdLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        nameStdLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        classStdLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        phutLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        secLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        question1Bt = new javax.swing.JButton();
        question2Bt = new javax.swing.JButton();
        question3Bt = new javax.swing.JButton();
        question4Bt = new javax.swing.JButton();
        question5Bt = new javax.swing.JButton();
        question6Bt = new javax.swing.JButton();
        question7Bt = new javax.swing.JButton();
        question8Bt = new javax.swing.JButton();
        question9Bt = new javax.swing.JButton();
        question10Bt = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        numberQuestionLabel = new javax.swing.JLabel();
        questionLabel = new javax.swing.JLabel();
        answer1Bt = new javax.swing.JRadioButton();
        answer2Bt = new javax.swing.JRadioButton();
        answer3Bt = new javax.swing.JRadioButton();
        answer4Bt = new javax.swing.JRadioButton();
        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        submitBt = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText(" ");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("   ID");

        studentIdLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        studentIdLabel.setText("studentId");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(69, 69, 69)
                .addComponent(studentIdLabel)
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jLabel5)
                .addComponent(studentIdLabel))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Name");

        nameStdLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nameStdLabel.setText("nameStd");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(68, 68, 68)
                .addComponent(nameStdLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(nameStdLabel))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Class");

        classStdLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        classStdLabel.setText("classStd");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(73, 73, 73)
                .addComponent(classStdLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(classStdLabel))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(250, 0, 0));
        jLabel6.setText("TIME");

        phutLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        phutLabel.setForeground(new java.awt.Color(250, 0, 0));
        phutLabel.setText("phut");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(250, 0, 0));
        jLabel9.setText(":");

        secLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        secLabel.setForeground(new java.awt.Color(250, 0, 0));
        secLabel.setText("giay");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(22, 22, 22)
                .addComponent(phutLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(secLabel)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(phutLabel)
                    .addComponent(jLabel9)
                    .addComponent(secLabel))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 204));
        jLabel2.setText("List Question");

        question1Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question1Bt.setText("1");
        question1Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                question1BtActionPerformed(evt);
            }
        });

        question2Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question2Bt.setText("2");
        question2Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                question2BtActionPerformed(evt);
            }
        });

        question3Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question3Bt.setText("3");
        question3Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                question3BtActionPerformed(evt);
            }
        });

        question4Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question4Bt.setText("4");
        question4Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                question4BtActionPerformed(evt);
            }
        });

        question5Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question5Bt.setText("5");
        question5Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                question5BtActionPerformed(evt);
            }
        });

        question6Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question6Bt.setText("6");
        question6Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                question6BtActionPerformed(evt);
            }
        });

        question7Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question7Bt.setText("7");

        question8Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question8Bt.setText("8");

        question9Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question9Bt.setText("9");

        question10Bt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        question10Bt.setText("10");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(question1Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question2Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question3Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question4Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question5Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question6Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question7Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question8Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question9Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(question10Bt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 44, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(question1Bt)
                .addGap(7, 7, 7)
                .addComponent(question2Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(question3Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(question4Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(question5Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(question6Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(question7Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(question8Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(question9Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(question10Bt)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        numberQuestionLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        numberQuestionLabel.setText("numberQuestion");

        questionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        questionLabel.setText("question");

        buttonGroup1.add(answer1Bt);
        answer1Bt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        answer1Bt.setText("answer1Bt");
        answer1Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                answer1BtActionPerformed(evt);
            }
        });

        buttonGroup1.add(answer2Bt);
        answer2Bt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        answer2Bt.setText("answer2Bt");
        answer2Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                answer2BtActionPerformed(evt);
            }
        });

        buttonGroup1.add(answer3Bt);
        answer3Bt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        answer3Bt.setText("answer3Bt");
        answer3Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                answer3BtActionPerformed(evt);
            }
        });

        buttonGroup1.add(answer4Bt);
        answer4Bt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        answer4Bt.setText("answer4Bt");
        answer4Bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                answer4BtActionPerformed(evt);
            }
        });

        backButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hust/soict/hedspi/examonlineproject/icon/back_icon.png"))); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        nextButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hust/soict/hedspi/examonlineproject/icon/next_icon.png"))); // NOI18N
        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        submitBt.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        submitBt.setForeground(new java.awt.Color(255, 0, 0));
        submitBt.setText("Submit");
        submitBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nextButton))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(numberQuestionLabel)
                        .addGap(32, 32, 32)
                        .addComponent(questionLabel))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(answer2Bt)
                            .addComponent(answer1Bt)
                            .addComponent(answer3Bt)
                            .addComponent(answer4Bt))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(submitBt)
                .addGap(219, 219, 219))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(nextButton))
                .addGap(37, 37, 37)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberQuestionLabel)
                    .addComponent(questionLabel))
                .addGap(32, 32, 32)
                .addComponent(answer1Bt)
                .addGap(29, 29, 29)
                .addComponent(answer2Bt)
                .addGap(31, 31, 31)
                .addComponent(answer3Bt)
                .addGap(29, 29, 29)
                .addComponent(answer4Bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(submitBt)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void answer1BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_answer1BtActionPerformed
        // TODO add your handling code here:
        currentQuestEntity.chooseAnswer = 1;
    }//GEN-LAST:event_answer1BtActionPerformed

    private void answer2BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_answer2BtActionPerformed
        // TODO add your handling code here:
        currentQuestEntity.chooseAnswer = 2;
    }//GEN-LAST:event_answer2BtActionPerformed

    private void answer3BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_answer3BtActionPerformed
        // TODO add your handling code here:
        currentQuestEntity.chooseAnswer = 3;
    }//GEN-LAST:event_answer3BtActionPerformed

    private void answer4BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_answer4BtActionPerformed
        // TODO add your handling code here:
        currentQuestEntity.chooseAnswer = 4;
    }//GEN-LAST:event_answer4BtActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        // TODO add your handling code here:
        this.numberQuestionLabel.setText(String.valueOf(++numberQuestion));
        if (listNumberQuestion.contains(numberQuestion)) {
            int currnetIndex = result.indexOf(currentQuestEntity);
            currentQuestEntity = result.get(currnetIndex + 1);
            load();
        } else {
            ExamFrame.listNumberQuestion.add(numberQuestion);
            int currnetIndex = result.indexOf(currentQuestEntity);
            currentQuestEntity = result.get(currnetIndex + 1);
            load();
        }
    }//GEN-LAST:event_nextButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        this.numberQuestionLabel.setText(String.valueOf(--numberQuestion));
        int currentIndex = result.indexOf(currentQuestEntity);
        currentQuestEntity = result.get(currentIndex - 1);
        load();
    }//GEN-LAST:event_backButtonActionPerformed
    
    private void submitBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBtActionPerformed
        // TODO add your handling code here:
        writeResult();
        if (listNumberQuestion.size() < result.size()) {
            writeResultNotSelected();
        }
        writeFinalResultToFile();
        showTestResult();
        thread.stop();
        this.setEnabled(false);
        ResultFrame.runFrame();
    }//GEN-LAST:event_submitBtActionPerformed

    private void question1BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_question1BtActionPerformed
        // TODO add your handling code here:
        loadQuestionWithQuestionBt(question1Bt.getText());
    }//GEN-LAST:event_question1BtActionPerformed

    private void question2BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_question2BtActionPerformed
        // TODO add your handling code here:
        loadQuestionWithQuestionBt(question2Bt.getText());
    }//GEN-LAST:event_question2BtActionPerformed

    private void question3BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_question3BtActionPerformed
        // TODO add your handling code here:
        loadQuestionWithQuestionBt(question3Bt.getText());
    }//GEN-LAST:event_question3BtActionPerformed

    private void question4BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_question4BtActionPerformed
        // TODO add your handling code here:
        loadQuestionWithQuestionBt(question4Bt.getText());
    }//GEN-LAST:event_question4BtActionPerformed

    private void question5BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_question5BtActionPerformed
        // TODO add your handling code here:
        loadQuestionWithQuestionBt(question5Bt.getText());
    }//GEN-LAST:event_question5BtActionPerformed

    private void question6BtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_question6BtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_question6BtActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void runFrame() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExamFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExamFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExamFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExamFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ExamFrame examFrame = new ExamFrame();
                examFrame.setVisible(true);
                examFrame.setLocationRelativeTo(null);
                thread = new Thread(examFrame);
                thread.start();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton answer1Bt;
    private javax.swing.JRadioButton answer2Bt;
    private javax.swing.JRadioButton answer3Bt;
    private javax.swing.JRadioButton answer4Bt;
    private javax.swing.JButton backButton;
    public javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel classStdLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel nameStdLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel numberQuestionLabel;
    private javax.swing.JLabel phutLabel;
    private javax.swing.JButton question10Bt;
    private javax.swing.JButton question1Bt;
    private javax.swing.JButton question2Bt;
    private javax.swing.JButton question3Bt;
    private javax.swing.JButton question4Bt;
    private javax.swing.JButton question5Bt;
    private javax.swing.JButton question6Bt;
    private javax.swing.JButton question7Bt;
    private javax.swing.JButton question8Bt;
    private javax.swing.JButton question9Bt;
    private javax.swing.JLabel questionLabel;
    private javax.swing.JLabel secLabel;
    private javax.swing.JLabel studentIdLabel;
    private javax.swing.JButton submitBt;
    // End of variables declaration//GEN-END:variables
}
