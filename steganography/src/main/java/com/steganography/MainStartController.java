package com.steganography;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class MainStartController {
    @FXML
    private ImageView extractImageView;

    @FXML
    private TextField extractImagePathField;

    @FXML
    private Button selectEmbedImageButton;

    @FXML
    private Button embedNextButton;

    @FXML
    private Button selectExtractImageButton;

    @FXML
    private Button extractNextButton;

    @FXML
    private ImageView embedImageView; // reference to ImageView in Embed tab

    @FXML
    private TextField embedImagePathField;

    @FXML
    private Label extractErrorLabel;

    @FXML
    private Label embedErrorLabel;

    private static File selectedEmbedImageFile;

    private static File selectedExtractImageFile;

    @FXML
    private void initialize() {
        extractNextButton.setDisable(true);
        reset();
    }

    @FXML
    private void onSelectEmbedImage() {
        embedErrorLabel.setVisible(false); // Hide any previous errors

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image for Embedding");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(selectEmbedImageButton.getScene().getWindow());
        if (file != null) {
            selectedEmbedImageFile = file;
            embedImagePathField.setText(file.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            embedImageView.setImage(image);
            System.out.println("Embed: Image selected - " + file.getAbsolutePath());
        } else {
            embedErrorLabel.setText("Embed: Image selection canceled.");
            embedErrorLabel.setVisible(true);
        }
    }

    @FXML
    private void onEmbedNext() throws IOException {
        embedErrorLabel.setVisible(false);

        if (selectedEmbedImageFile == null) {
            embedErrorLabel.setText("Please select an image before proceeding.");
            embedErrorLabel.setVisible(true);
            return;
        }

        System.out.println("Embed: Next clicked, moving to Embed Message...");
        App.setRoot("PaneEmbedMessage");
    }

    @FXML
    private void onSelectExtractImage() {
        extractErrorLabel.setVisible(false); // Clear previous error

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image for Extraction");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Supported Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(selectExtractImageButton.getScene().getWindow());
        if (file != null) {
            String filePath = file.getAbsolutePath().toLowerCase();
            if (!filePath.matches(".*\\.(png|jpg|jpeg|gif|bmp)$")) {
                extractErrorLabel.setText("Unsupported file type selected.");
                extractErrorLabel.setVisible(true);
                return;
            }

            boolean hasMessage = checkImageHasEmbeddedData(file);

            if (hasMessage) {
                selectedExtractImageFile = file;
                extractImagePathField.setText(file.getAbsolutePath());
                extractImageView.setImage(new Image(file.toURI().toString()));
                extractNextButton.setDisable(false);
                System.out.println("Extract: Image selected - " + file.getAbsolutePath());
            } else {
                extractNextButton.setDisable(true);
                extractErrorLabel.setText("The selected image does not appear to contain a hidden message.");
                extractErrorLabel.setVisible(true);
            }
        } else {
            extractErrorLabel.setText("Extract: Image selection canceled.");
            extractErrorLabel.setVisible(true);
        }
    }

    @FXML
    private void onExtractNext() throws IOException {
        extractErrorLabel.setVisible(false);

        if (selectedExtractImageFile == null) {
            extractErrorLabel.setText("Please select an image before proceeding.");
            extractErrorLabel.setVisible(true);
            return;
        }

        System.out.println("Extract: Next clicked, moving to Extract Secret...");
        App.setRoot("PaneExtractSecret");
    }

    public static void reset() {
        selectedEmbedImageFile = null;
        selectedExtractImageFile = null;
    }

    private boolean checkImageHasEmbeddedData(File file) {
        try {
            java.awt.image.BufferedImage image = javax.imageio.ImageIO.read(file);
            if (image == null) return false;

            int width = image.getWidth();
            int height = image.getHeight();

            // Check a few least-significant bits in the blue channel for a known marker (like 'S' = 0x53)
            int markerBits = 0;
            int bitIndex = 0;

            for (int y = 0; y < height && bitIndex < 8; y++) {
                for (int x = 0; x < width && bitIndex < 8; x++) {
                    int rgb = image.getRGB(x, y);
                    int blue = rgb & 0xFF;
                    int lsb = blue & 1;
                    markerBits = (markerBits << 1) | lsb;
                    bitIndex++;
                }
            }

            return markerBits == 0x53; // ASCII 'S' as a basic marker
        } catch (IOException e) {
            System.out.println("Error reading image: " + e.getMessage());
            return false;
        }
    }

    public static File getSelectedEmbedImageFile() {
        return selectedEmbedImageFile;
    }

    public static File getSelectedExtractImageFile() {
        return selectedExtractImageFile;
    }
}