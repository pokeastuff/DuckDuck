package com.example.duckduckv1_0;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.duckduckv1_0.Setup.GRID_DIFFCULTY_KEY;
import static com.example.duckduckv1_0.Setup.GRID_Level;
import static com.example.duckduckv1_0.Setup.GRID_SIZE_KEY;
import static com.example.duckduckv1_0.Setup.GRID_VS_DIFFCULTY;

public class GameGrid extends AppCompatActivity {
    public Context ctx = this;
    static int screenWidth = 0;
    static int screenHeight = 0;
    int gridVsDifficulty;
    int currentLevel;
    int currentSize;
    int currentDifficulty;
    public IdNumGen idNumGen = null;
    public Square[] main_grid_sqrs_array = null;
    public int score = 0;
    public int timeLeft = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_grid);

        //Get grid size, difficulty and initialize variables
        final Intent intent = getIntent();
        gridVsDifficulty=intent.getIntExtra(GRID_VS_DIFFCULTY,0);
        currentLevel=intent.getIntExtra(GRID_Level,0);
        currentSize = intent.getIntExtra(GRID_SIZE_KEY, 0);
        currentDifficulty = intent.getIntExtra(GRID_DIFFCULTY_KEY, 0);

        main_grid_sqrs_array = new Square[currentSize * currentSize];
        populateArray(currentSize);

        idNumGen = new IdNumGen(currentSize);

        populateGrid(currentSize);

        final ProgressBar mProgressBar;
        int i = 0;

        mProgressBar = (ProgressBar) findViewById(R.id.gameGrid_progressBar);
        mProgressBar.setProgress(i);

        Timer time = new Timer();

        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!idNumGen.getAvail_sqr_array().isEmpty()) {

                    timeLeft += 10;
                    if (timeLeft <= 100) {
                        mProgressBar.setProgress(timeLeft);
                        int activate = idNumGen.getRandomId(ctx);
                        activateSquare(activate);
                    } else if (gridVsDifficulty <= 3) {
                        this.cancel();
                        gridVsDifficulty += 1;
                        currentDifficulty += 1;
                        Intent intent = new Intent(ctx, NextLevel.class);
                        intent.putExtra(GRID_VS_DIFFCULTY, gridVsDifficulty);
                        intent.putExtra(GRID_DIFFCULTY_KEY, currentDifficulty);
                        intent.putExtra(GRID_SIZE_KEY, currentSize);
                        intent.putExtra(GRID_Level, currentLevel);
                        startActivity(intent);

                    } else {
                        gridVsDifficulty=1;
                        currentLevel+=1;
                        Intent intent = new Intent(ctx, NextLevel.class);
                        intent.putExtra(GRID_VS_DIFFCULTY, gridVsDifficulty);
                        intent.putExtra(GRID_DIFFCULTY_KEY, currentDifficulty);
                        intent.putExtra(GRID_SIZE_KEY, currentSize);
                        intent.putExtra(GRID_Level, currentLevel);
                        startActivity(intent);
                    }
                } else

                {

                    this.cancel();
                    Intent intent = new Intent(ctx, ErrorPage.class);
                    startActivity(intent);

                }
            }
        }, 0, 1000 - 150 * currentDifficulty);


    }

    /**
     * Creates a new square for each cell added to the grid layout and stores it in main_grid_sqrs_array
     *
     * @param currentSize the dimension of the size of the grid to be used. The grid will have
     *                    currentSize*currentSize cells
     */
    public void populateArray(int currentSize) {
        for (int i = 0; i < currentSize * currentSize; i++) {
            Square sqr = new Square();
            sqr.setId(i);
            main_grid_sqrs_array[i] = sqr;
        }
    }

    /**
     * takes the currentSize and generates a grid of currentSize by currentSize dimensions.
     * Creates a new square for each image added to the grid layout and stores it in main_grid_sqrs_array
     *
     * @param currentSize the grid size dimension
     */

    public void populateGrid(int currentSize) {

        //Using the screen width and height, get the right square measurments
        screenWidth = getScreenWidth();
        screenHeight = getScreenHeight();
        int maxImageSize;

        if (screenWidth <= screenHeight) {
            maxImageSize = screenWidth / currentSize;
        } else {
            maxImageSize = screenHeight / currentSize;
        }

        //Initialize gridLayout
        final GridLayout gridLayout = (GridLayout) findViewById(R.id.gameGrid_grid_layout);
        gridLayout.setRowCount(currentSize);
        gridLayout.setColumnCount(currentSize);

        int r;
        int c;
        int i;

        GridLayout.Spec rowSpec;
        GridLayout.Spec colSpec;
        //GridLayout.LayoutParams gridParam=null;

        for (r = 0, i = 0; r < currentSize; r++, i++) {
            for (c = 0; c < currentSize; c++, i++) {

                //Setup new Image
                final ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.green_square_pic_2_0);
                imageView.setAdjustViewBounds(true);
                imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //***********************WE ARE TRYING TO FIGURE OUT WHAT TO DO FOR ON CLICK**********
                        //
                        // Involves figuring out, deactivatable,
                        //then calling deactivate id - Set square and start watch
                        // and activate
                        //
                        //
                        int index = gridLayout.indexOfChild(v);
                        ImageView imageview = (ImageView) gridLayout.getChildAt(index);
                        imageview.setImageResource(R.drawable.green_square_pic_1_0);
                        if (main_grid_sqrs_array[index].isRunning()) {
                            deactivateSquare(index);
                        }
                    }
                });


                imageView.setPadding(3, 3, 3, 3);
                imageView.setMaxWidth(maxImageSize);


                rowSpec = GridLayout.spec(r, 1);
                colSpec = GridLayout.spec(c, 1);

                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpec, colSpec);
                gridLayout.addView(imageView, gridParam);

                //Update Level
                TextView textView = (TextView) findViewById(R.id.gameGrid_level);
                textView.setText(String.valueOf(currentLevel));
            }
        }
    }

    /**
     * Using the supplied id, returns this value into the idGenertor's
     * avail_sqr_array field and then sets the grid's square with the corresponding
     * id's running value to false and stops its timer
     *
     * @param id the id of the square to be deactivated
     */

    public void deactivateSquare(int id) {

        GridLayout gridLayout2 = (GridLayout) findViewById(R.id.gameGrid_grid_layout);
        ImageView image = (ImageView) gridLayout2.getChildAt(id);
        image.setImageResource(R.drawable.green_square_pic_1_0);
        idNumGen.returnId(id);
        main_grid_sqrs_array[id].stopWatch();
        score += main_grid_sqrs_array[id].getScore();
        TextView textView = (TextView) findViewById(R.id.gameGrid_score);
        textView.setText(String.valueOf(score));


    }// deactivateSquare()

    /**
     * uses the idGenerator to select a square to activate and then starts this
     * squares timer and marks its running value to true
     *
     * @return true if a square was successfully activated: false if an error
     * occurred and the square could not be activated
     */

    public void activateSquare(int id) {
        //CAN GET RID OF IF WHEN POSITIVE ALL ELSE WORKS
        if (!main_grid_sqrs_array[id].running) {
            GridLayout gridLayout2 = (GridLayout) findViewById(R.id.gameGrid_grid_layout);
            ImageView image = (ImageView) gridLayout2.getChildAt(id);
            image.setImageResource(R.drawable.red_square_pic_1_0);
            main_grid_sqrs_array[id].startWatch();
            Log.d("messgae_idNum_activ", "activated ne square");
        }
        Log.d("messgae_idNum_activ", idNumGen.toString());
        Log.d("messgae_idNum_active", main_grid_sqrs_array[id].toString());
    }

    public String toString() {
        String squares = "";
        for (int i = 0; i < getMain_grid_sqrs_array().length; i++) {
            squares += "Square " + getMain_grid_sqrs_array()[i] + "\n";
        }
        return "Grid size = " + -1 + "	|   Number of available squares = "
                + this.getIdNumGen().getAvail_sqr_array().size() + " \n" + squares + idNumGen;
    }

    public int getScreenWidth() {

        return this.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {

        return this.getResources().getDisplayMetrics().heightPixels;
    }

    public IdNumGen getIdNumGen() {
        return idNumGen;
    }

    public Square[] getMain_grid_sqrs_array() {
        return main_grid_sqrs_array;
    }

    /*==================================IdNumGen-INNER-CLASS==================================*/
    private class IdNumGen {
        /*--------------------------Attributes-------------------------- */
        private ArrayList<Integer> avail_sqr_array = new ArrayList<Integer>();

        /*--------------------------CUSTOM-METHODS-------------------------- */

        /**
         * Creates a new IdNumGen object and populates its avail_sqr_array
         *
         * @param size the number of squares in the grid
         */
        private IdNumGen(int size) {
            int numberSquares = size * size;
            populate_avail_sqr_array(numberSquares);

        }

        /**
         * Initializes the available squares array with number of possible id's
         *
         * @param size the number of squares in the grid
         */
        private void populate_avail_sqr_array(int size) {
            for (int i = 0; i < size; i++) {
                avail_sqr_array.add(i);
            }
        }

        /**
         * Uses the random number generator to select one of the available squares to
         * pick from. //selectIndex will be between 0-num_avail_sqr exclusive
         *
         * @return the randomly generated square ID
         **/
        private int getRandomId(Context c) {
            int num_avail_sqr;
            int selectIndex;
            int duck_id;

            num_avail_sqr = avail_sqr_array.size();

            selectIndex = (int) (Math.random() * num_avail_sqr);

            duck_id = avail_sqr_array.remove(selectIndex);
            //System.out.println("selectIndex = " + selectIndex + " id selected = " + duck_id + "\n");

            return duck_id;
        }//getRandomId()

        /**
         * @param id the id to be returned to the id array
         * @return true if the id passed to the function was susccesfully added to the id array
         * Returns false otherwise
         */
        private boolean returnId(int id) {
            this.getAvail_sqr_array().add(id);
            return getAvail_sqr_array().contains(id);
        }

        /*--------------------------STANDARD-METHODS-------------------------- */
        private ArrayList<Integer> getAvail_sqr_array() {
            return avail_sqr_array;
        }

        public void setAvail_sqr_array(ArrayList<Integer> avail_sqr_array) {
            this.avail_sqr_array = avail_sqr_array;
        }

        public String toString() {
            return "Current available id's = " + avail_sqr_array;
        }

    }

    /*==================================Square-INNER-CLASS==================================*/
    private class Square {
        /*--------------------------Attributes--------------------------*/
        private long start = 0;
        private long elapsedTime = 0;
        private double score = 0;
        private int id = -1;
        private boolean running = false;

        /*--------------------------CUSTOM-METHODS--------------------------*/

        /**
         * Checks to see if the square is already active and if not, assigns the start
         * field the current system time in milliseconds
         **/
        private void startWatch() {
            if (!isRunning()) {
                start = System.currentTimeMillis();
                elapsedTime = 0;
                //Log.d("Watch_start", String.valueOf(start));
                running = true;
            }
        }

        /**
         * Calculates the length of time the square has been activated and stores this
         * value in the 'elapsedTime' field. Throws and error
         *
         * @return the length of time the square was activated was on
         * //@throws exception
         * if the square was not activated when the method was called.
         **/
        private boolean stopWatch() {
            if (start == 0 || running == false) {
                //throw new SquareException("watch not running SQUARE class, line 16");
            } else {
                elapsedTime = (System.currentTimeMillis() - start);
                if (elapsedTime <= 1500) {

                    score = Math.round((double) (2000 - elapsedTime) * .1);
                } else {
                    score = 50;
                }

                start = 0; // reset start to allow you to start it again later
                running = false;
            }
            return !isRunning();

        }// stopWatch()

	/*--------------------------STANDARD-METHODS--------------------------*/

        public long getElapsedTime() {
            return elapsedTime;
        }//getElapsedTime()

        public int getId() {
            return id;
        }// getId()

        private double getScore() {
            return score;
        }//getScore()

        public long getStart() {
            return start;
        }

        private void setId(int id) {
            this.id = id;
        }// setId()

        public void setStart(long start) {
            this.start = start;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }// setRunning()

        private boolean isRunning() {
            return running;
        }// isRunning()

        public String toString() {
            return "id = " + id + "     running = " + running + "     start = " + start + "     elaspedTime = "
                    + elapsedTime;
        }// toString()

    }

}



