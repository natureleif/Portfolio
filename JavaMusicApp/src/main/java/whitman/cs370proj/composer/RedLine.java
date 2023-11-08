package whitman.cs370proj.composer;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class RedLine {
    private static Line rLine = new Line();
    private static TranslateTransition moveRLine = new TranslateTransition();

    public static void play() {
        reset();
        moveRLine.setOnFinished(e -> {CompManager.hide(rLine); CompPlayer.isPlaying = false;});
        int duration = CompPlayer.playLength();
        moveRLine.setDuration(Duration.seconds(duration/100.0));
        moveRLine.setNode(rLine);
        moveRLine.setByX(duration);
        moveRLine.setInterpolator(Interpolator.LINEAR);
        CompManager.show(rLine);
        moveRLine.playFromStart();
    }

    public static void reset() {
        CompManager.hide(rLine);
        rLine = new Line();
        moveRLine.stop();
        rLine.setStroke(Color.RED);
        rLine.setStartX(0);
        rLine.setStartY(0);
        rLine.setEndX(0);
        rLine.setEndY(2000);
        moveRLine.setNode(null);
        moveRLine.setDuration(Duration.millis(0));
        moveRLine.setByX(0);
    }
}