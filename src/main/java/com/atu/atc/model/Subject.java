package com.atu.atc.model;

public class Subject {
    private String subjectId;
    private String name;
    private String tutorId;
    private String level;

    public Subject(String subjectId, String name, String tutorId, String level) {
        this.subjectId = subjectId;
        this.name = name;
        this.tutorId = tutorId;
        this.level = level;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Subject{" +
               "subjectId='" + subjectId + '\'' +
               ", name='" + name + '\'' +
               ", tutorIds=" + tutorId + 
               ", level='" + level + '\'' +
               '}';
    }
    public String toFileString() {
        return String.join("|", subjectId, name, tutorId, level);
    }
}
