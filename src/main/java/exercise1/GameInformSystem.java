package exercise1;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.text.DateFormat;

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
    private TextField playerIdTxtField = new TextField();
    private TextField gameIdTxtField = new TextField();
    private TextField pgIdTxtField = new TextField();

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
    }

    private HBox addHBox()
    {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #008080;");

        // Add header, player id text field, two buttons
        Text header = new Text("Game Player Information System");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        TextField txtField = new TextField("Player ID");
        txtField.setPrefSize(80,5);
        Button btnGet = new Button("Get");
        Button btnUpdate = new Button("Update");

        // Set action functions to two buttons
        btnGet.setOnAction(e ->
        {
            int playerID;
            playerID = Integer.parseInt(txtField.getText());

            DBProcess dbProcess = new DBProcess();
            dbProcess.dbConnect();
            dbProcess.getInform(playerID);
            playerIdTxtField.setText(String.valueOf(dbProcess.pID));
            fNameTextField.setText(dbProcess.fName);
            lNameTextField.setText(dbProcess.lName);
            addrTextField.setText(dbProcess.addr);
            zipTextField.setText(dbProcess.pCode);
            provinceTextField.setText(dbProcess.provc);
            phoneNumberTextField.setText(dbProcess.pNum);
            gTitleTextField.setText(dbProcess.gTitle);
            pDatePicker.setValue(dbProcess.pyDate.toLocalDate());
            scoreTxtField.setText(String.valueOf(dbProcess.sc));
            gameIdTxtField.setText(String.valueOf(dbProcess.gID));
            pgIdTxtField.setText(String.valueOf(dbProcess.pgID));
        });

        btnUpdate.setOnAction(e ->
        {
            DBProcess dbProcess = new DBProcess();
            dbProcess.dbConnect();

            System.out.println("<------ Updating ------>");

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
            playerID = Integer.parseInt(playerIdTxtField.getText());
            gameID = Integer.parseInt(gameIdTxtField.getText());
            playerGameID = Integer.parseInt(pgIdTxtField.getText());

            // Convert the date type
            Date date = Date.from(((LocalDate)pDatePicker.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
            playingDate = new java.sql.Date(date.getTime());

            dbProcess.updateInform(firstName,lastName,address,postalCode,province,phoneNumber,
                    gameTitle,playingDate,score,playerID,gameID,playerGameID);

        });


        hbox.getChildren().addAll(header,txtField,btnGet,btnUpdate);

        return hbox;
    } // End of addHbox method

    private VBox addVBox()
    {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        Text title = new Text("Player");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
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
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
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
        GridPane.setHalignment(playerIdTxtField, HPos.RIGHT);
        grid.add(playerIdTxtField,1,3);

        Label gameIdLabel = new Label("Game ID: ");
        GridPane.setHalignment(gameIdLabel,HPos.LEFT);
        grid.add(gameIdLabel,0,4);
        GridPane.setHalignment(gameIdTxtField, HPos.RIGHT);
        grid.add(gameIdTxtField,1,4);

        Label pgIdLabel = new Label("Player Game ID: ");
        GridPane.setHalignment(pgIdLabel,HPos.LEFT);
        grid.add(pgIdLabel,0,5);
        GridPane.setHalignment(pgIdTxtField, HPos.RIGHT);
        grid.add(pgIdTxtField,1,5);

        //Add Save Cancel buttons
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");
        HBox hbox2 = new HBox();
        hbox2.setAlignment(Pos.BOTTOM_RIGHT);
        hbox2.setSpacing(10);

        // Add gridPane and hBox to VBox
        hBox.getChildren().add(grid);
        hbox2.getChildren().addAll(buttonSave,buttonCancel);
        vbox.getChildren().addAll(hBox,hbox2);

        // Add action function to the "save" button
        buttonSave.setOnAction(e ->
        {
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
            playerID = Integer.parseInt(playerIdTxtField.getText());
            gameID = Integer.parseInt(gameIdTxtField.getText());
            playerGameID = Integer.parseInt(pgIdTxtField.getText());

            // Convert the date type
            Date date = Date.from(((LocalDate)pDatePicker.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
            playingDate = new java.sql.Date(date.getTime());
            System.out.println(playingDate);



            DBProcess dbProcess = new DBProcess();
            dbProcess.dbConnect();
            try
            {
                dbProcess.tableCreate();
                dbProcess.dataInsert(firstName,lastName,address,postalCode,province,phoneNumber,
                        gameTitle,playingDate,score,playerID,gameID,playerGameID);
            }
            catch (SQLException ex)
            {
                System.out.println(ex.getMessage());
            }

        });
        return vbox;

    } //end of addPlayAndGame method



    public static void main(String[] args)
    {
        launch();
    }

}
