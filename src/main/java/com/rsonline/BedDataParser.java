package com.rsonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Class untuk parsing data tempat tidur dari response API Kemenkes
 */
public class BedDataParser {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Parse JSON response dan ekstrak semua id_t_tt
     * @param jsonResponse response JSON dari API
     * @return List of id_t_tt
     */
    public static List<String> extractIdTttList(String jsonResponse) {
        List<String> idTttList = new ArrayList<>();
        
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode fasyankesNode = rootNode.get("fasyankes");
            
            if (fasyankesNode != null && fasyankesNode.isArray()) {
                for (JsonNode bedNode : fasyankesNode) {
                    JsonNode idTttNode = bedNode.get("id_t_tt");
                    if (idTttNode != null && !idTttNode.isNull()) {
                        String idTtt = idTttNode.asText();
                        if (idTtt != null && !idTtt.trim().isEmpty()) {
                            idTttList.add(idTtt);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }
        
        return idTttList;
    }
    
    /**
     * Parse JSON response dan ekstrak informasi tempat tidur
     * @param jsonResponse response JSON dari API
     * @return List of bed information
     */
    public static List<BedInfo> extractBedInfoList(String jsonResponse) {
        List<BedInfo> bedInfoList = new ArrayList<>();
        
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode fasyankesNode = rootNode.get("fasyankes");
            
            if (fasyankesNode != null && fasyankesNode.isArray()) {
                for (JsonNode bedNode : fasyankesNode) {
                    BedInfo bedInfo = new BedInfo();
                    
                    bedInfo.setIdTt(getStringValue(bedNode, "id_tt"));
                    bedInfo.setTt(getStringValue(bedNode, "tt"));
                    bedInfo.setRuang(getStringValue(bedNode, "ruang"));
                    bedInfo.setIdTtt(getStringValue(bedNode, "id_t_tt"));
                    bedInfo.setJumlah(getStringValue(bedNode, "jumlah"));
                    bedInfo.setTerpakai(getStringValue(bedNode, "terpakai"));
                    bedInfo.setKosong(getStringValue(bedNode, "kosong"));
                    bedInfo.setCovid(getStringValue(bedNode, "covid"));
                    
                    bedInfoList.add(bedInfo);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }
        
        return bedInfoList;
    }
    
    /**
     * Helper method untuk mendapatkan string value dari JsonNode
     */
    private static String getStringValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode != null && !fieldNode.isNull()) {
            return fieldNode.asText();
        }
        return null;
    }
    
    /**
     * Inner class untuk menyimpan informasi tempat tidur
     */
    public static class BedInfo {
        private String idTt;
        private String tt;
        private String ruang;
        private String idTtt;
        private String jumlah;
        private String terpakai;
        private String kosong;
        private String covid;
        
        // Getters and Setters
        public String getIdTt() { return idTt; }
        public void setIdTt(String idTt) { this.idTt = idTt; }
        
        public String getTt() { return tt; }
        public void setTt(String tt) { this.tt = tt; }
        
        public String getRuang() { return ruang; }
        public void setRuang(String ruang) { this.ruang = ruang; }
        
        public String getIdTtt() { return idTtt; }
        public void setIdTtt(String idTtt) { this.idTtt = idTtt; }
        
        public String getJumlah() { return jumlah; }
        public void setJumlah(String jumlah) { this.jumlah = jumlah; }
        
        public String getTerpakai() { return terpakai; }
        public void setTerpakai(String terpakai) { this.terpakai = terpakai; }
        
        public String getKosong() { return kosong; }
        public void setKosong(String kosong) { this.kosong = kosong; }
        
        public String getCovid() { return covid; }
        public void setCovid(String covid) { this.covid = covid; }
        
        @Override
        public String toString() {
            return String.format("BedInfo{id_tt='%s', tt='%s', ruang='%s', id_t_tt='%s', jumlah='%s', terpakai='%s', kosong='%s', covid='%s'}", 
                idTt, tt, ruang, idTtt, jumlah, terpakai, kosong, covid);
        }
    }
}
