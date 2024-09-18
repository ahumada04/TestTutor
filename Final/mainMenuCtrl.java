package Final;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static Final.UniversalFunctions.sceneSwitch;

public class mainMenuCtrl {

    @FXML
    Button returnBttn;

    public void returnAction(ActionEvent event) throws IOException {
        sceneSwitch(event, "login.fxml");
    }

    public void practiceAction(ActionEvent event) throws IOException {
        sceneSwitch(event, "testSettings.fxml");
    }
}
