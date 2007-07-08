/*
    Copyright (C) 2007  Paul Richards.
 
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package imageconverter;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.*;

final class ImageScaler implements Runnable
{
    private final Image input;
    private final int newWidth;
    private final int newHeight;
    private final String outputType;
    private final File output;
    
    public ImageScaler(Image input, int newWidth, int newHeight, String outputType, File output)
    {
        this.input = input;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.outputType = outputType;
        this.output = output;
    }

    public void run()
    {
        try {
            BufferedImage scaled = new BufferedImage( newWidth, newHeight,
                BufferedImage.TYPE_INT_RGB );
            Graphics tg = scaled.getGraphics();
            tg.drawImage( input.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null );
            tg.dispose();
            writeImage( scaled, outputType, output );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void writeImage(BufferedImage image, String format, File outfile) throws IOException
    {
        
        ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName(format).next();
        ImageWriteParam params = writer.getDefaultWriteParam();
        if (params.canWriteProgressive())
        {
            params.setProgressiveMode(params.MODE_DEFAULT);
        }
        
        // Prepare output file
        ImageOutputStream ios = ImageIO.createImageOutputStream(outfile);
        writer.setOutput(ios);
        
        // Write the image
        writer.write(null, new IIOImage(image, null, null), params);
        
        // Cleanup
        ios.flush();
        writer.dispose();
        ios.close();
    }
}

/** Filter for JPEG files and directories, used by the Swing filechooser thing.
*/
final class ImageFileFilter extends javax.swing.filechooser.FileFilter
{
    public boolean accept(File f)
    {
        return
            f.isDirectory() ||
            f.getName().toUpperCase().endsWith(".JPEG") ||
            f.getName().toUpperCase().endsWith(".JPG") ||
            f.getName().toUpperCase().endsWith(".GIF") ||
            f.getName().toUpperCase().endsWith(".PNG");
    }

    public String getDescription()
    {
        return "Images";
    }
}

/** Program to resize a bunch of images and generate thumbnails.
 */
public final class Main
{
    
    /** Maximum dimension for a thumbnail.
     */
    protected static int thumbSize = 160;
    
    /** Maximum dimension for a full sized image.
     */
    protected static int fullSize = 1024;
    
    /** Image type.
     */
    protected static String imageType = "jpeg";
    
    /** Main method.
     */
    public static void main(String args[]) throws IOException, InterruptedException
    {
        
        // Load properties file
        Properties props = new Properties();
        props.load( new BufferedInputStream( ClassLoader.getSystemResourceAsStream("ImageConverter.properties") ) );
        
        // Retrieve entries, default is to leave alone the above values if entries aren't found
        thumbSize = Integer.parseInt( props.getProperty( "ThumbSize", Integer.toString(thumbSize) ) );
        fullSize = Integer.parseInt( props.getProperty( "FullSize", Integer.toString(fullSize) ) );
        imageType = props.getProperty( "ImageType", imageType );
        
        // Construct and show file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new ImageFileFilter());
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.showOpenDialog(null);
        
        // Get list of files and construct output dir
        File[] inputFiles = fileChooser.getSelectedFiles();
        if (inputFiles.length == 0)
            System.exit(0);
        File parentDir = inputFiles[0].getParentFile();
        
        String title = parentDir.getName();
        File outputDir = new File(parentDir, title);
        if (!outputDir.mkdir()) {
            throw new IOException("Could not create directory: " + outputDir);
        }
        extractStaticContent(outputDir);
        
        // Open index.html in output dir
        PrintWriter outWriter = new PrintWriter( new FileWriter( new File(outputDir, "index.html") ) );
        outWriter.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        outWriter.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        outWriter.println("<head>");
        outWriter.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        outWriter.println("<head>");
        outWriter.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"richards.css\" />");
        outWriter.println("<script type=\"text/javascript\" src=\"chooseImage.js\"></script>");
        outWriter.println("<title>" + title + "</title>");
        outWriter.println("</head>");

        outWriter.println("<body>");
        outWriter.println("  <div id=\"outer\">");
        outWriter.println("    <div id=\"topBanner\">");
        outWriter.println("      <script type=\"text/javascript\">chooseBanner();</script>");
        outWriter.println("      <div id=\"bannerText\">");
        outWriter.println("        <img src=\"images/bannerText.gif\" alt=\"The Richards Family Online Album\" />");
        outWriter.println("      </div>");
        outWriter.println("    </div>");
        outWriter.println("    <div id=\"content\">");
        outWriter.println("      <h2><a href=\"javascript:history.go(-1)\">&lt; Back</a></h2>");
                
