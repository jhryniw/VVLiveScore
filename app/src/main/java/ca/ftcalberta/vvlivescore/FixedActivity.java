package ca.ftcalberta.vvlivescore;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class FixedActivity extends Activity {

    private int beaconValue = 30;
    OpMode opMode = OpMode.AUTONOMOUS;
    private int redBeaconCount = 0;
    private int blueBeaconCount = 0;
    private int redParkingScore = 0;
    private int blueParkingScore = 0;
    private int redCapScore = 0;
    private int blueCapScore = 0;
    private Alliance beacon1Alliance = Alliance.NONE;
    private Alliance beacon2Alliance = Alliance.NONE;
    private Alliance beacon3Alliance = Alliance.NONE;
    private Alliance beacon4Alliance = Alliance.NONE;
    private ScoreUpdater updater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed);

        final Button btnOpMode = (Button) findViewById(R.id.btnOpMode);
        btnOpMode.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View view) {
                 //Swap OpMode
                 if(opMode == OpMode.AUTONOMOUS) {
                     opMode = OpMode.TELEOP;
                     beaconValue = 10;
                     sendScore(Alliance.RED, opMode, "Beacons", redBeaconCount * beaconValue);
                     sendScore(Alliance.BLUE, opMode, "Beacons", blueBeaconCount * beaconValue);
                     btnOpMode.setText("TeleOp");
                 }
                 else {
                     opMode = OpMode.AUTONOMOUS;
                     beaconValue = 30;
                     btnOpMode.setText("Autonomous");
                 }

                 return true;
             }
        });

        final Button btnBeacon1 = (Button) findViewById(R.id.btnBeacon1);
        btnBeacon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable colour = btnBeacon1.getBackground();

                if(beacon1Alliance == Alliance.NONE){
                    redBeaconCount++;
                } else if(beacon1Alliance == Alliance.RED){
                    redBeaconCount--;
                    blueBeaconCount++;
                } else if(beacon1Alliance == Alliance.BLUE){
                    blueBeaconCount--;
                    redBeaconCount++;
                }
                sendScore(Alliance.RED, opMode, "Beacons", redBeaconCount * beaconValue);
                sendScore(Alliance.BLUE, opMode, "Beacons", blueBeaconCount * beaconValue);
            }
        });

        final Button btnBeacon2 = (Button) findViewById(R.id.btnBeacon1);
        btnBeacon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable colour = btnBeacon1.getBackground();

                if(beacon2Alliance == Alliance.NONE){
                    redBeaconCount++;
                } else if(beacon2Alliance == Alliance.RED){
                    redBeaconCount--;
                    blueBeaconCount++;
                } else if(beacon2Alliance == Alliance.BLUE){
                    blueBeaconCount--;
                    redBeaconCount++;
                }
                sendScore(Alliance.RED, opMode, "Beacons", redBeaconCount * beaconValue);
                sendScore(Alliance.BLUE, opMode, "Beacons", blueBeaconCount * beaconValue);
            }
        });

        final Button btnBeacon3 = (Button) findViewById(R.id.btnBeacon1);
        btnBeacon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable colour = btnBeacon1.getBackground();

                if(beacon3Alliance == Alliance.NONE){
                    redBeaconCount++;
                } else if(beacon3Alliance == Alliance.RED){
                    redBeaconCount--;
                    blueBeaconCount++;
                } else if(beacon3Alliance == Alliance.BLUE){
                    blueBeaconCount--;
                    redBeaconCount++;
                }
                sendScore(Alliance.RED, opMode, "Beacons", redBeaconCount * beaconValue);
                sendScore(Alliance.BLUE, opMode, "Beacons", blueBeaconCount * beaconValue);
            }
        });

        final Button btnBeacon4 = (Button) findViewById(R.id.btnBeacon1);
        btnBeacon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable colour = btnBeacon1.getBackground();

                if(beacon4Alliance == Alliance.NONE){
                    redBeaconCount++;
                } else if(beacon4Alliance == Alliance.RED){
                    redBeaconCount--;
                    blueBeaconCount++;
                } else if(beacon4Alliance == Alliance.BLUE){
                    blueBeaconCount--;
                    redBeaconCount++;
                }
                sendScore(Alliance.RED, opMode, "Beacons", redBeaconCount * beaconValue);
                sendScore(Alliance.BLUE, opMode, "Beacons", blueBeaconCount * beaconValue);
            }
        });

        final Button btnRedParkingScore = (Button) findViewById(R.id.btnRedParkingScore);
        btnRedParkingScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(opMode == OpMode.TELEOP){
                    return;
                }
                if(redParkingScore == 0){
                    redParkingScore = 5;
                } else if(redParkingScore == 5) {
                    redParkingScore = 10;
                } else {
                    redParkingScore = 0;
                }
                sendScore(Alliance.RED, opMode, "Beacons", redParkingScore);
                btnRedParkingScore.setText(Integer.toString(redParkingScore));
            }
        });

        final Button btnBlueParkingScore = (Button) findViewById(R.id.btnBlueParkingScore);
        btnBlueParkingScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(opMode == OpMode.TELEOP){
                    return;
                }
                if(blueParkingScore == 0){
                    blueParkingScore = 5;
                } else if(blueParkingScore == 5) {
                    blueParkingScore = 10;
                } else {
                    blueParkingScore = 0;
                }
                sendScore(Alliance.BLUE, opMode, "Beacons", blueParkingScore);
                btnBlueParkingScore.setText(Integer.toString(blueParkingScore));
            }
        });

    }

    private void sendScore( Alliance alliance, OpMode opMode, String type, int score){

        String strOpMode = "Auto";
        String strScoreType;
        JSONObject updateJson = new JSONObject();

        if(opMode == OpMode.TELEOP){
            strOpMode = "Tele";
        }
        strScoreType = alliance.toString() + strOpMode + type;

        try {
            updateJson.put("\"" + strScoreType +"\"", score);
         }
        catch (JSONException e) {
            e.printStackTrace();
        }

        updater.getHttpConn(updateJson.toString());

    }


}
