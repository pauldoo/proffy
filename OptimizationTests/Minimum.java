import java.util.*;

final class Minimum {

    private static short minimum(final short[] values)
    {
        short result = Short.MAX_VALUE;
        for (final short v: values) {
            if (v < result) {
                result = v;
            }
        }
        return result;
    }

    public static void main(String[] args)
    {
        for (int i = 1024; i <= 128 * 1024 * 1024; i *= 2) {
            final short[] values = new short[i];
            short total = 0;
            final long begin = System.currentTimeMillis();
            for (int j = 0; j < 1000000000; j += i) {
                total += minimum(values);
            }
            final long end = System.currentTimeMillis();
            assert(total == 0);
            System.out.println(i + ": " + (end - begin) + "ms");
        }
    }
}
