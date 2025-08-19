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
            + "    RJKK.RJKK_KODE AS ID_TT,\n"
            + "    CASE WHEN (RJKK.RJKK_KODE IN ('24','25','26','27','28','29','30','31','32','33')) THEN RJKK.RJKK_NAMA ELSE NVL(RUANG, RJKK.RJKK_NAMA) END AS RUANG,\n"
            + "    NVL(JUMLAH_RUANG,0) AS JUMLAH_RUANG,\n"
            + "    NVL(JUMLAH,0) AS JUMLAH,\n"
            + "    NVL(TERPAKAI,0) AS TERPAKAI,\n"
            + "	   0 AS SUSPEK,\n"
            + "	   0 AS KONFIRMASI,\n"
            + "    0 AS ANTRIAN,\n"
            + "    NVL(PREPARE,0) AS PREPARE,\n"
            + "    NVL(PREPARE_PLAN,0) AS PREPARE_PLAN,\n"
            + "    CASE WHEN (RJKK.RJKK_KODE IN ('24','25','26','27','28','29','30','31','32','33')) THEN 1 ELSE 0 END AS COVID\n"
            + "FROM\n"
            + "(\n"
            + "	SELECT \n"
            + "	    REF_JNS_KMR_KEMK_RJKK_KODE AS ID_TT,\n"
            + "	    MSR_NAMA AS RUANG,\n"
            + "	    COUNT(DISTINCT MST_RUANG_MSR_KODE) AS JUMLAH_RUANG,\n"
            + "	    SUM(BEDOK) AS JUMLAH,\n"
            + "	    SUM(TERISI) AS TERPAKAI,\n"
            + "	    SUM (RESERVASI) AS PREPARE,\n"
            + "	    0 AS PREPARE_PLAN\n"
            + "	FROM\n"
            + "	(\n"
            + "	    SELECT \n"
            + "	        TUB.TMB_NOREG,\n"
            + "	        TUB.TMB_STATUS_PAKAI,\n"
            + "	        TUB.MST_BED_RWT_MBRWT_KODE,\n"
            + "	        MBR.MST_KAMAR_RAWAT_MKRWT_KODE,\n"
            + "	        MKR.MST_RUANG_MSR_KODE,\n"
            + "	        MR.MSR_NAMA,\n"
            + "	        MR.MSR_LABEL,\n"
            + "	        CASE WHEN MBR.MBRWT_ISAKTIF = 1 AND MKR.MKRWT_ISAKTIF = 1 THEN 1 ELSE 0 END AS BEDOK, -- ok\n"
            + "	        CASE WHEN (MBR.MBRWT_ISAKTIF = 0 AND MBR.REF_STATUS_INAKTIF_RSIN_KODE IN (0,1)) THEN 1 ELSE 0 END AS INVALID,\n"
            + "	        CASE WHEN MBR.MBRWT_ISAKTIF = 1 AND TUB.TMB_STATUS_PAKAI IN (1, 3, 5) THEN 1 ELSE 0 END AS TERISI, \n"
            + "	        CASE WHEN MBR.MBRWT_ISAKTIF = 1 AND TUB.TMB_STATUS_PAKAI IN 4 THEN 1 ELSE 0 END AS RESERVASI,\n"
            + "	        CASE WHEN (RK.RKLS_KODE = '00' AND MR.REF_FUNGSI_RUANG_RFR_KODE NOT IN (1,2)) THEN 1 ELSE 0 END AS NONRUANG,\n"
            + "	        NVL(MBR.REF_JNS_KMR_KEMK_RJKK_KODE, MKR.REF_JNS_KMR_KEMK_RJKK_KODE) AS REF_JNS_KMR_KEMK_RJKK_KODE\n"
            + "	    FROM MST_BED_RWT MBR\n"
            + "	    LEFT JOIN TRANS_UPDATE_BED TUB ON TUB.MST_BED_RWT_MBRWT_KODE = MBR.MBRWT_KODE\n"
            + "	    INNER JOIN MST_KAMAR_RAWAT MKR ON MKR.MKRWT_KODE = MBR.MST_KAMAR_RAWAT_MKRWT_KODE\n"
            + "	    INNER JOIN MST_RUANG MR ON MR.MSR_KODE = MKR.MST_RUANG_MSR_KODE AND MR.REF_FUNGSI_RUANG_RFR_KODE IN (1,2,3)\n"
            + "	    INNER JOIN REF_KELAS RK ON RK.RKLS_KODE = MKR.REF_KELAS_RLKS_KODE\n"
            + "	    \n"
            + "	    WHERE MKR.MKRWT_ISAKTIF = '1' AND MR.MSR_ISAKTIF = '1'  \n"
            + "	    ORDER BY MBR.REF_JNS_KMR_KEMK_RJKK_KODE, MBR.MST_KAMAR_RAWAT_MKRWT_KODE\n"
            + "	) \n"
            + "	WHERE INVALID != 1  --AND NONRUANG !=1\n"
            + "	GROUP BY REF_JNS_KMR_KEMK_RJKK_KODE, MSR_NAMA\n"
            + ")\n"
            + "RIGHT JOIN REF_JENIS_KAMAR_KEMKES RJKK ON RJKK.RJKK_KODE = ID_TT\n"
            + "WHERE RJKK.RJKK_KODE <> '32'\n"
            + "AND RJKK.RJKK_KODE NOT IN ('24','25','26','27','28','29','30','31','32','33')\n"
            + "UNION ALL \n"
            + "select \n"
            + "	'32' AS ID_TT,\n"
            + "	'IGD Khusus Covid' AS RUANG,\n"
            + "	1 AS JUMLAH_RUANG,\n"
            + "	LPBI_JUMLAH_BED_IGD AS JUMLAH,\n"
            + "	CASE WHEN LPBI_JUMLAH_PASIEN >= LPBI_JUMLAH_BED_IGD THEN LPBI_JUMLAH_BED_IGD ELSE LPBI_JUMLAH_PASIEN END AS TERPAKAI,\n"
            + "	0 AS SUSPEK,\n"
            + "	0 AS KONFIRMASI,\n"
            + "	CASE WHEN LPBI_JUMLAH_PASIEN >= LPBI_JUMLAH_BED_IGD THEN LPBI_JUMLAH_PASIEN-LPBI_JUMLAH_BED_IGD ELSE 0 END AS ANTRIAN,\n"
            + "	0 AS PREPARE,\n"
            + "	0 AS PREPARE_PLAN,\n"
            + "	1 AS COVID\n"
            + "from (\n"
            + "	SELECT LPBI.*,\n"
            + "		MAX(LPBI.LPBI_LASTUPDATE) over () AS MAX_LAST_UPDATE\n"
            + "	FROM LAP_RESUME_BED_IGD LPBI\n"
            + ") X\n"
            + "WHERE X.LPBI_LASTUPDATE =  X.MAX_LAST_UPDATE\n"
            + "UNION ALL \n"
            + "SELECT \n" 
            + "	ID_TT,\n" 
            + "	RUANG,\n" 
            + "	JUMLAH_RUANG,\n" 
            + "	JUMLAH,\n" 
            + "	TERPAKAI,\n" 
            + "	SUSPEK,\n" 
            + "	KONFIRMASI,\n" 
            + "	ANTRIAN,\n" 
            + "	PREPARE,\n" 
            + "	PREPARE_PLAN,\n" 
            + "	COVID\n" 
            + "FROM ZRSONLINE";
        
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
