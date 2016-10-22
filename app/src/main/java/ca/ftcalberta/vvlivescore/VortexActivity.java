package ca.ftcalberta.vvlivescore;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class VortexActivity extends Activity {

    private ScoreState scoreState = new ScoreState();
    private TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vortex);

        final Button btnOpMode = (Button) findViewById(R.id.btnOpMode);
        btnOpMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpMode opMode = scoreState.getOpMode();

                //Swap OpMode
                if (opMode == OpMode.AUTONOMOUS) {
                    scoreState.setOpMode(OpMode.TELEOP);
                    btnOpMode.setText("Teleop");
                } else {
                    scoreState.setOpMode(OpMode.AUTONOMOUS);
                    btnOpMode.setText("Autonomous");
                }
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

        scoreState.launch();
    }

    private void updateTotal() {
        txtTotal.setText(String.format(Locale.CANADA, "Total: %d", scoreState.getScore()));
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
}
