package whitman.cs370proj.composer;
import java.util.Collection;

import javafx.scene.paint.Color;

public class Group extends Gesture{
    private Collection<Gesture> groupItems;
    private boolean selected;

    // temporary variables
    private double difX;
    private double difY;
    private Boolean hasActiveEdit = false;

    Group(Collection<Gesture> items){
        // create a rectangle with the min x and y and max of width and height
        this.groupItems = items;
        this.parent = null;
        adjustBorder();
        this.select();
        this.getStyleClass().add("group");
    }

    //Adjust the border of the group
    private void adjustBorder(){
        int minX = 100000;
        int maxX = 0;
        int minY = 100000;
        int maxY = 0;

        for(Gesture gesture : groupItems){
            gesture.parent = this;
            if(gesture.getX() < minX){ minX = (int)gesture.getX(); }
            if(gesture.getX() + gesture.getWidth() > maxX){ maxX = (int)(gesture.getX() + gesture.getWidth()); }
            if(gesture.getY() < minY){ minY = (int)gesture.getY(); }
            if(gesture.getY() + gesture.getHeight() > maxY){ maxY = (int)(gesture.getY() + gesture.getHeight()); }
        }
        setX(minX);
        setY(minY);
        setHeight(maxY - minY);
        setWidth(maxX - minX);
    }

    // returns list of items in the gesture
    public Collection<Gesture> getGroupItems() {
        return groupItems;
    }

    // Sets difX, difY, and OldWidth
    public void startEdit(double mouseX, double mouseY){
        if(!hasActiveEdit){
            for (Gesture item : groupItems){
                item.startEdit(mouseX, mouseY);
            }
            this.difX = getX() - mouseX;
            this.difY = getY() - mouseY;
            this.oldWidth = getWidth();
            this.hasActiveEdit = true;
        }
    }

    // Sets difX, difY, and OldWidth back to 0
    public void finishEdit(){
        for (Gesture item : groupItems){
            item.finishEdit();
        }
        this.difX = 0;
        this.difY = 0;
        this.oldWidth = 0;
        this.hasActiveEdit = false;
    }

    // Edits the length of the note using the mouse X
    public void editLength(double editDistance){
        setWidth(getMaxTick() - getX());
        for (Gesture item : groupItems){
            item.editLength(editDistance);
        }
    }

    // Sets the position of the notes relative to the mouse
    public void editPos(double x, double y){
        setX(x + this.difX);
        setY(y + this.difY);
        for (Gesture item : groupItems){
            item.editPos(x, y);
        }
        setX(x + this.difX);
        setY(y + this.difY);
    }

    // Snaps gesture to the grid
    public void snap(){
        for (Gesture item : groupItems){
            item.snap();
        }
        adjustBorder();
    }

    // Adds the gesture to the midi player
    public void addToPlayer(){
        for (Gesture item : groupItems){
            item.addToPlayer();
        }
    }

    // Request selection and check for parents
    public void requestSelection(){
        if(this.parent != null){
            this.parent.requestSelection();
        }
        else{
            select();
        }
    }

    // selects all gestures in the group
    public void select() {
        for (Gesture item: groupItems){
            item.select();
        }
        this.selected = true;
        this.setStroke(Color.BLACK);
    }

    // de-selects all Gestures in the group
    public void deselect() {
        for (Gesture item : groupItems){
            item.deselect();
        }
        this.selected = false;
        this.setStroke(Color.GRAY);
    }

    // Ungroups the gesture
    public void ungroup(){
        for(Gesture item : groupItems){
            item.parent = null;
            CompManager.add(item);
        }
        CompManager.remove(this);
        CompManager.hide(this);
    }

    // Returns true if the edit is a move edit
    public boolean isMoveEdit(double mouseX){
        return (mouseX < getX() + getWidth() - 5);
    }

    // Delete the gesture
    public void delete(){
        for(Gesture item : groupItems){
            item.delete();
        }
        CompManager.remove(this);
        CompManager.hide(this);
    }

    // Hide the gesture
    public void hide(){
        for (Gesture item : groupItems){
            item.hide();
        }
        CompManager.hide(this);
    }

    // Show the gesture
    public void show(){
        for (Gesture item : groupItems){
            item.show();
        }
        CompManager.show(this);
        this.toBack();
    }


    // Returns bool based on if the gesture is selected
    public boolean isSelected(){
        return this.selected;
    }

    // Returns the maximum tick in the gesture
    public int getMaxTick(){
        int max = 0;
        for (Gesture item : groupItems){
            if(item.getMaxTick() > max){ max = item.getMaxTick(); }
        }
        return max;
    }

    // Checks if gesture intersects box
    public boolean isInsideBox(double x, double y, double width, double height){
        for (Gesture item : groupItems){
            if(item.isInsideBox(x, y, width, height)){
                return true;
            }
        }
        return false;
    }

    //convert the contents of the gesture to a string
    public String toString(){
        String out = "Group{";
        for(Gesture item : groupItems){
            out += item.toString();
        }
        out += "}";
        return out;
    }
}
