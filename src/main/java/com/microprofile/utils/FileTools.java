package com.microprofile.utils;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class FileTools {
    public static String getResource(String name) {
        try (var appId = FileTools.class.getClassLoader().getResourceAsStream(name)) {
            assert appId != null;
            return new String(appId.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static String getUserHomeConfig(String filePath) {
        try (FileInputStream fileStream = new FileInputStream(String.format("%s/.opensponsor/%s", System.getenv("HOME"), filePath))) {
            return new String(fileStream.readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
