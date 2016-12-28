package ca.ftcalberta.vvlivescore;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by James on 2016-12-28.
 */

enum VortexType {
    CORNER_VORTEX,
    CENTRE_VORTEX
}

public class VortexState {
    private ScoreUpdater updater;

    private int autoScore = 0;
    private int teleScore = 0;
    private int count = 0;

    private OpMode opMode;
    private VortexType type;
    private Alliance alliance;

    private int incrementAmount;

    /*
     *  Constructors
     */
    public VortexState(Alliance a, VortexType v) {
        this(a, v, OpMode.AUTONOMOUS);
    }
    public VortexState(Alliance a, VortexType v, OpMode o) {
        alliance = a;
        opMode = o;
        type = v;
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
        return type;
    }
    public void setVortexType(VortexType v){
        type = v;
        setIncrement();
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
        return opMode == OpMode.AUTONOMOUS ? autoScore : teleScore;
    }
    public int getCount() { return count; }

    public void setIncrement() {
        if(type == VortexType.CORNER_VORTEX && opMode == OpMode.AUTONOMOUS){
            incrementAmount = 5;
        } else if (type == VortexType.CORNER_VORTEX && opMode == OpMode.TELEOP){
            incrementAmount = 1;
        } else if (type == VortexType.CENTRE_VORTEX && opMode == OpMode.AUTONOMOUS){
            incrementAmount = 15;
        } else if (type == VortexType.CENTRE_VORTEX && opMode == OpMode.TELEOP){
            incrementAmount = 5;
        }
    }

    /*
     *  Scoring Methods
     */

    public void increment() {
        if(opMode == OpMode.AUTONOMOUS)
            autoScore += incrementAmount;
        else
            teleScore += incrementAmount;

        count++;
        updateState();
    }

    public void decrement() {
        if(opMode == OpMode.AUTONOMOUS)
            autoScore -= incrementAmount;
        else
            teleScore -= incrementAmount;

        count--;

        if(autoScore < 0)
            autoScore = 0;
        if(teleScore < 0) //Could be else if...
            teleScore = 0;
        if(count < 0)
            count = 0;

        updateState();
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

    private static String vortexTypeToString(VortexType v) {
        return v == VortexType.CENTRE_VORTEX ? "Centre" : "Corner";
    }

    private void updateState() {
        JSONObject updateJson = new JSONObject();

        try {
            updateJson.put("\"" + ScoreUpdater.getScoreType(opMode, alliance, vortexTypeToString(type)) +"\"", getScore());
            //updateJson.put("timeStamp", System.currentTimeMillis());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        updater.state = updateJson;
    }
}
