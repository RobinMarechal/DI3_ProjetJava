package controllers;

import lib.BaseController;
import lib.time.SimpleDate;
import lib.views.Tabs;
import lib.views.Template;
import models.Company;
import views.CompanyViewController;
import views.company.HomeCompany;

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
}
