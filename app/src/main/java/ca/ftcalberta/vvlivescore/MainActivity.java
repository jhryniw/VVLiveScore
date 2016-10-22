package ca.ftcalberta.vvlivescore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends Activity {

    //TODO: Log Updates

    private ScoreState scoreState = new ScoreState();

    private String scoreTypeState = "Vortex";
    private String vortexState = "Centre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        Button btnScoreType = (Button) findViewById(R.id.btnScoringType);
        btnScoreType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                LinearLayout vortexBlock = (LinearLayout) findViewById(R.id.layout_selectVortex);

                //Swap scoring type
                if(scoreTypeState.equals("Vortex")) {
                    scoreTypeState = "Fixed";
                    btn.setText("Fixed");
                    btn.setTextColor(Color.WHITE);
                    btn.setBackgroundResource(R.drawable.decrement_button);
                    vortexBlock.setVisibility(LinearLayout.GONE);
                }
                else { // == "Fixed"
                    scoreTypeState = "Vortex";
                    btn.setText("Vortex");
                    btn.setTextColor(Color.BLACK);
                    btn.setBackgroundResource(R.drawable.score_button);
                    vortexBlock.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        Button btnVortex = (Button) findViewById(R.id.btnVortex);
        btnVortex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;

                //Swap vortex mode
                if(vortexState.equals("Centre")) {
                    vortexState = "Corner";
                    btn.setText("Corner");
                }
                else { // == "Fixed"
                    vortexState = "Centre";
                    btn.setText("Centre");
                }
            }
        });

        final Button btnAlliance = (Button) findViewById(R.id.btnAlliance);
        btnAlliance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Swap alliance
                Alliance currentAlliance = scoreState.getAlliance();

                if(currentAlliance == Alliance.BLUE) {
                    scoreState.setAlliance(Alliance.RED);
                    btnAlliance.setBackgroundResource(R.drawable.alliance_button_red);
                    btnAlliance.setText("Red");
                }
                else {
                    scoreState.setAlliance(Alliance.BLUE);
                    btnAlliance.setBackgroundResource(R.drawable.alliance_button_blue);
                    btnAlliance.setText("Blue");
                }
            }
        });

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Button btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("scoreType", scoreState.getScoreType().ordinal());
                editor.putInt("vortex", scoreState.getVortexType().ordinal());
                editor.putInt("alliance", scoreState.getAlliance().ordinal());

                editor.apply();

                goToVortexActivity();
            }
        });
    }

    public void goToVortexActivity() {
        Intent intent = new Intent(this, VortexActivity.class);
        startActivity(intent);
    }
}
