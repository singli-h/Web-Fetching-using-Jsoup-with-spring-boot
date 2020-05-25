/**
 *
 * @author liSing */
package liSing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Album {
    private String title;
    private String artist;
    private float price;
    private int rank;
    
    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }
    
    public float getPrice()
    {
        return price;
    }
    
    public int getRank()
    {
        return rank;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public void setArtist(String artist)
    {
        this.artist = artist;
    }
    
    public void setPrice(float price)
    {
        this.price = price;
    }
    
    public void setRank(int rank)
    {
        this.rank = rank;
    }
    public String getJSON(){
            StringBuffer buf = new StringBuffer();
            buf.append("{");
            
            buf.append("\"id\":\"" + this.getTitle() +
                        "\", \"name\":\"" + this.getArtist()+ 
                        "\", \"year\":\"" + this.getPrice()+  
                        "\", \"rank\":\"" + this.getRank()
                       );

            buf.append("}");
            return buf.toString();
    }
    
    public void storeToDB() throws SQLException {
        String temp=this.getJSON();
                Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:derby://localhost:1527/imdb";
        String user = "test";
        String password = "test";
        
                    con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            con.close();
            st.close();
    }
}
