package escape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;


public final class Main {

    static List<State> nextStates(State current)
    {
        List<State> result = new ArrayList<State>();
        for (int i = 0; i < 10; i++) {
            State s = current.repMoveUp(i);
            if (s != null) {
                result.add(s);
            }
            s = current.repMoveDown(i);
            if (s != null) {
                result.add(s);
            }
            s = current.repMoveLeft(i);
            if (s != null) {
                result.add(s);
            }
            s = current.repMoveRight(i);
            if (s != null) {
                result.add(s);
            }
        }
        return result;
    }

    private static void printResult(State s, Map<State, State> parents)
    {
        if (s != null) {
            printResult(parents.get(s), parents);
            System.out.println(s);
        }
    }

    public static void main(String[] args)
    {
        /*
         * 022344
         * 112250
         * 116550
         * 066500
         * 076000
         */
        State initial = State.createEmpty().
                repSetCell(0, 0, 0).
                repSetCell(0, 1, 2).
                repSetCell(0, 2, 2).
                repSetCell(0, 3, 3).
                repSetCell(0, 4, 4).
                repSetCell(0, 5, 4).

                repSetCell(1, 0, 1).
                repSetCell(1, 1, 1).
                repSetCell(1, 2, 2).
                repSetCell(1, 3, 2).
                repSetCell(1, 4, 5).
                repSetCell(1, 5, 0).

                repSetCell(2, 0, 1).
                repSetCell(2, 1, 1).
                repSetCell(2, 2, 6).
                repSetCell(2, 3, 5).
                repSetCell(2, 4, 5).
                repSetCell(2, 5, 0).

                repSetCell(3, 0, 0).
                repSetCell(3, 1, 6).
                repSetCell(3, 2, 6).
                repSetCell(3, 3, 5).
                repSetCell(3, 4, 0).
                repSetCell(3, 5, 0).

                repSetCell(4, 0, 0).
                repSetCell(4, 1, 7).
                repSetCell(4, 2, 6).
                repSetCell(4, 3, 0).
                repSetCell(4, 4, 0).
                repSetCell(4, 5, 0);

        System.out.println(initial);
        System.out.println();

        final Queue<State> queue = new LinkedList<State>();
        final SortedMap<State, State> visited = new TreeMap<State, State>();

        queue.add(initial);
        visited.put(initial, null);

        while (queue.isEmpty() == false) {
            State current = queue.remove();

            List<State> next = nextStates(current);
            for (State s: next) {
                if (visited.containsKey(s) == false) {
                    visited.put(s, current);
                    queue.add(s);

                    if (s.isSolved()) {
                        printResult(s, visited);
                        return;
                    }
                }
            }
        }
    }
}
