package application.lib.views;

import application.controllers.CompanyController;
import application.controllers.DepartmentsController;
import application.controllers.EmployeesController;
import application.lib.BaseController;
import application.lib.views.custom.components.SlidingLabel;
import application.models.Company;
import application.views.company.HomeCompany;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Created by Robin on 25/04/2017. <br/>
 * This class is a singleton which represents the template of the principal frame.
 */
public class Template
{
    /** BorderPane's center content width */
    public static final int CENTER_WIDTH = 1100;
    /** BorderPane's left content width */
    public static final int LEFT_WIDTH = 200;
    /** Frame's height */
    public static final int STAGE_HEIGHT = 600;

    /** Singleton template instance */
    private static Template instance = new Template();

    /** The selected tab */
    private Node selectedTab;

    /** The name of the tabs */
    private final String[] tabNames = {
            Tabs.COMPANY.toString(),
            Tabs.EMPLOYEES.toString(),
            Tabs.STANDARD_DEPARTMENTS.toString()
    };

    /** The controllers associate with each tab */
    private final BaseController controllers[] = new BaseController[tabNames.length];

    /** The animated label that are the tabs */
    private final SlidingLabel navTabs[] = new SlidingLabel[tabNames.length];

    /** The scene */
    private Scene scene;


    private HBox contentLayout;
    private VBox principalLayout;
    private VBox tabLabelsLayout;
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
        principalLayout = new VBox();
        contentLayout = new HBox();
        tabLabelsLayout = new VBox();
        leftScrollPane = new ScrollPane();
        centerScrollPane = new ScrollPane();

        controllers[Tabs.COMPANY.ordinal()] = new CompanyController();
        controllers[Tabs.EMPLOYEES.ordinal()] = new EmployeesController();
        controllers[Tabs.STANDARD_DEPARTMENTS.ordinal()] = new DepartmentsController();

        contentLayout.getStyleClass().add("b-principalLayout");
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

        leftScrollPane.setContent(tabLabelsLayout);
        leftScrollPane.setMinWidth(LEFT_WIDTH);
        leftScrollPane.setPrefWidth(LEFT_WIDTH);

        contentLayout.getChildren().addAll(leftScrollPane, centerScrollPane);
        principalLayout.getChildren().addAll(menuBar, contentLayout);

        // We load the company home view
        setView(Tabs.COMPANY, new HomeCompany(Company.getCompany(), SimpleDate.TODAY));

        scene = new Scene(principalLayout);
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

            navTabs[index].setInitialPadding(new Insets(10, 0, 10, 20));
            navTabs[index].setAdditionalPadding(new Insets(0, 0, 0, 10));
            navTabs[index].prepareAnimation();
            navTabs[index].setMaxWidth(Double.MAX_VALUE);

            navTabs[index].setOnMouseClicked(e ->
            {
                loadHomeView(tValue);
                selectedTab = navTabs[index];
            });
        }

        tabLabelsLayout.getChildren().addAll(navTabs);
        tabLabelsLayout.getStyleClass().add("navbar");
        tabLabelsLayout.setPrefWidth(LEFT_WIDTH + 5);
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

        itemExportEmployeesToCSV.setOnAction(event -> new CompanyController().exportEmployeesCSV());
        itemExportDepartmentsToCSV.setOnAction(event -> new CompanyController().exportDepartmentsCSV());

        itemImportEmployeesCSV.setOnAction(event -> new CompanyController().importEmployeesCSV());
        itemImportDepartmentsCSV.setOnAction(event -> new CompanyController().importDepartmentsCSV());

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
        ObservableList<Node> children = tabLabelsLayout.getChildren();

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
