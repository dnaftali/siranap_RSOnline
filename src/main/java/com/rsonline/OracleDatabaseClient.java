package com.rsonline;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Client untuk koneksi dan query database Oracle
 */
public class OracleDatabaseClient {
    
    private Connection connection;
    
    /**
     * Membuka koneksi ke database Oracle
     */
    public void connect() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(
                Config.ORACLE_URL, 
                Config.ORACLE_USER, 
                Config.ORACLE_PASS
            );
            System.out.println("Berhasil terhubung ke database Oracle");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver tidak ditemukan", e);
        }
    }
    
    /**
     * Menutup koneksi database
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Koneksi database Oracle ditutup");
            } catch (SQLException e) {
                System.err.println("Error saat menutup koneksi: " + e.getMessage());
            }
        }
    }
    
    /**
     * Query data tempat tidur dari database Oracle
     * Menggunakan query yang ada di context.txt
     */
    public List<BedData> queryBedData() throws SQLException {
        List<BedData> bedDataList = new ArrayList<>();
        
        String sql = "SELECT \n"
            + "    ID_TT,\n"
            + "    RUANG,\n"
            + "    JUMLAH_RUANG,\n"
            + "    JUMLAH,\n"
            + "    TERPAKAI,\n"
            + "	   SUSPEK,\n"
            + "	   KONFIRMASI,\n"
            + "    ANTRIAN,\n"
            + "    PREPARE,\n"
            + "    PREPARE_PLAN,\n"
            + "    COVID\n"
            + "FROM\n"
            + "	TABLE";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                BedData bedData = new BedData();
                bedData.setIdTt(rs.getString("ID_TT"));
                bedData.setRuang(rs.getString("RUANG"));
                bedData.setJumlahRuang(rs.getString("JUMLAH_RUANG"));
                bedData.setJumlah(rs.getString("JUMLAH"));
                bedData.setTerpakai(rs.getString("TERPAKAI"));
                bedData.setTerpakaiSuspek(rs.getString("SUSPEK"));
                bedData.setTerpakaiKonfirmasi(rs.getString("KONFIRMASI"));
                bedData.setAntrian(rs.getString("ANTRIAN"));
                bedData.setPrepare(rs.getString("PREPARE"));
                bedData.setPreparePlan(rs.getString("PREPARE_PLAN"));
                bedData.setCovid(rs.getString("COVID"));
                
                bedDataList.add(bedData);
            }
        }
        
        return bedDataList;
    }
    
    /**
     * Inner class untuk menyimpan data tempat tidur dari database
     */
    public static class BedData {
        private String idTt;
        private String ruang;
        private String jumlahRuang;
        private String jumlah;
        private String terpakai;
        private String terpakaiSuspek;
        private String terpakaiKonfirmasi;
        private String antrian;
        private String prepare;
        private String preparePlan;
        private String covid;
        
        // Getters and Setters
        public String getIdTt() { return idTt; }
        public void setIdTt(String idTt) { this.idTt = idTt; }
        
        public String getRuang() { return ruang; }
        public void setRuang(String ruang) { this.ruang = ruang; }
        
        public String getJumlahRuang() { return jumlahRuang; }
        public void setJumlahRuang(String jumlahRuang) { this.jumlahRuang = jumlahRuang; }
        
        public String getJumlah() { return jumlah; }
        public void setJumlah(String jumlah) { this.jumlah = jumlah; }
        
        public String getTerpakai() { return terpakai; }
        public void setTerpakai(String terpakai) { this.terpakai = terpakai; }
        
        public String getTerpakaiSuspek() { return terpakaiSuspek; }
        public void setTerpakaiSuspek(String terpakaiSuspek) { this.terpakaiSuspek = terpakaiSuspek; }
        
        public String getTerpakaiKonfirmasi() { return terpakaiKonfirmasi; }
        public void setTerpakaiKonfirmasi(String terpakaiKonfirmasi) { this.terpakaiKonfirmasi = terpakaiKonfirmasi; }
        
        public String getAntrian() { return antrian; }
        public void setAntrian(String antrian) { this.antrian = antrian; }
        
        public String getPrepare() { return prepare; }
        public void setPrepare(String prepare) { this.prepare = prepare; }
        
        public String getPreparePlan() { return preparePlan; }
        public void setPreparePlan(String preparePlan) { this.preparePlan = preparePlan; }
        
        public String getCovid() { return covid; }
        public void setCovid(String covid) { this.covid = covid; }
        
        @Override
        public String toString() {
            return String.format("BedData{id_tt='%s', ruang='%s', jumlah='%s', terpakai='%s', covid='%s'}", 
                idTt, ruang, jumlah, terpakai, covid);
        }
    }
}

