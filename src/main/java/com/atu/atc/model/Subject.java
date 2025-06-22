package com.atu.atc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Subject {
    private String subjectId;
    private String name;
    private List<String> tutorIds;
    private String level;

    public Subject(String subjectId, String name, List<String> tutorIds, String level) {
        this.subjectId = subjectId;
        this.name = name;
        this.tutorIds = (tutorIds != null) ? new ArrayList<>(tutorIds) : new ArrayList<>();
        this.level = level;
    }

    public Subject(String subjectId, String name, String tutorIdsString, String level) {
        this.subjectId = subjectId;
        this.name = name;
        this.tutorIds = parseTutorIdsString(tutorIdsString);
        this.level = level;
    }

    private List<String> parseTutorIdsString(String tutorIdsString) {
        if (tutorIdsString == null || tutorIdsString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(tutorIdsString.split(" "))
                     .filter(id -> !id.trim().isEmpty())
                     .collect(Collectors.toList());
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

    public List<String> getTutorIds() {
        return new ArrayList<>(tutorIds);
    }

    public void setTutorIds(List<String> tutorIds) {
        this.tutorIds = (tutorIds != null) ? new ArrayList<>(tutorIds) : new ArrayList<>();
    }

    public void addTutorId(String tutorId) {
        if (!this.tutorIds.contains(tutorId)) {
            this.tutorIds.add(tutorId);
        }
    }

    public void removeTutorId(String tutorId) {
        this.tutorIds.remove(tutorId);
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        String tutorsString = String.join(" ", tutorIds);
        return String.join("|", subjectId, name, tutorsString, level);
    }
}
