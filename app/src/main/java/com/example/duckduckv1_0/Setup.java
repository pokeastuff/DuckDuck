package com.example.duckduckv1_0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Setup extends AppCompatActivity {

    public static final String GRID_SIZE_KEY = "size";
    public static final String GRID_DIFFCULTY_KEY = "difficulty";
    public static final String GRID_VS_DIFFCULTY = "griv vs difficulty";
    public static final String GRID_Level = "level";
    //These two variable keep track of the id of the currently checked radio button
    private static int checkedSize = 0;
    private static int checkedDifficulty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    /**
     * Ensures that a selection for the difficulty and grid size has been made. Using this data,
     * loads the approprtiate game grid activity
     *
     * @param view
     */
    public void startGame(View view) {
        int detrminedGridSize = gridSize();
        int determinedDifficulty = gridDifficulty();

        if (determinedDifficulty == 0) {
            Toast toast = Toast.makeText(this, "Please chose a difficulty level", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        Intent intent;
        if (detrminedGridSize == 0) {
            Toast toast = Toast.makeText(this, "Please chose a grid size", Toast.LENGTH_LONG);
            toast.show();
            return;

        } else {
            intent = new Intent(this, GameGrid.class);
        }

        intent.putExtra(GRID_VS_DIFFCULTY,1);
        intent.putExtra(GRID_Level,1);
        intent.putExtra(GRID_SIZE_KEY, detrminedGridSize);
        intent.putExtra(GRID_DIFFCULTY_KEY, determinedDifficulty);
        startActivity(intent);

    }

    /**
     * @return the size of the grid user selected.
     * Returns 0 if no grid has been selected, 4 for a 2X2 grid,
     * 9 for a 3X3 grid, and 16 for a 4X4 grid
     */
    public int gridSize() {
        int gridSize = 1;
        RadioButton rb = null;

        //check 2X2 Button
        rb = (RadioButton) findViewById(R.id.setup_btn_radio_2X2);
        if (rb.isChecked()) {
            return 2;
        }

        //check 3X3 Button
        rb = (RadioButton) findViewById(R.id.setup_btn_radio_3X3);
        if (rb.isChecked()) {
            return 3;
        }

        //check 4X4 Button
        rb = (RadioButton) findViewById(R.id.setup_btn_radio_4X4);
        if (rb.isChecked()) {
            return 4;
        }
        //Only get here if no button is selected
        return 0;
    }

    /**
     * @return the difficulty level the user selected. Returns 0 if no difficutly
     * is selected, 1 if easy is selected, 2 if medium is selected, 3 if hard is
     * selected and 4 if easy is selected
     */

    public int gridDifficulty() {
        int difficulty = 0;
        RadioButton rb = null;

        //check Easy Button
        rb = (RadioButton) findViewById(R.id.setup_btn_radio_E);
        if (rb.isChecked()) {
            return 1;
        }

        //check Medium Button
        rb = (RadioButton) findViewById(R.id.setup_btn_radio_M);
        if (rb.isChecked()) {
            return 2;
        }

        //check Hard Button
        rb = (RadioButton) findViewById(R.id.setup_btn_radio_H);
        if (rb.isChecked()) {
            return 3;
        }

        //check Expert Button
        rb = (RadioButton) findViewById(R.id.setup_btn_radio_EX);
        if (rb.isChecked()) {
            return 4;
        }

        //Only get here if no button is selected
        return 0;
    }

    public void selectOneSize(View view) {
        int current = view.getId();

        //First time button is pressed
        if (checkedSize == 0) {
            checkedSize = current;
            return;
        }

        //True if user clicks on radio button that is  not already selected
        if (checkedSize != current) {
            //Uncheck the previously checked button using the id stored in checkedSIze
            RadioButton rb = (RadioButton) findViewById(checkedSize);
            rb.setChecked(false);
            checkedSize = current;
        }

        //Get here if either radio button pressed was already selected or new button has been checked and
        // previosuly checked button has been unchecked
        return;

    }

    public void selectOneDifficulty(View view) {
        int current = view.getId();

        //First time button is pressed
        if (checkedDifficulty == 0) {
            checkedDifficulty = current;
            return;
        }

        //True if user clicks on radio button that is  not already selected
        if (checkedDifficulty != current) {
            RadioButton rb = (RadioButton) findViewById(checkedDifficulty);
            rb.setChecked(false);
            checkedDifficulty = current;
        }

        //Get here if either radio button pressed was already selected or new button has been checked and
        // previosuly checked button has been unchecked
        return;

    }
}

