/**
 *
 * @author liSings */
package liSing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/link")                // base URL for all handlers
public class RestBookController {

    Logger logger = LoggerFactory.getLogger(RestBookController.class);

    @GetMapping("/status")
    public String status() {
        return "this is status";
    }

    /* @PostMapping(path="/movies", produces="application/json")
    public String createMovie(@RequestBody Movie m){
        try{
            m.storeToDB();
            logger.info("create movie:"+m.getJSON());
            return m.getJSON();
        }catch(SQLException e){
            logger.error("fail to create new movie");
            return "{\"msg\":\"fail to create new movie\"}";
        }
    }*/
    @GetMapping("/albums") // page for JSON data
    public String createMovieList() throws SQLException, JSONException {
        Fetching fetch= new Fetching();
        fetch.dataFetching();
        List<Album> albums = this.DBselect(); //obtain all data from database and store in List
        StringBuffer buf = new StringBuffer();
        buf.append("[");

        for (Album album : albums) { // convert to json format for vue data creation
            buf.append("{");

            buf.append("\"title\":\"" + album.getTitle() 
                    + "\", \"artist\":\"" + album.getArtist()
                    + "\", \"price\":" + album.getPrice()
                    + ", \"rank\":" + album.getRank()
                    );

            buf.append("}");
            buf.append(",");
        }

        buf.setCharAt(buf.length() - 1, ']');
        System.out.println(buf);
        String str = buf.toString();

        return str;

    }





    public List<Album> DBselect() throws SQLException {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:derby://localhost:1527/imdb"; //local JDBC 
        String user = "test";
        String password = "test";
        
        List<Album> result = new ArrayList<Album>();
        try {
            // Class.forName(jdbcDriver);
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM ALBUMSTABLE");

            while (rs.next()) {
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                float price = rs.getFloat("price");
                int rank = rs.getInt("rank");

                Album m = new Album();
                m.setTitle(title);
                m.setArtist(artist);
                m.setPrice(price);
                m.setRank(rank);

                result.add(m);

            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
