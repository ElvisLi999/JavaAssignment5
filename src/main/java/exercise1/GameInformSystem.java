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

import java.sql.SQLException;
import java.time.LocalDate;

public class GameInformSystem extends Application
{
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

        Text header = new Text("Game Player Information System");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));


        hbox.getChildren().add(header);

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
        TextField fNameTextField = new TextField();
        GridPane.setHalignment(fNameLabel, HPos.LEFT);
        grid1.add(fNameLabel,0,0);
        GridPane.setHalignment(fNameTextField, HPos.RIGHT);
        grid1.add(fNameTextField,1,0);

        // Set Last Name label and textField on gridPane
        Label lNameLabel = new Label("Last Name: ");
        TextField lNameTextField = new TextField();
        GridPane.setHalignment(lNameLabel,HPos.LEFT);
        grid1.add(lNameLabel,0,1);
        GridPane.setHalignment(lNameTextField, HPos.RIGHT);
        grid1.add(lNameTextField,1,1);

        // Set Address label and textField on gridPane
        Label addrLabel = new Label("Address: ");
        TextField addrTextField = new TextField();
        GridPane.setHalignment(addrLabel,HPos.LEFT);
        grid1.add(addrLabel,0,2);
        GridPane.setHalignment(addrTextField, HPos.RIGHT);
        grid1.add(addrTextField,1,2);

        // Set Postal Code label and textField on gridPane
        Label zipLabel = new Label("Postal Code: ");
        TextField zipTextField = new TextField();
        GridPane.setHalignment(zipLabel,HPos.LEFT);
        grid1.add(zipLabel,0,3);
        GridPane.setHalignment(zipTextField, HPos.RIGHT);
        grid1.add(zipTextField,1,3);

        // Set Province label and textField on gridPane
        Label provinceLabel = new Label("Province: ");
        TextField provinceTextField = new TextField();
        GridPane.setHalignment(provinceLabel,HPos.LEFT);
        grid1.add(provinceLabel,0,4);
        GridPane.setHalignment(provinceTextField, HPos.RIGHT);
        grid1.add(provinceTextField,1,4);

        // Set Phone Number label and textField on gridPane
        Label phoneNumberLabel = new Label("Phone Number: ");
        TextField phoneNumberTextField = new TextField();
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
        vbox.setSpacing(55);              // Gap between nodes

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
        TextField GTitleTextField = new TextField();
        GridPane.setHalignment(gTitleLabel, HPos.LEFT);
        grid.add(gTitleLabel,0,0);
        GridPane.setHalignment(GTitleTextField, HPos.RIGHT);
        grid.add(GTitleTextField,1,0);

        // Set Playing Date label and datePicker on gridPane
        Label pDateLabel = new Label("Playing Date: ");
        DatePicker pDatePicker = new DatePicker(LocalDate.now());
        GridPane.setHalignment(pDateLabel,HPos.LEFT);
        grid.add(pDateLabel,0,1);
        GridPane.setHalignment(pDatePicker, HPos.RIGHT);
        grid.add(pDatePicker,1,1);

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
        buttonSave.setOnAction(e ->
        {
            DBProcess dbProcess = new DBProcess();
            dbProcess.dbConnect();
            try
            {
                dbProcess.tableCreate();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }

        });
        return vbox;

    } //end of method



    public static void main(String[] args)
    {
        launch();
    }

}
