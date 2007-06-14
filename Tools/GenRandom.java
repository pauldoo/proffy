import java.io.*;
import java.security.*;

public final class GenRandom
{
    /**
        Generates a random value between 0 and 2^63 - 1.
    */
    public static void main(String[] args) throws IOException
    {
        SecureRandom random = new SecureRandom();
        System.out.println("Provider: " + random.getProvider());
        long randomValue = -1;
        while (randomValue < 0) {
            randomValue = random.nextLong();
        }
        System.out.println(randomValue);
    }
}
