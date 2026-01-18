package com.microprofile.utils;

import java.util.Arrays;
import java.util.List;

public class Utils {
    public static String generateUserName(String phoneNumber) {
        List<String> letter = List.of(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j"
        );
        List<String> name = new java.util.ArrayList<>(List.of());
        Arrays.stream(phoneNumber.split("")).forEach(n -> {
            int index = Integer.parseInt(n);
            name.add(letter.get(index));
        });
        return String.join("", name);
    }
}
