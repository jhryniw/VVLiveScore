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

public class VortexActivity extends Activity {

    private VortexState centerVortex = new VortexState();
    private VortexState cornerVortex = new VortexState();
    private Button btnOpMode;
    private TextView txtVortex;
    private TextView txtCornerCount;
    private TextView txtCentreCount;

    private AlertDialog resetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vortex);

        resetDialog = new AlertDialog.Builder(this)
                .setTitle("Warning: Score Reset")
                .setMessage("You are about to reset the score.")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        centerVortex.reset();
                        cornerVortex.reset();
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
                OpMode opMode = centerVortex.getOpMode();

                //Swap OpMode
                if (opMode == OpMode.AUTONOMOUS)
                    goToTeleop();
                else
                    goToAutonomous();

                updateTotal();
                return true;
            }
        });

        Button btnCentreDecrement = (Button) findViewById(R.id.btnDecCentre);
        Button btnCornerDecrement = (Button) findViewById(R.id.btnDecCorner);

        btnCentreDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerVortex.decrement();
                updateTotal();
            }
        });
        btnCornerDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cornerVortex.decrement();
                updateTotal();
            }
        });

        btnCentreDecrement.setOnLongClickListener(resetListener);
        btnCornerDecrement.setOnLongClickListener(resetListener);

        Button btnCentreScore = (Button) findViewById(R.id.btnCentreScore);
        Button btnCornerScore = (Button) findViewById(R.id.btnCornerScore);
        btnCentreScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerVortex.increment();
                updateTotal();
            }
        });
        btnCornerScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cornerVortex.increment();
                updateTotal();
            }
        });

        txtCornerCount = (TextView) findViewById(R.id.txtCorner);
        txtCentreCount = (TextView) findViewById(R.id.txtCentre);
        txtVortex = (TextView) findViewById(R.id.txtVortexActivityTitle);

        applySetupParams();

        centerVortex.setVortexType(VortexType.CENTRE_VORTEX);
        cornerVortex.setVortexType(VortexType.CORNER_VORTEX);
    }

    @Override
    public void onBackPressed() {
        centerVortex.reset();
        cornerVortex.reset();
        super.onBackPressed();
    }

    private void updateTotal() {
        txtCornerCount.setText(String.format(Locale.CANADA, ": %d", cornerVortex.getCount()));
        txtCentreCount.setText(String.format(Locale.CANADA, ": %d", centerVortex.getCount()));
    }

    private void goToAutonomous() {
        centerVortex.setOpMode(OpMode.AUTONOMOUS);
        cornerVortex.setOpMode(OpMode.AUTONOMOUS);
        btnOpMode.setText(R.string.autonomous);
    }

    private void goToTeleop() {
        centerVortex.setOpMode(OpMode.TELEOP);
        cornerVortex.setOpMode(OpMode.TELEOP);
        btnOpMode.setText(R.string.teleop);
    }

    private void applySetupParams() {
        SharedPreferences setupPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Alliance a;
        String alliancePrefix, title;

        if (!setupPrefs.contains("alliance")) {
            Toast.makeText(getApplicationContext(), "Invalid setup parameters", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.activity_vortex);
        Button btnCentreScore = (Button) findViewById(R.id.btnCentreScore);
        Button btnCornerScore = (Button) findViewById(R.id.btnCornerScore);

        a = Alliance.values()[setupPrefs.getInt("alliance", 0)];

        if(a == Alliance.BLUE) {
            alliancePrefix = "Blue ";
            mainLayout.setBackgroundResource(R.color.FtcLightBlue);
            btnCentreScore.setBackgroundResource(R.drawable.alliance_button_blue);
            btnCornerScore.setBackgroundResource(R.drawable.alliance_button_blue);
        }
        else {
            alliancePrefix = "Red ";
            mainLayout.setBackgroundResource(R.color.FtcLightRed);
            btnCentreScore.setBackgroundResource(R.drawable.alliance_button_red);
            btnCornerScore.setBackgroundResource(R.drawable.alliance_button_red);
        }

        title = alliancePrefix + "Vortex Scoring";

        txtVortex.setText(title);
        centerVortex.setAlliance(a);
        cornerVortex.setAlliance(a);
    }

    private View.OnLongClickListener resetListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            resetDialog.show();
            return true;
        }
    };
}
