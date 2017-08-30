package com.example.duckduckv1_0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static com.example.duckduckv1_0.Setup.GRID_DIFFCULTY_KEY;
import static com.example.duckduckv1_0.Setup.GRID_Level;
import static com.example.duckduckv1_0.Setup.GRID_SIZE_KEY;
import static com.example.duckduckv1_0.Setup.GRID_VS_DIFFCULTY;

public class NextLevel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_level);


        Intent intent = getIntent();
        int gridVsDifficulty=intent.getIntExtra(GRID_VS_DIFFCULTY,0);
        int gridLevel=intent.getIntExtra(GRID_Level,0);
        int currentSize = intent.getIntExtra(GRID_SIZE_KEY, 0);
        int currentDifficulty = intent.getIntExtra(GRID_DIFFCULTY_KEY, 0);
        finish();

        intent=new Intent(this,GameGrid.class);
        intent.putExtra(GRID_VS_DIFFCULTY,gridVsDifficulty);
        intent.putExtra(GRID_Level,gridLevel);
        intent.putExtra(GRID_SIZE_KEY, currentSize);
        intent.putExtra(GRID_DIFFCULTY_KEY, currentDifficulty);
        Toast toast = Toast.makeText(this, "Next Level", Toast.LENGTH_LONG);
        toast.show();
        startActivity(intent);

    }
}
