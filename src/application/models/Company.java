package application.models;

import application.lib.csv.CSVLine;
import application.lib.csv.CSVParser;
import application.lib.csv.interfaces.CSVSaver;
import application.lib.json.JsonLoader;
import application.lib.json.Jsonable;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleDateTime;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Robin on 27/03/2017. <br/>
 * Represents the Company. <br/>
 * Singleton
 */
public class Company implements Jsonable, JsonLoader, Serializable, CSVSaver
{

    /** Serialization version UID */
    private static final long serialVersionUID = 1697406778784975282L;

    /** Serialization file path */
    private String serializationFilePath;

    /** JSON key for company object */
    private static final String JSON_KEY_COMPANY = "company";
    /** JSON key for boss object */
    private static final String JSON_KEY_BOSS = "boss";
    /** JSON key for management department object */
    private static final String JSON_KEY_MANAGEMENT_DEPARTMENT = "managementDepartment";
    /** JSON key for employees array */
    private static final String JSON_KEY_EMPLOYEES = "employees";
    /** JSON key for managers array */
    private static final String JSON_KEY_MANAGERS = "managers";
    /** JSON key for standard departments array */
    private static final String JSON_KEY_STANDARD_DEPARTMENTS = "standardDepartments";
    /** JSON key for company name value */
    private static final String JSON_KEY_COMPANY_NAME = "name";


    /** The instance of the class Company */
    private static Company companyInstance = new Company();

    /** The name of the Company */
    private StringProperty name = new SimpleStringProperty(this, "name", "Best company ever!");

    /** The number pf checks in per day */
    private ObservableMap<SimpleDate, Integer> totalChecksInPerDay = FXCollections.observableHashMap();

    /** The number of checks out per day */
    private ObservableMap<SimpleDate, Integer> totalChecksOutPerDay = FXCollections.observableHashMap();

    /** A list containing every created (and not removed) standard departments */
    private ObservableList<StandardDepartment> departments = FXCollections.observableArrayList();

    /**
     * A list containing every created (and not fired) employees <br/>
     * It's sorted in ascending order of the emloyees' id
     */
    private ObservableList<Employee> employees = FXCollections.observableArrayList();


    /** The Company's boss */
    private Boss bossInstance = Boss.getBoss();

    /** The Company's management department */
    private ManagementDepartment managementDepartmentInstance = ManagementDepartment.getManagementDepartment();

    /**
     * Default constructor
     */
    private Company ()
    {
        try
        {
            serializationFilePath = new File(".").getCanonicalPath() + "/data/serialized/company.ser";
        }
        catch (IOException e)
        {
            serializationFilePath = null;
        }
    }

    /**
     * Retrieve the Company instance
     *
     * @return the Company instance
     */
    public static Company getCompany ()
    {
        return companyInstance;
    }

    /**
     * Retrieve the Company's Boss instance
     *
     * @return the Company's Boss instance
     */
    public Boss getBoss ()
    {
        return bossInstance;
    }

    /**
     * Retrieve the Company's management department instance
     *
     * @return the Company's management department instance
     */
    public ManagementDepartment getManagementDepartment ()
    {
        return managementDepartmentInstance;
    }

    /**
     * Retrieve the name of the Company
     *
     * @return
     */
    public String getName ()
    {
        return name.getValueSafe();
    }

    /**
     * Get the name property which can be used for bindings
     *
     * @return the name property
     */
    public StringProperty nameProperty ()
    {
        return name;
    }

    /**
     * Modifies the name of the Company
     *
     * @param name the new name
     * @return this
     */
    public Company setName (String name)
    {
        this.name.setValue(name);
        return this;
    }

