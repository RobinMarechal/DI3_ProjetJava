package controllers;

import javafx.scene.control.TextField;
import lib.BaseController;
import lib.time.SimpleDate;
import lib.util.validator.Validator;
import lib.views.CSSClasses;
import lib.views.Tabs;
import lib.views.Template;
import models.Boss;
import models.Company;
import models.ManagementDepartment;
import org.intellij.lang.annotations.Language;
import views.CompanyViewController;
import views.company.HomeCompany;
import views.dialogs.EditCompanyDialog;

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

    public boolean updateCompany (TextField name, TextField bossFName, TextField bossLName, TextField mDepName,
                               TextField mDepSector)
    {
        @Language ("RegExp") String companyRegex = "^[A-Za-z-.[ ]0-9]+$";
        @Language ("RegExp") String bossRegexp = "^([A-Z]?[a-z]+)(([ -][A-Z]?[a-z]+))*$";
        @Language ("RegExp") String depRegexp = "^[A-Za-z][A-Za-z0-9- ]*$";

        String strName = null;
        String strBossFName  = null;
        String strBossLName  = null;
        String strMDepName  = null;
        String strMDepSector  = null;

        if (Validator.make(name, companyRegex))
        {
            strName = name.getText().trim();
            name.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            name.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        if (Validator.make(bossFName, bossRegexp))
        {
            strBossFName = bossFName.getText().trim();
            bossFName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            bossFName.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        if (Validator.make(bossLName, bossRegexp))
        {
            strBossLName = bossLName.getText().trim();
            bossLName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            bossLName.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        if (Validator.make(mDepName, depRegexp))
        {
            strMDepName = mDepName.getText().trim();
            mDepName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            mDepName.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        if (Validator.make(mDepSector, depRegexp))
        {
            strMDepSector = mDepSector.getText().trim();
            mDepSector.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            mDepSector.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }


        if (strName != null && strBossFName != null && strBossLName != null && strMDepName != null && strMDepSector != null)
        {
            Company company = Company.getCompany();
            Boss boss = company.getBoss();
            ManagementDepartment dep = company.getManagementDepartment();

            if (strName != "")
            {
                company.setName(strName);
            }

            if(strBossFName != "")
            {
                boss.setFirstName(strBossFName);
            }

            if (strBossLName != null)
            {
                boss.setLastName(strBossLName);
            }

            if(strMDepName != "")
            {
                dep.setName(strMDepName);
            }

            if(strMDepSector != "")
            {
                dep.setActivitySector(strMDepSector);
            }

            return true;
        }

        return false;
    }
}
