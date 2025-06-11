package com.steganography;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class EmbedSecretController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button returnToStartButton;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<String> hashAlgorithmComboBox;

    @FXML
    private AnchorPane rootPane;

    private String hashedPassword = "";


    @FXML
    private void initialize() {
        loadHeader();
        nextButton.setDisable(true);

        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPasswordCheckBox.setOnAction(e -> togglePasswordVisibility());

        hashAlgorithmComboBox.setStyle("-fx-font-size: 18px;");

        hashAlgorithmComboBox.getItems().addAll(
            "SHA-256 (Strong - Recommended)",
            "SHA-512 (Very Strong)",
            "SHA-384 (Strong)",
            "SHA-224 (Moderate)",
            "SHA-1 (Weak (Legacy) - Not Recommended)",
            "MD5 (Weak (Legacy) - Not Recommended)"
        );
        hashAlgorithmComboBox.getSelectionModel().select("SHA-256 (Strong - Recommended)");

        // Initialize error label
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        }
    }

    private void loadHeader() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/steganography/PaneHeader.fxml"));
            Node header = loader.load();

            // Anchor to top of rootPane
            AnchorPane.setTopAnchor(header, 0.0);
            AnchorPane.setLeftAnchor(header, 0.0);
            AnchorPane.setRightAnchor(header, 0.0);

            rootPane.getChildren().add(header);
        } catch (IOException e) {
            System.err.println("Failed to load header: " + e.getMessage());
            e.printStackTrace();
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
            System.out.println("Password hashed ("  + getAlgorithm() + "): " + hashedPassword);
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
        MessageDigest digest = MessageDigest.getInstance(getAlgorithm());
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
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

    private String getAlgorithm(){
        return hashAlgorithmComboBox.getValue().split(" ")[0];
    }

    private String getHashedPassword() {
        return hashedPassword;
    }
}