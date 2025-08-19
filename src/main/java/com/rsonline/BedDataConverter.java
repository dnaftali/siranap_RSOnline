package com.rsonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * Class untuk mengkonversi data tempat tidur dari database ke format JSON API Kemenkes
 */
public class BedDataConverter {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Mengkonversi data tempat tidur dari database ke format JSON untuk POST ke API Kemenkes
     * @param bedData data dari database Oracle
     * @return JSON string sesuai format API Kemenkes
     */
    public static String convertToKemenkesJson(OracleDatabaseClient.BedData bedData) {
        try {
            // Buat object sesuai format API Kemenkes
            KemenkesBedData kemenkesData = new KemenkesBedData();
            kemenkesData.setId_tt(bedData.getIdTt());
            kemenkesData.setRuang(bedData.getRuang());
            kemenkesData.setJumlah_ruang(bedData.getJumlahRuang());
            kemenkesData.setJumlah(bedData.getJumlah());
            kemenkesData.setTerpakai(bedData.getTerpakai());
            kemenkesData.setTerpakai_suspek(bedData.getTerpakaiSuspek());
            kemenkesData.setTerpakai_konfirmasi(bedData.getTerpakaiKonfirmasi());
            kemenkesData.setAntrian(bedData.getAntrian());
            kemenkesData.setPrepare(bedData.getPrepare());
            kemenkesData.setPrepare_plan(bedData.getPreparePlan());
            kemenkesData.setCovid(bedData.getCovid());
            
            // Set default values untuk field yang tidak ada di database
            kemenkesData.setTerpakai_dbd("0");
            kemenkesData.setTerpakai_dbd_anak("0");
            kemenkesData.setJumlah_dbd("0");
            
            return objectMapper.writeValueAsString(kemenkesData);
            
        } catch (Exception e) {
            System.err.println("Error converting to JSON: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Mengkonversi list data tempat tidur ke format JSON array
     * @param bedDataList list data dari database
     * @return JSON array string
     */
    public static String convertListToKemenkesJson(List<OracleDatabaseClient.BedData> bedDataList) {
        try {
            List<KemenkesBedData> kemenkesDataList = new java.util.ArrayList<>();
            
            for (OracleDatabaseClient.BedData bedData : bedDataList) {
                KemenkesBedData kemenkesData = new KemenkesBedData();
                kemenkesData.setId_tt(bedData.getIdTt());
                kemenkesData.setRuang(bedData.getRuang());
                kemenkesData.setJumlah_ruang(bedData.getJumlahRuang());
                kemenkesData.setJumlah(bedData.getJumlah());
                kemenkesData.setTerpakai(bedData.getTerpakai());
                kemenkesData.setTerpakai_suspek(bedData.getTerpakaiSuspek());
                kemenkesData.setTerpakai_konfirmasi(bedData.getTerpakaiKonfirmasi());
                kemenkesData.setAntrian(bedData.getAntrian());
                kemenkesData.setPrepare(bedData.getPrepare());
                kemenkesData.setPrepare_plan(bedData.getPreparePlan());
                kemenkesData.setCovid(bedData.getCovid());
                
                // Set default values
                kemenkesData.setTerpakai_dbd("0");
                kemenkesData.setTerpakai_dbd_anak("0");
                kemenkesData.setJumlah_dbd("0");
                
                kemenkesDataList.add(kemenkesData);
            }
            
            return objectMapper.writeValueAsString(kemenkesDataList);
            
        } catch (Exception e) {
            System.err.println("Error converting list to JSON: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Inner class untuk format data sesuai API Kemenkes
     * Menggunakan field names yang sama persis dengan context.txt
     */
    public static class KemenkesBedData {
        private String id_tt;
        private String ruang;
        private String jumlah_ruang;
        private String jumlah;
        private String terpakai;
        private String terpakai_suspek;
        private String terpakai_konfirmasi;
        private String antrian;
        private String prepare;
        private String prepare_plan;
        private String covid;
        private String terpakai_dbd;
        private String terpakai_dbd_anak;
        private String jumlah_dbd;
        
        // Getters and Setters
        public String getId_tt() { return id_tt; }
        public void setId_tt(String id_tt) { this.id_tt = id_tt; }
        
        public String getRuang() { return ruang; }
        public void setRuang(String ruang) { this.ruang = ruang; }
        
        public String getJumlah_ruang() { return jumlah_ruang; }
        public void setJumlah_ruang(String jumlah_ruang) { this.jumlah_ruang = jumlah_ruang; }
        
        public String getJumlah() { return jumlah; }
        public void setJumlah(String jumlah) { this.jumlah = jumlah; }
        
        public String getTerpakai() { return terpakai; }
        public void setTerpakai(String terpakai) { this.terpakai = terpakai; }
        
        public String getTerpakai_suspek() { return terpakai_suspek; }
        public void setTerpakai_suspek(String terpakai_suspek) { this.terpakai_suspek = terpakai_suspek; }
        
        public String getTerpakai_konfirmasi() { return terpakai_konfirmasi; }
        public void setTerpakai_konfirmasi(String terpakai_konfirmasi) { this.terpakai_konfirmasi = terpakai_konfirmasi; }
        
        public String getAntrian() { return antrian; }
        public void setAntrian(String antrian) { this.antrian = antrian; }
        
        public String getPrepare() { return prepare; }
        public void setPrepare(String prepare) { this.prepare = prepare; }
        
        public String getPrepare_plan() { return prepare_plan; }
        public void setPrepare_plan(String prepare_plan) { this.prepare_plan = prepare_plan; }
        
        public String getCovid() { return covid; }
        public void setCovid(String covid) { this.covid = covid; }
        
        public String getTerpakai_dbd() { return terpakai_dbd; }
        public void setTerpakai_dbd(String terpakai_dbd) { this.terpakai_dbd = terpakai_dbd; }
        
        public String getTerpakai_dbd_anak() { return terpakai_dbd_anak; }
        public void setTerpakai_dbd_anak(String terpakai_dbd_anak) { this.terpakai_dbd_anak = terpakai_dbd_anak; }
        
        public String getJumlah_dbd() { return jumlah_dbd; }
        public void setJumlah_dbd(String jumlah_dbd) { this.jumlah_dbd = jumlah_dbd; }
        
        @Override
        public String toString() {
            return String.format("KemenkesBedData{id_tt='%s', ruang='%s', jumlah='%s', terpakai='%s', covid='%s'}", 
                id_tt, ruang, jumlah, terpakai, covid);
        }
    }
}
