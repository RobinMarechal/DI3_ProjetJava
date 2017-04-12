package models;

import lib.json.JsonLoader;
import lib.json.JsonSaver;
import lib.json.Jsonable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017. <br/>
 * Represents the Company. <br/>
 * Singleton
 */
public class Company implements Jsonable, JsonSaver, JsonLoader
{
    /**
     * The instance of the class Company
     */
    private static Company companyInstance = new Company();

    /**
     * The name of the Company
     */
    private String name = "Best Company Ever !";


    /**
     * The Company's boss
     */
    private Boss bossInstance = Boss.getBoss();

    /**
     * The Company's management department
     */
    private ManagementDepartment managementDepartmentInstance = ManagementDepartment.getManagementDepartment();

    /**
     * A list containing every created (and not removes) standard departments
     */
    private ArrayList<StandardDepartment> departments = new ArrayList<>();

    /**
     * A list containing every created (and not fired) employees
     */
    private ArrayList<Employee> employees = new ArrayList<>();

    /**
     * Basic constructor
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
        this.name = name;
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
        if (employee == null)
        {
            return this;
        }

        employee.fire();
        employee = null;
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
        if (manager == null)
        {
            return this;
        }

        manager.fire();
        manager = null;
        return this;
    }

    /**
     * Removes en employee from the Company
     *
     * @param employee the employee the remove
     * @return this
     */
    public Company removeEmployee (Employee employee)
    {
        if (employee != null && employees.contains(employee))
        {
            employees.remove(employee);
            employee.fire();
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
    public static Employee addEmployee (String firstName, String lastName)
    {
        return new Employee(firstName, lastName);
    }

    /**
     * Creates a {@link Manager} instance and adds it to the Company
     *
     * @param firstName the manager's first-name
     * @param lastName  the manager's last-name
     * @return the new manager
     */
    public static Manager addManager (String firstName, String lastName)
    {
        return new Manager(firstName, lastName);
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
    public static StandardDepartment addStandardDepartment (String name, String activitySector)
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
    public static StandardDepartment addStandardDepartment (String name, String activitySector, Manager manager)
    {
        StandardDepartment dep = addStandardDepartment(name, activitySector);
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
            department = null;
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
    public ArrayList<Employee> searchEmployee (String firstName, String lastName)
    {
        ArrayList<Employee> list = new ArrayList<>();

        for (Employee e : employees)
        {
            if (e.getFirstName().toLowerCase().contains(firstName.toLowerCase()) && e.getLastName()
                                                                                     .toLowerCase()
                                                                                     .contains(lastName.toLowerCase()))
            {
                list.add(e);
            }
        }

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
    public ArrayList<StandardDepartment> searchStandardDepartment (String name, String activitySector)
    {
        ArrayList<StandardDepartment> list = new ArrayList<>();

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
        return "Company: " + name;
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

        json.put("name", name);

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
                if(obj.get("manager") == Boolean.TRUE)
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

                if(startingHourObject != null)
                {
                    String startingHourString = startingHourObject == null ? null : startingHourObject.toString();
                    startingHourTime = LocalTime.parse(startingHourString, timeFormatter);
                }

                if(endingHourObject != null)
                {
                    String endingHourString = startingHourObject == null ? null : startingHourObject.toString();
                    endingHourTime = LocalTime.parse(endingHourString, timeFormatter);
                }

                employee.setStartingHour(startingHourTime);
                employee.setEndingHour(endingHourTime);

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
                    LocalDateTime arrivedAtDT = null;
                    LocalDateTime leftAtDT = null;

                    if(arrivedAtObject != null)
                    {
                        String arrivedAtStr = arrivedAtObject == null ? null : arrivedAtObject.toString();
                        LocalTime arrivedAt = LocalTime.parse(arrivedAtStr, timeFormatter);
                        arrivedAtDT = LocalDateTime.of(date, arrivedAt);
                        employee.doCheck(arrivedAtDT); // in

                        if(leftAtObject != null)
                        {
                            String leftAtStr = leftAtObject == null ? null : arrivedAtObject.toString();
                            LocalTime leftAt = LocalTime.parse(leftAtStr, timeFormatter);
                            leftAtDT = LocalDateTime.of(date, leftAt);
                            employee.doCheck(leftAtDT); // out
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
}
