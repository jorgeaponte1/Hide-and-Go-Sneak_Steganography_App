package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

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

    @FXML
    private AnchorPane rootPane;

    static String secretMessage = "";

    @FXML
    private void initialize(){
        loadHeader();
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
    private void onSaveClicked() {
        secretMessage = messageTextArea.getText().trim();

        if (secretMessage.isEmpty()) {
            messageErrorLabel.setText("Message is empty.");
            messageErrorLabel.setVisible(true);
        } else {
            messageErrorLabel.setVisible(false);
            System.out.println("Message saved: " + secretMessage);
        }
    }

    @FXML
    private void onNextClicked() throws IOException {
        if (secretMessage.isEmpty()) {
            messageErrorLabel.setText("No message saved.");
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