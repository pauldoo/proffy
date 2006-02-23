import java.io.*;
import java.util.*;
import java.security.*;

public class LRC {
    
    private static int BLOCK_SIZE = 512;
    private static enum Mode { Raw, Block };
    
    private DataOutputStream output;
    private RollingChecksum check;
    private ByteArrayOutputStream currentRaw;
    private Mode mode;
    
    private int rawChunks = 0;
    private int blockChunks = 0;
    
    // weak checksum => (strong checksum => original file offset)
    private Map<Integer, Map<String, Integer>> inputBlocks = new HashMap<Integer, Map<String, Integer>>();
    
    public LRC(DataOutputStream output) throws IOException {
        this.output = output;
        output.writeInt(BLOCK_SIZE);
        check = new RollingChecksum(BLOCK_SIZE);
        mode = Mode.Raw;
    }
    
    private void writeRaw(int value) throws IOException {
        if (currentRaw == null) {
            currentRaw = new ByteArrayOutputStream();
        }
        currentRaw.write(value);
    }
    
    private void snipRawBuffer() {
        if (currentRaw != null) {
            final byte[] buffer = currentRaw.toByteArray();
            currentRaw = new ByteArrayOutputStream();
            currentRaw.write(buffer, 0, buffer.length - BLOCK_SIZE);
            System.err.println("Snip : " + buffer.length + " : " + currentRaw.size());
        }
    }
    
    private void flushRaw() throws IOException {
        if (currentRaw != null && currentRaw.size() > 0) {
            if (mode == Mode.Block) {
                output.writeInt(-1);
            }
            System.err.println("Writing raw chunk : " + currentRaw.size());
            rawChunks += currentRaw.size();
            output.writeInt( currentRaw.size() );
            currentRaw.writeTo( output );
            mode = Mode.Raw;
        }
        currentRaw = null;
    }
    
    private void writeBlock(int offset) throws IOException {
        flushRaw();
        blockChunks += BLOCK_SIZE;
        output.writeInt(offset);
        mode = Mode.Block;
    }
    
    public void writeStream(DataInputStream input) throws IOException, NoSuchAlgorithmException {
        try {
            int bytesRead = 0;
            int bytesToSkip = 0;
            
            while (true) {
                final int value = input.readUnsignedByte();
                bytesRead++;
                check.nextByte(value);
                writeRaw(value);
                final int weakChecksum = check.weakChecksum();
                String strongChecksum = null;

                if (bytesToSkip > 0) {
                    bytesToSkip--;
                } else {
                    Map<String, Integer> weakMatches = inputBlocks.get(weakChecksum);
                    if (weakMatches != null) {
                        strongChecksum = check.strongChecksum();
                        Integer previousOffset = weakMatches.get(strongChecksum);
                        if (previousOffset != null) {
                            snipRawBuffer();
                            System.err.println("Using previously remembered : " + previousOffset + " : " + (bytesRead - BLOCK_SIZE));
                            writeBlock(previousOffset);
                            bytesToSkip = BLOCK_SIZE - 1;
                        }
                    }
                }
    
                if ((bytesRead % BLOCK_SIZE) == 0) {
                    Map<String, Integer> weakMatches = inputBlocks.get(weakChecksum);
                    if (weakMatches == null) {
                        weakMatches = new HashMap<String, Integer>();
                        inputBlocks.put(weakChecksum, weakMatches);
                    }
                    if (strongChecksum == null) {
                        strongChecksum = check.strongChecksum();
                    }
                    if (!weakMatches.containsKey(strongChecksum)) {
                        weakMatches.put(strongChecksum, bytesRead - BLOCK_SIZE);
                        System.err.println("Remembering : " + weakChecksum + " : " + strongChecksum + " : " + (bytesRead - BLOCK_SIZE));
                    }
                }
            }
        } catch (EOFException e) {
            flushRaw();
        }
        System.err.println("Original data: " + rawChunks);
        System.err.println("Reused data: " + blockChunks);
    }
    
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(System.out));
        LRC lrc = new LRC(output);
        DataInputStream input = new DataInputStream(new BufferedInputStream(System.in));
        lrc.writeStream(input);
        output.flush();
    }
}

