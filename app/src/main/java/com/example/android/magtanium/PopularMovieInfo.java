package com.example.android.magtanium;

/**
 * Created by MAHE on 9/27/2016.
 */
public class PopularMovieInfo {

    String mMovieName;
    String mOverview;
    String mIMDBrating;
    String mImageLink;

    PopularMovieInfo(String mov, String over, String imdb, String image)
    {
        mMovieName=mov;
        mOverview=over;
        mIMDBrating=imdb;
        mImageLink=image;
    }

    PopularMovieInfo()
    {

    }
    public String getMovieName()
    {
        return mMovieName;
    }
    public String getOverview()
    {
        return mOverview;
    }
    public String getImageLink()
    {
        return mImageLink;
    }

    public String getIMDBrating()
    {
        return mIMDBrating;
    }

}
