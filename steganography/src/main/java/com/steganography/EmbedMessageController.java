package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class EmbedMessageController {

    @FXML
    private Button nextButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button returnToStartButton;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private Label messageErrorLabel;

    static String secretMessage = "";

    @FXML
    private void onSaveClicked() {
        secretMessage = messageTextArea.getText().trim();

        if (secretMessage.isEmpty()) {
            messageErrorLabel.setText("Message is empty. Please write a message before saving.");
            messageErrorLabel.setVisible(true);
        } else {
            messageErrorLabel.setVisible(false);
            System.out.println("Message saved: " + secretMessage);
        }
    }

    @FXML
    private void onNextClicked() throws IOException {
        if (secretMessage.isEmpty()) {
            messageErrorLabel.setText("No message saved. Please save a message before proceeding.");
            messageErrorLabel.setVisible(true);
        } else {
            messageErrorLabel.setVisible(false);
            System.out.println("Proceeding to Embed Secret...");
            App.setRoot("PaneEmbedSecret");
        }
    }

    @FXML
    private void onReturnToStartClicked() throws IOException {
        if(SteganographyUtil.confirmReturnToStart()){
            System.out.println("Returning to start (primary view)...");
            secretMessage = "";
            messageTextArea.clear();
            messageErrorLabel.setVisible(false);
            MainStartController.reset();
            App.setRoot("PaneMain");
        } else {
            System.out.println("Return to start canceled.");
        }
    }

    static String getSecretMessage() {
        return secretMessage;
    }
}