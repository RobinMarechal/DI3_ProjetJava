package models;

/**
 * Created by Robin on 27/03/2017.
 */
public class VirtualDepartment {
    private String name;
    private String activitySector;

    protected VirtualDepartment() { }

    public VirtualDepartment(String name, String activitySector) {
        this.name = name;
        this.activitySector = activitySector;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivitySector() {
        return activitySector;
    }

    public void setActivitySector(String activitySector) {
        this.activitySector = activitySector;
    }
}
