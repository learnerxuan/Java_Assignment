/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.model;

/**
 *
 * @author farris
 */
public class Request {
    private String request_id;
    private String student_id;
    private String current_subject_id;
    private String requested_subject_id;
    private String status;
    
    public Request(String request_id, String student_id, String current_subject_id,
                   String requested_subject_id, String status) {
        this.request_id = request_id;
        this.student_id = student_id;
        this.current_subject_id = current_subject_id;
        this.requested_subject_id = requested_subject_id;
        this.status = status;
    }
// getters
    public String getRequest_id(){
        return request_id;
    }
    public String getStudent_id(){
        return student_id;
    }
    public String getCurrent_subject_id(){
        return current_subject_id;
    }
    public String getRequested_subject_id(){
        return requested_subject_id;
    }
    public String getStatus(){
        return status;
    }
//setterss
    public void setRequest_id(String request_id){
        this.request_id = request_id;
    }
    public void setStudent_id(String student_id){
        this.student_id = student_id;
    }
    public void setCurrent_subject_id(String current_subject_id){
        this.current_subject_id = current_subject_id;
    }
    public void setRequested_subject_id(String requested_subject_id){
        this.requested_subject_id = requested_subject_id;
    } 
    public void setStatus(String status){
        this.status = status;
    }
    public String toFileString() {
        return request_id + "," + student_id + "," + current_subject_id + "," + 
               requested_subject_id + "," + status;    
    }
}
