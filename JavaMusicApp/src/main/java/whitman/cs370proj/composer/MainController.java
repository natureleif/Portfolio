package whitman.cs370proj.composer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import java.util.Collection;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.shape.Shape;

public class MainController {

    // private fields
    @FXML
    private Pane notePane;
    @FXML
    private MenuItem deleteMenuItem;
    @FXML
    private MenuItem cutMenuItem;
    @FXML
    private MenuItem copyMenuItem;
    @FXML
    private MenuItem pasteMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private MenuItem groupMenuItem;
    @FXML
    private MenuItem ungroupMenuItem;
    @FXML
    private RadioButton piano;
    private static final int WIDTH = 10000;
    private SelectionBox selectionBox;
    
    // status fields
    private boolean dragEvent;
    private boolean isMoveEdit;
    public static boolean isPlaying = false;
    private String oldversion;
    private Boolean senderSelected = false;

    // method to set up scroll pane and select default instrument
    @FXML
    public void initialize(){
        CompManager.setController(this);
        createLines();
        //set piano button to be clicked
        piano.setSelected(true);
        updateMenuStatus();
        pasteMenuItem.setDisable(true);
    }

    // creates lines within the scroll pane at 10 pixel intervals
    @FXML
    public void createLines() {
        for (int i = 1; i < 128; i++) {
            Line line = new Line(0, (i)*10, WIDTH, i*10);
            line.getStyleClass().add("line");
            CompManager.show(line);
        }
    }

    // event handler for the play menu item
    @FXML
    public void handlePlay() {
        CompPlayer.playComposition();
    }

    // event handler for the stop menu item
    @FXML
    public void handleStop() {
        CompPlayer.stopPlaying();
    }

    // event handler for the exit menu item
    @FXML
    public void handleExit() {
        System.exit(0);
    }

    @FXML 
    public void handleAbout() { 
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setHeaderText(null);
        alert.setContentText("This project was created by LJ Friedman, Andrew Kuhlken, Diego Quispe Vilcahuaman, and Henry Young. \n\nFind this project on Github to see more information about this project.");
        alert.showAndWait();
    }

    // event handler for the delete menu item, deletes all selected notes
    @FXML
    public void handleDelete(){
        VersionController.saveVersion();
        for(Gesture gest : CompManager.getSelectedCollection()){
            gest.delete();
        }
    }

    // Event handler for Undo
    @FXML
    public void handleUndo(){
        VersionController.undo(this);
    }

    // Event handler for Undo
    @FXML
    public void handleRedo(){
        VersionController.redo(this);
    }

    // event handler for select all menu item
    @FXML
    public void handleSelectAll(){
        Collection<Gesture> gestureList = CompManager.getComposition();
        for (Gesture gesture : gestureList) {
            gesture.select();
        }
        updateMenuStatus();
    }

    @FXML
    public void handleCut(){
        VersionController.saveVersion();
        VersionController.copy(true);
        pasteMenuItem.setDisable(false);
    }
    
    // event handler for copy menu item
    @FXML
    public void handleCopy(){
        VersionController.copy(false);
        pasteMenuItem.setDisable(false);
    }

    // event handler for paste menu item
    @FXML
    public void handlePaste(){
        VersionController.saveVersion();
        VersionController.paste(this);
    }

    // event handler for creating a group
    @FXML
    public void handleGroup(){
        VersionController.saveVersion();
        CompManager.createGroup();
    }

    //event handler for ungrouping a group
    @FXML
    public void handleUngroup(){
        VersionController.saveVersion();
        Collection<Gesture> tempList = CompManager.getSelectedCollection();
        for(Gesture gest : tempList){
            gest.ungroup();
        }
    }

    //event handler for adjusting the snapping interval of the composition
    @FXML
    public void handleSnapping(){
        TextInputDialog dialog = new TextInputDialog();  // create an instance
        dialog.setTitle("Change Snapping");
        dialog.setHeaderText("Enter a number between 1 and 500");
        dialog.setContentText("Current snapping interval: " + CompManager.getSnapping());
        Optional<String> result = dialog.showAndWait();  
        result.ifPresent(string -> {
            try{
                int i = Integer.valueOf(string);
                if(i > 0 && i <= 500){
                    CompManager.setSnapping(i);
                }
                else{ throw new NumberFormatException(); }
            }
            catch(NumberFormatException e){
                System.out.println("Inavlid Input");
            }
        });
    }

    // mouse drag event handler for the scroll pane, draws a selection box
    @FXML
    void selectionBox(MouseEvent event) {
        double mouseX = (double)event.getX();
        double mouseY = (double)event.getY();
        if (selectionBox == null){
            VersionController.saveVersion();
            selectionBox = new SelectionBox(mouseX, mouseY, this);
        }
        else{
            selectionBox.updateSelectionBox(mouseX, mouseY);
        }
        selectionBox.batchSelect(event.isControlDown());
    }

