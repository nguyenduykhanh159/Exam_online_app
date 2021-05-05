/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.hedspi.examonlineproject.entity;

/**
 *
 * @author DuyKhanh
 */
public class ResultEntity {
    private String idStudent;
    private String numberQuestion;
    private String question;
    private String answerSelected;

    public ResultEntity() {
        
    }

    public ResultEntity(String idStudent, String numberQuestion, String question, String answerSelected) {
        this.idStudent = idStudent;
        this.numberQuestion = numberQuestion;
        this.question = question;
        this.answerSelected = answerSelected;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(String numberQuestion) {
        this.numberQuestion = numberQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerSelected() {
        return answerSelected;
    }

    public void setAnswerSelected(String answerSelected) {
        this.answerSelected = answerSelected;
    }

    @Override
    public String toString() {
        return "ResultEntity{" + "idStudent=" + idStudent + ", numberQuestion=" + numberQuestion + ", question=" + question + ", answerSelected=" + answerSelected + '}';
    }
}
