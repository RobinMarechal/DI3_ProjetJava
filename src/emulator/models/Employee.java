package emulator.models;

/**
 * Created by Robin on 26/05/2017.
 */
public class Employee
{
    private String name;
    private int id;

    public Employee (String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    @Override
    public String toString ()
    {
        return id + " - " + name;
    }
}
