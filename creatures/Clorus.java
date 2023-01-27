package creatures;

import huglife.*;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Random;

public class Clorus extends Creature {

    private int r,g,b;

    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    /** Cloruses should lose 0.03 units of energy on a MOVE action. */
    public void move(){
        energy = energy - 0.03;
    }

    /** Cloruses should lose 0.01 units of energy on a STAY action. */
    public void stay() {
        energy = energy - 0.01;
    }

    /** The color() method for Cloruses should always return the color red = 34, green = 0, blue = 231. */
    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return color(r, g, b);
    }

    /** If a Clorus ATTACKs another creature, it should gain that creature’s energy. */
    public void attack(Creature creature) {
        this.energy += creature.energy();
    }

    public Clorus replicate() {
        energy = energy/2;
        return new Clorus(energy);
    }


    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1: If there are no empty squares,
        // the Clorus will STAY (even if there are Plips
        // nearby they could not attack since plip squares
        // do not count as empty squares).
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> PlipNeighbors = new ArrayDeque<>();


        /** Use emptyNeighbors to record empty neighbors. */
        for (Direction key : neighbors.keySet()) {
            if (neighbors.get(key).name().equals("empty")) {
                emptyNeighbors.add(key);
            }
            else if (neighbors.get(key).name().equals("plip")) {
                PlipNeighbors.add(key);
            }
        }
        boolean anyPlip = !PlipNeighbors.isEmpty();

        // Rule 1: If there are no empty squares,
        // the Clorus will STAY.
        if (emptyNeighbors.isEmpty()) {
//            this.stay();
            return new Action(Action.ActionType.STAY);
        }

        // Rule 2: Otherwise, if any Plips are seen,
        // the Clorus will ATTACK one of them randomly.
        else if (anyPlip) {
            Direction d = HugLifeUtils.randomEntry(PlipNeighbors);
            this.attack((Creature) neighbors.get(d));
            return new Action(Action.ActionType.ATTACK, d);
        }

        // Rule 3: if the Clorus has energy >= one,
        // it will REPLICATE to a random empty square.
        else if (this.energy >= 1) {
            Direction d = HugLifeUtils.randomEntry(emptyNeighbors);
//            this.replicate();
            return new Action(Action.ActionType.REPLICATE, d);
        }

        // Rule 4: Otherwise, the Clorus will MOVE to a random empty square.
        else {

            Direction d = HugLifeUtils.randomEntry(emptyNeighbors);
//            this.move();
            return new Action(Action.ActionType.MOVE, d);
        }
    }


}
