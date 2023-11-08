package whitman.cs370proj.composer;

import java.util.ArrayList;
import java.util.Collection;

public class Converter {

    // Correct String Format: 

    // Type Formats
        // Group{}
        // Note{x,y,width,instrument,isSelected}

    // Examples:
        // [Note{175,170,100,0,true}Note{159,200,100,0,true}]
        // [Group{Note{175,170,100,0,true}Note{159,200,100,0,true}}]
        // [Group{Group{Note{275,450,100,3,true}Note{375,490,100,3,true}}Note{375,580,100,3,true}}]
        // [Group{Group{Note{99,200,100,0,true}Note{173,230,100,0,true}}Note{134,280,100,0,true}}Note{339,290,100,0,true}]
    

    public static String toString(Collection<Gesture> items){
        String out = "[";
        for( Gesture item : items){
            out += item.toString();
        }
        out += "]";
        return out;
    }

    public static Collection<Gesture> toCollection(String s, MainController mc){
        Collection<Gesture> items = getContents(s, mc);
        return items;
    }

    private static Gesture parseGroup(String s, MainController mc){
        ArrayList<Gesture> items = getContents(s, mc);
        return new Group(items);
    }

    private static ArrayList<Gesture> getContents(String s, MainController mc){
        s = s.substring(1, s.length() - 1);
        ArrayList<Gesture> items = new  ArrayList<Gesture>();
        String word = "";
        int i = 0;
        while(i < s.length()-1){
            word += s.charAt(i);
            if( word.compareTo("Group") == 0 ){
                i+=2;
                word = "{";
                int open = 1;
                while(open > 0){
                    if(s.charAt(i) == '{'){ open++; }
                    if(s.charAt(i) == '}'){ open--; }
                    word += s.charAt(i);
                    i++;
                }
                i--;
                items.add(parseGroup(word, mc));
                word = "";
            }
            if( word.compareTo("Note") == 0 ){
                i++;
                word = "";
                Boolean done = false;
                while(!done){
                    if(s.charAt(i) == '}'){ done = true; }
                    word += s.charAt(i);
                    i++;
                }
                items.add(parseNote(word, mc));
                word = "";
            }
            else{ i++; }
        }
        return items;
    }

    private static Gesture parseNote(String s, MainController mc){
        s = s.substring(1, s.length() - 1);
        String[] a = s.split(",");
        int x = Integer.valueOf(a[0]);
        int y = Integer.valueOf(a[1]);
        int width = Integer.valueOf(a[2]);
        int inst = Integer.valueOf(a[3]);
        Boolean selected = Boolean.valueOf(a[4]);
        return new Note(x,y,width,inst,selected,mc);
    }
}
