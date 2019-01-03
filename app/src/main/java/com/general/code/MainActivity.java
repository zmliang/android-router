package com.general.code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.general.router.Router;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate();
            }
        });
    }

    private void navigate(){
        Router.with(this).uri("test://second").go();
    }
}
