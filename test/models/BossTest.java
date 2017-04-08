package models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Robin on 08/04/2017.
 */
public class BossTest {

    @Test
    public void basics() throws Exception
    {
        assertNotNull(Boss.getBoss());
        assertEquals(Boss.getBoss(), Company.getCompany().getBoss());
    }

}