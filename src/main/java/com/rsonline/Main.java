package com.rsonline;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import org.apache.hc.core5.http.ParseException;

/**
 * Main class untuk aplikasi RS Online Sync
 */
public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final KemenkesApiClient apiClient = new KemenkesApiClient();
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    APLIKASI RS ONLINE SYNC");
        System.out.println("    Sinkronisasi Data Tempat Tidur");
        System.out.println("    ke API Kemenkes");
        System.out.println("========================================");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("========================================");
        
        try {
            showMainMenu();
        } catch (Exception e) {
            JsonFormatter.displayError("Terjadi error pada aplikasi: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                apiClient.close();
            } catch (IOException e) {
                System.err.println("Error saat menutup HTTP client: " + e.getMessage());
            }
            scanner.close();
        }
    }
    
    /**
     * Menampilkan menu utama aplikasi
     */
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. GET Data Tempat Tidur dari Kemenkes");
            System.out.println("2. POST Data Tempat Tidur ke Kemenkes");
            System.out.println("3. PUT/Update Data Tempat Tidur");
            System.out.println("4. DELETE Data Tempat Tidur dari Kemenkes");
            System.out.println("5. Auto-Sync (DELETE + POST) Sekali Jalankan");
            System.out.println("6. Auto-Sync Berkelanjutan (Setiap 30 Menit)");
            System.out.println("7. Keluar");
            System.out.println("==================");
            System.out.print("Pilih menu (1-7): ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    getBedDataFromKemenkes();
                    break;
                case "2":
                    postBedDataToKemenkes();
                    break;
                case "3":
                    System.out.println("Fitur PUT akan diimplementasikan pada tahap selanjutnya");
                    break;
                case "4":
                    deleteBedDataFromKemenkes();
                    break;
                case "5":
                    autoSyncOnce();
                    break;
                case "6":
                    autoSyncContinuous();
                    break;
                case "7":
                    System.out.println("Terima kasih telah menggunakan aplikasi RS Online Sync!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid! Silakan pilih 1-7.");
            }
        }
    }
    
    /**
     * Melakukan GET data tempat tidur dari API Kemenkes
     */
    private static void getBedDataFromKemenkes() {
        System.out.println("\n=== GET DATA TEMPAT TIDUR DARI KEMENKES ===");
        System.out.println("URL: " + Config.GET_BED_DATA_URL);
        System.out.println("X-rs-id: " + Config.X_RS_ID);
        System.out.println("X-Timestamp (Unix): " + Config.getCurrentTimestamp());
        System.out.println("Timestamp (ISO): " + Config.getCurrentTimestampISO());
        System.out.println("=============================================");
        
        System.out.print("Tekan Enter untuk melanjutkan...");
        scanner.nextLine();
        
        try {
            System.out.println("Mengirim request ke API Kemenkes...");
            String response = apiClient.getBedData();
            
            if (response != null && !response.trim().isEmpty()) {
                JsonFormatter.displaySuccess("Berhasil mendapatkan data dari API Kemenkes!");
                JsonFormatter.displayJson(response);
            } else {
                JsonFormatter.displayError("Response kosong dari API Kemenkes");
            }
            
        } catch (IOException | ParseException e) {
            JsonFormatter.displayError("Error saat melakukan request ke API Kemenkes: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JsonFormatter.displayError("Error tidak terduga: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.print("Tekan Enter untuk kembali ke menu utama...");
        scanner.nextLine();
    }
    
    /**
     * Melakukan DELETE data tempat tidur dari API Kemenkes
     * Proses: GET semua data -> Loop delete setiap id_t_tt
     */
    private static void deleteBedDataFromKemenkes() {
        System.out.println("\n=== DELETE DATA TEMPAT TIDUR DARI KEMENKES ===");
        System.out.println("Proses: GET semua data -> Loop delete setiap id_t_tt");
        System.out.println("==================================================");
        
        try {
            // Step 1: GET semua data tempat tidur
            System.out.println("\n--- Step 1: Mengambil semua data tempat tidur ---");
            String response = apiClient.getBedData();
            
            if (response == null || response.trim().isEmpty()) {
                JsonFormatter.displayError("Gagal mendapatkan data dari API Kemenkes");
                return;
            }
            
            // Step 2: Parse response dan ekstrak id_t_tt
            System.out.println("--- Step 2: Parsing response dan ekstrak id_t_tt ---");
            List<String> idTttList = BedDataParser.extractIdTttList(response);
            
            if (idTttList.isEmpty()) {
                JsonFormatter.displayError("Tidak ada id_t_tt yang ditemukan dalam response");
                return;
            }
            
            System.out.println("Ditemukan " + idTttList.size() + " data tempat tidur untuk dihapus:");
            for (int i = 0; i < Math.min(5, idTttList.size()); i++) {
                System.out.println("  " + (i+1) + ". id_t_tt: " + idTttList.get(i));
            }
            if (idTttList.size() > 5) {
                System.out.println("  ... dan " + (idTttList.size() - 5) + " data lainnya");
            }
            
            // Step 3: Loop delete setiap id_t_tt
            System.out.println("\n--- Step 3: Memulai proses DELETE ---");
            int successCount = 0;
            int failCount = 0;
            
            for (int i = 0; i < idTttList.size(); i++) {
                String idTtt = idTttList.get(i);
                System.out.print("[" + (i+1) + "/" + idTttList.size() + "] Deleting id_t_tt: " + idTtt + " ... ");
                
                try {
                    String deleteResponse = apiClient.deleteBedData(idTtt);
                    
                    // Parse response untuk cek status menggunakan ResponseParser
                    if (ResponseParser.isSuccess(deleteResponse)) {
                        System.out.println("SUCCESS");
                        successCount++;
                    } else {
                        System.out.println("FAILED");
                        System.out.println("    Response: " + deleteResponse);
                        failCount++;
                    }
                    
                    // Delay kecil untuk menghindari rate limiting
                    Thread.sleep(100);
                    
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                    failCount++;
                }
            }
            
            // Step 4: Tampilkan summary
            System.out.println("\n--- SUMMARY DELETE OPERATION ---");
            System.out.println("Total data: " + idTttList.size());
            System.out.println("Berhasil dihapus: " + successCount);
            System.out.println("Gagal dihapus: " + failCount);
            
            if (successCount > 0) {
                JsonFormatter.displaySuccess("Berhasil menghapus " + successCount + " data tempat tidur!");
            }
            
            if (failCount > 0) {
                JsonFormatter.displayError("Gagal menghapus " + failCount + " data tempat tidur");
            }
            
        } catch (Exception e) {
            JsonFormatter.displayError("Error saat melakukan operasi DELETE: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Melakukan POST data tempat tidur ke API Kemenkes
     * Alur: Query database Oracle → Convert ke JSON → Loop POST
     */
    private static void postBedDataToKemenkes() {
        System.out.println("\n=== POST DATA TEMPAT TIDUR KE KEMENKES ===");
        System.out.println("Alur: Query database Oracle → Convert ke JSON → Loop POST");
        System.out.println("=====================================================");
        
        OracleDatabaseClient dbClient = new OracleDatabaseClient();
        
        try {
            // Step 1: Koneksi ke database Oracle
            System.out.println("\n--- Step 1: Koneksi ke database Oracle ---");
            dbClient.connect();
            
            // Step 2: Query data tempat tidur
            System.out.println("--- Step 2: Query data tempat tidur dari database ---");
            List<OracleDatabaseClient.BedData> bedDataList = dbClient.queryBedData();
            
            if (bedDataList.isEmpty()) {
                JsonFormatter.displayError("Tidak ada data tempat tidur yang ditemukan di database");
                return;
            }
            
            System.out.println("Ditemukan " + bedDataList.size() + " data tempat tidur dari database:");
            for (int i = 0; i < Math.min(5, bedDataList.size()); i++) {
                OracleDatabaseClient.BedData bedData = bedDataList.get(i);
                System.out.println("  " + (i+1) + ". " + bedData.toString());
            }
            if (bedDataList.size() > 5) {
                System.out.println("  ... dan " + (bedDataList.size() - 5) + " data lainnya");
            }
            
            // Step 3: Loop POST setiap data
            System.out.println("\n--- Step 3: Memulai proses POST ke API Kemenkes ---");
            int successCount = 0;
            int failCount = 0;
            
            for (int i = 0; i < bedDataList.size(); i++) {
                OracleDatabaseClient.BedData bedData = bedDataList.get(i);
                System.out.print("[" + (i+1) + "/" + bedDataList.size() + "] POST data: " + bedData.getIdTt() + " (" + bedData.getRuang() + ") ... ");
                
                try {
                    // Convert data ke JSON format Kemenkes
                    String jsonData = BedDataConverter.convertToKemenkesJson(bedData);
                    
                    if (jsonData == null) {
                        System.out.println("FAILED - Error converting to JSON");
                        failCount++;
                        continue;
                    }
                    
                    // POST ke API Kemenkes
                    String postResponse = apiClient.postBedData(jsonData);
                    
                    // Parse response untuk cek status menggunakan ResponseParser
                    if (ResponseParser.isSuccess(postResponse)) {
                        System.out.println("SUCCESS");
                        successCount++;
                    } else {
                        System.out.println("FAILED");
                        System.out.println("    Response: " + postResponse);
                        failCount++;
                    }
                    
                    // Delay kecil untuk menghindari rate limiting
                    Thread.sleep(100);
                    
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                    failCount++;
                }
            }
            
            // Step 4: Tampilkan summary
            System.out.println("\n--- SUMMARY POST OPERATION ---");
            System.out.println("Total data dari database: " + bedDataList.size());
            System.out.println("Berhasil di-POST: " + successCount);
            System.out.println("Gagal di-POST: " + failCount);
            
            if (successCount > 0) {
                JsonFormatter.displaySuccess("Berhasil mengirim " + successCount + " data tempat tidur ke API Kemenkes!");
            }
            
            if (failCount > 0) {
                JsonFormatter.displayError("Gagal mengirim " + failCount + " data tempat tidur");
            }
            
        } catch (Exception e) {
            JsonFormatter.displayError("Error tidak terduga: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Pastikan koneksi database ditutup
            dbClient.disconnect();
        }
    }
    
    /**
     * Melakukan auto-sync sekali jalankan: DELETE semua data + POST data baru
     */
    private static void autoSyncOnce() {
        System.out.println("\n=== AUTO-SYNC SEKALI JALANKAN ===");
        System.out.println("Proses: DELETE semua data → POST data baru dari database");
        System.out.println("=====================================================");
        
        try {
            // Step 1: DELETE semua data dari Kemenkes
            System.out.println("\n--- Step 1: DELETE semua data dari Kemenkes ---");
            deleteBedDataFromKemenkes();
            
            // Step 2: POST data baru dari database Oracle
            System.out.println("\n--- Step 2: POST data baru dari database Oracle ---");
            postBedDataToKemenkes();
            
            System.out.println("\n=== AUTO-SYNC SELESAI ===");
            JsonFormatter.displaySuccess("Proses auto-sync berhasil diselesaikan!");
            
        } catch (Exception e) {
            JsonFormatter.displayError("Error saat melakukan auto-sync: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.print("Tekan Enter untuk kembali ke menu utama...");
        scanner.nextLine();
    }
    
    /**
     * Melakukan auto-sync tanpa menunggu input user (untuk background processing)
     */
    private static void autoSyncOnceSilent() {
        try {
            // Step 1: DELETE semua data dari Kemenkes
            System.out.println("\n--- Step 1: DELETE semua data dari Kemenkes ---");
            deleteBedDataFromKemenkes();
            
            // Step 2: POST data baru dari database Oracle
            System.out.println("\n--- Step 2: POST data baru dari database Oracle ---");
            postBedDataToKemenkes();
            
            System.out.println("\n=== AUTO-SYNC SELESAI ===");
            JsonFormatter.displaySuccess("Proses auto-sync berhasil diselesaikan!");
            
        } catch (Exception e) {
            JsonFormatter.displayError("Error saat melakukan auto-sync: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Melakukan auto-sync berkelanjutan setiap 30 menit
     */
    private static void autoSyncContinuous() {
        System.out.println("\n=== AUTO-SYNC BERKELANJUTAN ===");
        System.out.println("Proses akan berjalan setiap 30 menit secara otomatis");
        System.out.println("Dimulai dari sekarang dan akan berjalan terus");
        System.out.println("=================================================");
        
        System.out.print("PERHATIAN: Aplikasi akan berjalan terus menerus!");
        System.out.print("\nUntuk menghentikan, tutup aplikasi atau tekan Ctrl+C");
        System.out.print("\nLanjutkan? (y/N): ");
        String confirm = scanner.nextLine().trim();
        
        if (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("yes")) {
            System.out.println("Auto-sync berkelanjutan dibatalkan.");
            return;
        }
        
        System.out.println("\nMemulai auto-sync berkelanjutan...");
        System.out.println("Sync pertama akan dijalankan sekarang...");
        
        // Jalankan sync pertama (tanpa menunggu input user)
        try {
            autoSyncOnceSilent();
        } catch (Exception e) {
            JsonFormatter.displayError("Error pada sync pertama: " + e.getMessage());
        }
        
        // Mulai timer untuk sync setiap 30 menit
        startAutoSyncTimer();
    }
    
    /**
     * Memulai timer untuk auto-sync setiap 30 menit
     */
    private static void startAutoSyncTimer() {
        System.out.println("\n=== TIMER AUTO-SYNC DIMULAI ===");
        System.out.println("Sync berikutnya akan dijalankan dalam 30 menit");
        System.out.println("=============================================");
        
        // Timer untuk sync setiap 30 menit (30 * 60 * 1000 = 1.800.000 ms)
        long syncInterval = 30 * 60 * 1000; // 30 menit dalam milliseconds
        
        Thread autoSyncThread = new Thread(() -> {
            try {
                while (true) {
                    // Tunggu 30 menit
                    Thread.sleep(syncInterval);
                    
                    // Jalankan sync
                    System.out.println("\n\n=== AUTO-SYNC OTOMATIS (Setiap 30 Menit) ===");
                    System.out.println("Waktu: " + java.time.LocalDateTime.now());
                    System.out.println("=============================================");
                    
                    try {
                        // Step 1: DELETE semua data dari Kemenkes
                        System.out.println("\n\n--- Step 1: DELETE semua data dari Kemenkes ---");
                        deleteBedDataFromKemenkes();
                        
                        // Step 2: POST data baru dari database Oracle
                        System.out.println("\n--- Step 2: POST data baru dari database Oracle ---");
                        postBedDataToKemenkes();
                        
                        System.out.println("\n=== AUTO-SYNC OTOMATIS SELESAI ===");
                        JsonFormatter.displaySuccess("Proses auto-sync otomatis berhasil diselesaikan!");
                        System.out.println("Sync berikutnya akan dijalankan dalam 30 menit...");
                        
                    } catch (Exception e) {
                        JsonFormatter.displayError("Error saat melakukan auto-sync otomatis: " + e.getMessage());
                        e.printStackTrace();
                        System.out.println("Sync berikutnya akan dijalankan dalam 30 menit...");
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Auto-sync timer dihentikan.");
            }
        });
        
        // Set thread sebagai daemon agar berhenti saat aplikasi utama berhenti
        autoSyncThread.setDaemon(true);
        autoSyncThread.start();
        
        System.out.println("Timer auto-sync berhasil dimulai!");
        System.out.println("Aplikasi akan melakukan sync otomatis setiap 30 menit.");
        System.out.println("Silakan gunakan menu lain atau biarkan aplikasi berjalan...");
    }
}
