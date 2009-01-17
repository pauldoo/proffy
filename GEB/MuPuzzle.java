import java.util.*;
import java.util.regex.*;

public final class MuPuzzle
{
    private static final Pattern fRule3Pattern = Pattern.compile("III");
    private static final Pattern fRule4Pattern = Pattern.compile("UU");

    /// xI => xIU
    private static List<String> Rule1(final String x)
    {
        List<String> result = new ArrayList<String>();
        if (x.endsWith("I")) {
            result.add(x + "U");
        }
        return result;
    }

    /// Mx => Mxx
    private static List<String> Rule2(final String x)
    {
        List<String> result = new ArrayList<String>();
        if (x.startsWith("M")) {
            final String tail = x.substring(1);
            result.add("M" + tail + tail);
        }
        return result;
    }

    /// xIIIy => xUy
    private static List<String> Rule3(final String x)
    {
        List<String> result = new ArrayList<String>();
        Matcher m = fRule3Pattern.matcher(x);
        while (m.find()) {
            int index = m.start();
            String newString =
                x.substring(0, index) +
                "U" +
                x.substring(index + 3);
            result.add(newString);
        }
        return result;
    }

    /// xUUy => xy
    private static List<String> Rule4(final String x)
    {
        List<String> result = new ArrayList<String>();
        Matcher m = fRule4Pattern.matcher(x);
        while (m.find()) {
            int index = m.start();
            String newString =
                x.substring(0, index) +
                x.substring(index + 2);
            result.add(newString);
        }
        return result;
    }

    private static void InternAndAdd(
        Queue<String> queue,
        Set<String> visited,
        List<String> strings)
    {
        for (String s: strings) {
            s = s.intern();
            if (visited.add(s)) {
                queue.add(s);
                if (visited.size() % 1000 == 0) {
                    System.out.println(visited.size() + ", " + queue.size() + ": " + s);
                }
                if (s.equals("MU")) {
                    throw new RuntimeException("Found!");
                }
            }
        }
    }

    private static final class StringLengthComparator implements Comparator<String>
    {
        public int compare(final String a, final String b)
        {
            return Integer.signum(a.length() - b.length());
        }
    }

    public static void main(String[] args)
    {
        Queue<String> queue = new PriorityQueue<String>(1, new StringLengthComparator());
        Set<String> visited = new HashSet<String>();

        List<String> axioms = new ArrayList<String>();
        axioms.add("MI");

        InternAndAdd(
            queue,
            visited,
            axioms);

        while (queue.isEmpty() == false) {
            final String current = queue.remove();

            InternAndAdd(queue, visited, Rule1(current));
            InternAndAdd(queue, visited, Rule2(current));
            InternAndAdd(queue, visited, Rule3(current));
            InternAndAdd(queue, visited, Rule4(current));
        }
        throw new RuntimeException("Not found!");
    }
}
