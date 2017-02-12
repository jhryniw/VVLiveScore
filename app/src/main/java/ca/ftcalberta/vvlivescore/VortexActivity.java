package ca.ftcalberta.vvlivescore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static ca.ftcalberta.vvlivescore.R.id.btnOpMode;

public class VortexActivity extends Activity {

    private VortexState vortexState = new VortexState();
    private Button btnOpMode;
    private TextView txtVortex;
    private TextView txtTotal;
    private TextView txtCount;

    private AlertDialog resetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vortex);

        resetDialog = new AlertDialog.Builder(this)
                .setTitle("Score Reset")
                .setMessage("Warning: You are about to reset the score.")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vortexState.reset();
                        goToAutonomous();
                        updateTotal();
                        dialog.dismiss();
                    }
                })
                .create();

        btnOpMode = (Button) findViewById(R.id.btnOpMode);
        btnOpMode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                OpMode opMode = vortexState.getOpMode();

                //Swap OpMode
                if (opMode == OpMode.AUTONOMOUS)
                    goToTeleop();
                else
                    goToAutonomous();

                updateTotal();
                return true;
            }
        });

        Button btnDecrement = (Button) findViewById(R.id.btnCorrect);
        btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vortexState.decrement();
                updateTotal();
            }
        });
        btnDecrement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetDialog.show();
                return true;
            }
        });

        Button btnScore = (Button) findViewById(R.id.btnScore);
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vortexState.increment();
                updateTotal();
            }
        });

        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtCount = (TextView) findViewById(R.id.txtCount);
        txtVortex = (TextView) findViewById(R.id.txtVortexActivityTitle);

        applySetupParams();
    }

    @Override
    public void onBackPressed() {
        vortexState.reset();
        super.onBackPressed();
    }

    private void updateTotal() {
        txtTotal.setText(String.format(Locale.CANADA, "Score: %d", vortexState.getScore()));
        txtCount.setText(String.format(Locale.CANADA, "Count: %d", vortexState.getCount()));
    }

    private void goToAutonomous() {
        vortexState.setOpMode(OpMode.AUTONOMOUS);
        btnOpMode.setText(R.string.autonomous);
    }

    private void goToTeleop() {
        vortexState.setOpMode(OpMode.TELEOP);
        btnOpMode.setText(R.string.teleop);
    }

    private void applySetupParams() {
        SharedPreferences setupPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Alliance a;
        VortexType v;
        String alliancePrefix, title;

        if (!setupPrefs.contains("vortex") || !setupPrefs.contains("alliance")) {
            Toast.makeText(getApplicationContext(), "Invalid setup parameters", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.activity_vortex);
        Button btnScore = (Button) findViewById(R.id.btnScore);

        a = Alliance.values()[setupPrefs.getInt("alliance", 0)];
        v = setupPrefs.getString("vortex", "Corner").equals("Corner") ? VortexType.CORNER_VORTEX : VortexType.CENTRE_VORTEX;

        if(a == Alliance.BLUE) {
            alliancePrefix = "Blue ";
            mainLayout.setBackgroundResource(R.color.FtcLightBlue);
            btnScore.setBackgroundResource(R.drawable.alliance_button_blue);

        }
        else {
            alliancePrefix = "Red ";
            mainLayout.setBackgroundResource(R.color.FtcLightRed);
            btnScore.setBackgroundResource(R.drawable.alliance_button_red);
        }

        if (v == VortexType.CORNER_VORTEX)
            title = alliancePrefix + "Corner Vortex";
        else
            title = alliancePrefix + "Center Vortex";

        txtVortex.setText(title);
        vortexState.setAlliance(a);
        vortexState.setVortexType(v);
    }
}