    /**
     * Adds an employee to the Company
     *
     * @param employee the employee to add
     * @return this
     */
    public Company addEmployee (Employee employee)
    {
        if (employee != null && !employees.contains(employee))
        {
            int     id    = employee.getId();
            boolean added = false;
            for (int i = 0; i < employees.size() && !added; i++)
            {
                if (id < employees.get(i).getId())
                {
                    employees.add(i, employee);
                    added = true;
                }
            }

            if (!added)
            {
                employees.add(employee);
            }
        }

        return this;
    }

    /**
     * fires an employee from the Company
     *
     * @param employee the employee to fire
     * @return this
     */
    public Company fire (Employee employee)
    {
        removeEmployee(employee);
        return this;
    }

    /**
     * fires a manager from the Company
     *
     * @param manager the manager to fire
     * @return this
     */
    public Company fire (Manager manager)
    {
        removeEmployee(manager);
        return this;
    }

    /**
     * Removes a manager from the Company
     *
     * @param manager the manager to remove
     * @return this
     */
    public Company removeManager (Manager manager)
    {
        removeEmployee(manager);
        return this;
    }

    /**
     * Removes a manager from the Company
     *
     * @param manager the manager the remove
     * @return this
     */
    public Company removeEmployee (Manager manager)
    {
        this.managementDepartmentInstance.removeManager(manager);

        return this;
    }

    /**
     * Removes an employee from the Company
     *
     * @param employee the employee the remove
     * @return this
     */
    public Company removeEmployee (Employee employee)
    {
        if (employee != null && employees.contains(employee))
        {
            employees.remove(employee);

            // remove this employee from its department
            // we don't use employee.fire() to avoid the recursion
            StandardDepartment dep = employee.getDepartment();
            if (dep != null)
            {
                dep.removeEmployee(employee);
                employee.setDepartment(null);
            }
        }

        return this;
    }

    /**
     * Creates an {@link Employee} instance and adds it to the Company
     *
     * @param firstName the employee's first-name
     * @param lastName  the employee's last-name
     * @return the new employee
     */
    public static Employee createEmployee (String firstName, String lastName)
    {
        return new Employee(firstName, lastName);
    }

    /**
     * Creates an {@link Employee} instance and adds it to the Company
     *
     * @param firstName the employee's first-name
     * @param lastName  the employee's last-name
     * @param id        the id to give to that new employee
     * @return the new employee
     * @throws Exception if there already is an employee with this ID
     */
    public static Employee createEmployee (String firstName, String lastName, int id) throws Exception
    {
        return new Employee(firstName, lastName, id);
    }

    /**
     * Creates a {@link Manager} instance and adds it to the Company
     *
     * @param firstName the manager's first-name
     * @param lastName  the manager's last-name
     * @return the new manager
     */
    public static Manager createManager (String firstName, String lastName)
    {
        return new Manager(firstName, lastName);
    }

    /**
     * Creates a {@link Manager} instance and adds it to the Company
     *
     * @param firstName the manager's first-name
     * @param lastName  the manager's last-name
     * @param id        the id to give to that new manager
     * @return the new manager
     * @throws Exception if there already is an employee with this ID
     */
    public static Manager createManager (String firstName, String lastName, int id) throws Exception
    {
        return new Manager(firstName, lastName, id);
    }

    /**
     * Retrieve the {@link Employee} instance with a specific ID
     *
     * @param id the id of the employee to retrieve
     * @return the employee with this id or null if no employee found
     */
    public Employee getEmployee (int id)
    {
        if (id < 0)
        {
            return null;
        }

        for (Employee e : employees)
        {
            if (e.getId() == id)
            {
                return e;
            }
        }

        return null;
    }

    /**
     * Get a list containing all the employees
     *
     * @return a list containing all employees
     */
    public ObservableList<Employee> getEmployeesList ()
    {
        return this.employees;
    }

    /**
     * Get a list containing all the standard departments
     *
     * @return a list containing all standard departments
     */
    public ObservableList<StandardDepartment> getStandardDepartmentsList ()
    {
        return this.departments;
    }

