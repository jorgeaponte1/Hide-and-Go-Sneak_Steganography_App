package com.steganography;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController {
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

    private static File selectedEmbedImageFile;

    private static File selectedExtractImageFile;

    @FXML
    private void onSelectEmbedImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image for Embedding");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            selectedEmbedImageFile = file;
            embedImagePathField.setText(file.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            embedImageView.setImage(image);
            System.out.println("Embed: Image selected - " + file.getAbsolutePath());
        } else {
            System.out.println("Embed: Image selection canceled.");
        }
    }

    @FXML
    private void onEmbedNext() throws IOException {
        if (selectedEmbedImageFile == null) {
            System.out.println("Please select an image before proceeding.");
            return;
        }
        // In the future, pass this file to PaneTwo
        System.out.println("Embed: Next clicked, moving to PaneTwo...");
        App.setRoot("PaneTwo");
    }

    @FXML
    private void onSelectExtractImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image for Extraction");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Supported Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String filePath = file.getAbsolutePath().toLowerCase();
            if (!filePath.endsWith(".png") && !filePath.endsWith(".jpg") &&
                !filePath.endsWith(".jpeg") && !filePath.endsWith(".gif") &&
                !filePath.endsWith(".bmp")) {
                System.out.println("Unsupported file type selected.");
                return;
            }

            // TEMP: Replace this with actual marker logic
            boolean hasMessage = checkImageHasEmbeddedData(file);

            if (hasMessage) {
                selectedExtractImageFile = file;
                extractImagePathField.setText(file.getAbsolutePath());
                extractImageView.setImage(new Image(file.toURI().toString()));
                extractNextButton.setDisable(false);
                System.out.println("Extract: Image selected - " + file.getAbsolutePath());
            } else {
                extractNextButton.setDisable(true);
                System.out.println("The selected image does not appear to contain a hidden message.");
            }
        } else {
            System.out.println("Extract: Image selection canceled.");
        }
    }

    @FXML
    private void onExtractNext() throws IOException {
        System.out.println("Extract: Next clicked");
        App.setRoot("PaneExtractOne");
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

            //outerLoop:
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

    // Legacy navigation (optional cleanup)
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToThird() throws IOException {
        App.setRoot("third");
    }

    @FXML
    private void switchToFourth() throws IOException {
        App.setRoot("fourth");
    }
}