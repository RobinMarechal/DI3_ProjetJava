package models;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;

/**
 * Created by Robin on 26/05/2017. <br>
 * This class represents a simplified Application's Employee object <br>
 * It only contrains the important information, which are the ID and the complete name
 */
public class Employee implements Serializable
{
    private static final long serialVersionUID = 6244788787823435122L;
    private static String employeeDataFormat = "{id};{name}";

    /** The complete name of the employee */
    private String name;
    /** The ID of the employee */
    private int id;

    /**
     * 2 parameters constructor
     * @param name the complete name of the employee
     * @param id the ID of the employee
     */
    public Employee (@NotNull String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    /**
     * Set the employee data format
     * @param employeeDataFormat the employee data format
     */
    public static void setEmployeeDataFormat (@NotNull String employeeDataFormat)
    {
        Employee.employeeDataFormat = employeeDataFormat;
    }

    /**
     * Get the employee data format
     * @return the employee data format
     */
    public static String getEmployeeDataFormat ()
    {
        return employeeDataFormat;
    }

    /**
     * Get the name of the employee
     * @return the name of the employee
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Set the name of the employee
     * @param name the name of the employee
     */
    public void setName (@NotNull String name)
    {
        this.name = name;
    }

    /**
     * Get the ID of the employee
     * @return the ID of the employee
     */
    public int getId ()
    {
        return id;
    }

    /**
     * Set the ID of the employee
     * @param id the ID of the employee
     */
    public void setId (int id)
    {
        this.id = id;
    }

    /**
     * Create a string that will be used of displaying the employee to the UI views
     * @return the created string
     */
    @Override
    public String toString ()
    {
        return id + " - " + name;
    }
}
