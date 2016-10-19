package ca.ftcalberta.vvlivescore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

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

    private UUID phoneId;

    private ScoreUpdater updater;

    private int score = 0;
    private Alliance alliance;
    private ScoreType type;
    private OpMode opMode;
    private int incrementAmount;

    /*
     *  Constructors
     */

    public ScoreState() {
        this(0, Alliance.BLUE, ScoreType.CENTRE_VORTEX, OpMode.AUTONOMOUS);
    }

    public ScoreState(int s, Alliance a, ScoreType t, OpMode o) {
        score = s;
        alliance = a;
        type = t;
        opMode = o;
        setIncrement();

        phoneId = UUID.randomUUID();

        updater = new ScoreUpdater();
        updater.launch();
    }

    /*
     *  Property Methods
     */

    public OpMode getOpMode() {
        return opMode;
    }
    public void setOpMode(OpMode o){
        opMode = o;
        setIncrement();
        updateState();
    }

    public ScoreType getScoreType() {
        return type;
    }
    public void setScoreType(ScoreType s){
        type = s;
        setIncrement();
        updateState();
    }

    public Alliance getAlliance() {
        return alliance;
    }
    public void setAlliance(Alliance a){
        alliance = a;
        setIncrement();
        updateState();
    }

    public int getScore(){
        return score;
    }

    public void setIncrement(){
        if(type == CORNER_VORTEX && opMode == AUTONOMOUS){
            incrementAmount = 5;
        } else if (type == CORNER_VORTEX && opMode == TELEOP){
            incrementAmount = 1;
        } else if (type == CENTRE_VORTEX && opMode == AUTONOMOUS){
            incrementAmount = 15;
        } else if (type == CENTRE_VORTEX && opMode == TELEOP){
            incrementAmount = 5;
        }
    }

    /*
     *  Scoring Methods
     */

    public int increase(){
        score += incrementAmount;

        updateState();
        return score;
    }

    public int decrease(){
        if(score - incrementAmount < 0){
            score = 0;
        } else {
            score -= incrementAmount;
        }

        updateState();
        return score;
    }

    /*
     *  Update Methods
     */

    private void updateState() {
        updater.state = serializeState();
    }

    private JSONObject serializeState() {
        JSONObject updateJson = new JSONObject();

        try {
            updateJson.put("phoneId", phoneId.toString());
            updateJson.put("scoreId", UUID.randomUUID().toString());
            updateJson.put("timeStamp", System.currentTimeMillis());

            updateJson.put("Alliance", alliance);
            updateJson.put("Vortex", type);
            updateJson.put("Mode", opMode);
            updateJson.put("TotalScore", score);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return updateJson;
    }
}
