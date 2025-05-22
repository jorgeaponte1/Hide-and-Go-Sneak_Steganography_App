package com.steganography;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class ThirdController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Label passwordMatchLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button nextButton;

    private String hashedPassword = "";

    @FXML
    private void initialize() {
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);

        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPasswordCheckBox.setOnAction(e -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        boolean show = showPasswordCheckBox.isSelected();
        passwordField.setVisible(!show);
        passwordField.setManaged(!show);
        visiblePasswordField.setVisible(show);
        visiblePasswordField.setManaged(show);
    }

    @FXML
    private void onSaveClicked() {
        String password = getPassword().trim();

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

        String message = SecondaryController.getSecretMessage();
        File imageFile = PrimaryController.getSelectedEmbedImageFile();

        if (message == null || message.isEmpty() || imageFile == null) {
            System.out.println("Missing image or message. Cannot embed.");
            return;
        }

        try {
            Image embeddedImage = SteganographyUtil.embedMessage(imageFile, getHashedPassword(), message);
            FourthController.setFinalImage(embeddedImage);
            System.out.println("Message embedded. Proceeding to PaneFour...");
            App.setRoot("PaneFour");
        } catch (IOException e) {
            System.out.println("Error embedding message: " + e.getMessage());
        }
    }

    private String getPassword() {
        return showPasswordCheckBox.isSelected() ? visiblePasswordField.getText() : passwordField.getText();
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

    private String getHashedPassword() {
        return hashedPassword;
    }
}