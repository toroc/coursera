import edu.duke.*;
import java.io.File;
/**
 * Write a description of InversionConverter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class InversionConverter {
    //Method to invert an image
    public ImageResource makeInverted(ImageResource originalImage){
        
        //create copy of image
        ImageResource invertedImage = new ImageResource(originalImage.getWidth(), originalImage.getHeight());
        
        // loop and do conversions for the pixels in image
        for(Pixel pixel: invertedImage.pixels()){
            //get corresponding pixel
            Pixel inPixel = originalImage.getPixel(pixel.getX(), pixel.getY());
            //compute inverted pixels
            int redPixel = 255 - inPixel.getRed();
            int greenPixel = 255 - inPixel.getGreen();
            int bluePixel = 255 - inPixel.getBlue();
            
            //Set the inverted pixels
            pixel.setRed(redPixel);
            pixel.setGreen(greenPixel);
            pixel.setBlue(bluePixel);
        }
        return invertedImage;
    }
    
    public void selectAndConvert(){
        DirectoryResource dr = new DirectoryResource();
        for(File f: dr.selectedFiles()){
            //create image resource from file
            ImageResource inImage = new ImageResource(f);
            ImageResource inverted = makeInverted(inImage);
            //Save as new file name
            String fname = inImage.getFileName();
            String newName = "inverted-" + fname;
            inverted.setFileName(newName);
            inverted.save();
            inverted.draw();
        }
    }
    
}
