package exercise1;

import java.sql.*;
import java.time.LocalDate;

public class DBProcess
{
    //Declare variables Globally
    Connection connection = null;
    Statement statement = null;

    // Declare variables Globally for getInform() method
    int pID, gID, pgID, sc;
    String fName, lName, addr, pCode, provc, pNum, gTitle;
    java.sql.Date pyDate;

    // DB configuration
    public final static String dbURL = "jdbc:oracle:thin:@199.212.26.208:1521:SQLD";
    public final static String username = "COMP214F21_011_P_15";
    public final static String password = "password";

    //Connect to the database
    public void dbConnect()
    {
        try
        {
            connection = DriverManager.getConnection(dbURL, username, password);
            System.out.println("Database is connected.");
        }
        catch (SQLException e)
        {
            System.out.println("Database is not connected.");
            System.out.println(e.getMessage());
        }
    }

    //Create tables if tables do not exist
    public void tableCreate() throws SQLException
    {
        for (Table myTable:Table.values())
        {
            System.out.println(myTable);
            String existSql = "SELECT COUNT(*) COUNT FROM USER_TABLES WHERE TABLE_NAME=UPPER('" + myTable +"')";

            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(existSql);
            int myCount = 0;

            while (rs.next())
            {
                myCount = rs.getInt("COUNT");
            }

            if(myCount == 1)
            {
                System.out.println(myTable +" table exists in Database.");
            }
            else
            {
                System.out.println(myTable +" table does not exist in Database.");

                // Create tables
                String sql = "";
                String sequenceSql = "";
                String str = String.valueOf(myTable);
                try
                {
                    if (str.equals("game"))
                    {
                        sql = "CREATE TABLE game (game_id integer PRIMARY KEY, game_title VARCHAR(100))";
                    }
                    else if (str.equals("player"))
                    {
                        sql = "CREATE TABLE player (player_id integer PRIMARY KEY, " +
                                "first_name VARCHAR(30), last_name VARCHAR(30), " +
                                "address VARCHAR(100), postal_code VARCHAR(15), " +
                                "province VARCHAR(10), phone_number VARCHAR(20))";
                    }
                    else
                    {
                        sql = "CREATE TABLE playerandgame (player_game_id integer PRIMARY KEY, " +
                                "game_id integer, player_id integer, " +
                                "playing_date date, score integer, " +
                                "CONSTRAINT game_fk FOREIGN KEY (game_id) REFERENCES game(game_id), " +
                                "CONSTRAINT player_fk FOREIGN KEY (player_id) REFERENCES player(player_id))";
                    }

                    statement.execute(sql);
                    System.out.println("Table " + myTable + " is created.");
                }
                catch (SQLException e)
                {
                    System.out.println("Table " + myTable + " is not created.");
                    System.out.println(e.getMessage());
                }
            }
        } // end of for each.
    } // End of tableCreate method

    //Insert data into tables
    public void dataInsert(String fName,String lName,String addr,String pCode,String pvc,String pNumber,
                           String gTitle,java.sql.Date pDate,Double sc,Integer pID,Integer gID,Integer pgID)
    {
        String firstName, lastName, address, postalCode, province, phoneNumber, gameTitle;
        java.sql.Date playingDate;
        Double score;
        Integer playerID, gameID, playerGameID;

        firstName = fName;
        lastName = lName;
        address = addr;
        postalCode = pCode;
        province = pvc;
        phoneNumber = pNumber;
        gameTitle = gTitle;
        playingDate = pDate;
        score = sc;
        playerID = pID;
        gameID = gID;
        playerGameID = pgID;

        // Insert data
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        try
        {
            //Inserting data into game table
            sql1 = "INSERT INTO game VALUES(" + gameID + ", '" + gameTitle + "')";
            statement.execute(sql1);
            System.out.println("Inserting data into game table is successful!");

            //Inserting data into player table
            sql2 = "INSERT INTO player VALUES(" + playerID + ", '" + firstName + "', '" + lastName + "', '" +
                    address + "', '" + postalCode + "', '" + province + "', '" + phoneNumber + "')";
            statement.execute(sql2);
            System.out.println("Inserting data into player table is successful!");

            //Inserting data into playerandgame table
            sql3 = "INSERT INTO playerandgame VALUES(?,?,?,?,?)";
            PreparedStatement preSt = connection.prepareStatement(sql3);
            preSt.setInt(1,playerGameID);
            preSt.setInt(2,gameID);
            preSt.setInt(3,playerID);
            preSt.setDate(4,playingDate);
            preSt.setDouble(5,score);
            ResultSet resultSet = preSt.executeQuery();
            System.out.println("Inserting data into playerandgame table is successful!");
        }
        catch(SQLException e)
        {
            System.out.println("Data are not saved.");
            System.out.println(e.getMessage());
        }


    } // end of dataInsert method

