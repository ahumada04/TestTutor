package Final;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static Final.testCtrl.questionNumber;
import static Final.testCtrl.userAnswers;

public class MultipleChoiceCtrl implements Initializable {

    @FXML
    Label answerkeyLabel;
    @FXML
    RadioButton choiceA, choiceB, choiceC, choiceD;
    @FXML
    CheckBox answerCheck;
    @FXML
    GridPane questionGrid;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (userAnswers[questionNumber-1]) {
            case 'A':
                choiceA.setSelected(true);
                break;
            case 'B':
                choiceB.setSelected(true);
                break;
            case 'C':
                choiceC.setSelected(true);
                break;
            case 'D':
                choiceD.setSelected(true);
                break;
        }
        questionGrid.setVisible(false);
        answerkeyLabel.setVisible(false);
    }

    public void answerCheck() {
        questionGrid.setVisible(answerCheck.isSelected());
    }

    public void aSelected() {
        userAnswers[questionNumber-1] = 'A';
    }

    public void bSelected() {
        userAnswers[questionNumber-1] = 'B';
    }

    public void cSelected() {
        userAnswers[questionNumber-1] = 'C';
    }

    public void dSelected() {
        userAnswers[questionNumber-1] = 'D';
    }

}
