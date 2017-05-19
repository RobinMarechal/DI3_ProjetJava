package views.departments;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import models.StandardDepartment;
import views.DepartmentsViewController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 13/05/2017.
 */
public class DepartmentsList extends DepartmentsViewController implements Initializable
{
    public DepartmentsList (ObservableList<StandardDepartment> departments)
    {
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {

    }
}
