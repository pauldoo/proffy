import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public final class ApplyXslt
{
    public static void main(String args[]) throws Exception
    {
        final InputStream xmlInputFile = new BufferedInputStream(new FileInputStream(args[0]));
        final InputStream xslFile = new BufferedInputStream(new FileInputStream(args[1]));
        final OutputStream xmlOutputFile = new BufferedOutputStream(new FileOutputStream(args[2]));

        final Transformer xslTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslFile));

        final long startTime = System.currentTimeMillis();
        xslTransformer.transform(
            new StreamSource(xmlInputFile),
            new StreamResult(xmlOutputFile));
        xmlOutputFile.close();
        final long endTime = System.currentTimeMillis();

        System.out.println("Done in " + ((endTime - startTime) / 1000.0) + "s");
    }
}

