package lib.views;

import controllers.CompanyController;
import controllers.DepartmentsController;
import controllers.EmployeesController;
import lib.BaseController;
import lib.views.custom.components.SlidingLabel;
import models.Company;
import org.jetbrains.annotations.NotNull;
import views.company.HomeCompany;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Created by Robin on 25/04/2017. <br>
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

    /** The frame layout that contains everything */
    private BorderPane frameLayout;
    /** The nav tab layout */
    private VBox tabLabelsLayout;
    /** The top menu bar */
    private MenuBar menuBar;
    /** Nav tab scroll pane */
    private ScrollPane leftScrollPane;
    /** Center content scroll pane */
    private ScrollPane centerScrollPane;

    /**
     * Get the singleton instance
     *
     * @return the singleton instance
     */
    public static Template getInstance ()
    {
        return instance;
    }


    /**
     * Default constructor <br>
     * Build the frame content
     */
    private Template ()
    {
        frameLayout = new BorderPane();
        tabLabelsLayout = new VBox();

        leftScrollPane = new ScrollPane();
        centerScrollPane = new ScrollPane();

        controllers[Tabs.COMPANY.ordinal()] = new CompanyController();
        controllers[Tabs.EMPLOYEES.ordinal()] = new EmployeesController();
        controllers[Tabs.STANDARD_DEPARTMENTS.ordinal()] = new DepartmentsController();

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

        // Addition of the menubar
        Pane menuBarPane = new Pane();
        menuBarPane.getChildren().add(menuBar);
        frameLayout.setTop(menuBarPane);

        // Displaying left and center contents
        frameLayout.setLeft(leftScrollPane);
        frameLayout.setCenter(centerScrollPane);

        // We load the company home view
        setView(Tabs.COMPANY, new HomeCompany(Company.getCompany(), SimpleDate.TODAY));

        // Then we create the scene with the frame layout and we add the stylesheet to it
        frameLayout.getStyleClass().add("bg-transparent");
        scene = new Scene(frameLayout);
        scene.getStylesheets().add("src/lib/views/style.css");
    }

    /**
     * Initialization of the left navigation bar
     */
    private void initNavBar ()
    {
        Tabs enumValues[] = {
                Tabs.COMPANY,
                Tabs.EMPLOYEES,
                Tabs.STANDARD_DEPARTMENTS
        };

        // foreach tab
        for (Tabs tabEnumValue : enumValues)
        {
            int    index = tabEnumValue.ordinal();
            String str   = tabEnumValue.toString();

            // The nav bar is composed by SlidingLabel
            navTabs[index] = new SlidingLabel(str);

            // We set the shapes and the positions
            navTabs[index].setInitialPadding(new Insets(10, 0, 10, 20));
            navTabs[index].setAdditionalPadding(new Insets(0, 0, 0, 10));
            navTabs[index].prepareAnimation();
            navTabs[index].setMaxWidth(Double.MAX_VALUE);

            // Preparation of the click evet
            navTabs[index].setOnMouseClicked(e ->
            {
                loadHomeView(tabEnumValue);
                selectedTab = navTabs[index];
            });
        }

        // Then we add all navtabs, with a css class
        tabLabelsLayout.getChildren().addAll(navTabs);
        tabLabelsLayout.getStyleClass().add("navbar");
        tabLabelsLayout.setPrefWidth(LEFT_WIDTH);
    }

    /**
     * Initialization of the top menu bar
     */
    private void initMenuBar ()
    {
        // Creation of the menu items
        // Add...
        MenuItem itemAddEmployee   = new MenuItem("Employee");
        MenuItem itemAddDepartment = new MenuItem("Department");

        // Export...
        MenuItem itemExportEmployeesToCSV   = new MenuItem("Employees & checks");
        MenuItem itemExportDepartmentsToCSV = new MenuItem("Departments");

        // Import...
        MenuItem itemImportEmployeesCSV   = new MenuItem("Employees & checks");
        MenuItem itemImportDepartmentsCSV = new MenuItem("Departments");


        // Preparation of the events
        itemAddEmployee.setOnAction(event -> new EmployeesController().openCreationEmployeeDialog());
        itemAddDepartment.setOnAction(event -> new DepartmentsController().openCreationDepartmentDialog());

        itemExportEmployeesToCSV.setOnAction(event -> new CompanyController().exportEmployeesCSV());
        itemExportDepartmentsToCSV.setOnAction(event -> new CompanyController().exportDepartmentsCSV());

        itemImportEmployeesCSV.setOnAction(event -> new CompanyController().importEmployeesCSV());
        itemImportDepartmentsCSV.setOnAction(event -> new CompanyController().importDepartmentsCSV());

        // Creation of the menus
        Menu menuAdd = new Menu("Add...");
        menuAdd.getItems().addAll(itemAddEmployee, itemAddDepartment);

        Menu menuExport = new Menu("Export...");
        menuExport.getItems().addAll(itemExportEmployeesToCSV, itemExportDepartmentsToCSV);

        Menu menuImport = new Menu("Import...");
        menuImport.getItems().addAll(itemImportEmployeesCSV, itemImportDepartmentsCSV);

        // We finally add all menus to the menubar
        menuBar = new MenuBar(menuAdd, menuExport, menuImport);
    }

    /**
     * Load the home view for a given tab
     *
     * @param wanted the wanted content
     */
    private void loadHomeView (@NotNull Tabs wanted)
    {
        int index = wanted.ordinal();
        if (index < controllers.length && index >= 0)
        {
            controllers[index].home();
        }
    }

    /**
     * Get the scene
     *
     * @return the scene
     */
    public Scene getScene ()
    {
        return scene;
    }

    /**
     * Change the actual view
     *
     * @param enumTab The navigation bar tab that is concerned
     * @param view    The view to display
     */
    public void setView (@NotNull Tabs enumTab, @NotNull BaseViewController view)
    {
        Pane pane = view.getPane();

        ObservableList<Node> children = tabLabelsLayout.getChildren();

        // We remove the CSS class "selected" for every nav tab which has it
        children.stream()
                .filter(n -> n.getStyleClass().contains("selected"))
                .forEach(n -> n.getStyleClass().remove("selected"));

        // We add the CSS class "selected" to the new selected nav tab
        children.get(enumTab.ordinal()).getStyleClass().add("selected");

        pane.setPrefHeight(Template.STAGE_HEIGHT);
        pane.setPrefWidth(Template.CENTER_WIDTH);

        // Then we change the content
        centerScrollPane.setContent(pane);
    }

    /**
     * Get the selected tab
     *
     * @return the selected tab
     */
    public Node getSelectedTab ()
    {
        return selectedTab;
    }
}
