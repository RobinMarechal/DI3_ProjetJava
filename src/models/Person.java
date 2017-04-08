package models;

/**
 * Created by Robin on 27/03/2017.
 */
public abstract class Person {
    private String firstName;
    private String lastName;

    public Person(){}

    public Person(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
