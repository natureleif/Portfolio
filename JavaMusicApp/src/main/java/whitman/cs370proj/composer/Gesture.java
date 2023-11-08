package whitman.cs370proj.composer;

import javafx.scene.shape.Rectangle;

public abstract class Gesture extends Rectangle{

    public Gesture parent;
    public double oldWidth;

    // Sets difX, difY, and OldWidth
    public abstract void startEdit(double mouseX, double mouseY);

    // Sets difX, difY, and OldWidth back to 0
    public abstract void finishEdit();

    // Edits the length of the note using the mouse X
    public abstract void editLength(double editDistance);

    // Sets the position of the notes relitive to the mouse
    public abstract void editPos(double x, double y);

    // Snaps gesture to the grid
    public abstract void snap();

    // Adds the gesture to the midi player
    public abstract void addToPlayer();

    // Request selection and check for parents
    public abstract void requestSelection();

    // Selects the gesture and does not call on parents
    public abstract void select();

    // De-selects the gesture
    public abstract void deselect();

    // Ungroups the gesture
    public abstract void ungroup();

    // Delete the gesture
    public abstract void delete();

    // Hide the gesture
    public abstract void hide();

    // Show the gesture
    public abstract void show();

    // Returns true if the edit is a move edit
    public abstract boolean isMoveEdit(double mouseX);

    // Returns whether or not the gesture is selected
    public abstract boolean isSelected();

    // Returns the maximum tick in the gesture
    public abstract int getMaxTick();

    // return true if gensure intersects box
    public abstract boolean isInsideBox(double x, double y, double width, double height);

    //convert the contents of the gesture to a string
    public abstract String toString();
}
