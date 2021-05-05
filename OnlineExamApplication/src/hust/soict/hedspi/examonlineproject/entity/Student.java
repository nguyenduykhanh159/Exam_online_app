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
public class Student {
    private String student_id;
    private String name;
    private String clazz;

    public Student() {
        
    }

    public Student(String student_id, String name, String clazz) {
        this.student_id = student_id;
        this.name = name;
        this.clazz = clazz;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "Student{" + "student_id=" + student_id + ", name=" + name + ", clazz=" + clazz + '}';
    }
}
