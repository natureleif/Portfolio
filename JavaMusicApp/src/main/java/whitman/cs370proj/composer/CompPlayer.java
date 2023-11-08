package whitman.cs370proj.composer;

import javax.sound.midi.ShortMessage;

public class CompPlayer {
    private static final MidiPlayer player = new MidiPlayer(100, 60);
    public static Boolean isPlaying = false;

    public static void playComposition(){
        player.clear();
        stopPlaying();
        setMidiChannels();
        for (Gesture gesture : CompManager.getComposition()) {
            gesture.addToPlayer();
        }
        isPlaying = true;
        RedLine.reset();
        player.play();
        RedLine.play();
    }

    public static void stopPlaying(){
        isPlaying = false;
        player.stop();
        RedLine.reset();
    }

    public static void addNoteToPlayer(Note note){
        player.addNote(note.getPitch(), 100, (int)note.getX(), (int)note.getWidth(), note.getInstrument(), 0);
    }

    // helper function for calculating the play length of notes in the scroll pane
    public static int playLength() {
        int max = 0;
        for (Gesture gesture : CompManager.getComposition()){
            if(gesture.getMaxTick() > max) { max = gesture.getMaxTick();}
        }
        return max;
    }

    // sets the correct channels for the midi player
    private static void setMidiChannels() {
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + 0, 0, 0, 0, 0); //piano
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + 1, 6, 0, 0, 0); //harpsichord
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + 2, 12, 0, 0, 0); //marimba
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + 3, 19, 0, 0, 0); //church organ
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + 4, 22, 0, 0, 0); //accordion
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + 5, 25, 0, 0, 0); //guitar
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + 6, 40, 0, 0, 0); //violin
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + 7, 60, 0, 0, 0); //french horn
    }
}
