package exercise1;

import java.sql.*;

public class DBProcess
{
    //Declare variables Globally
    Connection connection = null;
    Statement statement = null;
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
                String str = String.valueOf(myTable);
                try
                {
                    if (str.equals("game"))
                    {
                        sql = "CREATE TABLE game (game_id integer PRIMARY KEY, game_name VARCHAR(100))";
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

    public static void main(String[] args)
    {
        //DBProcess dbProcess = new DBProcess();
        //dbProcess.dbConnect();

    }




}
