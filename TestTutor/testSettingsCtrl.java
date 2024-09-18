package TestTutor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static TestTutor.SceneSwitch.sceneSwitch;
import static TestTutor.loginCtrl.userClass;
import static TestTutor.newStudentCtrl.dbURL;


public class testSettingsCtrl implements Initializable {

    @FXML
    private TreeView testItemsTree;
    @FXML
    private Button continueBttn, backBttn;
    @FXML
    private Spinner<Integer> numInput;
    @FXML
    private Label selectLabel;

    public static int totalItems; //idea would be that two below would track the # of wanted questions and total to this value
    public static ArrayList<Integer> chapterItems; //trouble identifying how I would effectively label such?
    public static ArrayList<Integer> sectionItems;
    public static String[] questionURLS;
    public static char[] answerkey;
    private String sqlTable;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        selectLabel.setText(userClass);
        // TEMP hardcode, update to pull from DB on TODO
        final int  itemsAvailable = 6;
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, itemsAvailable); //replace 10 with modular value of availible questions
        valueFactory.setValue(1);
        numInput.setValueFactory(valueFactory);
        //numInput.valueProperty().addListener((observable, oldValue, newValue) -> {})

        switch (userClass) {
            case "Math1B": {
                //populate later
            }
            case "Math2A": {
                //populate later
            }
            case "Math2B": {
                TreeItem<String> root = new TreeItem<>("Math2B");

                TreeItem<String> branch5 = new TreeItem<>("Chapter 5");
                TreeItem<String> branch6 = new TreeItem<>("Chapter 6");
//                TreeItem<String> branch11 = new TreeItem<>("Chapter 11");

                TreeItem<String> leaf5_1 = new TreeItem<>("5.1 (Area under a curve)");
                TreeItem<String> leaf5_2 = new TreeItem<>("5.2 (Definite Integrals)");
                TreeItem<String> leaf5_3 = new TreeItem<>("5.3 (FTC1 & FTC2)");
                TreeItem<String> leaf5_4 = new TreeItem<>("5.4 (Indefinite Integral & Net Change Theorem)");
                TreeItem<String> leaf5_5 = new TreeItem<>("5.5 (U-Substitution)");

                TreeItem<String> leaf6_1 = new TreeItem<>("6.1 (Area Between Curves)");
                TreeItem<String> leaf6_2 = new TreeItem<>("6.2 (Volume)");
                TreeItem<String> leaf6_5 = new TreeItem<>("6.5 (Average Value of a Function)");

//                TreeItem<String> leaf11_1 = new TreeItem<>("11.1 (Sequences)");
//                TreeItem<String> leaf11_2 = new TreeItem<>("11.2 (Series)");
//                TreeItem<String> leaf11_3 = new TreeItem<>("11.3 (Integral Tests)");

                testItemsTree.setRoot(root);
                root.getChildren().addAll(branch5, branch6); //add branch11 when I get there
                branch5.getChildren().addAll(leaf5_1, leaf5_2, leaf5_3, leaf5_4, leaf5_5);
                branch6.getChildren().addAll(leaf6_1, leaf6_2, leaf6_5);
//                branch11.getChildren().addAll(leaf11_1, leaf11_2, leaf11_3);

                sqlTable = "QBank_Math2B";
                break;
            }
            case "Math2D": {
                TreeItem<String> root = new TreeItem<>("Math2D");

                TreeItem<String> branch10 = new TreeItem<>("Chapter 10");
                TreeItem<String> branch12 = new TreeItem<>("Chapter 12");

                TreeItem<String> leaf10_1 = new TreeItem<>("10.1 (Curves Defined by Parametric Equations)");
                TreeItem<String> leaf10_2 = new TreeItem<>("10.2 (Calculus with Parametric Curves)");
                TreeItem<String> leaf10_3 = new TreeItem<>("10.3 (Polar Coordinates");
                TreeItem<String> leaf12_1 = new TreeItem<>("12.1 (3-Dimensional Coordinate Systems)");
                TreeItem<String> leaf12_2 = new TreeItem<>("12.2 (Vectors)");
                TreeItem<String> leaf12_3 = new TreeItem<>("12.3 (The Dot Product)");
                TreeItem<String> leaf12_4 = new TreeItem<>("12.4 (The Cross Product)");
                TreeItem<String> leaf12_5 = new TreeItem<>("12.5 (Equations of Lines and Planes)");
                TreeItem<String> leaf12_6 = new TreeItem<>("12.6 (Cylinders and Quadric Surfaces)");

                testItemsTree.setRoot(root);
                root.getChildren().addAll(branch10, branch12);
                branch10.getChildren().addAll(leaf10_1, leaf10_2, leaf10_3);
                branch12.getChildren().addAll(leaf12_1, leaf12_2, leaf12_3, leaf12_4, leaf12_5, leaf12_6);

                sqlTable = "QBank_Math2D";
                break;
            }

        }
    }

    public void continueAction(ActionEvent event) {
        totalItems = numInput.getValue();
        String query = "SELECT questionURL, answerkey FROM " + sqlTable; //Gather name column from students table
        questionURLS = new String[totalItems];
        answerkey = new char[totalItems];

        try (Connection conn = DriverManager.getConnection(dbURL)) {
             Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int i = 0;
            while (rs.next() && totalItems > i) {
                questionURLS[i] = rs.getString("questionURL");
                answerkey[i] = rs.getString("answerkey").charAt(0); //bandage fix, convert db to char datatype later
                i++;
            }
            sceneSwitch(event, "testFrame.fxml");
        } catch (SQLException e) {
            System.out.println("Broke trying to update SQL");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Broke trying to switch");
            throw new RuntimeException(e);
        }
    }

    public void backAction(ActionEvent event) throws IOException {
        sceneSwitch(event,"mainMenu.fxml");
    }

    // Future Method to allow users to select WHERE they want questions from with pulls of how many are available
    public void selectItem() {
//        TreeItem<String> item = (TreeItem<String>) testItemsTree.getSelectionModel().getSelectedItem();
//        if (item != null) {
//            selectLabel.setText(item.getValue());
//            int testval = numInput.getValue();
//            System.out.println(testval);
//            //rerout the numb input and collect data?
//        }
    }


}
