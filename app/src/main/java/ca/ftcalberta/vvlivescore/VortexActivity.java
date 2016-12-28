package ca.ftcalberta.vvlivescore;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class VortexActivity extends Activity {

    private ScoreState scoreState = new ScoreState();
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
                OpMode opMode = scoreState.getOpMode();

                //Swap OpMode
                if (opMode == OpMode.AUTONOMOUS) {
                    scoreState.setOpMode(OpMode.TELEOP);
                    btnOpMode.setText("Teleop");
                }
                else {
                    scoreState.setOpMode(OpMode.AUTONOMOUS);
                    btnOpMode.setText("Autonomous");
                }

                return true;
            }
        });

        Button btnDecrement = (Button) findViewById(R.id.btnCorrect);
        btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreState.decrease();
                updateTotal();
            }
        });

        Button btnScore = (Button) findViewById(R.id.btnScore);
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreState.increase();
                updateTotal();
            }
        });

        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtVortex = (TextView) findViewById(R.id.txtVortexActivityTitle);

        applySetupParams();

        scoreState.launch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scoreState.halt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scoreState.launch();
    }

    private void updateTotal() {
        txtTotal.setText(String.format(Locale.CANADA, "Total: %d", scoreState.getScore()));
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

        a = Alliance.values()[setupPrefs.getInt("alliance", 0)];
        v = setupPrefs.getString("vortex", "Corner").equals("Corner") ? VortexType.CORNER_VORTEX : VortexType.CENTRE_VORTEX;

        if(a == Alliance.BLUE) {
            alliancePrefix = "Blue ";
            mainLayout.setBackgroundResource(R.color.FtcLightBlue);
        }
        else {
            alliancePrefix = "Red ";
            mainLayout.setBackgroundResource(R.color.FtcLightRed);
        }

        if (v == VortexType.CORNER_VORTEX)
            title = alliancePrefix + "Corner Vortex";
        else
            title = alliancePrefix + "Center Vortex";

        txtVortex.setText(title);
        scoreState.setAlliance(a);
        scoreState.setVortexType(v);
    }
}
