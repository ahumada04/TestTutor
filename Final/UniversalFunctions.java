package Final;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UniversalFunctions {


    //Chapter --> Module
//    5.1 (Area under a curve)
//5.2 (Definite Integrals)
//5.3 (FTC1 & 2)
//
//11.1 (Sequences)
//11.2 (Series)
//11.3 (Integral Tests)
    
    //ch.1
    public static void sceneSwitch(ActionEvent event, String URL) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(UniversalFunctions.class.getResource(URL)));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
