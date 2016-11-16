package week5;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.providers.Microsoft;
/**
 * Created by carol on 11/15/2016.
 */
public class EventDisplay extends PApplet{
    private UnfoldingMap map;
    /*Member variables for button coordinates*/
    private int btn_x = 100;
    private int btn_y = 100;
    private int btn_offset_y = 50;
    private int btn_max = 25;

    public void setup()
    {
        size(800,600, OPENGL);
        map = new UnfoldingMap(this, 50, 50, 700, 500, new Microsoft.RoadProvider());
        //Listen for default events
        MapUtils.createDefaultEventDispatcher(this, map);

    }

    public void draw(){
        map.draw();

        //draw buttons
        fill(255,255,255);
        rect(btn_x, btn_y,25,25);
        fill(100,100,100);
        rect(btn_x,btn_y+btn_offset_y,25,25);

    }

    public void keyPressed()
    {
        if(key == 'w')
        {
            background(255,255,255);
        }
    }

    public void mousePressed(){}
    public void mouseClicked(){}
    /*Only care about when the mouse was released*/
    public void mouseReleased(){
        /*Check that release happened at coordinates of buttons*/
        if (mouseX > btn_x && mouseX < (btn_x+btn_max) && mouseY > btn_y && mouseY < (btn_max+btn_y)){
            background(255,255,255);
        }
        else if (mouseX > btn_x && mouseX < (btn_x+btn_max) && mouseY > (btn_y+btn_offset_y) && mouseY < (btn_offset_y+btn_y+btn_max))
        {
            background(100,100,100);
        }
    }

}
