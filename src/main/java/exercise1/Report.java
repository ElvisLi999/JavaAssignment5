package exercise1;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class Report extends JFrame
{

    public void createJTable() {
        Vector rowData, headers;
        JTable jtable = null;
        JScrollPane jScrollPane = null;

        DBProcess dbProcess = new DBProcess();
        dbProcess.dbConnect();
        ResultSet rs = null;


        // Create table headers
        headers = new Vector();
        headers.add("Player ID");
        headers.add("First Name");
        headers.add("Last Name");
        headers.add("Address");
        headers.add("Postal Code");
        headers.add("Province");
        headers.add("Phone Number");
        headers.add("Game ID");
        headers.add("Game Title");
        headers.add("Player Game ID");
        headers.add("Playing Date");
        headers.add("Score");

        // Create table content
        rowData = new Vector();

        String getInformSql = "SELECT p.player_id, p.first_name, p.last_name, p.address, p.postal_code, p.province, p.phone_number," +
                " g.game_id, g.game_title, pg.player_game_id, pg.playing_date, pg.score" +
                " FROM player p, game g, playerandgame pg" +
                " WHERE p.player_id = pg.player_id " +
                "      AND g.game_id = pg.game_id";

        try {
            dbProcess.statement = dbProcess.connection.createStatement();
            rs = dbProcess.statement.executeQuery(getInformSql);
            while (rs.next()) {
                Vector col = new Vector();
                col.add(rs.getInt("player_id"));
                col.add(rs.getString("first_name"));
                col.add(rs.getString("last_name"));
                col.add(rs.getString("address"));
                col.add(rs.getString("postal_code"));
                col.add(rs.getString("province"));
                col.add(rs.getString("phone_number"));
                col.add(rs.getString("game_title"));
                col.add(rs.getDate("playing_date"));
                col.add(rs.getInt("game_id"));
                col.add(rs.getInt("player_game_id"));
                col.add(rs.getDouble("score"));

                //add to rowData
                rowData.add(col);
            }
            // Close the connection
            dbProcess.connection.close();
            System.out.println("Database is disconnected.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Records of All Players");
        frame.setSize(1000, 400);

        //Close application when close report window
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        jtable = new JTable(rowData, headers);
        jScrollPane = new JScrollPane(jtable);
        contentPane.add(jScrollPane);
        // Set width value for address column
        jtable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Click one row to get inform
        JTable finalJtable = jtable;
        jtable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                        int row= finalJtable.getSelectedRow();//选中行
                        int col= finalJtable.getSelectedColumn();//选中列
                        System.out.println("select:"+ finalJtable.getValueAt(row, 0)+"\t"+ finalJtable.getValueAt(row, 1));
                }
            }
        });

        frame.setVisible(true);
    }
}
