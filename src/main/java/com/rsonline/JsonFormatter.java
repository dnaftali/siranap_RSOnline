package com.rsonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class untuk memformat dan menampilkan JSON
 */
public class JsonFormatter {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Memformat JSON string menjadi format yang mudah dibaca
     * @param jsonString JSON string yang akan diformat
     * @return JSON string yang sudah diformat
     */
    public static String formatJson(String jsonString) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (Exception e) {
            // Jika gagal format, return original string
            return jsonString;
        }
    }
    
    /**
     * Menampilkan JSON response dengan format yang rapi
     * @param jsonString JSON string yang akan ditampilkan
     */
    public static void displayJson(String jsonString) {
        System.out.println("\n=== RESPONSE JSON ===");
        System.out.println(formatJson(jsonString));
        System.out.println("====================\n");
    }
    
    /**
     * Menampilkan error message dengan format yang rapi
     * @param errorMessage pesan error
     */
    public static void displayError(String errorMessage) {
        System.out.println("\n=== ERROR ===");
        System.out.println(errorMessage);
        System.out.println("=============\n");
    }
    
    /**
     * Menampilkan success message dengan format yang rapi
     * @param message pesan sukses
     */
    public static void displaySuccess(String message) {
        System.out.println("\n=== SUCCESS ===");
        System.out.println(message);
        System.out.println("==============\n");
    }
}
