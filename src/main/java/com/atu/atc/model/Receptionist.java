// src/main/java/com/atu/atc/model/Receptionist.java
package com.atu.atc.model;

public class Receptionist extends User {
    public Receptionist(String id, String password, String fullName, String phoneNumber, String email, String gender) {
        super(id, password, "Receptionist", fullName, phoneNumber, email, gender);
    }

    @Override
    public boolean login(String enteredId, String enteredPassword) {
        // Basic login for now
        return getId().equals(enteredId) && getPassword().equals(enteredPassword);
    }

    // No need update profile override since it's the same

    public String toFileString(){
        return getId() + "," + getFullName() + "," + getPassword() + "," +
                getPhoneNumber() + "," + getEmail() + "," + getGender();
    }
}

