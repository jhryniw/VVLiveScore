package ca.ftcalberta.vvlivescore;

import android.app.Activity;
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

    private VortexState vortexState = new VortexState();
    private TextView txtVortex;
    private TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vortex);

        final Button btnOpMode = (Button) findViewById(R.id.btnOpMode);
        btnOpMode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                OpMode opMode = vortexState.getOpMode();

                //Swap OpMode
                if (opMode == OpMode.AUTONOMOUS) {
                    vortexState.setOpMode(OpMode.TELEOP);
                    btnOpMode.setText("Teleop");
                }
                else {
                    vortexState.setOpMode(OpMode.AUTONOMOUS);
                    btnOpMode.setText("Autonomous");
                }

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

        Button btnScore = (Button) findViewById(R.id.btnScore);
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vortexState.increment();
                updateTotal();
            }
        });

        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtVortex = (TextView) findViewById(R.id.txtVortexActivityTitle);

        applySetupParams();

        vortexState.launch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vortexState.halt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vortexState.launch();
    }

    private void updateTotal() {
        txtTotal.setText(String.format(Locale.CANADA, "Total: %d", vortexState.getScore()));
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
