import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lib.time.SimpleDate;
import lib.time.SimpleDateTime;
import lib.time.SimpleTime;
import lib.views.Template;
import models.Company;
import models.Employee;
import models.Manager;
import models.StandardDepartment;

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
        Employee e1 = new Employee("A", "A");
        Employee e2 = new Employee("B", "B");
        Employee e3 = new Employee("C", "C");
        Employee e4 = new Employee("D", "D");
        Employee e5 = new Employee("E", "E");

        Manager m1 = new Manager("Ma", "Ma");
        Manager m2 = new Manager("Mb", "Mb");

        StandardDepartment dep1 = new StandardDepartment("depa", "depa");
        StandardDepartment dep2 = new StandardDepartment("depb", "depb");

        dep1.setManager(m1);
        dep2.setManager(m2);

        dep1.addAllEmployees(e1, e2, e3);
        dep2.addAllEmployees(e4, e5);

        SimpleDate d = SimpleDate.TODAY;
        SimpleTime t = SimpleTime.of(8, 0);

        SimpleDateTime sdt8h = SimpleDateTime.fromDateAndTime(d, t);

        e1.doCheck(sdt8h.plusMinutes(15));
        e1.doCheck(sdt8h.plusHours(7));

        System.out.println("ADD HOURS E1 : " + e1.getOvertime());

        e2.doCheck(sdt8h.plusMinutes(30));
        e2.doCheck(sdt8h.plusHours(8));

        e3.doCheck(sdt8h.minusMinutes(15));
        e4.doCheck(sdt8h.minusMinutes(30));

        e5.setStartingHour(SimpleTime.of(11, 45));
        m1.setStartingHour(SimpleTime.of(11, 30));
        m2.setStartingHour(SimpleTime.of(11, 15));

        System.out.println(Company.getCompany().getEmployeesList().get(0).getCheckInOutAt(SimpleDate.TODAY));
        System.out.println(Company.getCompany().getTotalChecksInAt(SimpleDate.TODAY));
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

        runQuickTests();
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
