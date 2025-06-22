package com.atu.atc.data;

/**
 *
 * @author henge
 */

import com.atu.atc.model.User;
import java.util.ArrayList;
import java.util.List;

// Generic abstract class for user repositories
// T must extend User
public abstract class UserRepository<T extends User> {
    protected List<T> users = new ArrayList<>();

    // Load user from file into memory
    public abstract void load();

    // Save user to file from memory
    public abstract void save();

    // Get user by ID
    public T getById(String id){
        for (T user : users){
            if (user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    // Get all users
    public List<T> getAll(){
        return users;
    }

    // Add a new user and save
    public void add(T user){
        users.add(user);
        save();
    }

    // Update existing user
    public boolean update(T updatedUser){
        for (int i =0; i < users.size(); i++){
            if (users.get(i).getId().equals(updatedUser.getId())){
                users.set(i, updatedUser);
                save();
                return true;
            }
        }
        return false;
    }

    // Delete a user by ID
    public boolean delete(String id){
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (removed){
            save();
        }
        return removed;
    }
}
