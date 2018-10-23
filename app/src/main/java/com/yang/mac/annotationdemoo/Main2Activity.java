package com.yang.mac.annotationdemoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.yang.mac.annotations.InjectInt;
import com.yang.mac.annotations.InjectString;

public class Main2Activity extends AppCompatActivity {
    @InjectString
    public String aLv;
    @InjectInt
    public int one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        new Main2Activity_Inject(this);

        TextView textView = findViewById(R.id.textV);
        textView.setText(aLv);
    }
}
