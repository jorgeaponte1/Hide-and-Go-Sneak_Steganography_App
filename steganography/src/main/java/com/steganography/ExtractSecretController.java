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
import javafx.scene.layout.AnchorPane;

public class ExtractSecretController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Button savePasswordButton;

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

    private static String hashedPassword = "";

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
    private void onSavePasswordClicked() {
        String password = getPassword().trim();
        if (password.isEmpty()) {
            showError("Password field is empty.");
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
            showError("Please enter and save a password first.");
            return;
        }

        File file = MainStartController.getSelectedExtractImageFile();
        if (file == null) {
            showError("No image selected for extraction.");
            return;
        }

        try {
            String extracted = SteganographyUtil.extractMessage(file);
            if (extracted == null || !extracted.startsWith("S")) {
                showError("Invalid or corrupted image.");
                return;
            }

            String[] parts = extracted.substring(1).split("\\|", 2);
            if (parts.length < 2) {
                showError("Corrupted embedded data.");
                return;
            }

            String embeddedHash = parts[0];
            String message = parts[1];

            if (embeddedHash.equals(hashedPassword)) {
                hideError();
                ExtractMessageController.setExtractedMessage(message);
                System.out.println("Password correct. Proceeding...");
                App.setRoot("PaneExtractMessage");
            } else {
                showError("Incorrect password.");
            }

        } catch (IOException e) {
            showError("Error extracting message: " + e.getMessage());
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

    private String getAlgorithm(){
        return hashAlgorithmComboBox.getValue().split(" ")[0];
    }

    static String getHashedPassword() {
        return hashedPassword;
    }
}