package TestTutor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import static TestTutor.SceneSwitch.sceneSwitch;
import static TestTutor.testSettingsCtrl.answerkey;
import static TestTutor.testSettingsCtrl.questionURLS;

// Controls the "outer frame" of each Question as it stays static.
public class testCtrl implements Initializable {

    @FXML
    Button backBttn, continueBttn, submitBttn;
    @FXML
    StackPane questionPane;
    @FXML
    Label QNumbLabel;
    @FXML
    ProgressBar testProgress;

    private static int numCorrect=0;
    private static double progress;

    public static double percentCorrect=0.;
    public static int questionNumber=1;
    private static int totalQuestions;
    public static char [] userAnswers;

    private boolean switchCheck= false;


    public void initialize(URL location, ResourceBundle resources) {
            try {
                if (questionURLS.length > 0)
                {
                    // Sets up answer key + background tracking of user inputs
                    totalQuestions = questionURLS.length;
                    userAnswers = new char[totalQuestions];
                    System.out.println();

                    //sets up front end processes like visual and buttons
                    questionSwitch(questionURLS[0]);
                    QNumbLabel.setText("1");
                    backBttn.setDisable(true);
                    setTestProgress();
                    testProgress.setStyle("-fx-accent: DARKBLUE");

                    // Small edgecase to ensure test doesn't break with only 1 question selected
                    if(questionURLS.length==1)
                    {
                        continueBttn.setVisible(false);
                        submitBttn.setVisible(true);
                    }
                }
                else
                {
                    System.out.println("No questions selected, please return to the main menu.");
                }
            } catch (ExceptionInInitializerError e) {
                System.out.println("Failed to initialize test, please try again.");
                e.printStackTrace();
            }
    }


    public void continueAction() {
        questionNumber++;
        backBttn.setDisable(false);
        QNumbLabel.setText(Integer.toString(questionNumber));
        setTestProgress();
        questionSwitch(questionURLS[questionNumber - 1]);

        if(questionNumber == totalQuestions) {
            continueBttn.setVisible(false);
            submitBttn.setVisible(true);
            // Add scene switch to "Results Page"
        }
    }


    public void backAction() {
        if(questionNumber ==  totalQuestions) {
            continueBttn.setVisible(true);
            submitBttn.setVisible(false);
        }

        questionNumber--;
        QNumbLabel.setText(Integer.toString(questionNumber));
        setTestProgress();
        questionSwitch(questionURLS[questionNumber - 1]);

        if(questionNumber == 1) {
            backBttn.setDisable(true);
        }
    }


    public void submitAction(ActionEvent event) throws IOException {
        submitAlert(); //asks user if they're actually ready to submit

        if(switchCheck)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Test Results");
            alert.setHeaderText(null);
            alert.setContentText("You got " + percentCorrect + "% Correct!");
            alert.showAndWait();

            sceneSwitch(event, "mainMenu.fxml");
        }
    }


    private void submitAlert() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Test Submit Confirmation");
        alert.setHeaderText(null);

        // Show the actual alert
        alert.setContentText("Are you sure you want to submit the test?"
                            + "\nThis cannot be undone.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                testGrade();
                switchCheck = true;
            }
        });
    }


    private void testGrade()
    {
        for(int j = 0; j < totalQuestions; j++)
        {
            if(userAnswers[j] == answerkey[j])
            {
                numCorrect++;
            }
        }
        // Update to format to a XX.XX%
        percentCorrect= getPercent(numCorrect,totalQuestions) * 100;
    }


    public void questionSwitch(String URL) {
    try {
        // Load the question layout
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(URL));
        Parent question = loader.load();

        // Clear the inner pane and set the new question
        questionPane.getChildren().clear();
        questionPane.getChildren().add(question);
    } catch (IOException e) {
        System.out.println("Failed switching question items, try again.");
        e.printStackTrace();
    }
    }


    private double getPercent(int numerator, int denominator) {
        return (double) Math.round(((double) numerator / denominator) * 100) / 100;
    }

    private void setTestProgress() {
        progress = getPercent(questionNumber, totalQuestions);
        testProgress.setProgress(progress);
    }

}
