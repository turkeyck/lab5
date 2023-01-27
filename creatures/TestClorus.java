package creatures;


import huglife.*;
import jh61b.grader.GradedTest;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;

public class TestClorus{

    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(1.5);
        Clorus cCopy = c.replicate();
        assertEquals(1.5/2, c.energy(), 0.01);
        assertEquals(1.5/2, cCopy.energy(), 0.01);
    }

    @Test
    public void testChoose() {

        // No empty adjacent spaces; stay.
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Plip());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        // No empty adjacent spaces; stay.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> AllPlip = new HashMap<Direction, Occupant>();
        AllPlip.put(Direction.TOP, new Plip());
        AllPlip.put(Direction.BOTTOM, new Plip());
        AllPlip.put(Direction.LEFT, new Plip());
        AllPlip.put(Direction.RIGHT, new Plip());

        actual = c.chooseAction(AllPlip);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        // with empty and one plip surround; attack top.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> plipEmpty = new HashMap<Direction, Occupant>();
        plipEmpty.put(Direction.TOP, new Plip());
        plipEmpty.put(Direction.BOTTOM, new Empty());
        plipEmpty.put(Direction.LEFT, new Impassible());
        plipEmpty.put(Direction.RIGHT, new Impassible());

        Action runResult = c.chooseAction(plipEmpty);
        Action expect = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expect, runResult);

        // with empty and one plip surround; attack top, bottom, or left.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> Allplip = new HashMap<Direction, Occupant>();
        Allplip.put(Direction.TOP, new Plip());
        Allplip.put(Direction.BOTTOM, new Plip(0.4));
        Allplip.put(Direction.LEFT, new Plip());
        Allplip.put(Direction.RIGHT, new Empty());

        Action threePlipResult = c.chooseAction(Allplip);
        expect = new Action(Action.ActionType.ATTACK, Direction.RIGHT);

        assertNotEquals(expect, threePlipResult);


        // Energy >= 1; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Impassible());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> allEmpty = new HashMap<Direction, Occupant>();
        allEmpty.put(Direction.TOP, new Empty());
        allEmpty.put(Direction.BOTTOM, new Empty());
        allEmpty.put(Direction.LEFT, new Empty());
        allEmpty.put(Direction.RIGHT, new Empty());

        actual = c.chooseAction(allEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);


        // Energy < 1; move.
        c = new Clorus(.99);

        actual = c.chooseAction(allEmpty);
        unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);


        // Energy < 1; move top.
        c = new Clorus(.99);

        actual = c.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.MOVE, Direction.TOP);

        assertEquals(expected, actual);

    }
}
