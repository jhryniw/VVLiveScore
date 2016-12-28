package ca.ftcalberta.vvlivescore;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class FixedActivity extends Activity {

    private int beaconValue = 30;
    OpMode opMode = OpMode.AUTONOMOUS;
    private int redBeaconCount = 0;
    private int blueBeaconCount = 0;
    private int[][] parkingScores = {{0,0},{0,0}};
    private int[] capScores = {0,0};
    private Alliance[] alliances = {Alliance.NONE, Alliance.NONE, Alliance.NONE, Alliance.NONE};
    private ScoreUpdater updater = new ScoreUpdater();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed);

        final Button btnBeacon1 = (Button) findViewById(R.id.btnBeacon1);
        btnBeacon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beaconButton(1, btnBeacon1);
            }
        });

        final Button btnBeacon2 = (Button) findViewById(R.id.btnBeacon2);
        btnBeacon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beaconButton(2, btnBeacon2);
            }
        });

        final Button btnBeacon3 = (Button) findViewById(R.id.btnBeacon3);
        btnBeacon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beaconButton(3, btnBeacon3);
            }
        });

        final Button btnBeacon4 = (Button) findViewById(R.id.btnBeacon4);
        btnBeacon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beaconButton(4, btnBeacon4);
            }
        });

        final Button btnRed1ParkingScore = (Button) findViewById(R.id.btnRed1ParkingScore);
        btnRed1ParkingScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingButton(Alliance.RED, 1, btnRed1ParkingScore);
            }
        });

        final Button btnRed2ParkingScore = (Button) findViewById(R.id.btnRed2ParkingScore);
        btnRed2ParkingScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingButton(Alliance.RED, 2, btnRed2ParkingScore);
            }
        });

        final Button btnBlue1ParkingScore = (Button) findViewById(R.id.btnBlue1ParkingScore);
        btnBlue1ParkingScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingButton(Alliance.BLUE, 1, btnBlue1ParkingScore);
            }
        });

        final Button btnBlue2ParkingScore = (Button) findViewById(R.id.btnBlue2ParkingScore);
        btnBlue2ParkingScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingButton(Alliance.BLUE, 2, btnBlue2ParkingScore);
            }
        });

        final Button btnRedCapScore = (Button) findViewById(R.id.btnRedCapScore);
        btnRedCapScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capScoreButton(Alliance.RED, btnRedCapScore);
            }
        });

        final Button btnBlueCapScore = (Button) findViewById(R.id.btnBlueCapScore);
        btnBlueCapScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capScoreButton(Alliance.BLUE, btnBlueCapScore);
            }
        });

        LinearLayout parkingBlock = (LinearLayout) findViewById(R.id.layout_parkingScore);
        final Button btnOpMode = (Button) findViewById(R.id.btnOpMode);
        btnOpMode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Swap OpMode
                capScores[0] = 0;
                capScores[1] = 0;
                LinearLayout parkingBlock = (LinearLayout) findViewById(R.id.layout_parkingScore);
                if(opMode == OpMode.AUTONOMOUS) {
                    opMode = OpMode.TELEOP;
                    parkingBlock.setVisibility(LinearLayout.GONE);
                    beaconValue = 10;
                    updater.sendScore(Alliance.RED, opMode, "Beacons", redBeaconCount * beaconValue);
                    updater.sendScore(Alliance.BLUE, opMode, "Beacons", blueBeaconCount * beaconValue);
                    btnRedCapScore.setText("On Floor");
                    btnBlueCapScore.setText("On Floor");
                    btnOpMode.setText("TeleOp");
                }
                else {
                    opMode = OpMode.AUTONOMOUS;
                    parkingBlock.setVisibility(LinearLayout.VISIBLE);
                    beaconValue = 30;
                    btnOpMode.setText("Autonomous");
                    btnRed1ParkingScore.setText("Not Parked");
                    btnRed2ParkingScore.setText("Not Parked");
                    btnBlue1ParkingScore.setText("Not Parked");
                    btnBlue2ParkingScore.setText("Not Parked");
                    parkingScores[0][0] = 0;
                    parkingScores[0][1] = 0;
                    parkingScores[1][0] = 0;
                    parkingScores[1][1] = 0;
                    btnRedCapScore.setText("Not On Floor");
                    btnBlueCapScore.setText("Not On Floor");
                }
                return true;
            }
        });

    }

    private void capScoreButton(Alliance robotAlliance, Button button){
        int allianceNum = 0;
        if(robotAlliance == Alliance.BLUE){
            allianceNum = 1;
        }
        if(opMode == OpMode.TELEOP){
            if(capScores[allianceNum] == 0){
                capScores[allianceNum] = 10;
                button.setText("Off Floor");
            } else if(capScores[allianceNum] == 10){
                capScores[allianceNum] = 20;
                button.setText("Above 30\"");
            } else if(capScores[allianceNum] == 20){
                capScores[allianceNum] = 40;
                button.setText("Capped");
            } else {
                capScores[allianceNum] = 0;
                button.setText("On Floor");
            }
        } else { //Autonomous
            if(capScores[allianceNum] == 0){
                capScores[allianceNum] = 5;
                button.setText("Touching Floor");
            } else {
                capScores[allianceNum] = 0;
                button.setText("Not On Floor");
            }
        }

        updater.sendScore(robotAlliance, opMode, "CapBall", capScores[allianceNum]);
        button.setText("Cap Ball: " + Integer.toString(capScores[allianceNum]));
    }

    private void parkingButton(Alliance robotAlliance, int buttonNum, Button button){
        if(opMode == OpMode.TELEOP){
            return;
        }
        int allianceNum = 0;
        if(robotAlliance == Alliance.BLUE){
            allianceNum = 1;
        }
        if(parkingScores[allianceNum][buttonNum - 1] == 0){
            parkingScores[allianceNum][buttonNum - 1] = 5;
            button.setText("Partial");
        } else if(parkingScores[allianceNum][buttonNum - 1] == 5) {
            parkingScores[allianceNum][buttonNum - 1] = 10;
            button.setText("Full");
        } else {
            parkingScores[allianceNum][buttonNum - 1] = 0;
            button.setText("Not Parked");
        }
        updater.sendScore(robotAlliance, opMode, "Parking", parkingScores[allianceNum][0] + parkingScores[allianceNum][1]);
        button.setText("Parking " + Integer.toString(buttonNum) + ": " + Integer.toString(parkingScores[allianceNum][buttonNum - 1]));

    }

    private void beaconButton(int beaconNum, Button button) {
            if(alliances[beaconNum - 1] == Alliance.NONE){
                redBeaconCount++;
                button.setBackgroundResource(R.drawable.alliance_button_red);
                alliances[beaconNum - 1] = Alliance.RED;
            } else if(alliances[beaconNum - 1] == Alliance.RED){
                redBeaconCount--;
                blueBeaconCount++;
                button.setBackgroundResource(R.drawable.alliance_button_blue);
                alliances[beaconNum - 1] = Alliance.BLUE;
            } else if(alliances[beaconNum - 1] == Alliance.BLUE){
                blueBeaconCount--;
                redBeaconCount++;
                button.setBackgroundResource(R.drawable.alliance_button_red);
                alliances[beaconNum - 1] = Alliance.RED;
            }
            updater.sendScore(Alliance.RED, opMode, "Beacons", redBeaconCount * beaconValue);
            updater.sendScore(Alliance.BLUE, opMode, "Beacons", blueBeaconCount * beaconValue);
    }
}
