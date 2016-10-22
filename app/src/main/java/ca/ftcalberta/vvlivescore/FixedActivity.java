package ca.ftcalberta.vvlivescore;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static ca.ftcalberta.vvlivescore.R.drawable.alliance_button_blue;

public class FixedActivity extends AppCompatActivity {

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




    }

    private void sendScore( Alliance alliance, OpMode opMode, String type, int score){

        String strOpMode = "Auto";
        String strScoreType;

        if(opMode == OpMode.TELEOP){
            strOpMode = "Tele";
        }


    }


}
