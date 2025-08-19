package com.rsonline;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Client untuk melakukan request ke API Kemenkes
 */
public class KemenkesApiClient {
    
    public KemenkesApiClient() {
        // Tidak perlu HTTP client lagi karena menggunakan HttpURLConnection standar Java
    }
    
    /**
     * Melakukan GET request untuk mendapatkan data tempat tidur
     * @return response dalam bentuk JSON string
     * @throws Exception jika terjadi error pada HTTP request
     */
    public String getBedData() throws Exception {
        String jsonResponse = null;
        String strTimeStamp = Config.getCurrentTimestamp();
        
        // Gunakan HttpURLConnection untuk konsistensi dan kompatibilitas maksimal
        URL url = new URL(Config.GET_BED_DATA_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // Set method GET
        conn.setRequestMethod("GET");
        
        // Set headers
        conn.setRequestProperty("X-rs-id", Config.X_RS_ID);
        conn.setRequestProperty("X-pass", Config.X_PASS);
        conn.setRequestProperty("X-Timestamp", strTimeStamp);
        conn.setRequestProperty("Accept", "application/json");
        
        // Timeout
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        
        // Baca response
        int responseCode = conn.getResponseCode();
        System.out.println("Response Status: " + responseCode);
        System.out.println("Response Headers:");
        System.out.println("  Content-Type: " + conn.getHeaderField("Content-Type"));
        System.out.println("  Content-Length: " + conn.getHeaderField("Content-Length"));
        
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            jsonResponse = response.toString();
        } catch (IOException e) {
            // Jika response code bukan 2xx, gunakan error stream
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                jsonResponse = response.toString();
            } catch (IOException ex) {
                jsonResponse = "Error reading response: " + ex.getMessage();
            }
        } finally {
            conn.disconnect();
        }
        
        return jsonResponse;
    }
    
    /**
     * Melakukan POST request untuk mengirim data tempat tidur
     * @param jsonData data JSON yang akan dikirim
     * @return response dari server
     * @throws Exception jika terjadi error pada HTTP request
     */
    public String postBedData(String jsonData) throws Exception {
        String jsonResponse = null;
        String strTimeStamp = Config.getCurrentTimestamp();
        
        // Gunakan HttpURLConnection untuk konsistensi dan kompatibilitas maksimal
        URL url = new URL(Config.POST_BED_DATA_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // Set method POST
        conn.setRequestMethod("POST");
        
        // Set headers
        conn.setRequestProperty("X-rs-id", Config.X_RS_ID);
        conn.setRequestProperty("X-pass", Config.X_PASS);
        conn.setRequestProperty("X-Timestamp", strTimeStamp);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        
        // Set doOutput untuk mengirim body
        conn.setDoOutput(true);
        
        // Timeout
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        
        // Kirim body JSON
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Baca response
        int responseCode = conn.getResponseCode();
        System.out.println("HTTP Status Code: " + responseCode);
        
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            jsonResponse = response.toString();
        } catch (IOException e) {
            // Jika response code bukan 2xx, gunakan error stream
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                jsonResponse = response.toString();
            } catch (IOException ex) {
                jsonResponse = "Error reading response: " + ex.getMessage();
            }
        } finally {
            conn.disconnect();
        }
        
        return jsonResponse;
    }
    
    /**
     * Melakukan PUT request untuk update data tempat tidur
     * @param jsonData data JSON yang akan diupdate
     * @return response dari server
     * @throws Exception jika terjadi error pada HTTP request
     */
    public String putBedData(String jsonData) throws Exception {
        String jsonResponse = null;
        String strTimeStamp = Config.getCurrentTimestamp();
        
        // Gunakan HttpURLConnection untuk konsistensi dan kompatibilitas maksimal
        URL url = new URL(Config.PUT_BED_DATA_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // Set method PUT
        conn.setRequestMethod("PUT");
        
        // Set headers
        conn.setRequestProperty("X-rs-id", Config.X_RS_ID);
        conn.setRequestProperty("X-pass", Config.X_PASS);
        conn.setRequestProperty("X-Timestamp", strTimeStamp);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        
        // Set doOutput untuk mengirim body
        conn.setDoOutput(true);
        
        // Timeout
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        
        // Kirim body JSON
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Baca response
        int responseCode = conn.getResponseCode();
        System.out.println("HTTP Status Code: " + responseCode);
        
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            jsonResponse = response.toString();
        } catch (IOException e) {
            // Jika response code bukan 2xx, gunakan error stream
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                jsonResponse = response.toString();
            } catch (IOException ex) {
                jsonResponse = "Error reading response: " + ex.getMessage();
            }
        } finally {
            conn.disconnect();
        }
        
        return jsonResponse;
    }
    
    /**
     * Melakukan DELETE request untuk menghapus data tempat tidur berdasarkan id_t_tt
     * Menggunakan HttpURLConnection standar Java untuk kompatibilitas maksimal
     * @param idTtt ID tempat tidur yang akan dihapus
     * @return response dari server
     * @throws Exception jika terjadi error pada HTTP request
     */
    public String deleteBedData(String idTtt) throws Exception {
        String jsonResponse = null;
        String strTimeStamp = Config.getCurrentTimestamp();
        
        // Buat body JSON untuk delete
        String jsonBody = "{\n"
                + "    \"id_t_tt\": \"" + idTtt + "\"\n"
                + "}";
        
        System.out.println("Request body untuk delete: " + jsonBody);
        
        // Gunakan HttpURLConnection untuk konsistensi dan kompatibilitas maksimal
        java.net.URL url = new java.net.URL(Config.DELETE_BED_DATA_URL);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        
        // Set method DELETE
        conn.setRequestMethod("DELETE");
        
        // Set header
        conn.setRequestProperty("X-rs-id", Config.X_RS_ID);
        conn.setRequestProperty("X-Timestamp", strTimeStamp);
        conn.setRequestProperty("X-pass", Config.X_PASS);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        
        // Set doOutput untuk mengirim body
        conn.setDoOutput(true);
        
        // Timeout
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        
        // Kirim body JSON
        try (java.io.OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Baca response
        int responseCode = conn.getResponseCode();
        System.out.println("HTTP Status Code: " + responseCode);
        
        try (java.io.BufferedReader in = new java.io.BufferedReader(
                new java.io.InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            jsonResponse = response.toString();
        } catch (IOException e) {
            // Jika response code bukan 2xx, gunakan error stream
            try (java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                jsonResponse = response.toString();
            } catch (IOException ex) {
                jsonResponse = "Error reading response: " + ex.getMessage();
            }
        } finally {
            conn.disconnect();
        }
        
        return jsonResponse;
    }
    
    /**
     * Menutup HTTP client (tidak diperlukan lagi karena menggunakan HttpURLConnection)
     */
    public void close() throws IOException {
        // Tidak perlu menutup apa-apa karena HttpURLConnection otomatis ter-disconnect
        // setelah setiap request selesai
    }
}
