package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Created by Robin on 09/04/2017.
 */
public class CheckInOutTest {
    Employee e1;

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
        LocalDate date = LocalDate.of(2017, 4, 9);

        int atDate = CheckInOut.getTotalChecksAt(date);
        int inAtDate = CheckInOut.getTotalChecksInAt(date);
        int total = CheckInOut.getTotalChecks();

        CheckInOut c = new CheckInOut(e1, date);

        assertEquals(e1, c.getEmployee());
        assertEquals(date, c.getDate());

        assertNull(c.getArrivedAt());
        assertNull(c.getLeftAt());

        LocalTime in = LocalTime.of(8, 5, 30);
        LocalTime out = LocalTime.of(16, 56, 30);

        LocalTime roundedIn = LocalTime.of(8, 0);
        LocalTime roundedOut = LocalTime.of(17, 0);

        c.setArrivedAt(in);
        c.setLeftAt(out);

        assertEquals(roundedIn, c.getArrivedAt());
        assertEquals(roundedOut, c.getLeftAt());

        assertEquals(atDate + 2, CheckInOut.getTotalChecksAt(date));
        assertEquals(inAtDate + 1, CheckInOut.getTotalChecksInAt(date));
        assertEquals(total + 2, CheckInOut.getTotalChecks());
    }

    @Test
    public void employeeTest()  throws Exception
    {
        //2017-04-10 7h55 & 2017-04-10 17h09
        LocalDateTime arrivingDT1 = LocalDateTime.of(2017, 4, 10, 7, 55);
        LocalDateTime leavingDT1 = LocalDateTime.of(2017, 4, 10, 17, 9);

        //2017-04-11 8h23 & 2017-04-11 16h35
        LocalDateTime arrivingDT2 = LocalDateTime.of(2017, 4, 11, 8, 23);
        LocalDateTime leavingDT2 = LocalDateTime.of(2017, 4, 11, 16, 35);


        LocalDate today = LocalDate.from(arrivingDT1);
        LocalDate tomorrow = today.plusDays(1);

        e1.setStartingHour(LocalTime.of(8, 0));
        e1.setEndingHour(LocalTime.of(17, 0));

        // First Day

        e1.doCheck(arrivingDT1);

        assertEquals(LocalTime.of(8, 0), e1.getArrivingTimeAt(today));
        assertNull(e1.getArrivingTimeAt(tomorrow));
        assertNull(e1.getLeavingTimeAt(today));

        e1.doCheck(leavingDT1);

        assertEquals(LocalTime.of(8, 0), e1.getArrivingTimeAt(today));
        assertEquals(LocalTime.of(17, 15), e1.getLeavingTimeAt(today));

        // Second Day

        e1.doCheck(arrivingDT2);

        assertEquals(LocalTime.of(8, 30), e1.getArrivingTimeAt(tomorrow));
        assertNull(e1.getLeavingTimeAt(tomorrow));

        e1.doCheck(leavingDT2);

        assertEquals(LocalTime.of(16, 30), e1.getLeavingTimeAt(tomorrow));

        e1.doCheck(LocalDateTime.of(2017, 4, 10, 18, 0));

        assertEquals("Third check of the day shouldn't do anything.", LocalTime.of(8, 30), e1.getArrivingTimeAt(tomorrow));
        assertEquals("Third check of the day shouldn't do anything.", LocalTime.of(16, 30), e1.getLeavingTimeAt(tomorrow));

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