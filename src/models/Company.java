package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lib.json.JsonLoader;
import lib.json.JsonSaver;
import lib.json.Jsonable;
import lib.time.SimpleDate;
import lib.time.SimpleDateTime;
import lib.time.SimpleTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Robin on 27/03/2017. <br/>
 * Represents the Company. <br/>
 * Singleton
 */
public class Company implements Jsonable, JsonSaver, JsonLoader, Serializable
{
    private static final String serializationFilename = "data\\company.ser";

    /** The instance of the class Company  */
    private static Company companyInstance = new Company();

    /** The name of the Company */
    private StringProperty name = new SimpleStringProperty(this, "name", "Best company ever!");

    /** The number pf checks in per day */
    private ObservableMap<SimpleDate, Integer> totalChecksInPerDay = FXCollections.observableHashMap();

    /** The number of checks out per day */
    private ObservableMap<SimpleDate, Integer> totalChecksOutPerDay = FXCollections.observableHashMap();

    /**  A list containing every created (and not removed) standard departments  */
    private ObservableList<StandardDepartment> departments = FXCollections.observableArrayList();

    /**  A list containing every created (and not fired) employees */
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
        //deserialize();
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
            employees.add(employee);
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

            return empFnLower.contains(searchLower) || empLnLower.contains(searchLower) || (empFnLower + " " + empLnLower).contains(searchLower);
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
     * Save the data of a class instance into a json file
     */
    @Override
    public void save ()
    {
        String path = "data\\files";
        String filename = "company.json";
        saveToFile(path, filename, toJson());
    }

    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json = new JSONObject();

        json.put("name", name.getValueSafe());

