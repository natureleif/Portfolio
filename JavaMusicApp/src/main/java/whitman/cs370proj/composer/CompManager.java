package whitman.cs370proj.composer;

import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.shape.Shape;

public class CompManager {
    private static Collection<Gesture> composition = new ArrayList<Gesture>();
    private static MainController controller;
    private static int snap = 1;

    public static void setController(MainController mc){
        controller = mc;
    }

    public static String getCompositionString(){
        return Converter.toString(composition);
    }

    public static Collection<Gesture> getComposition(){
        return composition;
    }

    public static Collection<Gesture> getSelectedCollection(){
        Collection<Gesture> selected = new ArrayList<>();
        for(Gesture gest : composition){
            if(gest.isSelected()){
                selected.add(gest);
            }
        }
        return selected;
    }

    public static void setComposition(String newComposition){
        hideComposition();
        composition = Converter.toCollection(newComposition, controller);
        for(Gesture gest : composition){
            gest.show();
        }
    }

    public static void hideComposition(){
        for(Gesture gest : composition){
            gest.hide();
        }
    }

    public static void add(Gesture item){
        composition.add(item);
    }

    public static void show(Shape shape){
        controller.addToPane(shape);
    }

    public static void remove(Gesture item){
        composition.remove(item);
        controller.removeFromPane(item);
    }

    public static void hide(Shape shape){
        controller.removeFromPane(shape);
    }

    public static void createGroup(){
        Collection<Gesture> items = getSelectedCollection();
        for(Gesture gest : items){
            composition.remove(gest);
        }
        Group newGroup = new Group(items);
        composition.add(newGroup);
        controller.addToPane(newGroup);
        newGroup.toBack();
    }

    public static void setSnapping(int i){
        snap = i;
    }

    public static int getSnapping(){
        return snap;
    }
}
