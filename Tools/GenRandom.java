import java.io.*;

public final class GenRandom
{
    /**
        Generates a random value between 0 and 2^63 - 1.

        Uses /dev/random to create a hopefully good random value.
    */
    public static void main(String[] args) throws IOException
    {
        DataInput in = new DataInputStream(new FileInputStream("/dev/random"));
        long randomValue = -1;
        while (randomValue < 0) {
            randomValue = in.readLong();
        }
        System.out.println(randomValue);
    }
}