package TestTutor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static TestTutor.newStudentCtrl.dbURL;
import static TestTutor.SceneSwitch.sceneSwitch;

public class loginCtrl implements Initializable {

    @FXML
    ComboBox<String> nameSelect;
    @FXML
    Button continueBttn;
    @FXML
    TextField pwdText; //replace with password entry tool!
    @FXML
    Button signupBttn;

    public static int userID;
    public static String userName; //query redefined based on need
    public static String userClass;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // SQL query to retrieve item data
        try (Connection conn = DriverManager.getConnection(dbURL)) {
            Statement stmt = conn.createStatement();
            String query = "SELECT name FROM students"; //Gather name column from students table
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                nameSelect.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        nameSelect.setOnAction(this::getName);
    }


    public void getName(ActionEvent event) {
        userName = nameSelect.getValue();
    }

    public void getUserInfo() {
        String query = "SELECT student_id, class1 FROM students WHERE name = ?"; //Gather name column from students table

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            userID = rs.getInt("student_id");
            userClass = rs.getString("class1");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void continueAction(ActionEvent event) throws IOException {
        String userpwd = pwdText.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (pwdCheck(userpwd)) {
            getUserInfo(); //grabs user info from db for later use

            // Show confimation window
            alert.setTitle("Welcome!");
            alert.setHeaderText(null);
            alert.setContentText("Welcome " + userName + "!");
            alert.showAndWait();

            sceneSwitch(event, "mainMenu.fxml");
        } else if (userpwd.isEmpty()) {
            alert.setTitle("Empty Password");
            alert.setHeaderText(null);
            alert.setContentText("No Password Entered!");
            alert.showAndWait();
        } else {
            //replace with popup later
            alert.setTitle("Wrong Password");
            alert.setHeaderText(null);
            alert.setContentText("Wrong password!" +
                    "\nCheck for case sensitivity and extra spaces");
            alert.showAndWait();
        }
    }

    public void signupAction(ActionEvent event) throws IOException {
        sceneSwitch(event, "newStudent.fxml");
    }

    public boolean pwdCheck(String password) {
        String query = "SELECT password FROM students where name = ?"; //Gets password of name selected

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            return password.equals(rs.getString("password"));

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
