package sample;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField taskDescription;
    @FXML
    private ComboBox userSelection;
    @FXML
    private Button insertTask;

}
