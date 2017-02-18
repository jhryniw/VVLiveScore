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

    Button btnBeacon1, btnBeacon2, btnBeacon3, btnBeacon4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed);

        btnBeacon1 = (Button) findViewById(R.id.btnBeacon1);
        btnBeacon1.setOnClickListener(beaconClick(1, btnBeacon1));
        btnBeacon1.setOnLongClickListener(beaconLongClick(1, btnBeacon1));

        btnBeacon2 = (Button) findViewById(R.id.btnBeacon2);
        btnBeacon2.setOnClickListener(beaconClick(2, btnBeacon2));
        btnBeacon2.setOnLongClickListener(beaconLongClick(2, btnBeacon2));

        btnBeacon3 = (Button) findViewById(R.id.btnBeacon3);
        btnBeacon3.setOnClickListener(beaconClick(3, btnBeacon3));
        btnBeacon3.setOnLongClickListener(beaconLongClick(3, btnBeacon3));

        btnBeacon4 = (Button) findViewById(R.id.btnBeacon4);
        btnBeacon4.setOnClickListener(beaconClick(4, btnBeacon4));
        btnBeacon4.setOnLongClickListener(beaconLongClick(4, btnBeacon4));

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
                    btnRedCapScore.setText(R.string.on_floor);
                    btnBlueCapScore.setText(R.string.on_floor);
                    btnOpMode.setText(R.string.teleop);
                }
                else {
                    opMode = OpMode.AUTONOMOUS;
                    resetScores();
                    parkingBlock.setVisibility(LinearLayout.VISIBLE);
                    beaconValue = 30;
                    btnOpMode.setText(R.string.autonomous);
                    btnRed1ParkingScore.setText(R.string.not_parked);
                    btnRed2ParkingScore.setText(R.string.not_parked);
                    btnBlue1ParkingScore.setText(R.string.not_parked);
                    btnBlue2ParkingScore.setText(R.string.not_parked);
                    parkingScores[0][0] = 0;
                    parkingScores[0][1] = 0;
                    parkingScores[1][0] = 0;
                    parkingScores[1][1] = 0;
                    btnRedCapScore.setText(R.string.not_on_floor);
                    btnBlueCapScore.setText(R.string.not_on_floor);

                    btnBeacon1.setBackgroundResource(R.drawable.score_button);
                    btnBeacon2.setBackgroundResource(R.drawable.score_button);
                    btnBeacon3.setBackgroundResource(R.drawable.score_button);
                    btnBeacon4.setBackgroundResource(R.drawable.score_button);
                }
                return true;
            }
        });

    }

    private void resetScores(){
        updater.sendScore(Alliance.BLUE, OpMode.AUTONOMOUS, "Parking", 0);
        updater.sendScore(Alliance.RED, OpMode.AUTONOMOUS, "Parking", 0);
        updater.sendScore(Alliance.BLUE, OpMode.AUTONOMOUS, "Beacons", 0);
        updater.sendScore(Alliance.RED, OpMode.AUTONOMOUS, "Beacons", 0);
        updater.sendScore(Alliance.BLUE, OpMode.AUTONOMOUS, "CapBall", 0);
        updater.sendScore(Alliance.RED, OpMode.AUTONOMOUS, "CapBall", 0);
        updater.sendScore(Alliance.BLUE, OpMode.TELEOP, "Parking", 0);
        updater.sendScore(Alliance.RED, OpMode.TELEOP, "Parking", 0);
        updater.sendScore(Alliance.BLUE, OpMode.TELEOP, "Beacons", 0);
        updater.sendScore(Alliance.RED, OpMode.TELEOP, "Beacons", 0);
        updater.sendScore(Alliance.BLUE, OpMode.TELEOP, "CapBall", 0);
        updater.sendScore(Alliance.RED, OpMode.TELEOP, "CapBall", 0);

        alliances[0] = Alliance.NONE;
        alliances[1] = Alliance.NONE;
        alliances[2] = Alliance.NONE;
        alliances[3] = Alliance.NONE;
        redBeaconCount = 0;
        blueBeaconCount = 0;
    }

    @Override
    public void onBackPressed(){
        resetScores();
        super.onBackPressed();
    }

    private void capScoreButton(Alliance robotAlliance, Button button){
        int allianceNum = 0;
        if(robotAlliance == Alliance.BLUE){
            allianceNum = 1;
        }
        if(opMode == OpMode.TELEOP){
            if(capScores[allianceNum] == 0){
                capScores[allianceNum] = 10;
                button.setText(R.string.off_floor);
            } else if(capScores[allianceNum] == 10){
                capScores[allianceNum] = 20;
                button.setText(R.string.above_30);
            } else if(capScores[allianceNum] == 20){
                capScores[allianceNum] = 40;
                button.setText(R.string.capped);
            } else {
                capScores[allianceNum] = 0;
                button.setText(R.string.on_floor);
            }
        } else { //Autonomous
            if(capScores[allianceNum] == 0){
                capScores[allianceNum] = 5;
                button.setText(R.string.touching_floor);
            } else {
                capScores[allianceNum] = 0;
                button.setText(R.string.not_on_floor);
            }
        }

        updater.sendScore(robotAlliance, opMode, "CapBall", capScores[allianceNum]);
        button.setText(String.format(getString(R.string.btn_capball), capScores[allianceNum]));
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
            button.setText(R.string.partial);
        } else if(parkingScores[allianceNum][buttonNum - 1] == 5) {
            parkingScores[allianceNum][buttonNum - 1] = 10;
            button.setText(R.string.full);
        } else {
            parkingScores[allianceNum][buttonNum - 1] = 0;
            button.setText(R.string.not_parked);
        }
        updater.sendScore(robotAlliance, opMode, "Parking", parkingScores[allianceNum][0] + parkingScores[allianceNum][1]);
        button.setText("Parking " + Integer.toString(buttonNum) + ": " + Integer.toString(parkingScores[allianceNum][buttonNum - 1]));

    }

    private View.OnLongClickListener beaconLongClick(final int beaconNum, final Button button) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(alliances[beaconNum - 1] == Alliance.RED){
                    redBeaconCount--;
                    updater.sendScore(Alliance.RED, opMode, "Beacons", redBeaconCount * beaconValue);
                } else if(alliances[beaconNum - 1] == Alliance.BLUE){
                    blueBeaconCount--;
                    updater.sendScore(Alliance.BLUE, opMode, "Beacons", blueBeaconCount * beaconValue);
                }
                alliances[beaconNum - 1] = Alliance.NONE;
                button.setBackgroundResource(R.drawable.score_button);

                return true;
            }
        };
    }

    private View.OnClickListener beaconClick(final int beaconNum, final Button button) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alliances[beaconNum - 1] == Alliance.NONE){
                    redBeaconCount++;
                    alliances[beaconNum - 1] = Alliance.RED;
                    button.setBackgroundResource(R.drawable.alliance_button_red);
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
        };
    }
}
