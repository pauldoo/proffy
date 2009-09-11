import java.io.*;
import java.util.*;
import java.security.*;

public final class Coins
{
    public static void main(String[] args) throws IOException
    {
        Random random = new SecureRandom();
        for (int guessA = 0; guessA < 8; guessA++) {
            int guessB = (~guessA & 0x2) << 1 | (guessA & 0x6) >> 1;

            int winsForA = 0;
            int winsForB = 0;
            for (int trials = 0; trials < 1000000; trials++) {
                int value = 0;
                for (int i = 0; i < 2; i++) {
                    value = ((value << 1) | (random.nextBoolean() ? 1 : 0)) & 7;
                }
                while (true) {
                    value = ((value << 1) | (random.nextBoolean() ? 1 : 0)) & 7;
                    if (value == guessA) {
                        winsForA ++;
                        break;
                    } else if (value == guessB) {
                        winsForB ++;
                        break;
                    }
                }
            }

            System.out.println("A=" + guessA + "  B=" + guessB + "  " + ((double)winsForA) / (winsForA + winsForB));
        }
    }
}
