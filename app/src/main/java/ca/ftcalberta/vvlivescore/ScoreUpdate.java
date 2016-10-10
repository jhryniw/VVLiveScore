package ca.ftcalberta.vvlivescore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * ScoreUpdate Created by s on 10/10/2016.
 */

public class ScoreUpdate {

    private UUID phoneId;
    private UUID scoreId;
    private ScoreType type;
    private Alliance alliance;
    private OpMode opMode;
    private Long timeStamp;
    private int score;

    public ScoreUpdate(UUID pId, ScoreType t, Alliance a, OpMode o, int s){
        phoneId = pId;
        scoreId = UUID.randomUUID();
        type = t;
        alliance = a;
        opMode = o;
        timeStamp = System.currentTimeMillis();
        score = s;
    }

    public JSONObject getUpdateJSON() throws JSONException {
        JSONObject update = new JSONObject();
        update.put("phoneId", phoneId.toString());
        update.put("scoreId", scoreId.toString());
        update.put("type",type);
        update.put("alliance",alliance);
        update.put("opMode",opMode);
        update.put("timeStamp",timeStamp);
        update.put("score",score);
        return update;
    }

}
