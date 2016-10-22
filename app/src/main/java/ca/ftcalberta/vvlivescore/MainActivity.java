package ca.ftcalberta.vvlivescore;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends Activity {

    //todo: log the updates
    //private List<ScoreUpdater> updates = new ArrayList<ScoreUpdater>();

    private ScoreState scoreState = new ScoreState();
    private TextView txtTotal;
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = sharedPref.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //put your value
        editor.putString("userName", "stackoverlow");

        //commits your edits
        editor.commit();

        setContentView(R.layout.activity_main);

        final Button btnAlliance = (Button) findViewById(R.id.btnAlliance);
        btnAlliance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Swap alliance
                Alliance currentAlliance = scoreState.getAlliance();

                if(currentAlliance == Alliance.BLUE) {
                    scoreState.setAlliance(Alliance.RED);
                    btnAlliance.setBackgroundResource(R.drawable.alliance_button_red);
                    btnAlliance.setText("Red Alliance");
                }
                else {
                    scoreState.setAlliance(Alliance.BLUE);
                    btnAlliance.setBackgroundResource(R.drawable.alliance_button_blue);
                    btnAlliance.setText("Blue Alliance");
                }
            }
        });

        final Button btnOpMode = (Button) findViewById(R.id.btnOpMode);
        btnOpMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpMode opMode = scoreState.getOpMode();

                //Swap OpMode
                if(opMode == OpMode.AUTONOMOUS) {
                    scoreState.setOpMode(OpMode.TELEOP);
                    btnOpMode.setText("Teleop");
                }
                else {
                    scoreState.setOpMode(OpMode.AUTONOMOUS);
                    btnOpMode.setText("Autonomous");
                }
            }
        });

        final Spinner ddScoringType = (Spinner) findViewById(R.id.scoringType);
        ddScoringType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                switch (selected) {
                    case "Corner Vortex":
                        scoreState.setVortexType(VortexType.CORNER_VORTEX);
                        break;
                    case "Centre Vortex":
                        scoreState.setVortexType(VortexType.CENTRE_VORTEX);
                        break;
                    default:
                        Log.d("ScoreType", "Score type selected is not valid!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    }

    private void updateTotal() {
        txtTotal.setText(String.format(Locale.CANADA, "Total: %d", scoreState.getScore()));
    }
}
