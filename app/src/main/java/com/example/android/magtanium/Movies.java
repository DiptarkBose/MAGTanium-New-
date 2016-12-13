package com.example.android.magtanium;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movies extends android.support.v4.app.Fragment{


    private ListAdapter adapter;
    public  List<PopularMovieInfo> movieList = new ArrayList<>();
    // private List<SilhouetteDescription> filteredList=new ArrayList<>();
    private RecyclerView recyclerView;
    TextView tv1, tv2, tv4, tv5, tv6, tv7, tv8, tv9;

    ImageView img;
    PopularMovieInfo defaultmo= new PopularMovieInfo();
    PopularMovieInfo movie2;
    ProgressDialog mProgressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View v= inflater.inflate(R.layout.activity_movies, container, false);
        Button btn = (Button)v.findViewById(R.id.se_mov);
        img=(ImageView) v.findViewById(R.id.individual_mov_im);
        tv1=(TextView) v.findViewById(R.id.movie_name);
        tv2=(TextView) v.findViewById(R.id.rating);



        final EditText et= (EditText) v.findViewById(R.id.ed_text);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.popflixlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
         //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        adapter = new ListAdapter(movieList, getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        StringBuilder QUERY = new StringBuilder();
        QUERY.append("http://api.themoviedb.org/3/movie/popular?api_key=514d57fbd64c2ccaaa4aeb5c9f81c89b");
        MovieAsyncTask movie_search = new MovieAsyncTask();
        movie_search.execute(QUERY.toString());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(getActivity(), LoadingScreen.class);
                        String film_name = (movieList.get(position).getMovieName());
                        i.putExtra("Movie name", film_name);
                        startActivity(i);
                        // do whatever
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                })
        );
                    btn.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View view) {
                            Intent i = new Intent(getActivity(), LoadingScreen.class);
                            i.putExtra("Movie name", et.getText().toString());
                            startActivity(i);
                        }
                    });
                    return v;

                }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Movies.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Movies.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private class MovieAsyncTask extends AsyncTask<String, Void, List<PopularMovieInfo>> {
        @Override
        protected List<PopularMovieInfo> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            String jsonresp = QueryUtils.getData(urls[0]);
            if (jsonresp != null) {
                return extractMovie(jsonresp);
            }
            else
                return null;

        }

        @Override
        protected void onPostExecute(List<PopularMovieInfo> mov) {

            recyclerView.setHasFixedSize(true);
            adapter = new ListAdapter(movieList, getActivity());
            //recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }
    }


    private List<PopularMovieInfo> extractMovie(String jsonResp) {
        String plot = "";
        String title = "";
        String imdb = "";
        String poster="";
        String poster_movie="";
        int i;


        if (TextUtils.isEmpty(jsonResp)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResp);
            JSONArray popular_movies_array=baseJsonResponse.getJSONArray("results");
            for(i=0; i<50; i++)
            {
                JSONObject movie_item=popular_movies_array.getJSONObject(i);
                plot = movie_item.optString("overview");
                title = movie_item.optString("original_title");
                imdb =movie_item.optString("vote_average");
                poster=movie_item.optString("poster_path");
                poster_movie=("http://image.tmdb.org/t/p/w780/" + poster.substring(1));
                movieList.add(new PopularMovieInfo(title,plot,imdb,poster_movie));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

}



