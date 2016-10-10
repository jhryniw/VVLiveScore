package ca.ftcalberta.vvlivescore;

import android.graphics.Path;

import java.lang.reflect.Type;

import static ca.ftcalberta.vvlivescore.Alliance.BLUE;
import static ca.ftcalberta.vvlivescore.OpMode.AUTONOMOUS;
import static ca.ftcalberta.vvlivescore.OpMode.TELEOP;
import static ca.ftcalberta.vvlivescore.ScoreType.CENTRE_VORTEX;
import static ca.ftcalberta.vvlivescore.ScoreType.CORNER_VORTEX;

/**
 * Created by s on 10/10/2016.
 */
enum Alliance{
    RED,
    BLUE
}

enum ScoreType {
    CORNER_VORTEX,
    CENTRE_VORTEX
}

enum OpMode {
    AUTONOMOUS,
    TELEOP
}

public class ScoreState {

    private int score = 0;
    private Alliance alliance = BLUE;
    private ScoreType type = CORNER_VORTEX;
    private OpMode opMode = AUTONOMOUS;
    private int incrementAmount = 1;

    public ScoreState(int s, Alliance a, ScoreType t, OpMode o){
        score = s;
        alliance = a;
        type = t;
        opMode = o;
        setIncrement();
    }

    public ScoreState(Alliance a, ScoreType t){
        alliance = a;
        type = t;
        setIncrement();
    }

    public ScoreState(){
        setIncrement();
    }

    public void setOpMode(OpMode o){
        opMode = o;
        setIncrement();
    }

    public void setScoreType(ScoreType s){
        type = s;
        setIncrement();
    }

    public Alliance getAlliance() {
        return alliance;
    }
    public void setAlliance(Alliance a){
        alliance = a;
        setIncrement();
    }

    public int getScore(){
        return score;
    }

    public void setIncrement(){
        if(type == CORNER_VORTEX && opMode == AUTONOMOUS){
            incrementAmount = 5;
        } else if (type == CORNER_VORTEX && opMode == TELEOP){
            incrementAmount = 5;
        } else if (type == CENTRE_VORTEX && opMode == AUTONOMOUS){
            incrementAmount = 5;
        } else if (type == CENTRE_VORTEX && opMode == TELEOP){
            incrementAmount = 5;
        }
    }

    public int increase(){
        if(score - incrementAmount < 0){
            score = 0;
        } else {
            score += incrementAmount;
        }
        return score ;
    }

    public int decrease(){
        score -= incrementAmount;
        return score ;
    }
}
