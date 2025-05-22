package com.steganography;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;

public class ThirdController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button saveButton;

    @FXML
    private Button nextButton;

    private String hashedPassword = "";

    @FXML
    private void onSaveClicked() {
        String password = passwordField.getText().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        try {
            hashedPassword = hashPassword(password);
            System.out.println("Password saved and hashed (SHA-256): " + hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
        }
    }

    @FXML
    private void onNextClicked() throws IOException {
        if (hashedPassword.isEmpty()) {
            System.out.println("Please save a password before continuing.");
            return;
        }

        // TEMPORARY: Simulate message and image
        String message = SecondaryController.getSecretMessage(); // Assume message is public static
        File imageFile = PrimaryController.getSelectedEmbedImageFile(); // Assume public static

        if (message == null || message.isEmpty() || imageFile == null) {
            System.out.println("Missing image or message. Cannot embed.");
            return;
        }

        try {
            Image embeddedImage = SteganographyUtil.embedMessage(imageFile, message);
            FourthController.setFinalImage(embeddedImage);
            System.out.println("Message embedded. Proceeding to PaneFour...");
            App.setRoot("PaneFour");
        } catch (IOException e) {
            System.out.println("Error embedding message: " + e.getMessage());
        }
    }


    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    // For future pane communication
    public String getHashedPassword() {
        return hashedPassword;
    }

    // OLD NAVIGATION BUTTONS
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToFourth() throws IOException {
        App.setRoot("fourth");
    }
}