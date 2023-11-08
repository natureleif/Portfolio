package whitman.cs370proj.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Stack;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class VersionController {
    final static Clipboard clipboard = Clipboard.getSystemClipboard();
    private static Stack<String> undos = new Stack<String>();
    private static Stack<String> redos = new Stack<String>();
    public static Boolean hasChanged = false;

    public static void copy(Boolean isCut){
        Collection<Gesture> selected = new ArrayList<Gesture>();
        Collection<Gesture> toDelete = new ArrayList<Gesture>();
        for(Gesture gesture : CompManager.getComposition()){
            if(gesture.isSelected()){
                selected.add(gesture);
                if(isCut){
                    toDelete.add(gesture);
                }
            }
        }
        String selectedNotes = Converter.toString(selected);
        ClipboardContent content = new ClipboardContent();
        content.putString(selectedNotes);
        clipboard.setContent(content);
        for (Gesture gest: toDelete) {
            CompManager.remove(gest);
            CompManager.hide(gest);
        }
    }

    public static void paste(MainController mc){
        mc.deselectAllNotes();
        String comp = "";
        if (clipboard.hasString()) {
            comp = clipboard.getString();
        }
        Collection<Gesture> items = Converter.toCollection(comp, mc);
        for(Gesture gest : items){
            CompManager.add(gest);
            gest.show();
            gest.requestSelection();
        }
        hasChanged = true;
    }

    public static void saveVersion(){
        String version = Converter.toString(CompManager.getComposition());
        undos.push(version);
        redos.clear();
        hasChanged = true;
    }

    public static void saveVersion(String version){
        //System.out.println("Saving version: " + version);
        undos.push(version);
        redos.clear();
        hasChanged = true;
    }

    public static void undo(MainController mc){
        try{
            String oldVersion = CompManager.getCompositionString();
            String version = undos.pop();
            CompManager.setComposition(version);
            redos.push(oldVersion);
            //System.out.println("undo version: " + version);
        } catch (EmptyStackException e){
            System.out.println("nothing to undo");
        }
        hasChanged = true;
    }

    public static void redo(MainController mc){
        try{
            String oldVersion = CompManager.getCompositionString();
            String version = redos.pop();
            CompManager.setComposition(version);
            undos.push(oldVersion);
            System.out.println("redo version: " + version);
        } catch (EmptyStackException e){
            System.out.println("nothing to redo");
        }
        hasChanged = true;
    }
}
