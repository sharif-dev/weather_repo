package edu.sharif.sharif_dev.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText;
                EditText search_input = findViewById(R.id.search_inp);
                if (search_input.getText() != null) {
                    searchText = search_input.getText().toString(); // get query from user

                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);

                    GetMap getMap = new GetMap(searchText, getApplicationContext(),progressBar); // make map box thread
                    getMap.start();



                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.serach_input_error, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
}
