package com.example.android.magtanium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Eases.EaseType;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.ClickEffectType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.OrderType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class Movie extends AppCompatActivity {

    TextView tv, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;
    ImageView img;
    String sentence;
    String movie;
    ProgressDialog mProgressDialog;
    MovieInfo defaultmo= new MovieInfo();
    private boolean init = false;
    private BoomMenuButton boomMenuButton;

    private TextView code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent i=getIntent();
        movie=sentence=i.getStringExtra("Movie name");
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);
        code = (TextView)findViewById(R.id.code);



        tv1= (TextView) findViewById(R.id.movie_name);
        tv2= (TextView) findViewById(R.id.imdb_rating);

        tv3 = (TextView) findViewById(R.id.metscore);
        tv4= (TextView) findViewById(R.id.genre);
        tv5= (TextView) findViewById(R.id.release_date);

        tv6 = (TextView) findViewById(R.id.director_name);
        tv7= (TextView) findViewById(R.id.writer_name);
        tv8= (TextView) findViewById(R.id.actors);
        tv9= (TextView) findViewById(R.id.movie_plot);
        img=(ImageView) findViewById(R.id.movieposter);

        StringBuilder QUERY = new StringBuilder();
        QUERY.append("http://www.omdbapi.com/?t=");
        sentence.toLowerCase();
        sentence = sentence.replace(' ', '+');
        QUERY.append(sentence);
        QUERY.append("&y=&plot=full&r=json\\");
        MovieAsyncTask movie_search = new MovieAsyncTask();
        movie_search.execute(QUERY.toString());


    }

    private class MovieAsyncTask extends AsyncTask<String, Void, MovieInfo> {
        @Override
        protected MovieInfo doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            String jsonresp = QueryUtils.getData(urls[0]);
            if (jsonresp != null) {
                return extractMovie(jsonresp);
            }
            else
                return defaultmo;

        }

        @Override
        protected void onPostExecute(MovieInfo mov) {
            String quote = mov.getPlot();
            String name= mov.getMovieName();
            String imdb= mov.getIMDBRating();

            String genre = mov.getGenre();
            String meta= mov.getMeta();
            String rel= mov.getRelease();

            String dir = mov.getDirector();
            String writer= mov.getWriter();
            String actor= mov.getActors();

            tv9.setText(quote);
            tv1.setText(name);
            tv2.setText(imdb);
            tv3.setText(meta);
            tv4.setText(genre);
            tv5.setText(rel);
            tv6.setText(dir);
            tv7.setText(writer);
            tv8.setText(actor);
            DownloadImage downloadImage=new DownloadImage();
            downloadImage.execute(mov.getPoster());
        }
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(Movie.this);
            // Set progressdialog title
            mProgressDialog.setTitle("MAGTanium");
            // Set progressdialog message
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            img.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }

    private static MovieInfo extractMovie(String jsonResp) {
        String plot = "";
        String title = "";
        String imdb = "";
        String genre="";
        String meta="";
        String actors="";
        String director="";
        String writers="";
        String rel="";
        String poster="";

        if (TextUtils.isEmpty(jsonResp)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResp);
            plot = baseJsonResponse.optString("Plot");
            title = baseJsonResponse.optString("Title");
            imdb =baseJsonResponse.optString("imdbRating");

            genre = baseJsonResponse.optString("Genre");
            meta = baseJsonResponse.optString("Metascore");
            actors =baseJsonResponse.optString("Actors");

            director = baseJsonResponse.optString("Director");
            writers = baseJsonResponse.optString("Writer");
            rel =baseJsonResponse.optString("Released");
            poster=baseJsonResponse.optString("Poster");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MovieInfo movie1 = new MovieInfo(title, genre, imdb, meta, rel, director, writers, actors, plot,poster);
        return movie1;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (init) return;
        init = true;

        Drawable[] subButtonDrawables = new Drawable[3];
        int[] drawablesResource = new int[]{
                R.drawable.final_logo,
                R.drawable.final_logo,
                R.drawable.final_logo
        };
        for (int i = 0; i < 3; i++)
            subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

        String[] subButtonTexts = new String[]{"BoomMenuButton", "View source code", "Follow me"};

        int[][] subButtonColors = new int[3][2];
        for (int i = 0; i < 3; i++) {
            subButtonColors[i][1] = ContextCompat.getColor(this, R.color.material_white);
            subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
        }

        // this is an example to show how to use the builder
        new BoomMenuButton.Builder()
                // set all sub buttons with subButtons method
                //.subButtons(subButtonDrawables, subButtonColors, subButtonTexts)
                // or add each sub button with addSubButton method
                .addSubButton(this, R.drawable.fav, subButtonColors[0], "Add to \"Favorites\"" )
                .addSubButton(this, R.drawable.seen, subButtonColors[1], "Add to \"Already watched\"")
                .addSubButton(this, R.drawable.wan, subButtonColors[2], "Add to \"Want to watch\"")
                .frames(80)
                .duration(800)
                .delay(100)
                .showOrder(OrderType.RANDOM)
                .hideOrder(OrderType.RANDOM)
                .button(ButtonType.HAM)
                .boom(BoomType.PARABOLA_2)
                .place(PlaceType.HAM_3_1)
                .showMoveEase(EaseType.EaseOutBack)
                .hideMoveEase(EaseType.EaseOutCirc)
                .showScaleEase(EaseType.EaseOutBack)
                .hideScaleType(EaseType.EaseOutCirc)
                .rotateDegree(720)
                .showRotateEase(EaseType.EaseOutBack)
                .hideRotateType(EaseType.Linear)
                .autoDismiss(true)
                .cancelable(true)
                .dim(DimType.DIM_6)
                .clickEffect(ClickEffectType.RIPPLE)
                .boomButtonShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonTextColor(Color.BLACK)
                .onBoomButtonBlick(null)
                .animator(null)
                .onSubButtonClick(null)
                // this only work when the place type is SHARE_X_X
                .shareStyle(0, 0, 0)
                .init(boomMenuButton);
        boomMenuButton.setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener() {
            @Override
            public void onClick(int buttonIndex) {

                if(buttonIndex==0)
                {
                Intent i=new Intent(Movie.this, TestDatabaseActivity.class);
                i.putExtra("movie_name", movie);
                startActivity(i);}

                if(buttonIndex==1)
                {
                    //Intent i1=new Intent(Movie.this, TestDatabaseActivity2.class);
                    //i1.putExtra("movie_name", movie);
                    //startActivity(i1);
                    Toast.makeText(Movie.this, "Feature under development. Please wait for next update", Toast.LENGTH_LONG).show();
                    }

                if(buttonIndex==2)
                {
                    //Intent i2=new Intent(Movie.this, TestDatabaseActivity1.class);
                    //i2.putExtra("movie_name", movie);
                    //startActivity(i2);
                    Toast.makeText(Movie.this, "Feature under development. Please wait for next update", Toast.LENGTH_LONG).show();
                    }
        }
    });
    };
}
