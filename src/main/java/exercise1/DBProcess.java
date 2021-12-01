package exercise1;

import java.sql.*;

public class DBProcess
{
    //Declare variables Globally
    Connection connection = null;
    Statement statement = null;

    // Declare variables Globally for getInform() method
    int pID, gID, pgID;
    Double sc;
    String fName, lName, addr, pCode, provc, pNum, gTitle;
    java.sql.Date pyDate;

    // Declare variables globally for getMaxId() methods
    int gMaxID, pMaxID;

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
                        sequenceSql = "CREATE SEQUENCE sequence_game " +
                                      " START WITH 2000" +
                                      " INCREMENT BY 1" +
                                      " NOCACHE " +
                                      " NOCYCLE";

                    }
                    else if (str.equals("player"))
                    {
                        sql = "CREATE TABLE player (player_id integer PRIMARY KEY, " +
                                "first_name VARCHAR(30), last_name VARCHAR(30), " +
                                "address VARCHAR(100), postal_code VARCHAR(15), " +
                                "province VARCHAR(10), phone_number VARCHAR(20))";
                        sequenceSql = "CREATE SEQUENCE sequence_player " +
                                " START WITH 1000" +
                                " INCREMENT BY 1" +
                                " NOCACHE " +
                                " NOCYCLE";
                    }
                    else
                    {
                        sql = "CREATE TABLE playerandgame (player_game_id integer PRIMARY KEY, " +
                                "game_id integer, player_id integer, " +
                                "playing_date date, score number(10,2), " +
                                "CONSTRAINT game_fk FOREIGN KEY (game_id) REFERENCES game(game_id), " +
                                "CONSTRAINT player_fk FOREIGN KEY (player_id) REFERENCES player(player_id))";
                        sequenceSql = "CREATE SEQUENCE sequence_PlayerGame " +
                                " START WITH 3000" +
                                " INCREMENT BY 1" +
                                " NOCACHE " +
                                " NOCYCLE";
                    }

                    statement.execute(sequenceSql);
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

    //Insert data1 into tables
    public void dataInsert1(String fName,String lName,String addr,String pCode,String pvc,String pNumber,
                           String gTitle)
    {
        String firstName, lastName, address, postalCode, province, phoneNumber, gameTitle;

        firstName = fName;
        lastName = lName;
        address = addr;
        postalCode = pCode;
        province = pvc;
        phoneNumber = pNumber;
        gameTitle = gTitle;

        // Insert data
        String sql1 = "";
        String sql2 = "";
        try
        {
            //Inserting data into game table
            sql1 = "INSERT INTO game VALUES(sequence_game.nextval, '" + gameTitle + "')";
            statement.execute(sql1);
            System.out.println("Inserting data into game table is successful!");

            //Inserting data into player table
            sql2 = "INSERT INTO player VALUES(sequence_player.nextval, '" + firstName + "', '" + lastName + "', '" +
                    address + "', '" + postalCode + "', '" + province + "', '" + phoneNumber + "')";
            statement.execute(sql2);
            System.out.println("Inserting data into player table is successful!");

        }
        catch(SQLException e)
        {
            System.out.println("Data are not saved.");
            System.out.println(e.getMessage());
        }


    } // end of dataInsert1 method


    //Insert data2 into tables
    public void dataInsert2(java.sql.Date pDate,Double sc)
    {
        java.sql.Date playingDate;
        Double score;

        playingDate = pDate;
        score = sc;

        // Insert data
        String sql3 = "";
        try
        {
            //Inserting data into playerandgame table
            sql3 = "INSERT INTO playerandgame VALUES(sequence_PlayerGame.nextval,?, ?, ?, ?)";
            PreparedStatement preSt = connection.prepareStatement(sql3);
            preSt.setInt(1,gMaxID);
            preSt.setInt(2,pMaxID);
            preSt.setDate(3,playingDate);
            preSt.setDouble(4,score);
            ResultSet resultSet = preSt.executeQuery();
            System.out.println("Inserting data into playerandgame table is successful!");
            connection.close();
            System.out.println("Database is disconnected.");
        }
        catch(SQLException e)
        {
            System.out.println("Data are not saved.");
            System.out.println(e.getMessage());
        }
    } // end of dataInsert2 method

    // get MaxID from database
    public void getMaxID()
    {
        String getIdSql1 = "SELECT max(player_id) player_id FROM player";
        String getIdSql2 = "SELECT max(game_id) game_id FROM game";

        try
        {
            statement = connection.createStatement();
            ResultSet rs1 = statement.executeQuery(getIdSql1);
            while(rs1.next())
            {
                pMaxID = rs1.getInt("player_id");
            }
            ResultSet rs2 = statement.executeQuery(getIdSql2);
            while(rs2.next())
            {
                gMaxID = rs2.getInt("game_id");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    } // End of getMaxID method

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
                sc = rs.getDouble("score");
            }
            // close the database connection
            connection.close();
            System.out.println("Database is disconnected.");
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

            //Close database connection
            statement.close();
            System.out.println("Database is disconnected.");

        }
        catch(SQLException e)
        {
            System.out.println("Data are not saved.");
            System.out.println(e.getMessage());
        }

    } // End of updateInform method

}
