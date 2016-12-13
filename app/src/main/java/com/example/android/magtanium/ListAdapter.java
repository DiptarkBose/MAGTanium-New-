package com.example.android.magtanium;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<PopularMovieInfo> movie_list_item;
    public Context mcontext;


    public ListAdapter(List<PopularMovieInfo> list, Context context) {

        movie_list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.individual_list_item, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        final PopularMovieInfo obj = movie_list_item.get(position);
        viewHolder.name.setText(obj.getMovieName());

        viewHolder.info.setText(obj.getIMDBrating());
        Picasso.with(mcontext).load(obj.getImageLink()).resize(125, 100).into(viewHolder.image);



    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;
        public TextView info;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.movie_name);
            info = (TextView) itemLayoutView.findViewById((R.id.rating));
            image=(ImageView) itemLayoutView.findViewById(R.id.individual_mov_im);

        }
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return movie_list_item.size();
    }
}

