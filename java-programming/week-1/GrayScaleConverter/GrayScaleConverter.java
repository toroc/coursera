import edu.duke.*;
import java.io.File;

/**
 * Write a description of GrayScaleConverter here.
 * 
 * @author Carol D. Toro
 * @version 11/09/16
 */
public class GrayScaleConverter 
{
    //Begin with selected image (inImage)
    public ImageResource makeGray(ImageResource inImage){
        //create blank image of same size
        ImageResource outImage = new ImageResource(inImage.getWidth(), inImage.getHeight());
        
        //do converstions for the pixels in inImage
        for(Pixel pixel: outImage.pixels()){
            //view corresponding pixel in inImage
            Pixel inPixel = inImage.getPixel(pixel.getX(), pixel.getY());
            //compute inPixel's red + blue + green
            //divide sum by 3
            int average = (inPixel.getRed() + inPixel.getBlue() + inPixel.getGreen()) / 3;
 
            pixel.setRed(average); //set pixel's red to average
            pixel.setGreen(average); //set pixel's green to average
            pixel.setBlue(average); //set pixel's blue to average
           
        }
        
        //outImage was converted
        return outImage;
    }
    
    public void saveImages(ImageResource inImage, ImageResource grayImage){
        String fname = inImage.getFileName();
        String newName = "gray-" + fname;
        grayImage.setFileName(newName);
        grayImage.save();
    }
    
    public void selectAndConvert(){
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()){
            //create new image resource from file f
            ImageResource inImage = new ImageResource(f);
            //Call conversion method
            ImageResource gray = makeGray(inImage);
            String fname = inImage.getFileName();
            String newName = "gray-" + fname;
            gray.setFileName(newName);
            gray.save();
            gray.draw();
            //saveImages(inImage, gray);
        }
    }
            
           
    public void testGray(){
        ImageResource ir = new ImageResource();
        ImageResource gray = makeGray(ir);
        gray.draw();
    }
}