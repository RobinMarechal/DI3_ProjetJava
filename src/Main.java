import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import lib.time.SimpleDate;
import lib.time.SimpleDateTime;
import lib.time.SimpleTime;
import lib.views.Template;
import models.Company;
import models.Employee;
import models.Manager;
import models.StandardDepartment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Robin on 27/03/2017.
 */
public class Main extends Application
{
    public static void main (String args[])
    {
        launch(args);
    }

    private static void testData ()
    {
        String names[] = {
                "Arlen Gabby",
                "Jake Reagan",
                "Roger Fitzroy",
                "Winslow Humphrey",
                "Theodore Luke",
                "Jewel Rodney",
                "Buddy Zachary",
                "Rocky Cordell",
                "Jake Reagan",
                "Roger Fitzroy",
                "Winslow Humphrey",
                "Theodore Luke",
                "Jewel Rodney",
                "Buddy Zachary",
                "Rocky Cordell",
                "Jake Reagan",
                "Roger Fitzroy",
                "Winslow Humphrey",
                "Theodore Luke",
                "Jewel Rodney",
                "Buddy Zachary",
                "Rocky Cordell",
                "Jake Reagan",
                "Roger Fitzroy",
                "Winslow Humphrey",
                "Theodore Luke",
                "Jewel Rodney",
                "Buddy Zachary",
                "Rocky Cordell"
        };

        Manager m1 = new Manager("Audley", "Gilbert");
        Manager m2 = new Manager("Davy", "Levi");
        Manager m3 = new Manager("Sacheverell", "Lovel");

        StandardDepartment dep1 = new StandardDepartment("Maintenance", "informatique");
        StandardDepartment dep2 = new StandardDepartment("Comptabilité", "Finance");
        StandardDepartment dep3 = new StandardDepartment("Chaine de production", "Manufacture");

        dep1.setManager(m1);
        dep2.setManager(m2);
        dep3.setManager(m3);

        final ObservableList<StandardDepartment> depList = Company.getCompany().getStandardDepartmentsList();

        for (String name : names)
        {
            String fn = name.split(" ")[0];
            String ln = name.split(" ")[1];

            Employee e = new Employee(fn, ln);

            StandardDepartment dep = depList.get((int) (Math.random() * 500) % depList.size());

            dep.addEmployee(e);
        }
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param window the primary stage for this application, onto which
     *               the application scene can be set. The primary stage will be embedded in
     *               the browser if the application was launched as an applet.
     *               Applications may create other stages, if needed, but they will not be
     *               primary stages and will not be embedded in the browser.
     */
    @Override
    public void start (Stage window) throws Exception
    {
        testData();
        window.setTitle("Pointeuse");
        window.setScene(Template.getInstance().getScene());
        window.setResizable(false);

        window.sizeToScene();

        //window.setOnCloseRequest(event -> Company.getCompany().serialiaze());

        window.show();

        runAutomaticTests();

        //runQuickTests();
    }

    private void runAutomaticTests ()
    {
        new Thread(() ->
        {
            try
            {
                SimpleDate date = SimpleDate.TODAY;
                ObservableList<Employee> tmp  = Company.getCompany().getEmployeesList();
                ArrayList<Employee>      emps = new ArrayList<Employee>(tmp);

                for (int i = 0; i < 20; i++)
                {
                    Collections.shuffle(emps);

                    final SimpleDate fDate = date.plusDays(i);
                    System.out.println("Date : " + fDate);

                    for (Employee e : emps)
                    {
                        SimpleTime time = e.getStartingHour().plusMinutes((int) (Math.random() * 120) - 60);
                        Thread.sleep(200);
                        Platform.runLater(() -> e.doCheck(SimpleDateTime.fromDateAndTime(fDate, time)));
                    }

                    Collections.shuffle(emps);

                    for (Employee e : emps)
                    {
                        SimpleTime time = e.getEndingHour().plusMinutes((int) (Math.random() * 120) - 60);
                        Thread.sleep(200);
                        Platform.runLater(() -> e.doCheck(SimpleDateTime.fromDateAndTime(fDate, time)));
                    }

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    private void runQuickTests ()
    {

        new Thread(new Runnable()
        {
            @Override
            public void run ()
            {
                try
                {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            try
                            {
                                Company.createEmployee("Robin", "Marechal", 50);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            Company.getCompany().getEmployeesList().get(0).setFirstName("AAAAAAA");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            final Employee e = Company.getCompany().getEmployee(50);
                            Company.getCompany().getStandardDepartmentsList().get(0).addEmployee(e);
                            Company.getCompany().getEmployeesList().get(0).setFirstName("BBBBBBB");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            final Employee e = Company.getCompany().getEmployee(50);
                            e.doCheck(SimpleDateTime.fromDateAndTime(SimpleDate.TODAY, SimpleTime.of(8, 30)));
                            Company.getCompany().getEmployeesList().get(0).setFirstName("CCCCCCCC");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            final Employee e = Company.getCompany().getEmployee(50);
                            e.doCheck(SimpleDateTime.fromDateAndTime(SimpleDate.TODAY, SimpleTime.of(17, 30)));
                            Company.getCompany().getEmployeesList().get(0).setFirstName("DDDDDDDD");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            final Employee e = Company.getCompany().getEmployee(50);
                            e.doCheck(SimpleDateTime.fromDateAndTime(SimpleDate.TODAY.plusDays(1), SimpleTime.of(8, 10)));
                            Company.getCompany().getEmployeesList().get(0).setFirstName("EEEEEEEE");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            final Employee e = Company.getCompany().getEmployee(50);
                            e.doCheck(SimpleDateTime.fromDateAndTime(SimpleDate.TODAY.plusDays(1), SimpleTime.of(17, 35)));
                            Company.getCompany().getEmployeesList().get(0).setFirstName("FFFFFFF");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            final Employee e = Company.getCompany().getEmployee(50);
                            e.doCheck(SimpleDateTime.fromDateAndTime(SimpleDate.TODAY.plusDays(2), SimpleTime.of(7, 42)));
                            Company.getCompany().getEmployeesList().get(0).setFirstName("GGGGGGGG");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            final Employee e = Company.getCompany().getEmployee(50);
                            e.doCheck(SimpleDateTime.fromDateAndTime(SimpleDate.TODAY.plusDays(2), SimpleTime.of(16, 55)));
                            Company.getCompany().getEmployeesList().get(0).setFirstName("HHHHHHHH");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            Employee e = Company.getCompany().getEmployee(50);
                            Company.getCompany().getStandardDepartmentsList().get(1).addEmployee(e);
                            Company.getCompany().getEmployeesList().get(0).setFirstName("IIIIIIIIII");
                            System.out.println("Done");
                        }
                    });
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run ()
                        {
                            Employee e = Company.getCompany().getEmployee(50);
                            e.setFirstName("AAAA");
                            Company.getCompany().getEmployeesList().get(0).setFirstName("JJJJJJJJJ");
                            System.out.println("Done");
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
