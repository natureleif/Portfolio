package whitman.cs370proj.composer;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class SelectionBox{
    //private fields
    MainController mc;
    private double initX;
    private double initY;
    private Rectangle sBox;

    //constructor of the selection box
    public SelectionBox(double initX, double initY, MainController mc){
        this.sBox = new Rectangle((double)initX, 
        (double)initY, 0 , 0);
        sBox.setFill(Color.TRANSPARENT);
        sBox.setStroke(Color.BLACK);
        this.initX = initX;
        this.initY = initY;
        this.mc = mc;
        CompManager.show(sBox);
    }

    //changes the dimensions of the selection box depending on the mouse position both forwards and backwards
    public void updateSelectionBox(double x, double y){
        if(initX < x){
            sBox.setX(initX);
            sBox.setWidth(x - initX);
        }
        else{
            sBox.setX(x);
            sBox.setWidth(initX - x);
        }

        if(initY < y){
            sBox.setY(initY);
            sBox.setHeight(y - initY);
        }
        else{
            sBox.setY(y);
            sBox.setHeight(initY - y);
        }
    }
    
    //selects all notes within the selection box
    public void batchSelect(boolean isControlDown){
        for (Gesture gesture : CompManager.getComposition()){
            if(gesture.isInsideBox(sBox.getX(), sBox.getY(), sBox.getWidth(), sBox.getHeight())){
                gesture.select();
            }
            else if(!isControlDown){
                gesture.deselect();
            }
        }
    }
        
    //removes the selection box from the notepane in the main controller
    public void deleteSelectionBox(MainController mc){
        CompManager.hide(this.sBox);
    }
}