    /**
     * Get a list containing all the managers
     *
     * @return a list containing all managers
     */
    public ObservableList<Manager> getManagersList ()
    {
        return this.managementDepartmentInstance.getManagersList();
    }


    /**
     * Retrieve the total number of {@link Employee} instances
     *
     * @return the total number of {@link Employee} instances
     */
    public int getNbEmployees ()
    {
        return employees.size();
    }

    /**
     * Retrieve the {@link StandardDepartment} instance with a specific ID
     *
     * @param id the id of the standard department to retrieve
     * @return the standard department with this id or null if no department found
     */
    public StandardDepartment getStandardDepartment (int id)
    {
        if (id < 0)
        {
            return null;
        }

        for (StandardDepartment dep : departments)
        {
            if (dep.getId() == id)
            {
                return dep;
            }
        }

        return null;
    }

    /**
     * Retrieve the total number of {@link StandardDepartment} instances
     *
     * @return the total number of {@link StandardDepartment} instances
     */
    public int getNbStandardDepartments ()
    {
        return departments.size();
    }

    /**
     * Adds a {@link StandardDepartment} instance to the Company
     *
     * @param department the {@link StandardDepartment} instance to add
     * @return this
     */
    public Company addStandardDepartment (StandardDepartment department)
    {
        departments.add(department);
        return this;
    }

    /**
     * Creates an {@link StandardDepartment} instance and adds it to the Company
     *
     * @param name           the department's name
     * @param activitySector the department's activity sector
     * @return the new standard department
     */
    public static StandardDepartment createStandardDepartment (String name, String activitySector)
    {
        StandardDepartment department = new StandardDepartment(name, activitySector);
        return department;
    }

    /**
     * Creates an {@link StandardDepartment} instance and adds it to the Company
     *
     * @param name           the department's name
     * @param activitySector the department's activity sector
     * @param manager        the manager who has to manage this department
     * @return the new standard department
     */
    public static StandardDepartment createStandardDepartment (String name, String activitySector, Manager manager)
    {
        StandardDepartment dep = createStandardDepartment(name, activitySector);
        dep.setManager(manager);
        return dep;
    }

    /**
     * Removes a standard department from the Company
     *
     * @param department the department to remove
     * @return this
     */
    public Company removeStandardDepartment (StandardDepartment department)
    {
        if (department != null && departments.contains(department))
        {
            departments.remove(department);
        }

        return this;
    }

    /**
     * Search an employee by first-name and last-name <br/>
     * an employee is found if it's firstName attribute contains the firstName parameter
     * AND it's lastName attribute contains the lastName parameter <br/>
     * To search only by first-name, use it like this : <code>searchEmployee("john", "");</code> <br/>
     * To search only by last-name only, use it like this : <code>searchEmployee("", "doe");</code>
     *
     * @param firstName
     * @param lastName
     * @return a list of {@link Employee} instances matching with the parameters
     */
    public ObservableList<Employee> searchEmployee (String firstName, String lastName)
    {
        final String searchFnLower = firstName.toLowerCase();
        final String searchLnLower = lastName.toLowerCase();

        ObservableList<Employee> list = employees.stream().filter(e ->
        {
            final String empFnLower = e.getFirstName().toLowerCase();
            final String empLnLower = e.getLastName().toLowerCase();

            return empFnLower.contains(searchFnLower) && empLnLower.contains(searchLnLower);
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));

        return list;
    }

    /**
     * Search an employee <br/>
     * an employee is found if it's firstName attribute contains the firstName parameter
     * AND it's lastName attribute contains the lastName parameter <br/>
     * To search only by first-name, use it like this : <code>searchEmployee("john", "");</code> <br/>
     * To search only by last-name only, use it like this : <code>searchEmployee("", "doe");</code>
     *
     * @param name a part of the name of the wanted employee
     * @return a list of {@link Employee} instances found
     */
    public ObservableList<Employee> searchEmployee (String name)
    {
        final String searchLower = name.toLowerCase();

        ObservableList<Employee> list = employees.stream().filter(e ->
        {
            final String empFnLower = e.getFirstName().toLowerCase();
            final String empLnLower = e.getLastName().toLowerCase();

            return empFnLower.contains(searchLower) || empLnLower.contains(searchLower) || (empFnLower + " " + empLnLower).contains
                    (searchLower);
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));

        return list;
    }

