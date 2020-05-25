/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liSing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author sjxy
 */
public class Fetching {

    private static final String UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36";
    private static String url = "https://www.amazon.com/Best-Sellers-MP3-Downloads/zgbs/dmusic/digital-music-album/";
    private static Connection con = null;
    private static Statement st = null;
    private static PreparedStatement pst = null;

    public void dataFetching() throws SQLException {
        url = "https://www.amazon.com/Best-Sellers-MP3-Downloads/zgbs/dmusic/digital-music-album/";

        String dburl = "jdbc:derby://localhost:1527/imdb;create=true";
        String user = "test";
        String password = "test";

        con = DriverManager.getConnection(dburl, user, password);
        st = con.createStatement();
        String sql = "CREATE TABLE albumstable (\n"
                + "rank INT NOT NULL PRIMARY KEY, \n"
                + "title VARCHAR(255) NOT NULL, \n"
                + "artist VARCHAR(255) NOT NULL,\n"
                + "price DECIMAL(5,2) NOT NULL\n"
                //+ "img VARCHAR(511)\n"
                + ")";

        if (tableExist(con, "albumstable")) {
            sql="TRUNCATE TABLE albumstable";
        }
        
        try {
            st.executeUpdate(sql);
        } catch (SQLTransactionRollbackException e) {
            e.printStackTrace();
        }

        pst = con.prepareStatement("INSERT INTO albumstable VALUES (?, ?, ?, ?)");

        try {

            while (true) {
                Document doc = Jsoup
                        .connect(url)
                        .userAgent(UA)
                        .get();
                insertAlbum(doc);
                Element next = doc.selectFirst("li.a-last > a");
                if (next != null) {
                    url = next.attr("abs:href");
                } else {
                    break;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Fetching.class.getName()).log(Level.SEVERE, null, ex);
        }

        st.close();
        con.close();
    }

    public static void insertAlbum(Document doc) throws SQLException {

        //System.out.println(doc.html());
        Elements albums = doc.select(".zg-item-immersion");
        for (Element album : albums) {

            String rank = album.selectFirst(".zg-badge-text").text().substring(1);
            String title = album.selectFirst(".a-link-normal").text();
            String artist = album.selectFirst(".a-row.a-size-small").text();
            String price = album.selectFirst(".p13n-sc-price").text().substring(1);

            pst.setInt(1, Integer.parseInt(rank));
            pst.setString(2, title);
            pst.setString(3, artist);
            pst.setFloat(4, Float.parseFloat(price));

            pst.executeUpdate();

            //System.out.println(rank + " " + title + " "  + artist + " " + price);
        }

    }

    public boolean tableExist(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet result = meta.getTables(null, null, tableName.toUpperCase(), null);
        return result.next();
    }
}
