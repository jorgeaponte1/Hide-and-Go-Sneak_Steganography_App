module com.steganography {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop; // Needed for ImageIO
    requires javafx.swing; // Needed for SwingFXUtils

    opens com.steganography to javafx.fxml;
    exports com.steganography;
}