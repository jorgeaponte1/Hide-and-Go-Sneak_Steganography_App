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

public class EmbedSecretController {

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

    @FXML
    private Label errorLabel;

    @FXML
    private Button returnToStartButton;

    private String hashedPassword = "";

    @FXML
    private void initialize() {
        nextButton.setDisable(true);

        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPasswordCheckBox.setOnAction(e -> togglePasswordVisibility());

        // Initialize error label
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        }
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
            showError("Password cannot be empty.");
            return;
        }

        try {
            hashedPassword = hashPassword(password);
            hideError();
            nextButton.setDisable(false);
            System.out.println("Password saved and hashed (SHA-256): " + hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            showError("Error hashing password.");
        }
    }

    @FXML
    private void onNextClicked() throws IOException {
        if (hashedPassword.isEmpty()) {
            showError("Please save a password before continuing.");
            return;
        }

        String message = EmbedMessageController.getSecretMessage();
        File imageFile = MainStartController.getSelectedEmbedImageFile();

        if (message == null || message.isEmpty()) {
            showError("No message entered. Please provide a secret message to embed.");
            return;
        }

        if (imageFile == null) {
            showError("No image selected. Please choose an image to embed the message into.");
            return;
        }

        try {
            Image embeddedImage = SteganographyUtil.embedMessage(imageFile, getHashedPassword(), message);
            if (embeddedImage == null) {
                showError("Embedding failed. Please ensure your image is valid and try again.");
                return;
            }

            hideError(); 
            SaveEmbedImageController.setFinalImage(embeddedImage);
            System.out.println("Message embedded. Proceeding to Save Embedded Image...");
            App.setRoot("PaneSaveEmbedImage");

        } catch (IOException e) {
            showError("An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void onReturnToStartClicked() throws IOException {
        if(SteganographyUtil.confirmReturnToStart()){
            System.out.println("Returning to start (primary view)...");
            passwordField.clear();
            visiblePasswordField.clear();
            showPasswordCheckBox.setSelected(false);
            hashedPassword = "";
            errorLabel.setVisible(false);
            nextButton.setDisable(true);
            EmbedMessageController.secretMessage = "";
            MainStartController.reset();
            App.setRoot("PaneMain");
        } else {
            System.out.println("Return to start canceled.");
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

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void hideError() {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        }
    }

    private String getHashedPassword() {
        return hashedPassword;
    }
}