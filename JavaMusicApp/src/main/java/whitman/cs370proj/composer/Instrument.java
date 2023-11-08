package whitman.cs370proj.composer;

public class Instrument {

    private static String[] instrumentStrings = {"piano", "harpsichord", "marimba",
    "organ", "accordion", "guitar", "violin", "frenchHorn"};
    private static int currentInstrument = 0;

    public static int getInstrument() {
        return currentInstrument;
    }

    public static void setInstrument(String source) {
        for (int i = 0; i < instrumentStrings.length; i++) {
            if (instrumentStrings[i].equals(source)) {
                currentInstrument = i;
                break;
            }
        }
    }
}