        for (int i = 0; i < inputFiles.length; i++)
        {
            // Open original file
            BufferedImage orig = ImageIO.read( inputFiles[i] );
            
            // Calculate image sizes for the thumb and full images
            int
                thumbWidth = thumbSize, thumbHeight = thumbSize,
                fullWidth = fullSize, fullHeight = fullSize;
            
            if ( orig.getWidth() > orig.getHeight() )
            {
                thumbHeight = thumbWidth * orig.getHeight() / orig.getWidth();
                fullHeight = fullWidth * orig.getHeight() / orig.getWidth();
            }
            else
            {
                thumbWidth = thumbHeight * orig.getWidth() / orig.getHeight();
                fullWidth = fullHeight * orig.getWidth() / orig.getHeight();
            }
            
            if ( thumbWidth >= orig.getWidth() )
            {
                thumbWidth = orig.getWidth();
                thumbHeight = orig.getHeight();
            }
            if ( fullWidth >= orig.getWidth() )
            {
                fullWidth = orig.getWidth();
                fullHeight = orig.getHeight();
            }
            
            String thumbName = "thumb_" + inputFiles[i].getName();
            thumbName = thumbName.substring( 0, thumbName.lastIndexOf( '.' ) ) + "." + imageType;
            File thumbFileName = new File(outputDir, thumbName);

            String fullName = inputFiles[i].getName();
            fullName = fullName.substring( 0, fullName.lastIndexOf( '.' ) ) + "." + imageType;
            File fullFileName = new File(outputDir, fullName);

            // Create thumbnail image and write it out
            Thread thumbThread = new Thread(new ImageScaler(orig, thumbWidth, thumbHeight, imageType, thumbFileName));
            thumbThread.start();
            
            Thread fullsizeThread = new Thread(new ImageScaler(orig, fullWidth, fullHeight, imageType, fullFileName));
            fullsizeThread.start();
            
            thumbThread.join();
            fullsizeThread.join();
            
            thumbName = URIEscape(thumbName);
            fullName = URIEscape(fullName);
            String altText = fullWidth + "x" + fullHeight + ", " + ((int)(fullFileName.length() / 1024)) + "KiB";
            
            if (i % 3 == 0) {
                outWriter.println("      <div class=\"groupof3\">");
            }
            outWriter.println("        <div class=\"album\">");
            outWriter.println("          <a href=\"image.html?image=" + fullName + "\"><img src=\"" + thumbName + "\" alt=\"" + altText + "\"/></a>");
            outWriter.println("        </div>");
            if (i % 3 == 2) {
                outWriter.println("      </div>");
            }
            
        }
        
        // All done.
	outWriter.println("      <h2 class=\"clear\"><a href=\"javascript:history.go(-1)\">&lt; Back</a></h2>");
        outWriter.println("    </div>");
        outWriter.println("  </div>");
        outWriter.println("</body>");
        outWriter.println("</html>");        
        outWriter.close();
        
        System.exit(0);
    }
    
    // Calculate URL safe filenames, (convert spaces to %20 etc..)
    private static String URIEscape(String str)
    {
        try {
            return (new URI( "http", str, null )).getRawSchemeSpecificPart();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Value '" + str + "' could not be made URI safe");
        }
    }
    
    private static void extractStaticContent(File baseDirectory) throws IOException
    {
        InputStream in = null;
        try {
            in = new BufferedInputStream(ClassLoader.getSystemResourceAsStream("static.zip"));
            ZipInputStream zis = new ZipInputStream(in);
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(baseDirectory, entry.getName());
                if (entry.isDirectory()) {
                    if (!newFile.mkdir()) {
                        throw new IOException("Failed to create directory: " + newFile.toString());
                    }
                } else {
                    OutputStream out = null;
                    try {
                        out = new BufferedOutputStream(new FileOutputStream(newFile));
                        byte[] buf = new byte[(int)entry.getSize()];
                        new DataInputStream(zis).readFully(buf);
                        out.write(buf);
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                    }
                }
                zis.closeEntry();
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}