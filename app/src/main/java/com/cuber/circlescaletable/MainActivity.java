package com.cuber.circlescaletable;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private CircleScaleTable circleScaleTable;
    private EditText editText_r;
    private EditText editText_totail;
    private EditText editText_n;
    private EditText editText_long;
    private EditText editText_rataion;
    private EditText editText_point_length;

    private EditText editText_light_A;
    private EditText editText_light_R;
    private EditText editText_light_G;
    private EditText editText_light_B;
    private EditText editText_light_W;
    private EditText editText_dark_A;
    private EditText editText_dark_R;
    private EditText editText_dark_G;
    private EditText editText_dark_B;
    private EditText editText_dark_W;
    private EditText editText_arc_A;
    private EditText editText_arc_R;
    private EditText editText_arc_G;
    private EditText editText_arc_B;
    private EditText editText_arc_W;

    private Button button_submit;
    private Button button_submit_argb;

    private float r;
    private float totailDegree;
    private int n;
    private int longCalibration;
    private float rataionAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        init();
    }

    private void findView() {
        circleScaleTable = (CircleScaleTable) findViewById(R.id.regulator);
        editText_r = (EditText) findViewById(R.id.editText_r);
        editText_totail = (EditText) findViewById(R.id.editText_totailDegree);
        editText_n = (EditText) findViewById(R.id.EditText_n);
        editText_long = (EditText) findViewById(R.id.EditText_long);
        editText_rataion = (EditText) findViewById(R.id.EditText_rataionAngle);
        editText_point_length = (EditText) findViewById(R.id.EditText_point_length);

        editText_light_A = (EditText) findViewById(R.id.EditText_light_A);
        editText_light_R = (EditText) findViewById(R.id.EditText_light_R);
        editText_light_G = (EditText) findViewById(R.id.EditText_light_G);
        editText_light_B = (EditText) findViewById(R.id.EditText_light_B);
        editText_light_W = (EditText) findViewById(R.id.EditText_light_W);
        editText_dark_A = (EditText) findViewById(R.id.EditText_dark_A);
        editText_dark_R = (EditText) findViewById(R.id.EditText_dark_R);
        editText_dark_G = (EditText) findViewById(R.id.EditText_dark_G);
        editText_dark_B = (EditText) findViewById(R.id.EditText_dark_B);
        editText_dark_W = (EditText) findViewById(R.id.EditText_dark_W);
        editText_arc_A = (EditText) findViewById(R.id.EditText_arc_A);
        editText_arc_R = (EditText) findViewById(R.id.EditText_arc_R);
        editText_arc_G = (EditText) findViewById(R.id.EditText_arc_G);
        editText_arc_B = (EditText) findViewById(R.id.EditText_arc_B);
        editText_arc_W = (EditText) findViewById(R.id.EditText_arc_W);

        button_submit = (Button) findViewById(R.id.button1);
        button_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                r = Float.valueOf(editText_r.getText().toString());
                totailDegree = Float.valueOf(editText_totail.getText().toString());
                n = Integer.valueOf(editText_n.getText().toString());
                longCalibration = Integer.valueOf(editText_long.getText().toString());
                rataionAngle = Float.valueOf(editText_rataion.getText().toString());

                circleScaleTable.setPoint_length(Integer.valueOf(editText_point_length.getText().toString()));
                circleScaleTable.setParameter(r, totailDegree, n, longCalibration, rataionAngle);

                circleScaleTable.setPaint_light(Integer.valueOf(editText_light_A.getText().toString()), Integer.valueOf(editText_light_R.getText().toString()),
                        Integer.valueOf(editText_light_G.getText().toString()), Integer.valueOf(editText_light_B.getText().toString()), Integer.valueOf(editText_light_W.getText().toString()));
                circleScaleTable.setPaint_dark(Integer.valueOf(editText_dark_A.getText().toString()), Integer.valueOf(editText_dark_R.getText().toString()),
                        Integer.valueOf(editText_dark_G.getText().toString()), Integer.valueOf(editText_dark_B.getText().toString()), Integer.valueOf(editText_dark_W.getText().toString()));
                circleScaleTable.setPaint_arc(Integer.valueOf(editText_arc_A.getText().toString()), Integer.valueOf(editText_arc_R.getText().toString()),
                        Integer.valueOf(editText_arc_G.getText().toString()), Integer.valueOf(editText_arc_B.getText().toString()), Integer.valueOf(editText_arc_W.getText().toString()));

            }
        });
    }

    TextView tv;

    private void init() {
        tv = (TextView) findViewById(R.id.value);
        circleScaleTable.setParameter(300, 360, 60, 5, 0);
        circleScaleTable.setOnRegulatorChangeListener(new CircleScaleTable.OnRegulatorChangeListener() {
            @Override
            public void onRegulatorChange(int value) {
                tv.setText("" + value);
            }
        });
    }
}
