package com.steganography;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class ExtractPaneOneController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button savePasswordButton;

    @FXML
    private Button nextButton;

    private static String hashedPassword = "";

    @FXML
    private void initialize() {
        // Optionally disable next until password is set
        nextButton.setDisable(true);
    }

    @FXML
    private void onSavePasswordClicked() {
        String password = passwordField.getText().trim();
        if (password.isEmpty()) {
            System.out.println("Password field is empty.");
            return;
        }

        try {
            hashedPassword = hashPassword(password);
            System.out.println("Password hashed (SHA-256): " + hashedPassword);
            nextButton.setDisable(false); // Enable Next
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
        }
    }

    @FXML
    private void onNextClicked() throws IOException {
        if (hashedPassword.isEmpty()) {
            System.out.println("Please enter a password first.");
            return;
        }

        System.out.println("Password accepted. Proceeding to extraction pane...");
        //App.setRoot(""); // This will be the next pane we build
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String getHashedPassword() {
        return hashedPassword;
    }
}