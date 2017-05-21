package controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import lib.BaseController;
import lib.util.validator.Validator;
import lib.views.CSSClasses;
import lib.views.Tabs;
import lib.views.Template;
import models.Company;
import models.Manager;
import models.StandardDepartment;
import org.intellij.lang.annotations.Language;
import views.DepartmentsViewController;
import views.departments.DepartmentsList;
import views.dialogs.CreateDepartmentDialog;

/**
 * Created by Robin on 26/04/2017.
 */
public class DepartmentsController extends BaseController
{
    @Override
    public void home ()
    {
        ObservableList<StandardDepartment> departments = Company.getCompany().getStandardDepartmentsList();
        DepartmentsViewController          view        = new DepartmentsList(departments);

        departments = Company.getCompany().getStandardDepartmentsList();

        Template.getInstance().setView(Tabs.STANDARD_DEPARTMENTS, view);
    }

    public void show (int id)
    {
        StandardDepartment dep = Company.getCompany().getStandardDepartment(id);
        show(dep);
    }

    public void show (StandardDepartment dep)
    {
        System.out.println("Show standard department n°" + dep.getId() + "...");
    }

    public void openCreationDepartmentDialog ()
    {
        new CreateDepartmentDialog();
    }

    public StandardDepartment createDepartment (TextField fieldName, TextField fieldSector, TextField fieldManagerFN,
                                                TextField fieldManagerLN)
    {
        @Language ("RegExp") String regexp = "^[A-Za-z0-9- ]+$";

        String name   = null;
        String sector = null;

        Manager manager = (Manager) new EmployeesController().createEmploye(fieldManagerFN, fieldManagerLN, null, true);

        if (Validator.make(fieldName, regexp))
        {
            name = fieldName.getText().trim();
            fieldName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            fieldName.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        if (Validator.make(fieldSector, regexp))
        {
            sector = fieldSector.getText().trim();
            fieldSector.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            fieldSector.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        if (name != null && sector != null && manager != null)
        {
            StandardDepartment dep = Company.createStandardDepartment(name, sector);
            dep.setManager(manager);
            fieldName.clear();
            fieldSector.clear();
            fieldName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            fieldSector.getStyleClass().remove(CSSClasses.INPUT_INVALID);

            return dep;
        }

        return null;
    }
}
