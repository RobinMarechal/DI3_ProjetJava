package models;

import lib.time.SimpleDate;
import lib.time.SimpleDateTime;
import lib.time.SimpleTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Robin on 09/04/2017.
 */
public class CheckInOutTest {
    private Employee e1;

    @Before
    public void setUp() throws Exception {
        e1 = new Employee("a", "b");
    }

    @After
    public void tearDown() throws Exception {
        e1.fire();
    }

    @Test
    public void basics() throws Exception
    {
        SimpleDate date = SimpleDate.of(2017, 4, 9);

        int atDate = Company.getCompany().getTotalChecksAt(date);
        int inAtDate = Company.getCompany().getTotalChecksInAt(date);
        int total = Company.getCompany().getTotalChecks();

        CheckInOut c = new CheckInOut(e1, date);

        assertEquals(e1, c.getEmployee());
        assertEquals(date, c.getDate());

        assertNull(c.getArrivedAt());
        assertNull(c.getLeftAt());

        SimpleTime in = SimpleTime.of(8, 5, 30);
        SimpleTime out = SimpleTime.of(16, 56, 30);

        SimpleTime roundedIn = SimpleTime.of(8, 0);
        SimpleTime roundedOut = SimpleTime.of(17, 0);

        c.setArrivedAt(in);
        c.setLeftAt(out);

        assertEquals(roundedIn, c.getArrivedAt());
        assertEquals(roundedOut, c.getLeftAt());

        assertEquals(atDate + 2, Company.getCompany().getTotalChecksAt(date));
        assertEquals(inAtDate + 1, Company.getCompany().getTotalChecksInAt(date));
        assertEquals(total + 2, Company.getCompany().getTotalChecks());
    }

    @Test
    public void employeeTest()  throws Exception
    {
        //2017-04-10 7h55 & 2017-04-10 17h09
        SimpleDateTime arrivingDT1 = SimpleDateTime.of(2017, 4, 10, 7, 55);
        SimpleDateTime leavingDT1 = SimpleDateTime.of(2017, 4, 10, 17, 9);

        //2017-04-11 8h23 & 2017-04-11 16h35
        SimpleDateTime arrivingDT2 = SimpleDateTime.of(2017, 4, 11, 8, 23);
        SimpleDateTime leavingDT2 = SimpleDateTime.of(2017, 4, 11, 16, 35);


        SimpleDate today = SimpleDate.fromSimpleDateTime(arrivingDT1);
        SimpleDate tomorrow = today.plusDays(1);

        e1.setStartingHour(SimpleTime.of(8, 0));
        e1.setEndingHour(SimpleTime.of(17, 0));

        // First Day

        e1.doCheck(arrivingDT1);

        assertEquals(SimpleTime.of(8, 0), e1.getArrivingTimeAt(today));
        assertNull(e1.getArrivingTimeAt(tomorrow));
        assertNull(e1.getLeavingTimeAt(today));

        e1.doCheck(leavingDT1);

        assertEquals(SimpleTime.of(8, 0), e1.getArrivingTimeAt(today));
        assertEquals(SimpleTime.of(17, 15), e1.getLeavingTimeAt(today));

        // Second Day

        e1.doCheck(arrivingDT2);

        assertEquals(SimpleTime.of(8, 30), e1.getArrivingTimeAt(tomorrow));
        assertNull(e1.getLeavingTimeAt(tomorrow));

        e1.doCheck(leavingDT2);

        assertEquals(SimpleTime.of(16, 30), e1.getLeavingTimeAt(tomorrow));

        e1.doCheck(SimpleDateTime.of(2017, 4, 10, 18, 0));

        assertEquals("Third check of the day shouldn't do anything.", SimpleTime.of(8, 30), e1.getArrivingTimeAt(tomorrow));
        assertEquals("Third check of the day shouldn't do anything.", SimpleTime.of(16, 30), e1.getLeavingTimeAt(tomorrow));

        // Late or on time?

//        assertFalse(e1.arrivedEarlierAt(today));
//        assertFalse(e1.arrivedLateAt(today));
//        assertFalse(e1.leftEarlierAt(today));
//        assertFalse(e1.leftLateAt(today));
//
//        assertTrue(e1.arrivedLateAt(tomorrow));
//        assertFalse(e1.arrivedEarlierAt(tomorrow));
//        assertTrue(e1.leftEarlierAt(tomorrow));
//        assertFalse(e1.leftLateAt(tomorrow));
    }

}