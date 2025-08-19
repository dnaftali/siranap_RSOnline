package com.rsonline;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Class untuk menyimpan konfigurasi aplikasi
 */
public class Config {
    
    // Oracle Database credentials
    public static final String ORACLE_USER = "SIMRSLIVE";
    public static final String ORACLE_PASS = "Xk9ReNheBbTb7Nf5";
    public static final String ORACLE_URL = "jdbc:oracle:thin:@10.100.254.8:1521:DBPROD";
    
    // API Kemenkes credentials
    public static final String BASE_URL = "https://sirs.kemkes.go.id/fo/index.php/";
    public static final String X_RS_ID = "3404015"; // kode faskes
    public static final String X_PASS = "S4rdj1t@!"; // password bridging
    
    // API Endpoints
    public static final String GET_BED_DATA_URL = BASE_URL + "Fasyankes";
    public static final String POST_BED_DATA_URL = BASE_URL + "Fasyankes";
    public static final String PUT_BED_DATA_URL = BASE_URL + "Fasyankes";
    public static final String DELETE_BED_DATA_URL = BASE_URL + "Fasyankes";
    
    /**
     * Mendapatkan timestamp saat ini untuk header X-Timestamp
     * Format: Unix timestamp dalam detik (bukan milliseconds)
     */
    public static String getCurrentTimestamp() {
        // Menggunakan Unix timestamp dalam detik (bukan milliseconds)
        long unixTimestamp = System.currentTimeMillis() / 1000;
        return String.valueOf(unixTimestamp);
    }
    
    /**
     * Mendapatkan timestamp dalam format ISO 8601 untuk debugging
     */
    public static String getCurrentTimestampISO() {
        Instant now = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(now);
    }
}
