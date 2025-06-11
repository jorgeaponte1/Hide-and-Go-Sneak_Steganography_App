package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ExtractMessageController {

    @FXML
    private Label errorLabel;

    @FXML
    private Button returnButton;

    @FXML
    private Text messageText;

    @FXML
    private AnchorPane rootPane;

    private static String extractedMessage = "";

    @FXML
    private void initialize() {
        loadHeader();
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        }

        if (extractedMessage == null || extractedMessage.trim().isEmpty()) {
            showError("No message could be extracted. Please try again with a valid image and password.");
            messageText.setText("");
        } else {
            hideError();
            messageText.setText(extractedMessage);
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

    @FXML
    private void onReturnClicked() throws IOException {
        App.setRoot("PaneMain");
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

    static void setExtractedMessage(String message) {
        extractedMessage = message;
    }
}