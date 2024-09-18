package TestTutor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static TestTutor.SceneSwitch.sceneSwitch;

public class newStudentCtrl implements Initializable {

    @FXML
    TextField nameText, pwdText;
    @FXML
    Label nameLabel, pwdLabel, class1Label,
            nameAlert, pwdAlert, classAlert ;
    @FXML
    ChoiceBox<String> gradeChoice;
    @FXML
    ChoiceBox<String> class1Choice;


    private final String[] grades = {"Freshman", "Sophmore", "Junior", "Senior"};
    private final String[] classes = {"Math1B", "Math2A", "Math2B", "Math2D"};

    // URL to connect to database easily
    public static final String dbURL = "jdbc:sqlite:C:/Users/Ahuma/OneDrive/Desktop/CMPR113/src/main/java/TestTutor/TestTutorDB";

    //lower three to allow transition into next scene

    private Boolean switchCheck = false;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        gradeChoice.getItems().addAll(grades);
        class1Choice.getItems().addAll(classes);
    }


    public boolean userInputValidation(String name,String pwd)
    {
        boolean flag = true;

        if(name.isEmpty() || containsSpecialChar(name) || containsNumber(name)) {
            nameText.clear();

            nameLabel.setStyle("-fx-text-fill: red;");
            nameAlert.setVisible(true);
            flag = false; //purposefully makeing sure both checks are ran to allow the user to be notified.
        }
        else{
            nameAlert.setVisible(false);
            nameLabel.setStyle("-fx-text-fill: black;");
        }

        if(pwd.isEmpty() || !containsNumber(pwd) || !containsUpperCase(pwd) || !containsLowerCase(pwd)) {
            pwdText.clear();
            pwdLabel.setStyle("-fx-text-fill: red;");
            pwdAlert.setVisible(true);
            flag = false;
        }
        else {
            pwdAlert.setVisible(false);
            pwdLabel.setStyle("-fx-text-fill: black;");
        }
        return flag;
    }


    public void studentSubmit(ActionEvent event) throws IOException {
        String username = nameText.getText();
        String userpwd = pwdText.getText();
        String usergrade = gradeChoice.getValue();
        String class1 = class1Choice.getValue();
        //bridges connection to sql, doesn't add if user doesn't like data
        boolean inputVal = userInputValidation(username, userpwd);// I want this to run to clear text even if user input is marked wrong by empty class

        if (class1Choice.getSelectionModel().isEmpty())
        {
            class1Label.setStyle("-fx-text-fill: red;");
            classAlert.setVisible(true);
        }
        else if (inputVal) { //validates name and password input
            class1Label.setStyle("-fx-text-fill: black;");
            classAlert.setVisible(false);
            signupAlert(username, userpwd, usergrade, class1);
        }
        else{
            class1Label.setStyle("-fx-text-fill: black;");
            classAlert.setVisible(false);
        }

        //runs if user confirms on alert menu
        if(switchCheck)
        {
            sceneSwitch(event, "login.fxml");
            switchCheck = false;
        }
    }

    public void backAction(ActionEvent event) throws IOException {
        sceneSwitch(event, "login.fxml");
    }

    private void studentsSQLAdd(String name, String password, String grade, String class1)
    {
        String addQuerry = "INSERT INTO students(name, password, grade, class1) VALUES(?,?,?,?)";

        // Try-with-resources statement to ensure the resources are closed after use
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(addQuerry)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, grade);
            pstmt.setString(4, class1);

            pstmt.executeUpdate(); // Execute the update

            //might want to replace with a popup, for now leave be
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("You have been added to our database");

            // Show the alert
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unexpected Error");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred: " + e.getMessage());

            // Show the alert
            alert.showAndWait();
        }
    }

    private void signupAlert(String username, String pwd, String grade, String class1) {
        // Create an alert for user input validation
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Login Confirmation");
        alert.setHeaderText(null);

        // Show the actual alert
        alert.setContentText("Profile info entered:" +
                "\nName: " + username +
                "\nPassword: " + pwd +
                "\nGrade: " + grade +
                "\nClass1: " + class1);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                studentsSQLAdd(username, pwd, grade, class1);
                switchCheck = true;
            }
        });
    }


    private boolean containsNumber(String text) {
        return text.matches(".*\\d.*");
    }

    private boolean containsUpperCase(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isUpperCase(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsLowerCase(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLowerCase(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialChar(String text) {
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSpecial = special.matcher(text);
        return hasSpecial.find();
    }
}
