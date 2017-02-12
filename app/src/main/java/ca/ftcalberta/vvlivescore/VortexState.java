package ca.ftcalberta.vvlivescore;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by James on 2016-12-28.
 */

enum VortexType {
    CORNER_VORTEX,
    CENTRE_VORTEX;

    @Override
    public String toString() {
        switch (this) {
            case CORNER_VORTEX:
                return "Corner";
            case CENTRE_VORTEX:
                return "Centre";
            default:
                return super.toString();
        }
    }
}

public class VortexState {
    private ScoreUpdater updater;

    private int autoScore = 0;
    private int teleScore = 0;
    private int autoCount = 0;
    private int teleCount = 0;

    private OpMode opMode;
    private VortexType type;
    private Alliance alliance;

    private int incrementAmount;

    /*
     *  Constructors
     */
    public VortexState() {
        this(Alliance.BLUE, VortexType.CORNER_VORTEX, OpMode.AUTONOMOUS);
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
    public int getCount() { return opMode == OpMode.AUTONOMOUS ? autoCount : teleCount; }

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
        if(opMode == OpMode.AUTONOMOUS) {
            autoScore += incrementAmount;
            autoCount++;
        }
        else {
            teleScore += incrementAmount;
            teleCount++;
        }

        updateState();
    }

    public void decrement() {
        if(opMode == OpMode.AUTONOMOUS) {
            autoScore -= incrementAmount;
            autoCount--;

            if (autoScore < 0)
                autoScore = 0;
            if (autoCount < 0)
                autoCount = 0;
        }
        else {
            teleScore -= incrementAmount;
            teleCount--;

            if (teleScore < 0)
                teleScore = 0;
            if (teleCount < 0)
                teleCount = 0;
        }

        updateState();
    }

    /*
     *  Update Methods
     */

    void launch() {
        updater.launch();
    }

    void halt() {
        updater.halt();
    }

    void reset() {
        autoScore = 0;
        autoCount = 0;

        teleScore = 0;
        teleCount = 0;

        updater.sendScore(alliance, OpMode.AUTONOMOUS, type.toString(), 0);
        updater.sendScore(alliance, OpMode.TELEOP, type.toString(), 0);
    }

    private void updateState() {
        /*JSONObject updateJson = new JSONObject();

        try {
            updateJson.put("\"" + ScoreUpdater.getScoreType(opMode, alliance, type.toString()) +"\"", getScore());
            //updateJson.put("timeStamp", System.currentTimeMillis());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        updater.setState(updateJson);*/

        updater.sendScore(alliance, opMode, type.toString(), getScore());
    }
}
