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

    public static Image embedMessage(File inputImageFile, String hashedPassword, String secretMessage) throws IOException {
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

        StringBuilder binaryMessage = new StringBuilder();
        for (byte b : fullBytes) {
            binaryMessage.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

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

    public static String extractMessage(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) return null;

        StringBuilder binary = new StringBuilder();
        
        outerLoop:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int b = rgb & 0xFF;
                binary.append(b & 1);

                if (binary.length() % 8 == 0) {
                    String byteStr = binary.substring(binary.length() - 8);
                    if (Integer.parseInt(byteStr, 2) == 0x00) break outerLoop;
                }
            }
        }

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String byteStr = binary.substring(i, Math.min(i + 8, binary.length()));
            int charCode = Integer.parseInt(byteStr, 2);
            if (charCode == 0x00) break;
            message.append((char) charCode);
        }

        return message.toString(); // should be: S<hash>|<secret>
    }
}