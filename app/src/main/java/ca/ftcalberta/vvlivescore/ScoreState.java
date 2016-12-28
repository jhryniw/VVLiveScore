package ca.ftcalberta.vvlivescore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static ca.ftcalberta.vvlivescore.Alliance.*;
import static ca.ftcalberta.vvlivescore.OpMode.*;
import static ca.ftcalberta.vvlivescore.ScoreType.*;
import static ca.ftcalberta.vvlivescore.VortexType.*;

/**
 * Created by s on 10/10/2016.
 */

enum ScoreType {
    RedAutoCentre,
    BlueAutoCentre,
    RedAutoCorner,
    BlueAutoCorner,
    RedParking,
    BlueParking,
    RedAutoBeacons,
    BlueAutoBeacons,
    RedTeleCentre,
    BlueTeleCentre,
    RedTeleCorner,
    BlueTeleCorner,
    RedTeleBeacons,
    BlueTeleBeacons,
    RedCapBall,
    BlueCapBall
}

public class ScoreState {

    private UUID phoneId = UUID.randomUUID();

    private ScoreUpdater updater;

    private int score = 0;

    private Alliance alliance;
    private VortexType vortexType;
    private ScoreType type;
    private OpMode opMode;
    private int incrementAmount;

    /*
     *  Constructors
     */

    public ScoreState() {
        this(Alliance.BLUE, CORNER_VORTEX, OpMode.AUTONOMOUS);
    }

    public ScoreState(Alliance a, VortexType v, OpMode o) {
        alliance = a;
        opMode = o;
        vortexType = v;
        setIncrement();

        updater = new ScoreUpdater();
        updateState();
    }

    /*
     *  Getters and Setters
     */

    public OpMode getOpMode() {
        return opMode;
    }
    public void setOpMode(OpMode o){
        opMode = o;
        setIncrement();
        updateState();
    }

    public VortexType getVortexType() {
        return vortexType;
    }
    public void setVortexType(VortexType v){
        vortexType = v;
        setIncrement();
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
        if(vortexType == CORNER_VORTEX && opMode == AUTONOMOUS){
            type = RedAutoCorner;
            incrementAmount = 5;
        } else if (vortexType == CORNER_VORTEX && opMode == TELEOP){
            type = RedTeleCorner;
            incrementAmount = 1;
        } else if (vortexType == CENTRE_VORTEX && opMode == AUTONOMOUS){
            type = RedAutoCentre;
            incrementAmount = 15;
        } else if (vortexType == CENTRE_VORTEX && opMode == TELEOP){
            type = RedTeleCentre;
            incrementAmount = 5;
        }
        if(alliance == BLUE){
            type = ScoreType.values()[type.ordinal() + 1];
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

    public void launch() {
        updater.launch();
    }

    public void halt() {
        updater.halt();
    }

    private void updateState() {
        updater.state = serializeState();
    }

    private JSONObject serializeState() {
        JSONObject updateJson = new JSONObject();

        try {
            updateJson.put("\"" + type.toString() +"\"", score);
            //updateJson.put("phoneId", phoneId.toString());
            //updateJson.put("scoreId", UUID.randomUUID().toString());
            //updateJson.put("timeStamp", System.currentTimeMillis());

            //updateJson.put("Alliance", alliance);
            //updateJson.put("Vortex", type);
            //updateJson.put("Mode", opMode);
            //updateJson.put("TotalScore", score);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return updateJson;
    }
}
