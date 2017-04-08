package models;

/**
 * Created by Robin on 27/03/2017.
 */
public class VirtualDepartment {
    private String name;
    private String activitySector;

    public VirtualDepartment(String name, String activitySector) {
        this.name = name;
        this.activitySector = activitySector;
    }

    public String getName() {

        return name;
    }

    public VirtualDepartment setName(String name) {
        this.name = name;
        return this;
    }

    public String getActivitySector() {
        return activitySector;
    }

    public VirtualDepartment setActivitySector(String activitySector) {
        this.activitySector = activitySector;
        return this;
    }

    @Override
    public String toString() {
        return "VirtualDepartment{" +
                "name='" + name + '\'' +
                ", activitySector='" + activitySector + '\'' +
                '}';
    }
}
