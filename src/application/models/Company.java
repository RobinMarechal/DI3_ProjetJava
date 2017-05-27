package application.models;

import fr.etu.univtours.marechal.SimpleDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import application.lib.json.JsonLoader;
import application.lib.json.Jsonable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Robin on 27/03/2017. <br/>
 * Represents the Company. <br/>
 * Singleton
 */
public class Company implements Jsonable, JsonLoader, Serializable
{

    private static final String serializationFilename = "data/company.ser";

    /** JSON object name keys */
    private static final String JSON_KEY_COMPANY = "company";
    private static final String JSON_KEY_BOSS = "boss";
    private static final String JSON_KEY_MANAGEMENT_DEPARTMENT = "managementDepartment";
    private static final String JSON_KEY_EMPLOYEES = "employees";
    private static final String JSON_KEY_MANAGERS = "managers";
    private static final String JSON_KEY_STANDARD_DEPARTMENTS = "standardDepartments";

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
    public List<Employee> searchEmployee (String firstName, String lastName)
    {
        final String searchFnLower = firstName.toLowerCase();
        final String searchLnLower = lastName.toLowerCase();

        List<Employee> list = employees.stream().filter(e ->
        {
            final String empFnLower = e.getFirstName().toLowerCase();
            final String empLnLower = e.getLastName().toLowerCase();

            return empFnLower.contains(searchFnLower) && empLnLower.contains(searchLnLower);
        }).collect(Collectors.toList());

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
    public List<Employee> searchEmployee (String name)
    {
        final String searchLower = name.toLowerCase();

        List<Employee> list = employees.stream().filter(e ->
        {
            final String empFnLower = e.getFirstName().toLowerCase();
            final String empLnLower = e.getLastName().toLowerCase();

            return empFnLower.contains(searchLower) || empLnLower.contains(searchLower) || (empFnLower + " " + empLnLower).contains
                    (searchLower);
        }).collect(Collectors.toList());

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
        ObservableList<StandardDepartment> list = FXCollections.observableArrayList();

        for (StandardDepartment e : departments)
        {
            if (e.getName().toLowerCase().contains(name.toLowerCase()) && e.getActivitySector()
                                                                           .toLowerCase()
                                                                           .contains(activitySector.toLowerCase()))
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

    public ObservableMap<SimpleDate, Integer> getTotalChecksInPerDay ()
    {
        return totalChecksInPerDay;
    }

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

        // ----------------- Managers -----------------
        //        JSONArray manArray = managementDepartmentInstance.getManagersList()
        //                                                         .stream()
        //                                                         .map(Manager::toJson)
        //                                                         .collect(Collectors.toCollection(JSONArray::new));
        //        json.put(JSON_KEY_MANAGERS, manArray);

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

    public void deserialize ()
    {
        File f = new File(serializationFilename);
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
            System.out.println("Loading json from file failed: file '" + serializationFilename + "' not found.");
        }
    }

    public void serialiaze ()
    {
        try
        {
            File               f       = new File(serializationFilename);
            FileOutputStream   fileOut = new FileOutputStream(f);
            ObjectOutputStream out     = new ObjectOutputStream(fileOut);

            JSONObject json = toJson();

            out.writeUTF(json.toJSONString());
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + serializationFilename);
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    //    private void deserialize ()
    //    {
    //        Company tmp = null;
    //        try
    //        {
    //            File              f      = new File(serializationFilename);
    //            FileInputStream   fileIn = new FileInputStream(f);
    //            ObjectInputStream in     = new ObjectInputStream(fileIn);
    //            tmp = (Company) in.readObject();
    //            in.close();
    //            fileIn.close();
    //
    //            name = tmp.name;
    //            employees = tmp.employees;
    //            departments = tmp.departments;
    //            totalChecksOutPerDay = tmp.totalChecksOutPerDay;
    //            totalChecksInPerDay = tmp.totalChecksInPerDay;
    //
    //            int maxId = -1;
    //            for (Employee e : employees)
    //            {
    //                int id = e.getId();
    //                if (id > maxId)
    //                {
    //                    maxId = id;
    //                }
    //            }
    //
    //            maxId = Collections.max(employees.stream().map(Employee::getId).collect(Collectors.toList()));
    //
    //            Employee.setNextId(maxId + 1);
    //
    //            maxId = -1;
    //            for (StandardDepartment d : departments)
    //            {
    //                int id = d.getId();
    //                if (id > maxId)
    //                {
    //                    maxId = id;
    //                }
    //            }
    //
    //            StandardDepartment.setNextId(maxId + 1);
    //
    //            bossInstance.loadFromDeserialization(tmp.bossInstance);
    //            bossInstance = Boss.getBoss();
    //
    //            managementDepartmentInstance.loadFromDeserialization(tmp.managementDepartmentInstance);
    //            managementDepartmentInstance = ManagementDepartment.getManagementDepartment();
    //        }
    //        catch (Exception e)
    //        {
    //            System.out.println("Deserialization failed...");
    //            System.err.println(e.getMessage());
    //        }
    //    }

    public void incrementChecksOutAt (SimpleDate date)
    {
        int total = 0;
        if (totalChecksOutPerDay.containsKey(date))
        {
            total = totalChecksOutPerDay.get(date);
        }

        totalChecksOutPerDay.put(date, total + 1);
    }

    public void incrementChecksInAt (SimpleDate date)
    {
        int total = 0;
        if (totalChecksInPerDay.containsKey(date))
        {
            total = totalChecksInPerDay.get(date);
        }

        totalChecksInPerDay.put(date, total + 1);
    }

    public void decrementChecksOutAt (SimpleDate date)
    {
        int total = 0;
        if (totalChecksOutPerDay.containsKey(date))
        {
            total = totalChecksOutPerDay.get(date);
        }

        totalChecksOutPerDay.put(date, total - 1);
    }

    public void decrementChecksInAt (SimpleDate date)
    {
        int total = 0;
        if (totalChecksInPerDay.containsKey(date))
        {
            total = totalChecksInPerDay.get(date);
        }

        totalChecksInPerDay.put(date, total - 1);
    }

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
}
