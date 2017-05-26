package emulator;

import fr.etu.univtours.marechal.SimpleTime;

/**
 * Created by Robin on 23/05/2017.
 */
public class Main
{
    public static void main (String[] args)
    {
        SimpleTime t = SimpleTime.NOW.plusMinutes(10);
        System.out.println(t);
    }
}
