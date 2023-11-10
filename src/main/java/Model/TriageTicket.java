package Model;

import java.time.LocalDateTime;
import java.util.NavigableMap;

public class TriageTicket {
    private String chiefComplaint, currentCondition, priorityLevel, patientNumber, name, number, gender, currentMedication;
    private int patientID, priorityNumber, age, counter;
    private LocalDateTime timeStamp;

    public TriageTicket(PatientInfo patientInfo, String chiefComplaint, String currentCondition,
                        String priorityLevel, String currentMedication, int counter) {
        patientID = patientInfo.getPatientID();
        name = patientInfo.getName();
        number = patientInfo.getContactInfo();
        gender = patientInfo.getGender();
        age = patientInfo.getAge();
        this.counter = counter;
        this.chiefComplaint = chiefComplaint;
        this.currentCondition = currentCondition;
        this.priorityLevel = priorityLevel;
        setPriorityNumber();
        this.currentMedication = currentMedication;
        this.timeStamp = LocalDateTime.now();
    }

    public TriageTicket() {
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getCurrentMedication() {
        return currentMedication;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(String currentCondition) {
        this.currentCondition = currentCondition;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
        setPriorityNumber();
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public void setPriorityNumber() {
        priorityNumber = counter + getPriority();
    }

    public String getInfo() {
        return "Patient Priority: " + priorityNumber;
    }

    public int getPriority() {
        return switch (priorityLevel) {
            case "Resuscitation" -> -15;
            case "Emergency" -> -9;
            case "Urgent" -> -5;
            case "Semi-urgent" -> -3;
            case "Non-urgent" -> 0;
            default -> 1;
        };
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setCurrentMedication(String currentMedication) {
        this.currentMedication = currentMedication;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getStationData() {
        return "Patient Name: " + name;
    }
}
