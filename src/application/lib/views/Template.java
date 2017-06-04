package application.lib.views;

import application.controllers.CompanyController;
import application.controllers.DepartmentsController;
import application.controllers.EmployeesController;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import application.lib.BaseController;
import application.lib.views.custom.components.Scene;
import application.lib.views.custom.components.SlidingLabel;
import application.models.Company;
import application.views.company.HomeCompany;

/**
 * Created by Robin on 25/04/2017.
 */
public class Template
{
    public static final int CENTER_WIDTH = 1100;
    public static final int STAGE_HEIGHT = 600;

    public static final int LEFT_WIDTH = 200;

    // singleton
    private static Template instance = new Template();

    private Node selectedTab;

    private final String[] tabNames = {
            Tabs.COMPANY.toString(),
            Tabs.EMPLOYEES.toString(),
            Tabs.STANDARD_DEPARTMENTS.toString()
    };

    private final BaseController controllers[] = new BaseController[tabNames.length];
    private final SlidingLabel navTabs[] = new SlidingLabel[tabNames.length];

    private Scene scene;
    private HBox contentLayout;
    private VBox layout;
    private VBox navBar;
    private MenuBar menuBar;
    private ScrollPane leftScrollPane;
    private ScrollPane centerScrollPane;

    public static Template getInstance ()
    {
        return instance;
    }

    public static void setInstance (Template instance)
    {
        Template.instance = instance;
    }

    private Template ()
    {
        layout = new VBox();
        contentLayout = new HBox();
        navBar = new VBox();
        leftScrollPane = new ScrollPane();
        centerScrollPane = new ScrollPane();

        controllers[Tabs.COMPANY.ordinal()] = new CompanyController();
        controllers[Tabs.EMPLOYEES.ordinal()] = new EmployeesController();
        controllers[Tabs.STANDARD_DEPARTMENTS.ordinal()] = new DepartmentsController();

        contentLayout.getStyleClass().add("b-layout");
        leftScrollPane.getStyleClass().addAll("left-scrollpane");
        centerScrollPane.getStyleClass().addAll("center-scrollpane");

        // We hide the horizontal scrollbar, and we prevent the scroll if it is horizontal
        leftScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        leftScrollPane.addEventFilter(ScrollEvent.SCROLL, event ->
        {
            if (event.getDeltaX() != 0)
            {
                event.consume();
            }
        });

        // Initialization of the menu bar
        initMenuBar();

        // Initialization of the left navigation bar
        initNavBar();

        leftScrollPane.setContent(navBar);
        leftScrollPane.setMinWidth(LEFT_WIDTH);
        leftScrollPane.setPrefWidth(LEFT_WIDTH);

        contentLayout.getChildren().addAll(leftScrollPane, centerScrollPane);
        layout.getChildren().addAll(menuBar, contentLayout);

        // We load the company home view
        setView(Tabs.COMPANY, new HomeCompany(Company.getCompany(), SimpleDate.TODAY));

        scene = new Scene(layout);
        scene.getStylesheets().add("application/lib/views/style.css");
    }

    private void initNavBar ()
    {
        Tabs enumValues[] = {
                Tabs.COMPANY,
                Tabs.EMPLOYEES,
                Tabs.STANDARD_DEPARTMENTS
        };

        for (Tabs tValue : enumValues)
        {
            int index = tValue.ordinal();
            String str = tValue.toString();

            navTabs[index] = new SlidingLabel(str);

            navTabs[index].setBasePadding(new Insets(10, 0, 10, 20));
            navTabs[index].setAdditionalPadding(new Insets(0, 0, 0, 10));
            navTabs[index].prepareAnimation();
            navTabs[index].setMaxWidth(Double.MAX_VALUE);

            navTabs[index].setOnMouseClicked(e ->
            {
                loadHomeView(tValue);
                selectedTab = navTabs[index];
            });
        }

        navBar.getChildren().addAll(navTabs);
        navBar.getStyleClass().add("navbar");
        navBar.setPrefWidth(LEFT_WIDTH + 5);
    }

    private void initMenuBar ()
    {
        MenuItem itemAddEmployee = new MenuItem("Employee");
        MenuItem itemAddDepartment = new MenuItem("Department");

        MenuItem itemExportEmployeesToCSV = new MenuItem("Employees & checks");
        MenuItem itemExportDepartmentsToCSV = new MenuItem("Departments");

        MenuItem itemImportEmployeesCSV = new MenuItem("Employees & checks");
        MenuItem itemImportDepartmentsCSV = new MenuItem("Departments");

        itemAddEmployee.setOnAction(event -> new EmployeesController().openCreationEmployeeDialog());
        itemAddDepartment.setOnAction(event -> new DepartmentsController().openCreationDepartmentDialog());

        itemExportEmployeesToCSV.setOnAction(event -> new CompanyController().exportEmployees());
        itemExportDepartmentsToCSV.setOnAction(event -> new CompanyController().exportDepartments());

        itemImportEmployeesCSV.setOnAction(event -> new CompanyController().openImportEmployeesDialog());
        itemImportDepartmentsCSV.setOnAction(event -> new CompanyController().openImportDepartmentsDialog());

        Menu menuAdd = new Menu("Add...");
        menuAdd.getItems().addAll(itemAddEmployee, itemAddDepartment);

        Menu menuExport = new Menu("Export...");
        menuExport.getItems().addAll(itemExportEmployeesToCSV, itemExportDepartmentsToCSV);

        Menu menuImport = new Menu("Import...");
        menuImport.getItems().addAll(itemImportEmployeesCSV, itemImportDepartmentsCSV);

        menuBar = new MenuBar(menuAdd, menuExport, menuImport);
    }

    private void loadHomeView (Tabs wanted)
    {
        int index = wanted.ordinal();
        if (index < controllers.length && index >= 0)
        {
            controllers[index].home();
            System.out.println(tabNames[index] + " Tab displayed.");
        }
    }

    public Scene getScene ()
    {
        return scene;
    }

    public Template setView (Tabs enumTab, BaseViewController view)
    {
        Pane pane = view.getPane();
        ObservableList<Node> children = navBar.getChildren();

        children.stream()
                .filter(n -> n.getStyleClass().contains("selected"))
                .forEach(n -> n.getStyleClass().remove("selected"));

        children.get(enumTab.ordinal()).getStyleClass().add("selected");

        pane.setPrefHeight(Template.STAGE_HEIGHT);
        pane.setPrefWidth(Template.CENTER_WIDTH);

        centerScrollPane.setContent(pane);

        return this;
    }

    public Node getSelectedTab ()
    {
        return selectedTab;
    }
}