        return json;
    }

    /**
     * Saves all data into json files
     */
    public void saveAll ()
    {
        this.save();
        bossInstance.save();
        managementDepartmentInstance.save();

        for (StandardDepartment dep : departments)
        {
            dep.save();
        }

        for (Employee e : employees)
        {
            e.save();
        }
    }

    /**
     * Load Employee and Manager instances from json files
     */
    private void loadEmployeesAndManagers ()
    {
        JSONParser parser = new JSONParser();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String directory = "data\\files\\employees";

        File dir = new File(directory);

        if (!dir.exists())
        {
            System.out.println("There is no employee recorded.");
            return;
        }

        for (File file : dir.listFiles())
        {
            int id = -1;
            try
            {
                JSONObject obj = (JSONObject) parser.parse(new FileReader(file));

                String firstName = obj.get("firstName").toString();
                String lastName = obj.get("lastName").toString();
                id = Integer.parseInt(obj.get("id").toString());

                Employee employee;

                // firstName, lastName sector, id
                if (obj.get("manager") == Boolean.TRUE)
                {
                    employee = new Manager(firstName, lastName, id);
                }
                else
                {
                    employee = new Employee(firstName, lastName, id);
                }


                // Starting and ending hours
                LocalTime startingHourTime = null;
                LocalTime endingHourTime = null;

                Object startingHourObject = obj.get("startingHour");
                Object endingHourObject = obj.get("endingHour");

                if (startingHourObject != null)
                {
                    String startingHourString = startingHourObject == null ? null : startingHourObject.toString();
                    startingHourTime = LocalTime.parse(startingHourString, timeFormatter);
                }

                if (endingHourObject != null)
                {
                    String endingHourString = startingHourObject == null ? null : startingHourObject.toString();
                    endingHourTime = LocalTime.parse(endingHourString, timeFormatter);
                }

                employee.setStartingHour(SimpleTime.fromLocalTime(startingHourTime));
                employee.setEndingHour(SimpleTime.fromLocalTime(endingHourTime));

                // checks
                JSONArray checks = (JSONArray) obj.get("checks");

                for (Object object : checks)
                {
                    // retrieve the information...
                    JSONObject check = (JSONObject) object;
                    String dateStr = check.get("date").toString();

                    Object arrivedAtObject = check.get("arrivedAt");
                    Object leftAtObject = check.get("leftAt");
                    LocalDate date = LocalDate.parse(dateStr, dateFormatter);

                    if (arrivedAtObject != null)
                    {
                        String arrivedAtStr = arrivedAtObject == null ? null : arrivedAtObject.toString();
                        LocalTime arrivedAt = LocalTime.parse(arrivedAtStr, timeFormatter);
                        employee.doCheck(SimpleDateTime.fromLocalTime(arrivedAt)); // in

                        if (leftAtObject != null)
                        {
                            String leftAtStr = leftAtObject == null ? null : arrivedAtObject.toString();
                            LocalTime leftAt = LocalTime.parse(leftAtStr, timeFormatter);
                            employee.doCheck(SimpleDateTime.fromLocalTime(leftAt)); // in
                        }
                    }
                }
            }
            catch (IOException | ParseException e)
            {
                System.err.println("The standard department with id " + id + " could not be created.");
                System.exit(0);
            }
            catch (Exception e)
            {
                System.err.println("An error occurred while trying to parse an employee's file : " + e.getMessage());
            }
        }
    }

    /**
     * Created StandardDepartment instances from json files
     */
    private void loadStandardDepartments ()
    {
        JSONParser parser = new JSONParser();

        String directory = "data\\files\\departments";

        File dir = new File(directory);

        if (!dir.exists())
        {
            System.out.println("There is no standard department recorded.");
            return;
        }

        for (File file : dir.listFiles())
        {
            int id = -1;
            try
            {
                JSONObject obj = (JSONObject) parser.parse(new FileReader(file));

                String name = obj.get("name").toString();
                String activitySector = obj.get("activitySector").toString();
                id = Integer.parseInt(obj.get("id").toString());

                int managerId = Integer.parseInt(obj.get("manager").toString());
                Manager manager = ManagementDepartment.getManagementDepartment().getManager(managerId);

                // name, activity sector, id
                StandardDepartment dep = new StandardDepartment(name, activitySector, id);

                // manager
                dep.setManager(manager);

                // employees
                JSONArray employees = (JSONArray) obj.get("employees");

                for (Object employeeId : employees)
                {
                    int empId = Integer.parseInt(employeeId.toString());
                    dep.addEmployee(getEmployee(empId));
                }
            }
            catch (IOException | ParseException e)
            {
                System.err.println("The standard department with id " + id + " could not be created.");
            }
            catch (Exception e)
            {
                System.err.println("An error occurred while trying to parse a standard department's file : " + e.getMessage());
            }
        }
    }

    /**
     * load Company's data from a json file
     */
    private void loadCompany ()
    {
        JSONParser parser = new JSONParser();

        String path = "data\\files\\company.json";

        try
        {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(path));
            setName(obj.get("name").toString());
        }
        catch (IOException | ParseException e)
        {
            System.err.println("The management department could not be created.");
            System.exit(0);
        }
    }

    /**
     * load Boss' data from a json file
     */
    private void loadBoss ()
    {
        JSONParser parser = new JSONParser();

        String path = "data\\files\\boss.json";

        try
        {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(path));
            bossInstance.setFirstName(obj.get("firstName").toString());
            bossInstance.setLastName(obj.get("lastName").toString());
        }
        catch (IOException | ParseException e)
        {
            System.err.println("The management department could not be created.");
            System.exit(0);
        }
    }

    /**
     * load ManagementDepartment's data from a json file
     */
    private void loadManagementDepartment ()
    {
        JSONParser parser = new JSONParser();

        String path = "data\\files\\management_department.json";

        try
        {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(path));
            managementDepartmentInstance.setName(obj.get("name").toString());
            managementDepartmentInstance.setActivitySector(obj.get("activitySector").toString());
        }
        catch (IOException | ParseException e)
        {
            System.err.println("The management department could not be created.");
            System.exit(0);
        }
    }

    /**
     * Load all data from json files.
     */
    @Override
    public void load ()
    {
        loadCompany();
        loadBoss();
        loadManagementDepartment();
        loadEmployeesAndManagers();
        loadStandardDepartments();
    }

    public void serialiaze ()
    {
        try
        {
            File f = new File(serializationFilename);
            FileOutputStream fileOut = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + serializationFilename);
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    private void deserialize ()
    {
        Company tmp = null;
        try
        {
            File f = new File(serializationFilename);
            FileInputStream fileIn = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tmp = (Company) in.readObject();
            in.close();
            fileIn.close();

            name = tmp.name;
            employees = tmp.employees;
            departments = tmp.departments;
            totalChecksOutPerDay = tmp.totalChecksOutPerDay;
            totalChecksInPerDay = tmp.totalChecksInPerDay;

            int maxId = -1;
            for (Employee e : employees)
            {
                int id = e.getId();
                if (id > maxId)
                {
                    maxId = id;
                }
            }

            maxId = Collections.max(employees.stream().map(Employee::getId).collect(Collectors.toList()));

            Employee.setNextId(maxId + 1);

            maxId = -1;
            for (StandardDepartment d : departments)
            {
                int id = d.getId();
                if (id > maxId)
                {
                    maxId = id;
                }
            }

            StandardDepartment.setNextId(maxId + 1);

            bossInstance.loadFromDeserialization(tmp.bossInstance);
            bossInstance = Boss.getBoss();

            managementDepartmentInstance.loadFromDeserialization(tmp.managementDepartmentInstance);
            managementDepartmentInstance = ManagementDepartment.getManagementDepartment();
        }
        catch (Exception e)
        {
            System.out.println("Deserialization failed...");
            System.err.println(e.getMessage());
        }
    }

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
}
