package exercise1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.Color;
import java.sql.SQLException;
import java.awt.*;
import java.time.*;
import java.util.Date;

import static javafx.scene.text.Font.*;

public class GameInformSystem extends Application
{
    private TextField fNameTextField = new TextField();
    private TextField lNameTextField = new TextField();
    private TextField addrTextField = new TextField();
    private TextField zipTextField = new TextField();
    private TextField provinceTextField = new TextField();
    private TextField phoneNumberTextField = new TextField();
    private TextField gTitleTextField = new TextField();
    private DatePicker pDatePicker = new DatePicker();
    private TextField scoreTxtField = new TextField();
    private Text playerIdTxt = new Text();
    private Text gameIdTxt = new Text();
    private Text pgIdTxt = new Text();
    private Label msgTxt = new Label();

    private boolean dataFullFilled = false;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        border.setCenter(addPlayAndGame());

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("Game Player Information System");
        stage.show();
        // Close the app when the stage is closed
        stage.setOnCloseRequest(e -> {System.exit(0);});
    }

    private HBox addHBox()
    {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #008080;");

        // Add header, player id text field, three buttons
        Text header = new Text("Game Player Information System");
        header.setFont(font("Arial", FontWeight.BOLD, 20));
        TextField txtField = new TextField("Player ID");
        txtField.setPrefSize(80,5);
        Button btnGet = new Button("Get");
        Button btnUpdate = new Button("Update");
        Button btnReport = new Button("Report");

        // Set action functions to three buttons

        // Get information depends on the player_id
        btnGet.setOnAction(e ->
        {
            int playerID;

            if(txtField.getText().trim().equals("")||txtField.getText().trim().equals("Player ID"))
            {
                // Set notice
                msgTxt.setText(("Please prompt the Player ID first!"));
                msgTxt.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

            }
            else
            {
                //Enter the player ID
                playerID = Integer.parseInt(txtField.getText());
                System.out.println("<------ Getting information ------>");

                // Create the database connection
                DBProcess dbProcess = new DBProcess();
                dbProcess.dbConnect();

                // Execute the select sql statement to get detailed information and closed connection
                dbProcess.getInform(playerID);
                // Load the values into UI's objects
                playerIdTxt.setText(String.valueOf(dbProcess.pID));
                fNameTextField.setText(dbProcess.fName);
                lNameTextField.setText(dbProcess.lName);
                addrTextField.setText(dbProcess.addr);
                zipTextField.setText(dbProcess.pCode);
                provinceTextField.setText(dbProcess.provc);
                phoneNumberTextField.setText(dbProcess.pNum);
                gTitleTextField.setText(dbProcess.gTitle);
                pDatePicker.setValue(dbProcess.pyDate.toLocalDate());
                scoreTxtField.setText(String.valueOf(dbProcess.sc));
                gameIdTxt.setText(String.valueOf(dbProcess.gID));
                pgIdTxt.setText(String.valueOf(dbProcess.pgID));

                System.out.println("<------ Getting information is completed! ------>");
                // Set notice
                msgTxt.setText(("Player ID: " + playerID + " | Data are loaded!"));
                msgTxt.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
            }

        });

        // Update information depends on the player_id and values from UI objects
        btnUpdate.setOnAction(e ->
        {
            if(!fNameTextField.getText().trim().equals("")
                    &&!lNameTextField.getText().trim().equals("")
                    &&!addrTextField.getText().trim().equals("")
                    &&!zipTextField.getText().trim().equals("")
                    &&!provinceTextField.getText().trim().equals("")
                    &&!phoneNumberTextField.getText().trim().equals("")
                    &&!gTitleTextField.getText().trim().equals("")
                    &&!scoreTxtField.getText().trim().equals("")
                    &&!pDatePicker.getValue().equals("")
            )
            {
                dataFullFilled = true;
            }

            if(playerIdTxt.getText().trim().equals("")||gameIdTxt.getText().trim().equals("")||pgIdTxt.getText().trim().equals(""))
            {
                // Set notice
                msgTxt.setText(("Please get the data first!"));
                msgTxt.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            }
            else if(!dataFullFilled)
            {
                // Set notice
                msgTxt.setText(("Please don't leave blank!"));
                msgTxt.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            }
            else
            {
                // Create the database connection
                DBProcess dbProcess = new DBProcess();
                dbProcess.dbConnect();

                System.out.println("<------ Updating ------>");

                //Declare and initialize the local variables
                String firstName, lastName, address, postalCode, province, phoneNumber, gameTitle;
                java.sql.Date playingDate;
                Double score;
                int playerID, gameID, playerGameID;

                firstName = fNameTextField.getText();
                lastName = lNameTextField.getText();
                address = addrTextField.getText();
                postalCode = zipTextField.getText();
                province = provinceTextField.getText();
                phoneNumber = phoneNumberTextField.getText();
                gameTitle = gTitleTextField.getText();
                score = Double.valueOf(scoreTxtField.getText());
                playerID = Integer.parseInt(playerIdTxt.getText());
                gameID = Integer.parseInt(gameIdTxt.getText());
                playerGameID = Integer.parseInt(pgIdTxt.getText());

                // Convert the date type from localDate to SQL Date
                Date date = Date.from(((LocalDate)pDatePicker.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
                playingDate = new java.sql.Date(date.getTime());

                // Execute the update sql statement to modify detailed information and close the connection
                dbProcess.updateInform(firstName,lastName,address,postalCode,province,phoneNumber,
                        gameTitle,playingDate,score,playerID,gameID,playerGameID);

                System.out.println("<------ Updating is completed! ------>");
                // Set notice
                msgTxt.setText(("Player ID: " + playerID + " | Data are updated!"));
                msgTxt.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                System.out.println(pDatePicker.getValue());
            }


        });

        // Get report
        btnReport.setOnAction(e ->
        {
            // Execute create JTable method
            Report report = new Report();
            report.createJTable();
        });

        hbox.getChildren().addAll(header,txtField,btnGet,btnUpdate,btnReport);

        return hbox;
    } // End of addHbox method

    private VBox addVBox()
    {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        Text title = new Text("Player");
        title.setFont(font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        HBox hBox1 = new HBox();

        // Add GridPane to UI
        GridPane grid1 = new GridPane();
        grid1.setAlignment(Pos.CENTER);
        grid1.setHgap(8);
        grid1.setVgap(8);
        grid1.setPadding(new Insets(5,5,5,5));//Margins around grid(top,right,bottom,left)

        // Set First Name label and textField on gridPane
        Label fNameLabel = new Label("First Name: ");
        GridPane.setHalignment(fNameLabel, HPos.LEFT);
        grid1.add(fNameLabel,0,0);
        GridPane.setHalignment(fNameTextField, HPos.RIGHT);
        grid1.add(fNameTextField,1,0);

        // Set Last Name label and textField on gridPane
        Label lNameLabel = new Label("Last Name: ");
        GridPane.setHalignment(lNameLabel,HPos.LEFT);
        grid1.add(lNameLabel,0,1);
        GridPane.setHalignment(lNameTextField, HPos.RIGHT);
        grid1.add(lNameTextField,1,1);

        // Set Address label and textField on gridPane
        Label addrLabel = new Label("Address: ");
        GridPane.setHalignment(addrLabel,HPos.LEFT);
        grid1.add(addrLabel,0,2);
        GridPane.setHalignment(addrTextField, HPos.RIGHT);
        grid1.add(addrTextField,1,2);

        // Set Postal Code label and textField on gridPane
        Label zipLabel = new Label("Postal Code: ");
        GridPane.setHalignment(zipLabel,HPos.LEFT);
        grid1.add(zipLabel,0,3);
        GridPane.setHalignment(zipTextField, HPos.RIGHT);
        grid1.add(zipTextField,1,3);

        // Set Province label and textField on gridPane
        Label provinceLabel = new Label("Province: ");
        GridPane.setHalignment(provinceLabel,HPos.LEFT);
        grid1.add(provinceLabel,0,4);
        GridPane.setHalignment(provinceTextField, HPos.RIGHT);
        grid1.add(provinceTextField,1,4);

        // Set Phone Number label and textField on gridPane
        Label phoneNumberLabel = new Label("Phone Number: ");
        GridPane.setHalignment(phoneNumberLabel,HPos.LEFT);
        grid1.add(phoneNumberLabel,0,5);
        GridPane.setHalignment(phoneNumberTextField, HPos.RIGHT);
        grid1.add(phoneNumberTextField,1,5);

        // Add gridPane and hBox to VBox
        hBox1.getChildren().add(grid1);
        vbox.getChildren().add(hBox1);

        return vbox;
    } // end of addVbox method

    private VBox addPlayAndGame()
    {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        Text title = new Text("Game");
        title.setFont(font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        HBox hBox = new HBox();

        // Add GridPane to UI
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(5,5,5,5));//Margins around grid(top,right,bottom,left)

        // Set Game Title label and textField on gridPane
        Label gTitleLabel = new Label("Game Title: ");
        GridPane.setHalignment(gTitleLabel, HPos.LEFT);
        grid.add(gTitleLabel,0,0);
        GridPane.setHalignment(gTitleTextField, HPos.RIGHT);
        grid.add(gTitleTextField,1,0);

        // Set Playing Date label and datePicker on gridPane
        Label pDateLabel = new Label("Playing Date: ");
        GridPane.setHalignment(pDateLabel,HPos.LEFT);
        grid.add(pDateLabel,0,1);
        GridPane.setHalignment(pDatePicker, HPos.RIGHT);
        grid.add(pDatePicker,1,1);



        // Set Score
        Label scoreLabel = new Label("Score: ");
        GridPane.setHalignment(scoreLabel,HPos.LEFT);
        grid.add(scoreLabel,0,2);
        GridPane.setHalignment(scoreTxtField, HPos.RIGHT);
        grid.add(scoreTxtField,1,2);

        // Set IDs
        Label playerIdLabel = new Label("Player ID: ");
        GridPane.setHalignment(playerIdLabel,HPos.LEFT);
        grid.add(playerIdLabel,0,3);
        GridPane.setHalignment(playerIdTxt, HPos.RIGHT);
        grid.add(playerIdTxt,1,3);

        Label gameIdLabel = new Label("Game ID: ");
        GridPane.setHalignment(gameIdLabel,HPos.LEFT);
        grid.add(gameIdLabel,0,4);
        GridPane.setHalignment(gameIdTxt, HPos.RIGHT);
        grid.add(gameIdTxt,1,4);

        Label pgIdLabel = new Label("Player Game ID: ");
        GridPane.setHalignment(pgIdLabel,HPos.LEFT);
        grid.add(pgIdLabel,0,5);
        GridPane.setHalignment(pgIdTxt, HPos.RIGHT);
        grid.add(pgIdTxt,1,5);

        GridPane.setHalignment(msgTxt, HPos.RIGHT);
        grid.add(msgTxt,1,6);

        //Add Save Cancel buttons
        Button buttonSave = new Button("Insert");
        Button buttonCancel = new Button("Cancel");
        HBox hbox2 = new HBox();
        hbox2.setAlignment(Pos.BOTTOM_RIGHT);
        hbox2.setSpacing(10);

        // Add gridPane and hBox to VBox
        hBox.getChildren().add(grid);
        hbox2.getChildren().addAll(buttonSave,buttonCancel);
        vbox.getChildren().addAll(hBox,hbox2);

        // Add action function to the "insert" button
        buttonSave.setOnAction(e ->
        {
            if(!fNameTextField.getText().trim().equals("")
                &&!lNameTextField.getText().trim().equals("")
                &&!addrTextField.getText().trim().equals("")
                &&!zipTextField.getText().trim().equals("")
                &&!provinceTextField.getText().trim().equals("")
                &&!phoneNumberTextField.getText().trim().equals("")
                &&!gTitleTextField.getText().trim().equals("")
                &&!scoreTxtField.getText().trim().equals("")
                &&!pDatePicker.getValue().equals("")
            )
            {
                dataFullFilled = true;
            }
            if(!dataFullFilled)
            {
                // Set notice
                msgTxt.setText(("Please fill all the blank fields first!"));
                msgTxt.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            }
            else if (!playerIdTxt.getText().trim().equals("")
                    ||!gameIdTxt.getText().trim().equals("")
                    ||!pgIdTxt.getText().trim().equals(""))
            {
                // Set notice
                msgTxt.setText(("Please click update button."));
                msgTxt.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

            }
            else
            {
                //Declare variables to contain values of text boxes
                String firstName, lastName, address, postalCode, province, phoneNumber, gameTitle;
                java.sql.Date playingDate;
                Double score;

                firstName = fNameTextField.getText();
                lastName = lNameTextField.getText();
                address = addrTextField.getText();
                postalCode = zipTextField.getText();
                province = provinceTextField.getText();
                phoneNumber = phoneNumberTextField.getText();
                gameTitle = gTitleTextField.getText();
                score = Double.valueOf(scoreTxtField.getText());

                // Convert the date type
                Date date = Date.from(((LocalDate)pDatePicker.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
                playingDate = new java.sql.Date(date.getTime());
                System.out.println(playingDate);

                //Connect to the database
                DBProcess dbProcess = new DBProcess();
                dbProcess.dbConnect();

                //Create tables and insert values
                try
                {
                    //Create tables if needed
                    dbProcess.tableCreate();
                    //Insert values into Player and Game tables
                    dbProcess.dataInsert1(firstName,lastName,address,postalCode,province,phoneNumber,
                            gameTitle);
                    //Get the newest id from player and game tables
                    dbProcess.getMaxID();
                    //Insert values into PlayAndGame table and close the connection
                    dbProcess.dataInsert2(playingDate,score);
                    // Set notice
                    msgTxt.setText(("Name: " + firstName + " | Data are inserted!"));
                    msgTxt.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                    System.out.println(pDatePicker.getValue());
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

        });

        //Add function to cancel button
        buttonCancel.setOnAction(e -> {System.exit(0);});
        return vbox;

    } //end of addPlayAndGame method



    public static void main(String[] args)
    {
        launch();
    }

}
