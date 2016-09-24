package com.udacity.mohamed.popularmovies.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.mohamed.popularmovies.DetailActivity;
import com.udacity.mohamed.popularmovies.DetailFragment;
import com.udacity.mohamed.popularmovies.MovieListener;
import com.udacity.mohamed.popularmovies.R;
import com.udacity.mohamed.popularmovies.model.Movie;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

	private List<Movie> mDataset = Collections.emptyList();
	private MovieListener mListener;

	// Provide a suitable constructor (depends on the kind of dataset)
	public MoviesAdapter(List<Movie> myDataset) {
		mDataset = myDataset;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		// Declare items
		private ItemClickListener clickListener;
		public ImageView mImageView;
		public TextView movieTitle, movieRating;
		public ViewHolder(View v) {
			super(v);
			mImageView = (ImageView) v.findViewById(R.id.grid_item_movie_imageview);
			movieTitle = (TextView) v.findViewById(R.id.grid_item_movie_title_textview);
			movieRating = (TextView) v.findViewById(R.id.grid_item_movie_rating_textview);
			v.setTag(v);
			v.setOnClickListener(this);
		}

		public void setClickListener(ItemClickListener itemClickListener) {
			this.clickListener = itemClickListener;
		}

		@Override
		public void onClick(View view) {
			clickListener.onClick(view, getPosition(), false);
		}

	}

	// No passed params constructor
	public MoviesAdapter() {
		mDataset = new ArrayList<Movie>();
	}

	// Create new views (invoked by the layout manager)
	@Override
	public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
	                                               int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.grid_item_movie, parent, false);
		// set the view's size, margins, paddings and layout parameters
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Context context = holder.mImageView.getContext();
		final Movie movie = mDataset.get(position);

		// Check whether this is a favorite movie or not for loading the poster
		if (movie.getFavorite()) {
			ContextWrapper cw = new ContextWrapper(context);
			File directory = cw.getDir("com.udacity.popularmovies", Context.MODE_PRIVATE);
			File myImageFile = new File(directory, movie.getTitle());
			Picasso.with(context).load(myImageFile).into(holder.mImageView);
		}
		else {
			Picasso.with(context).load("http://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(holder.mImageView);
		}
		holder.movieTitle.setText(movie.getTitle());
		holder.movieRating.setText(String.valueOf(movie.getVote_average()));

		// Handling click Event
		holder.setClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position, boolean isLongClick) {
				if(mListener != null) {
					mListener.onMovieSelected(movie);
				}
			}
		});
	}

	public  void setMovieListener (MovieListener movieListener) {
		this.mListener = movieListener;
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mDataset.size();
	}
}
