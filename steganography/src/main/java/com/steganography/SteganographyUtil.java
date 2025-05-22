package com.steganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SteganographyUtil {

    public static Image embedMessage(File inputImageFile, String message) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputImageFile);
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        // Convert message to binary
        byte[] messageBytes = message.getBytes();
        StringBuilder binaryMessage = new StringBuilder();
        for (byte b : messageBytes) {
            binaryMessage.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        binaryMessage.append("00000000"); // Null terminator

        int msgIndex = 0;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        outerLoop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                if (msgIndex < binaryMessage.length()) {
                    b = (b & 0xFE) | (binaryMessage.charAt(msgIndex) - '0');
                    msgIndex++;
                }

                int newRgb = (r << 16) | (g << 8) | b;
                outputImage.setRGB(x, y, newRgb);

                if (msgIndex >= binaryMessage.length()) {
                    break outerLoop;
                }
            }
        }

        // Convert to JavaFX Image
        WritableImage fxImage = new WritableImage(width, height);
        PixelWriter writer = fxImage.getPixelWriter();
        for (int y = 0; y < outputImage.getHeight(); y++) {
            for (int x = 0; x < outputImage.getWidth(); x++) {
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
}