    // get data from database
    public void getInform(int plID)
    {
        String getInformSql = "";
        getInformSql = "SELECT p.player_id, p.first_name, p.last_name, p.address, p.postal_code, p.province, p.phone_number," +
                " g.game_id, g.game_title, pg.player_game_id, pg.playing_date, pg.score" +
                " FROM player p, game g, playerandgame pg" +
                " WHERE p.player_id = pg.player_id " +
                "      AND g.game_id = pg.game_id" +
                "      AND p.player_id = " + plID;

        try
        {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getInformSql);
            while(rs.next())
            {
                pID = rs.getInt("player_id");
                fName = rs.getString("first_name");
                lName = rs.getString("last_name");
                addr = rs.getString("address");
                pCode = rs.getString("postal_code");
                provc = rs.getString("province");
                pNum = rs.getString("phone_number");
                gTitle = rs.getString("game_title");
                pyDate = rs.getDate("playing_date");
                gID = rs.getInt("game_id");
                pgID = rs.getInt("player_game_id");
                sc = rs.getInt("score");

            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }



    } // End of getInform method


    // Update information to database
    public void updateInform(String fName,String lName,String addr,String pCode,String pvc,String pNumber,
                             String gTitle,java.sql.Date pDate,Double sc,Integer pID,Integer gID,Integer pgID)
    {
        String firstName, lastName, address, postalCode, province, phoneNumber, gameTitle;
        java.sql.Date playingDate;
        Double score;
        Integer playerID, gameID, playerGameID;

        firstName = fName;
        lastName = lName;
        address = addr;
        postalCode = pCode;
        province = pvc;
        phoneNumber = pNumber;
        gameTitle = gTitle;
        playingDate = pDate;
        score = sc;
        playerID = pID;
        gameID = gID;
        playerGameID = pgID;

        // Update data
        String updateInformSql1 = "";
        String updateInformSql2 = "";
        String updateInformSql3 = "";


        try
        {
            statement = connection.createStatement();

            // Updating game table
            updateInformSql1 = "UPDATE game SET game_title = '" + gameTitle + "'" +
                    " WHERE game_id = " + gameID;
            statement.executeUpdate(updateInformSql1);
            System.out.println("Updating game table is successful!");

            //Updating player table
            updateInformSql2 = "UPDATE player SET first_name = '" + firstName + "', last_name = '" + lastName +
                    "', address = '" + address + "', postal_code = '" + postalCode + "', province = '" + province + "', phone_number = '" + phoneNumber + "'" +
                    "WHERE player_id = " + playerID;
            statement.executeUpdate(updateInformSql2);
            System.out.println("Updating player table is successful!");

            //Updating playerandgame table
            updateInformSql3 = "UPDATE playerandgame SET playing_date = to_date('" + playingDate + "', 'yyyy-mm-dd'), score = " + score + " WHERE player_game_id = " + playerGameID;
            statement.executeUpdate(updateInformSql3);

            System.out.println("Updating playerandgame table is successful!");
        }
        catch(SQLException e)
        {
            System.out.println("Data are not saved.");
            System.out.println(e.getMessage());
        }

    } // End of updateInform method
    public static void main(String[] args)
    {
        //DBProcess dbProcess = new DBProcess();
        //dbProcess.dbConnect();

    }




}