    /**
     * Search a standard department by name and activity sector <br/>
     * To search only by name, use it like this : <code>searchStandardDepartment("name", "");</code> <br/>
     * To search only by lastName only, use it like this : <code>searchStandardDepartment("", "activity sector");</code>
     *
     * @param name
     * @param activitySector
     * @return a list of {@link StandardDepartment} instances matching with the parameters
     */
    public ObservableList<StandardDepartment> searchStandardDepartment (String name, String activitySector)
    {
        name = name.toLowerCase();
        activitySector = activitySector.toLowerCase();
        ObservableList<StandardDepartment> list = FXCollections.observableArrayList();

        for (StandardDepartment e : departments)
        {
            String tmpName   = e.getName().toLowerCase();
            String tmpSector = e.getActivitySector().toLowerCase();
            if (tmpName.contains(name) && tmpSector.contains(activitySector))
            {
                list.add(e);
            }
        }

        return list;
    }

    /**
     * Retrieve the total number of checks in.
     *
     * @return the number of checks in
     */
    public int getTotalChecksIn ()
    {
        int count = 0;
        for (Map.Entry<SimpleDate, Integer> entry : totalChecksInPerDay.entrySet())
        {
            count += entry.getValue();
        }
        return count;
    }

    /**
     * Retrieve the total number of checks out.
     *
     * @return the number of checks out
     */
    public int getTotalChecksOut ()
    {
        int count = 0;
        for (Map.Entry<SimpleDate, Integer> entry : totalChecksOutPerDay.entrySet())
        {
            count += entry.getValue();
        }
        return count;
    }

    /**
     * Retrieve the total number of checks (in and out)
     *
     * @return the number of checks
     */
    public int getTotalChecks ()
    {
        return getTotalChecksIn() + getTotalChecksOut();
    }

    /**
     * Retrieve the number of checks in at a specific date
     *
     * @param date The date
     * @return the number of checks in at the date
     */
    public int getTotalChecksInAt (SimpleDate date)
    {
        return totalChecksInPerDay.containsKey(date) ? totalChecksInPerDay.get(date) : 0;
    }

    /**
     * Retrieve the number of checks out at a specific date
     *
     * @param date The date
     * @return the number of checks out at the date
     */
    public int getTotalChecksOutAt (SimpleDate date)
    {
        return totalChecksOutPerDay.containsKey(date) ? totalChecksOutPerDay.get(date) : 0;
    }

    /**
     * Retrieve the number of checks (in + out) at a specific date
     *
     * @param date The date
     * @return the number of checks (in + out) at the date
     */
    public int getTotalChecksAt (SimpleDate date)
    {
        return getTotalChecksInAt(date) + getTotalChecksOutAt(date);
    }

    /**
     * Get the {@link ObservableMap} that contains the number of checks in per day
     *
     * @return the {@link ObservableMap} that contains the number of checks in per day
     */
    public ObservableMap<SimpleDate, Integer> getTotalChecksInPerDay ()
    {
        return totalChecksInPerDay;
    }

    /**
     * Get the {@link ObservableMap} that contains the number of checks out per day
     *
     * @return the {@link ObservableMap} that contains the number of checks out per day
     */
    public ObservableMap<SimpleDate, Integer> getTotalChecksOutPerDay ()
    {
        return totalChecksOutPerDay;
    }

