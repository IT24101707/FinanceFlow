package com.financeflow.financeflow.controller;

import com.financeflow.financeflow.config.WebConfig;
import com.financeflow.financeflow.entity.User;
import com.financeflow.financeflow.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    // View / Edit Profile
    @GetMapping
    public String profilePage(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        User user = userService.findById(userId);

        model.addAttribute("user", user);

        return "profile";
    }

    // Update Profile
    @PostMapping("/update")
    public String updateProfile(@RequestParam String fullName,
                                 @RequestParam String phone,
                                 HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        User updated = userService.updateProfile(userId, fullName, phone);

        session.setAttribute("loggedInUserName", updated.getFullName());

        model.addAttribute("user", updated);
        model.addAttribute("success", "Profile updated successfully!");

        return "profile";
    }

    // Upload / replace profile photo
    @PostMapping("/photo")
    public String uploadPhoto(@RequestParam("photo") MultipartFile photo,
                               HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        User user = userService.findById(userId);

        if (photo.isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Please choose an image to upload.");
            return "profile";
        }

        String contentType = photo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Only image files are allowed.");
            return "profile";
        }

        try {
            Path uploadDir = Paths.get(WebConfig.UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            String original = photo.getOriginalFilename();
            String ext = (original != null && original.contains("."))
                    ? original.substring(original.lastIndexOf('.'))
                    : ".jpg";

            String filename = "user_" + userId + "_" + UUID.randomUUID() + ext;
            Path target = uploadDir.resolve(filename);
            Files.copy(photo.getInputStream(), target);

            // Remove the old photo file, if any
            if (user.getPhotoUrl() != null) {
                File oldFile = new File(WebConfig.UPLOAD_DIR, user.getPhotoUrl());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            User updated = userService.updatePhoto(userId, filename);
            model.addAttribute("user", updated);
            model.addAttribute("success", "Profile photo updated!");

        } catch (IOException e) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Could not upload photo: " + e.getMessage());
        }

        return "profile";
    }

    // Change Password Page
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    // Change Password
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                  @RequestParam String newPassword,
                                  HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        boolean success = userService.changePassword(userId, oldPassword, newPassword);

        if (success) {
            model.addAttribute("success", "Password changed successfully!");
        } else {
            model.addAttribute("error", "Old password is incorrect!");
        }

        return "change-password";
    }

}
