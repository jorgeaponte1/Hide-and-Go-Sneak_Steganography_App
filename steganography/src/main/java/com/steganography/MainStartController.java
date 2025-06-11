package com.steganography;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    private ImageView embedImageView;
    
    @FXML
    private TextField embedImagePathField;
    
    @FXML
    private Label extractErrorLabel;
    
    @FXML
    private Label embedErrorLabel;
    
    @FXML
    private Label embedOverlayLabel;
    
    @FXML
    private Label extractOverlayLabel;
    
    @FXML
    private StackPane embedImageStack;
    
    @FXML
    private StackPane extractImageStack;

    @FXML
    private AnchorPane rootPane;

    private static File selectedEmbedImageFile;
    
    private static File selectedExtractImageFile;

    @FXML
    private void initialize() {
        loadHeader();

        extractNextButton.setDisable(true);
        reset();

        setupDragAndDrop(embedImageStack, embedImageView, embedImagePathField, embedOverlayLabel, true);
        setupDragAndDrop(extractImageStack, extractImageView, extractImagePathField, extractOverlayLabel, false);

        embedOverlayLabel.setVisible(true);
        extractOverlayLabel.setVisible(true);
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

    private void setupDragAndDrop(StackPane stackPane, ImageView imageView, TextField pathField, Label overlayLabel, boolean isEmbed) {
        stackPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        stackPane.setOnDragEntered(event -> {
            imageView.setStyle("-fx-border-color: #4A90E2; -fx-border-width: 2;");
            overlayLabel.setVisible(true);
        });

        stackPane.setOnDragExited(event -> {
            imageView.setStyle("-fx-border-color: gray; -fx-border-width: 2;");
        });

        stackPane.setOnDragDropped(event -> {
            boolean success = false;
            var db = event.getDragboard();
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                if (isValidImageFile(file)) {
                    if (!isEmbed && !checkImageHasEmbeddedData(file)) {
                        extractErrorLabel.setText("Dropped image has no hidden message.");
                        extractErrorLabel.setVisible(true);
                        extractNextButton.setDisable(true);
                    } else {
                        if (isEmbed) {
                            selectedEmbedImageFile = file;
                        } else {
                            selectedExtractImageFile = file;
                            extractNextButton.setDisable(false);
                        }
                        pathField.setText(file.getAbsolutePath());
                        imageView.setImage(new Image(file.toURI().toString()));
                        overlayLabel.setVisible(false);
                        extractErrorLabel.setVisible(false);
                        embedErrorLabel.setVisible(false);
                        success = true;
                    }
                } else {
                    if (isEmbed) {
                        embedErrorLabel.setText("Unsupported file type dropped.");
                        embedErrorLabel.setVisible(true);
                    } else {
                        extractErrorLabel.setText("Unsupported file type dropped.");
                        extractErrorLabel.setVisible(true);
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private boolean isValidImageFile(File file) {
        String path = file.getAbsolutePath().toLowerCase();
        return path.matches(".*\\.(png|jpg|jpeg|gif|bmp)$");
    }

    @FXML
    private void onSelectEmbedImage() {
        embedErrorLabel.setVisible(false);
        File file = chooseImageFile("Select Image for Embedding");
        if (file != null) {
            selectedEmbedImageFile = file;
            embedImagePathField.setText(file.getAbsolutePath());
            embedImageView.setImage(new Image(file.toURI().toString()));
            embedOverlayLabel.setVisible(false);
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
        App.setRoot("PaneEmbedMessage");
    }

    @FXML
    private void onSelectExtractImage() {
        extractErrorLabel.setVisible(false); // Clear previous error
        File file = chooseImageFile("Select Image for Extraction");
        if (file != null) {
            if (!checkImageHasEmbeddedData(file)) {
                extractNextButton.setDisable(true);
                extractErrorLabel.setText("The selected image does not appear to contain a hidden message.");
                extractErrorLabel.setVisible(true);
                return;
            }
            selectedExtractImageFile = file;
            extractImagePathField.setText(file.getAbsolutePath());
            extractImageView.setImage(new Image(file.toURI().toString()));
            extractOverlayLabel.setVisible(false);
            extractNextButton.setDisable(false);
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

        App.setRoot("PaneExtractSecret");
    }

    private File chooseImageFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        return fileChooser.showOpenDialog(null);
    }

    static void reset() {
        selectedEmbedImageFile = null;
        selectedExtractImageFile = null;
    }

    private boolean checkImageHasEmbeddedData(File file) {
        try {
            java.awt.image.BufferedImage image = javax.imageio.ImageIO.read(file);
            if (image == null) return false;

            // Extract the first 8 bits (1 byte) from LSBs of blue channel
            int marker = 0;
            int bitIndex = 0;

            outerLoop:
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    int blue = rgb & 0xFF;
                    int lsb = blue & 1;

                    marker = (marker << 1) | lsb;
                    bitIndex++;

                    if (bitIndex == 8) break outerLoop;
                }
            }

            return marker == 0x53; // ASCII value of 'S' is 0x53

        } catch (IOException e) {
            System.out.println("Error reading image: " + e.getMessage());
            return false;
        }
    }

    static File getSelectedEmbedImageFile() {
        return selectedEmbedImageFile;
    }

    static File getSelectedExtractImageFile() {
        return selectedExtractImageFile;
    }
}