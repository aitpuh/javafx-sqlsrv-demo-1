package sample;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.management.Query;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Controller {

    @FXML
    private TextField taskDescription;
    @FXML
    private ComboBox<Users> userSelection;
    @FXML
    private Button insertTask;
    @FXML
    private TableView<TaskReport> tasksTable;
    @FXML
    private DatePicker dueDatePicker;

    public static SQLServerDataSource sqlServerDataSource;
    private Connection conn;

    public void initialize() throws SQLException {
        sqlServerDataSource = new SQLServerDataSource();
        sqlServerDataSource.setURL("jdbc:sqlserver://aitpuh.database.windows.net:1433;database=aitp-demo");
        sqlServerDataSource.setUser("julius@aitpuh");
        sqlServerDataSource.setPassword("Cougar 2017");


        userSelection.setItems(FXCollections.observableArrayList(getUsers()));
        userSelection.getSelectionModel().select(0);
        reload();
    }

    public List<Users> getUsers() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(sqlServerDataSource);
        ResultSetHandler<List<Users>> h = new BeanListHandler<Users>(Users.class);

        List<Users> users = queryRunner.query("select * from aitp_users", h);
        return users;
    }

    public void reload() throws SQLException {
       QueryRunner queryRunner = new QueryRunner(sqlServerDataSource);
       ResultSetHandler<List<TaskReport>> h = new BeanListHandler<TaskReport>(TaskReport.class);

       List<TaskReport> tasks = queryRunner.query("select aitp_tasks.task_id ,description, duedate, complete, name\n" +
               "from aitp_tasks\n" +
               "inner join aitp_user_tasks ut\n" +
               "  on aitp_tasks.task_id = ut.task_id\n" +
               "inner join aitp_users au\n" +
               "  on ut.user_id = au.user_id;", h);
       tasksTable.setItems(FXCollections.observableArrayList(tasks));
    }

    @FXML
    private void insertTask() throws SQLException {

            QueryRunner queryRunner = new QueryRunner(sqlServerDataSource);
            ResultSetHandler<BigDecimal> h = new ScalarHandler<BigDecimal>();
            String sql = "insert into aitp_tasks(description, duedate) values(?, ?)";
            BigDecimal insertedId = queryRunner.insert(sql, h,
                    taskDescription.getText(), dueDatePicker.getValue());

            sql = "insert into aitp_user_tasks(user_id, task_id) values (?,?)";
            int inserts = queryRunner.update(sql, userSelection.getSelectionModel().getSelectedItem().getUser_id(), insertedId);

            reload();
    }

    @FXML
    private void markComplete(javafx.scene.input.MouseEvent event) throws SQLException {
        if (event.getClickCount() % 2 == 0) {
            QueryRunner queryRunner = new QueryRunner(sqlServerDataSource);
            String sql = "update aitp_tasks set complete = ? where task_id = ?";
            int updated = queryRunner.update(sql,
                    !tasksTable.getSelectionModel().getSelectedItem().isComplete(),
                    tasksTable.getSelectionModel().getSelectedItem().getTask_id());
            reload();
        }
    }
}
