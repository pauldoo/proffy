import java.io.*;
import java.security.*;
import java.util.*;

public class RollingChecksum {
    
    private static final int M = 1 << 15;
    
    private int rollingA = 0;
    private int rollingB = 0;
    private int position = 0;
    private final int[] buffer;
    
    public RollingChecksum(int bufferSize) {
        this.buffer = new int[bufferSize];
    }
    
    public void nextByte(int newByte) {
        if (newByte < 0 || newByte > 255) {
            throw new IllegalArgumentException("Byte value out of range: " + newByte);
        }
        
        final int oldByte = buffer[position % buffer.length];
        buffer[position % buffer.length] = newByte;
        position++;
        
        rollingA = (rollingA - oldByte + newByte) % M;
        if (rollingA < 0) {
            rollingA += M * ((-rollingA / M) + 1);
        }
        rollingB = (rollingB - (buffer.length) * oldByte + rollingA) % M;
        if (rollingB < 0) {
            rollingB += M * ((-rollingB / M) + 1);
        }
        
        assert rollingA >= 0 && rollingA < M;
        assert rollingB >= 0 && rollingB < M;
    }
    
    public int weakChecksum() {
        final int result = rollingA + rollingB * M;
        return result;
    }
    
    private byte[] currentContent() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (int i = 0; i < buffer.length; i++) {
            output.write(buffer[(position + i) % buffer.length]);
        }
        return output.toByteArray();
    }
    
    public String strongChecksum() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(currentContent());
        final byte[] strongChecksum = digest.digest();
        return Arrays.toString(strongChecksum);
    }
    
    public static void main(String[] args) {
        RollingChecksum check = new RollingChecksum(512);
        assert check.weakChecksum() == 0;
        check.nextByte(1);
        assert check.weakChecksum() == 1 + 1 * M;
        
        for (int i = 1; i <= 511; i++) {
            check.nextByte(0);
            assert check.weakChecksum() == 1 + (i+1) * M;
        }
        assert check.weakChecksum() == 1 + 512 * M;
        
        check.nextByte(0);
        assert check.weakChecksum() == 0;
    }
    
}

