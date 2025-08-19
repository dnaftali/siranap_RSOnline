package com.rsonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class untuk parsing response dari API Kemenkes
 */
public class ResponseParser {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Parse response POST/PUT/DELETE dari API Kemenkes
     * @param response response string dari API
     * @return true jika berhasil, false jika gagal
     */
    public static boolean isSuccess(String response) {
        if (response == null || response.trim().isEmpty()) {
            return false;
        }
        
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode fasyankesNode = rootNode.get("fasyankes");
            
            if (fasyankesNode != null && fasyankesNode.isArray() && fasyankesNode.size() > 0) {
                JsonNode firstItem = fasyankesNode.get(0);
                JsonNode statusNode = firstItem.get("status");
                
                if (statusNode != null) {
                    String status = statusNode.asText();
                    // Status "200" berarti berhasil
                    return "200".equals(status);
                }
            }
            
            // Fallback: cek apakah ada kata "success" atau "200" dalam response
            String responseLower = response.toLowerCase();
            return responseLower.contains("success") || responseLower.contains("\"200\"");
            
        } catch (Exception e) {
            // Jika parsing JSON gagal, gunakan fallback
            String responseLower = response.toLowerCase();
            return responseLower.contains("success") || responseLower.contains("200");
        }
    }
    
    /**
     * Parse response dan ekstrak message jika ada
     * @param response response string dari API
     * @return message dari response, atau null jika tidak ada
     */
    public static String extractMessage(String response) {
        if (response == null || response.trim().isEmpty()) {
            return null;
        }
        
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode fasyankesNode = rootNode.get("fasyankes");
            
            if (fasyankesNode != null && fasyankesNode.isArray() && fasyankesNode.size() > 0) {
                JsonNode firstItem = fasyankesNode.get(0);
                JsonNode messageNode = firstItem.get("message");
                
                if (messageNode != null && !messageNode.isNull()) {
                    String message = messageNode.asText();
                    return message.isEmpty() ? null : message;
                }
            }
            
        } catch (Exception e) {
            // Ignore parsing errors
        }
        
        return null;
    }
    
    /**
     * Parse response dan ekstrak status code
     * @param response response string dari API
     * @return status code dari response, atau null jika tidak ada
     */
    public static String extractStatus(String response) {
        if (response == null || response.trim().isEmpty()) {
            return null;
        }
        
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode fasyankesNode = rootNode.get("fasyankes");
            
            if (fasyankesNode != null && fasyankesNode.isArray() && fasyankesNode.size() > 0) {
                JsonNode firstItem = fasyankesNode.get(0);
                JsonNode statusNode = firstItem.get("status");
                
                if (statusNode != null) {
                    return statusNode.asText();
                }
            }
            
        } catch (Exception e) {
            // Ignore parsing errors
        }
        
        return null;
    }
}
