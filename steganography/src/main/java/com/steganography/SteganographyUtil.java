package com.steganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SteganographyUtil {

    static Image embedMessage(File inputImageFile, String hashedPassword, String secretMessage) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputImageFile);
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        String data = hashedPassword + "|" + secretMessage;
        byte[] fullMessageBytes = data.getBytes();

        // Prefix with marker 'S' and append null terminator
        byte[] fullBytes = new byte[fullMessageBytes.length + 2];
        fullBytes[0] = 0x53; // ASCII 'S' marker
        System.arraycopy(fullMessageBytes, 0, fullBytes, 1, fullMessageBytes.length);
        fullBytes[fullBytes.length - 1] = 0x00;

        // Convert message to binary string
        StringBuilder binaryMessage = new StringBuilder();
        for (byte b : fullBytes) {
            binaryMessage.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        int bitIndex = 0;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        outer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // First 8 bits go to Blue channel LSBs for marker 'S' only
                if (bitIndex < 8) {
                    int bit = binaryMessage.charAt(bitIndex++) - '0';
                    b = (b & 0xFE) | bit;
                } else {
                    // After marker, use R -> G -> B in sequence
                    if (bitIndex < binaryMessage.length()) {
                        int bit = binaryMessage.charAt(bitIndex++) - '0';
                        r = (r & 0xFE) | bit;
                    }
                    if (bitIndex < binaryMessage.length()) {
                        int bit = binaryMessage.charAt(bitIndex++) - '0';
                        g = (g & 0xFE) | bit;
                    }
                    if (bitIndex < binaryMessage.length()) {
                        int bit = binaryMessage.charAt(bitIndex++) - '0';
                        b = (b & 0xFE) | bit;
                    }
                }

                int newRgb = (r << 16) | (g << 8) | b;
                outputImage.setRGB(x, y, newRgb);

                if (bitIndex >= binaryMessage.length()) {
                    // Copy the rest of the image unchanged
                    for (int y2 = y; y2 < height; y2++) {
                        for (int x2 = (y2 == y ? x + 1 : 0); x2 < width; x2++) {
                            outputImage.setRGB(x2, y2, inputImage.getRGB(x2, y2));
                        }
                    }
                    break outer;
                }
            }
        }

        // Convert to JavaFX WritableImage
        WritableImage fxImage = new WritableImage(width, height);
        PixelWriter writer = fxImage.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = outputImage.getRGB(x, y);
                Color color = Color.rgb(
                    (rgb >> 16) & 0xFF,
                    (rgb >> 8) & 0xFF,
                    rgb & 0xFF
                );
                writer.setColor(x, y, color);
            }
        }

        return fxImage;
    }

    static String extractMessage(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) return null;

        StringBuilder bits = new StringBuilder();
        StringBuilder message = new StringBuilder();
        int width = image.getWidth();
        int height = image.getHeight();
        //int pixelCount = width * height;

        // Step 1: Extract first 8 bits from blue channel only (marker)
        int markerBitCount = 8;
        int pixelIndex = 0;

        for (int y = 0; y < height && markerBitCount > 0; y++) {
            for (int x = 0; x < width && markerBitCount > 0; x++) {
                int rgb = image.getRGB(x, y);
                int b = rgb & 0xFF;
                bits.append(b & 1);
                pixelIndex++;
                markerBitCount--;
            }
        }

        // Decode first byte (should be 'S')
        if (bits.length() < 8) return null;
        int marker = Integer.parseInt(bits.substring(0, 8), 2);
        if (marker != 0x53) {
            System.out.println("Decoded message does not start with marker 'S'");
            return null;
        }

        message.append((char) marker);
        bits.setLength(0); // clear bits

        // Step 2: Continue extracting from remaining pixels using R, G, B pattern
        int x = pixelIndex % width;
        int y = pixelIndex / width;

        outer:
        for (; y < height; y++) {
            for (; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                bits.append(r & 1);
                if (bits.length() >= 8) {
                    String byteStr = bits.substring(0, 8);
                    bits.delete(0, 8);
                    int charCode = Integer.parseInt(byteStr, 2);
                    if (charCode == 0) break outer;
                    message.append((char) charCode);
                }

                bits.append(g & 1);
                if (bits.length() >= 8) {
                    String byteStr = bits.substring(0, 8);
                    bits.delete(0, 8);
                    int charCode = Integer.parseInt(byteStr, 2);
                    if (charCode == 0) break outer;
                    message.append((char) charCode);
                }

                bits.append(b & 1);
                if (bits.length() >= 8) {
                    String byteStr = bits.substring(0, 8);
                    bits.delete(0, 8);
                    int charCode = Integer.parseInt(byteStr, 2);
                    if (charCode == 0) break outer;
                    message.append((char) charCode);
                }
            }
            x = 0; // reset x after first row
        }

        String decoded = message.toString();
        return decoded;
    }

    static boolean confirmReturnToStart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Navigation");
        alert.setHeaderText("Return to Start");
        alert.setContentText("Are you sure you want to return to the start? All unsaved data will be lost.");

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }
}