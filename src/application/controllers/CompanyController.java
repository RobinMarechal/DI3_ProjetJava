package application.controllers;

import fr.etu.univtours.marechal.SimpleDate;
import javafx.scene.control.TextField;
import application.lib.BaseController;
import application.lib.util.form.Form;
import application.lib.views.Tabs;
import application.lib.views.Template;
import application.models.Boss;
import application.models.Company;
import application.models.ManagementDepartment;
import application.views.CompanyViewController;
import application.views.company.HomeCompany;
import application.views.dialogs.EditCompanyDialog;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Robin on 26/04/2017.
 */
public class CompanyController extends BaseController implements Observer
{
    public CompanyController ()
    {
        Company.getCompany();
    }

    @Override
    public void home ()
    {
        CompanyViewController view = new HomeCompany(Company.getCompany(), SimpleDate.TODAY);

        Template.getInstance().setView(Tabs.COMPANY, view);
    }

    @Override
    public void update (Observable o, Object arg)
    {
        System.out.println("Company's home view updated...");
        home();
    }

    public void openEditCompanyDialog ()
    {
        new EditCompanyDialog(Company.getCompany());
    }

    public boolean updateCompany (Form form)
    {
        boolean allPassed = validateForm(form);

        if(!allPassed)
        {
            return false;
        }
        else
        {
            String companyName = ((TextField) form.get("companyName").getField()).getText();
            String bossFirstName = ((TextField) form.get("bossFirstName").getField()).getText();
            String bossLastName = ((TextField) form.get("bossLastName").getField()).getText();
            String manDepName = ((TextField) form.get("managementDepartmentName").getField()).getText();
            String manDepSector = ((TextField) form.get("managementDepartmentActivitySector").getField()).getText();

            Company company = Company.getCompany();
            Boss    boss    = company.getBoss();
            ManagementDepartment managementDepartment = company.getManagementDepartment();

            company.setName(companyName);
            boss.setFirstName(bossFirstName);
            boss.setLastName(bossLastName);
            managementDepartment.setName(manDepName);
            managementDepartment.setActivitySector((manDepSector));

            return true;
        }
    }
}
