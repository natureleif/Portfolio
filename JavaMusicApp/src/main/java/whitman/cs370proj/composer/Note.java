package whitman.cs370proj.composer;
import javafx.scene.paint.Color;


public class Note extends Gesture{
    // private fields
    private boolean selected;
    private int instrument;
    
    // temporary variables
    private double difX;
    private double difY;
    private Boolean hasActiveEdit = false;

    // Constructors:
    public Note(int x, int y, int width, int instrument, Boolean isSelected, MainController mc){
        setX(x);
        setY(y/10 * 10);
        setHeight(10);
        setWidth(width);
        snap();
        this.getStyleClass().add("note" + Integer.toString(instrument));
        setOnMousePressed(event -> {mc.handleNoteClicked(event, this); event.consume();});
        setOnMouseDragged(event -> {mc.handleNoteDragged(event, this); event.consume();});
        setOnMouseReleased(event -> {mc.handleNoteDropped(event, this); event.consume();});
        if(isSelected){ this.select(); }
        this.instrument = instrument;
        this.parent = null;
    }

    // getter for the note's instrument
    public int getInstrument() {
        return this.instrument;
    }

    // returns the notes pitch
    public int getPitch(){
        return (int)(127 - getY() / 10);
    }

    // Sets difX, difY, and OldWidth
    public void startEdit(double mouseX, double mouseY){
        if(!hasActiveEdit){
            this.difX = getX() - mouseX;
            this.difY = getY() - mouseY;
            this.oldWidth = getWidth();
            this.hasActiveEdit = true;
        }
    }

    // Sets difX, difY, and OldWidth back to 0
    public void finishEdit(){
        this.difX = 0;
        this.difY = 0;
        this.oldWidth = 0;
        this.hasActiveEdit = false;
    }

    // Edits the length of the note using the mouse X
    public void editLength(double editDistance){
        double newWidth = this.oldWidth + editDistance;
        if(newWidth > 5){
            setWidth(newWidth);
        }
    }

    // Sets the position of the notes relative to the mouse
    public void editPos(double x, double y){
        setX(x + this.difX);
        setY(y + this.difY);
    }

    // Snaps gesture to the grid
    public void snap(){
        int snap = CompManager.getSnapping();
        setX((int)(getX() + (snap / 2)) / snap * snap);
        setY((int)(getY() + 5) / 10 * 10);
        setWidth((int)(getWidth() + (snap / 2)) / snap * snap);
        if(getWidth() < 5) { setWidth(5); }
    }

    // Adds the gesture to the midi player
    public void addToPlayer(){
        CompPlayer.addNoteToPlayer(this);
    }

    // Request selection and check for parents
    public void requestSelection(){
        if(parent != null){
            parent.requestSelection();
        }
        else{
            select();
        }
    }

    // Selects the gesture and does not call on parents
    public void select(){
        selected = true;
        this.setStrokeWidth(2.0);
        this.setStroke(Color.BLACK);
    }

    // De-selects the gesture
    public void deselect(){
        selected = false;
        this.setStrokeWidth(0.0);
        this.setStroke(Color.TRANSPARENT);
    }

    // Ungroups the gesture
    public void ungroup(){
        // Does nothing
    }

    // Delete the gesture
    public void delete(){
        CompManager.remove(this);
        CompManager.hide(this);
    }

    // Hide the gesture
    public void hide(){
        CompManager.hide(this);
    }

    // Show the gesture
    public void show(){
        CompManager.show(this);
    }

    // Returns true if the edit is a move edit
    public boolean isMoveEdit(double mouseX){
        return (mouseX < getX() + getWidth() - 5);
    }

    // Returns whether or not the gesture is selected
    public boolean isSelected(){
        return this.selected;
    }

    // Returns the maximum tick in the gesture
    public int getMaxTick(){
        return (int)(getX() + getWidth());
    }

    // return true if gensure intersects box
    public boolean isInsideBox(double x, double y, double width, double height){
        if(x < getX() + getWidth() && x + width > getX() && y < getY() + getHeight() && y + height > getY()){
            return true;
        }
        return false;
    }

    //convert the contents of the gesture to a string
    public String toString(){
        String out = "Note{";
        out += String.valueOf((int)getX()) + ",";
        out += String.valueOf((int)getY()) + ",";
        out += String.valueOf((int)getWidth()) + ",";
        out += String.valueOf((int)instrument) + ",";
        out += String.valueOf(isSelected()) + "}";
        return out;
    }
}
