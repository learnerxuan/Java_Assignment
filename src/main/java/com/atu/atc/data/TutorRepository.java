package com.atu.atc.data;

import com.atu.atc.model.Tutor;
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class TutorRepository extends UserRepository<Tutor> {

    private static final String FILE_PATH = "src/main/resources/data/tutors.txt";
    private static final String HEADER = "userId,fullName,password,phoneNumber,email,gender,level,subject";

    @Override
    public void load() {
        users.clear();
        List<String> lines = FileUtils.readDataLines(FILE_PATH);
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length == 8) {
                Tutor tutor = new Tutor(
                    parts[0].trim(), parts[1].trim(), parts[2].trim(),
                    parts[3].trim(), parts[4].trim(), parts[5].trim(),
                    parts[6].trim(), parts[7].trim()
                );
                users.add(tutor);
            }
        }
    }

    @Override
    public void save() {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Tutor t : users) {
            lines.add(t.toFileString());
        }
        FileUtils.writeLines(FILE_PATH, lines);
    }

    public boolean update(Tutor updatedTutor) {
        for (int i = 0; i < users.size(); i++) {
            Tutor t = users.get(i);
            if (t.getId().equalsIgnoreCase(updatedTutor.getId())) {
                users.set(i, updatedTutor);
                return true;
            }
        }
        return false;
    }

    public Tutor getById(String id) {
        for (Tutor t : users) {
            if (t.getId().equalsIgnoreCase(id)) {
                return t;
            }
        }
        return null;
    }
}
