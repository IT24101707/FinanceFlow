package com.financeflow.financeflow.service;

import com.financeflow.financeflow.entity.User;
import com.financeflow.financeflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register a new user (password is hashed before saving)
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Verify login credentials
    public User authenticate(String email, String rawPassword) {

        User user = userRepository.findByEmail(email);

        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }

        return null;
    }

    // Update basic profile info
    public User updateProfile(Long id, String fullName, String phone) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        user.setFullName(fullName);
        user.setPhone(phone);

        return userRepository.save(user);
    }

    // Update profile photo path
    public User updatePhoto(Long id, String photoUrl) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        user.setPhotoUrl(photoUrl);

        return userRepository.save(user);
    }

    // Change password (requires correct old password)
    public boolean changePassword(Long id, String oldPassword, String newPassword) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }

}
