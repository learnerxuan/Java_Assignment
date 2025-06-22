package com.atu.atc.data;

/**
 * @author henge
 */

import com.atu.atc.model.User;
import com.atu.atc.util.FileUtils;

// Generic abstract class for user repositories
// T must extend User
public abstract class UserRepository<T extends User> {
}
