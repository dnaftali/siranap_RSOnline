/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siranappost;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.HttpClient;

/**
 *
 * @author dody
 */
public class SiranapPOST {

    /**
     * @param args the command line arguments
     */
    //Database credential - MySQL
    private static final String MysqlUser = "yourDatabaseUsername";
    private static final String MysqlPass = "yourDatabasePassword";
    private static final String MysqlURL = "jdbc:mysql://yourIPServer:3306/siranap";
    private static Connection MysqlCon;
    private static Statement MysqlStmt;

    //Web Service header
    private static final String xrsid = "KodeRSKemenkes";
    private static final String xpass = "PasswordSIRSOnline";
    private static final String strURLSiranap = "http://sirs.yankes.kemkes.go.id/sirsservice/ranap";

    // constructor
    public SiranapPOST() {
        getMysqlConnection();
    }

    private static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getMysqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.print("Connection fail: ");
            System.err.println(e.getException());
        }
        try {
            MysqlCon = DriverManager.getConnection(MysqlURL, MysqlUser, MysqlPass);
            System.out.println("Connected to MySQL Database.");
        } catch (SQLException e) {
            System.err.println("Invalid user and/or password. " + e.getMessage());
        }
        return MysqlCon;
    }

    private void sendData() {
        String strSQL = "SELECT\n"
                + "bed_monitoring.kode_ruang,\n"
                + "bed_monitoring.tipe_pasien,\n"
                + "bed_monitoring.total_TT,\n"
                + "bed_monitoring.terpakai_male,\n"
                + "bed_monitoring.terpakai_female,\n"
                + "bed_monitoring.kosong_male,\n"
                + "bed_monitoring.kosong_female,\n"
                + "bed_monitoring.waiting,\n"
                + "bed_monitoring.tgl_update\n"
                + "FROM\n"
                + "bed_monitoring";
        try {
            PostMethod post = new PostMethod(strURLSiranap);
            post.setRequestHeader("content-type", "application/xml; charset=ISO-8859-1");
            post.setRequestHeader("X-rs-id", xrsid);
            post.setRequestHeader("X-pass", generateMD5(xpass));
            HttpClient httpClient = new HttpClient();
            System.out.println("Password = " + xpass);
            System.out.println("MD5 = " + generateMD5(xpass));

            MysqlStmt = MysqlCon.createStatement();
            ResultSet rs = MysqlStmt.executeQuery(strSQL);
            String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<xml>\n";
            while (rs.next()) {
                xmlStr = xmlStr + "<data>\n";
                xmlStr = xmlStr + "<kode_ruang>" + rs.getString(1) + "</kode_ruang>\n";
                xmlStr = xmlStr + "<tipe_pasien>" + rs.getString(2) + "</tipe_pasien>\n";
                xmlStr = xmlStr + "<total_TT>" + rs.getString(3) + "</total_TT>\n";
                xmlStr = xmlStr + "<terpakai_male>" + rs.getString(4) + "</terpakai_male>\n";
                xmlStr = xmlStr + "<terpakai_female>" + rs.getString(5) + "</terpakai_female>\n";
                xmlStr = xmlStr + "<kosong_male>" + rs.getString(6) + "</kosong_male>\n";
                xmlStr = xmlStr + "<kosong_female>" + rs.getString(7) + "</kosong_female>\n";
                xmlStr = xmlStr + "<waiting>" + rs.getString(8) + "</waiting>\n";
                xmlStr = xmlStr + "<tgl_update>" + rs.getString(9) + "</tgl_update>\n";
                xmlStr = xmlStr + "</data>\n";
            }
            xmlStr = xmlStr + "</xml>\n";
            StringRequestEntity params = new StringRequestEntity(xmlStr, "application/xml", "ISO-8859-1");
            System.out.println(xmlStr);
            //request create
           post.setRequestEntity(params);
            int result = httpClient.executeMethod(post);
            System.out.println("Response status code: " + result);
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());
            post.releaseConnection();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(SiranapPOST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        SiranapPOST siranapPost = new SiranapPOST();
        siranapPost.sendData();
    }

}