    /**
     * Creates a String representing the Company with a list of all employees
     *
     * @return the String created
     */
    public String employeesToString ()
    {
        String str = "There are/is " + employees.size() + " employee(s) : " + System.lineSeparator() + "  ";

        for (Employee e : employees)
        {
            str += "\t - " + e + "," + System.lineSeparator();
        }

        str = str.substring(0, str.length() - 2);

        return str;
    }

    /**
     * Creates a String representing the Company <br/>
     * Ex: 'Company: [name of the company]'
     *
     * @return the String created
     */
    @Override
    public String toString ()
    {
        return "Company: " + name.getValueSafe();
    }

    /**
     * Creates an instance of {@link JSONObject} containing the entire company's data.
     *
     * @return the json object containing the entire company's data.
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json = new JSONObject();

        JSONObject company = new JSONObject();
        company.put(JSON_KEY_COMPANY_NAME, name.getValueSafe());

        json.put(JSON_KEY_COMPANY, company);

        // ----------------- Boss -----------------
        JSONObject boss = bossInstance.toJson();
        json.put(JSON_KEY_BOSS, boss);

        // ----------------- Management department -----------------
        JSONObject manDep = managementDepartmentInstance.toJson();
        json.put(JSON_KEY_MANAGEMENT_DEPARTMENT, manDep);

        // ----------------- Employees -----------------
        JSONArray empArray = employees.stream().map(Employee::toJson).collect(Collectors.toCollection(JSONArray::new));
        json.put(JSON_KEY_EMPLOYEES, empArray);

        // ----------------- Standard Departments -----------------
        JSONArray depArray = departments.stream().map(StandardDepartment::toJson).collect(Collectors.toCollection(JSONArray::new));
        json.put(JSON_KEY_STANDARD_DEPARTMENTS, depArray);

        return json;
    }

    /**
     * Load Employee and Manager instances from json files
     */
    private void loadEmployees (JSONArray json)
    {
        if (json == null)
        {
            System.out.println("Failed to load employees from JSON...");
            return;
        }

        for (Object obj : json)
        {
            try
            {
                Employee.loadFromJson((JSONObject) obj);
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Created StandardDepartment instances from json files
     */
    private void loadStandardDepartments (JSONArray json)
    {
        if (json == null)
        {
            System.out.println("Failed to load standard departments from JSON...");
            return;
        }

        for (Object obj : json)
        {
            StandardDepartment.loadFromJson((JSONObject) obj);
        }
    }

    /**
     * load Company's data from a json file
     */
    private void loadCompany (JSONObject json)
    {
        if (json == null)
        {
            System.out.println("Failed to load Boss from JSON...");
            return;
        }

        name.setValue(json.getOrDefault(JSON_KEY_COMPANY_NAME, "").toString());
    }

    /**
     * load Boss' data from a json file
     */
    private void loadBoss (JSONObject json)
    {
        if (json == null)
        {
            System.out.println("Failed to load Boss from JSON...");
            return;
        }

        Boss.loadFromJson(json);
    }

    /**
     * load ManagementDepartment's data from a json file
     */
    private void loadManagementDepartment (JSONObject json)
    {
        if (json == null)
        {
            System.out.println("Failed to load Management Department from JSON...");
            return;
        }

        ManagementDepartment.loadFromJson(json);
    }

    /**
     * Load all data from json files.
     */
    @Override
    public void load (JSONObject json)
    {
        loadCompany((JSONObject) json.get(JSON_KEY_COMPANY));
        loadBoss((JSONObject) json.get(JSON_KEY_BOSS));
        loadManagementDepartment((JSONObject) json.get(JSON_KEY_MANAGEMENT_DEPARTMENT));
        loadEmployees((JSONArray) json.get(JSON_KEY_EMPLOYEES));
        loadStandardDepartments((JSONArray) json.get(JSON_KEY_STANDARD_DEPARTMENTS));
    }

    /**
     * Load the company's data (including employees, departments etc...) from a serialization file
     */
    public void deserialize ()
    {
        File f = new File(serializationFilePath);
        if (f.exists())
        {
            try
            {
                JSONParser        parser     = new JSONParser();
                FileInputStream   fileIn     = new FileInputStream(f);
                ObjectInputStream in         = new ObjectInputStream(fileIn);
                String            jsonString = in.readUTF();
                JSONObject        json       = (JSONObject) parser.parse(jsonString);
                in.close();
                fileIn.close();
                load(json);
            }
            catch (Exception e)
            {
                System.out.println("Loading json from file failed...");
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Loading json from file failed: file '" + serializationFilePath + "' not found.");
        }
    }

    /**
     * Serialize the company's data (including employees, departments etc...) into a file
     */
    public void serialiaze ()
    {
        try
        {
            // We create the folders if they doesn't exist yet
            String dirPath = serializationFilePath.substring(0, serializationFilePath.lastIndexOf("/"));
            new File(dirPath).mkdirs();

            File               f       = new File(serializationFilePath);
            FileOutputStream   fileOut = new FileOutputStream(f);
            ObjectOutputStream out     = new ObjectOutputStream(fileOut);

            JSONObject json = toJson();

            out.writeUTF(json.toJSONString());
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + serializationFilePath);
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    /**
     * Increment the number of checks out at a specific date <br/>
     * This should only be used by {@link CheckInOut} when a check out is performed.
     *
     * @param date the date of the check out
     */
    void incrementChecksOutAt (SimpleDate date)
    {
        int total = 0;
        if (totalChecksOutPerDay.containsKey(date))
        {
            total = totalChecksOutPerDay.get(date);
        }

        totalChecksOutPerDay.put(date, total + 1);
    }

    /**
     * Increment the number of checks in at a specific date <br/>
     * This should only be used by {@link CheckInOut} when a check in is performed.
     *
     * @param date the date of the check in
     */
    void incrementChecksInAt (SimpleDate date)
    {
        int total = 0;
        if (totalChecksInPerDay.containsKey(date))
        {
            total = totalChecksInPerDay.get(date);
        }

        totalChecksInPerDay.put(date, total + 1);
    }

    /**
     * Decrement the number of checks out at a specific date <br/>
     * This should only be called when a {@link CheckInOut} is deleted, <br/>
     * which should only happen when an employee is fired
     *
     * @param date the date of the check out
     */
    void decrementChecksOutAt (SimpleDate date)
    {
        int total = 0;
        if (totalChecksOutPerDay.containsKey(date))
        {
            total = totalChecksOutPerDay.get(date);
        }

        totalChecksOutPerDay.put(date, total - 1);
    }

    /**
     * Decrement the number of checks in at a specific date <br/>
     * This should only be called when a {@link CheckInOut} is deleted, <br/>
     * which should only happen when an employee is fired
     *
     * @param date the date of the check in
     */
    void decrementChecksInAt (SimpleDate date)
    {
        int total = 0;
        if (totalChecksInPerDay.containsKey(date))
        {
            total = totalChecksInPerDay.get(date);
        }

        totalChecksInPerDay.put(date, total - 1);
    }

    /**
     * Get the list of the employees who have not any department
     * @return the list of the employees who have not any department
     */
    public ObservableList<Employee> getEmployeesWithoutDepartment ()
    {
        ObservableList<Employee> list = employees.stream()
                                                 .filter(employee -> employee.getDepartment() == null)
                                                 .collect(Collectors.toCollection(FXCollections::observableArrayList));

        employees.addListener(new ListChangeListener<Employee>()
        {
            @Override
            public void onChanged (ListChangeListener.Change<? extends Employee> c)
            {
                while (c.next())
                {
                    list.clear();
                    list.addAll(employees.stream()
                                         .filter(employee -> employee.getDepartment() == null)
                                         .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                }
            }
        });

        return list;
    }

    /**
     * Save the list of employees into a CSV file
     * @param file the destination file
     */
    public void saveEmployeesToCSV (File file)
    {
        CSVParser parser         = new CSVParser();
        CSVLine   headerEmployee = new CSVLine();
        CSVLine   headerCheck    = new CSVLine();
        headerEmployee.add(CSVParser.HEADER_INDICATOR + "ID", "FIRSTNAME", "LASTNAME", "IS-MANAGER", "STARTING-HOUR", "ENDING-HOUR",
                "OVERTIME");
        headerCheck.add(CSVParser.HEADER_INDICATOR + "DATE", "ARRIVED-AT", "LEFT-AT");

        parser.addLine(headerEmployee);
        parser.addLine(headerCheck);

        if (!employees.isEmpty())
        {
            employees.get(0).buildCSV(parser);
            for (int i = 1; i < employees.size(); i++)
            {
                parser.addLine("-");
                employees.get(i).buildCSV(parser);
            }
        }

        saveCSVToFile(file, parser);
    }

    /**
     * Save the list of departments into a CSV file
     * @param file the destination file
     */
    public void saveDepartmentsToCSV (File file)
    {
        CSVParser parser           = new CSVParser();
        CSVLine   headerDepartment = new CSVLine();
        CSVLine   headerEmployees  = new CSVLine();
        headerDepartment.add(CSVParser.HEADER_INDICATOR + "ID", "NAME", "ACTIVITY-SECTOR", "MANAGER-ID");
        headerEmployees.add(CSVParser.HEADER_INDICATOR + "EMPLOYEE1", "EMPLOYEE2", "EMPLOYEE3", "...");

        parser.addLine(headerDepartment);
        parser.addLine(headerEmployees);

        if (!departments.isEmpty())
        {
            departments.get(0).buildCSV(parser);
            for (int i = 1; i < departments.size(); i++)
            {
                parser.addLine(CSVParser.SECTION_SEPARATOR + "");
                departments.get(i).buildCSV(parser);
            }
        }

        saveCSVToFile(file, parser);
    }

    /**
     * Load employees and their checks from a CSV file
     * @param file the source file
     * @throws FileNotFoundException if the file has not been found
     */
    public void loadEmployeesFromCSVFile (File file) throws FileNotFoundException
    {
        final CSVParser parser = CSVParser.loadFromFile(file);

        Employee employee = null;

        while (parser.hasNext())
        {
            CSVLine line = parser.next();
            if (line.charAt(0) == CSVParser.HEADER_INDICATOR || line.charAt(0) == CSVParser.SECTION_SEPARATOR)
            {
                employee = null;
                continue;
            }

            if (employee == null)
            {
                // Creation of an employee

                // We try to get the already existing employee with this id
                int id = Integer.parseInt(line.get(0));
                employee = getEmployee(id);

                // If not employee exists with this ID
                if (employee == null)
                {
                    // If there are not all information
                    if (line.size() < 6)
                    {
                        continue;
                    }

                    String firstName = line.get(1);
                    String lastName  = line.get(2);
                    boolean isManager = Boolean.valueOf(line.get(3));
                    String shStr = line.get(4);
                    String ehStr = line.get(5);

                    // if time has format H:mm, we add a 0 to transform it to HH:mm format
                    if (shStr.charAt(1) == ':')
                    {
                        shStr = "0" + shStr;
                    }

                    // if time has format H:mm, we add a 0 to transform it to HH:mm format
                    if (ehStr.charAt(1) == ':')
                    {
                        ehStr = "0" + ehStr;
                    }

                    SimpleTime sh = SimpleTime.fromLocalTime(LocalTime.parse(shStr, DateTimeFormatter.ofPattern("HH:mm")));
                    SimpleTime eh = SimpleTime.fromLocalTime(LocalTime.parse(ehStr, DateTimeFormatter.ofPattern("HH:mm")));

                    if (id < 0)
                    {
                        // Invalid id, we create a new Employee without specified ID
                        employee = new Employee(firstName, lastName);
                    }
                    else
                    {
                        // valid id, we create a new Employee with the specified ID
                        employee = new Employee(firstName, lastName, id);
                    }

                    // If the employee should be a manager, we upgrade it
                    if (isManager)
                    {
                        employee = employee.upgradeToManager();
                    }

                    employee.setStartingHour(sh);
                    employee.setStartingHour(eh);
                }
            }
            else
            {
                // it's a check int/out line

                if (employee != null)
                {
                    // If all information are not specified
                    if (line.size() < 2)
                    {
                        continue;
                    }

                    SimpleDate date = SimpleDate.fromLocalDate(LocalDate.parse(line.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    String checkInStr = line.get(1);

                    // if time has format H:mm, we add a 0 to transform it to HH:mm format
                    if (checkInStr.charAt(1) == ':')
                    {
                        checkInStr = "0" + checkInStr;
                    }

                    SimpleTime checkIn = SimpleTime.fromLocalTime(LocalTime.parse(checkInStr, DateTimeFormatter.ofPattern("HH:mm")));

                    // We perform the check in
                    employee.doCheck(SimpleDateTime.fromDateAndTime(date, checkIn));

                    SimpleTime checkOut;
                    if (line.size() >= 3)
                    {
                        // A check out is also specified
                        String checkOutStr = line.get(2);

                        // if time has format H:mm, we add a 0 to transform it to HH:mm format
                        if (checkOutStr.charAt(1) == ':')
                        {
                            checkOutStr = "0" + checkOutStr;
                        }

                        checkOut = SimpleTime.fromLocalTime(LocalTime.parse(checkOutStr, DateTimeFormatter.ofPattern("HH:mm")));

                        // We perfom the check out
                        employee.doCheck(SimpleDateTime.fromDateAndTime(date, checkOut));
                    }
                }
            }
        }
    }


    /**
     * Load departments and their employees from a CSV file
     * @param file the source file
     * @throws FileNotFoundException if the file has not been found
     */
    public void loadDepartmentsFromCSVFile (File file) throws FileNotFoundException
    {
        final CSVParser parser = CSVParser.loadFromFile(file);

        StandardDepartment dep = null;

        while (parser.hasNext())
        {
            CSVLine line = parser.next();
            if (line.charAt(0) == CSVParser.HEADER_INDICATOR || line.charAt(0) == CSVParser.SECTION_SEPARATOR)
            {
                dep = null;
                continue;
            }

            if (dep == null)
            {
                // If not all information are specified
                if (line.size() < 4)
                {
                    continue;
                }

                // We try to retrieve a department with this ID
                int id = Integer.parseInt(line.get(0));
                dep = getStandardDepartment(id);

                if (dep == null)
                {
                    // No department found, we create a new one
                    String name   = line.get(1);
                    String sector = line.get(2);
                    int    manId  = Integer.parseInt(line.get(3));

                    if (id < 0)
                    {
                        // ID invalid, we create it with auto increment ID
                        dep = new StandardDepartment(name, sector);
                    }
                    else
                    {
                        // ID valid, we create it with specified ID
                        dep = new StandardDepartment(name, sector, id);
                    }

                    // We add the manager to the departemnt...
                    Employee empMan = getEmployee(manId);
                    if (empMan != null)
                    {
                        //... if he exists
                        Manager manager = empMan.upgradeToManager();
                        dep.setManager(manager);
                    }
                }
            }
            else
            {
                // Its the employees line
                for (String idStr : line)
                {
                    // foreach value, we try to get to Employee with the ID specified
                    int      id = Integer.parseInt(idStr);
                    Employee e  = getEmployee(id);
                    if (e != null)
                    {
                        // If we found one (if he exits), we add it to the department
                        dep.addEmployee(e);
                    }
                }
            }
        }
    }
}