    // event handler for mouse release on the scroll pane
    // adds notes if it was a click and cleans up selection box if it was a drag
    @FXML
    void handleMouseRelease(MouseEvent event) {
        if(selectionBox == null){ //if there was no drag (only click)
            if (isPlaying == true){
                handleStop();
                return;
            }
            addNote((int)event.getX(),(int)event.getY(), event.isControlDown());
        }
        else{ // if this is the end of a drag event
            selectionBox.deleteSelectionBox(this);
            selectionBox = null;
        }
        updateMenuStatus();
    }

    // event handler for the instrument selection panel
    // updates the instrument field of main controller
    @FXML
    public void handleInstrument(ActionEvent event) {
        Instrument.setInstrument((String)((RadioButton)event.getSource()).getId());
        System.out.println(Instrument.getInstrument());
    }

    // event handler for the mouse press on a note
    // initializes temp variables of note
    // selects and deselects notes appropriately
    @FXML
    void handleNoteClicked(MouseEvent event, Gesture sender){
        this.oldversion = Converter.toString(CompManager.getComposition());
        this.senderSelected = sender.isSelected();
        // Need to have Group send this event handler as well.
        dragEvent = false;
        if (isPlaying == true) {
            handleStop();
            return;
        }
        isMoveEdit = sender.isMoveEdit((int)event.getX());
        // calculate the relative positions of notes to the mouse and store it
        for(Gesture gest : CompManager.getSelectedCollection()){
            gest.startEdit((int)event.getX(), (int)event.getY());
        }
        if(!sender.isSelected() && !event.isControlDown()){
            deselectAllNotes();
        }
        sender.requestSelection();
        updateMenuStatus();
    }

    // event handler for drag event in note
    // move or lengthen selected notes appropriately
    @FXML
    void handleNoteDragged(MouseEvent event, Gesture sender){
        dragEvent = true;
        double mouseX = (int)event.getX();
        double mouseY = (int)event.getY();
        double editDistance = mouseX - (sender.getX() + sender.oldWidth);
        // edit all selected notes with the mouse on entering a drag
        Collection<Gesture> tempList = CompManager.getSelectedCollection();
        for(Gesture gest : tempList){
            // calculate the relative positions of notes to the mouse and store it
            gest.startEdit((int)event.getX(), (int)event.getY());
            if(isMoveEdit){
                gest.editPos(mouseX, mouseY);
            }
            else{
                gest.editLength(editDistance);
            }
        }
    }

    // event handler for mouse release on note
    // snap notes to correct position
    // select and deselect notes appropriately
    @FXML
    void handleNoteDropped(MouseEvent event, Gesture sender){
        if(dragEvent){ VersionController.saveVersion(oldversion); }
        // snap all selected notes into position
        for( Gesture gest : CompManager.getComposition()){
            gest.snap();
            gest.finishEdit();
        }
        if(!dragEvent && !event.isControlDown()){
            if(!this.senderSelected || CompManager.getSelectedCollection().size() > 1){ // sender always selected on mouse release
                VersionController.saveVersion(oldversion);
                deselectAllNotes();
                sender.requestSelection();
            } 
        }
        updateMenuStatus();
    }


    @FXML 
    void handleSave(){
        fileManager.save();
    }

    @FXML
    void handleSaveAs(){
        fileManager.saveAs();
    }

    @FXML
    void handleOpen(){
        fileManager.open();
    }

    @FXML
    public void handleNew() {
        fileManager.newFile();
    }
    

    // adds a note to the composition and to the scroll pane
    public void addNote(int x, int y, boolean ctrl){
        VersionController.saveVersion();
        if (!ctrl){
            deselectAllNotes();
        }
        Gesture note = new Note(x, y, 100, Instrument.getInstrument(), true, this);
        CompManager.add(note);
        CompManager.show(note);
    }

    // helper function to deselect all notes
    public void deselectAllNotes(){
        for(Gesture gest : CompManager.getSelectedCollection()){
            gest.deselect();
        }
    }

    public void addToPane(Shape shape){
        notePane.getChildren().add(shape);
    }

    public void removeFromPane(Shape shape){
        notePane.getChildren().remove(shape);
    }

    private void updateMenuStatus(){
        if(CompManager.getSelectedCollection().isEmpty()){
            deleteMenuItem.setDisable(true);
            cutMenuItem.setDisable(true);
            copyMenuItem.setDisable(true);
            groupMenuItem.setDisable(true);
            ungroupMenuItem.setDisable(true);
        }
        else{
            deleteMenuItem.setDisable(false);
            cutMenuItem.setDisable(false);
            copyMenuItem.setDisable(false);
            groupMenuItem.setDisable(false);
            ungroupMenuItem.setDisable(false);
        }
    }
}