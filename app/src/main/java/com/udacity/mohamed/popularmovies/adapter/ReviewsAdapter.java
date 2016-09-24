package com.udacity.mohamed.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.mohamed.popularmovies.R;
import com.udacity.mohamed.popularmovies.model.MovieReview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This adapter is responsible of managing the movie reviews within the recycler view of reviews
 * Created by Mohamed on 17/09/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

	private List<MovieReview> movieReviews = Collections.emptyList();

	// Basic Constructor
	public ReviewsAdapter(List<MovieReview> reviews) {
		this.movieReviews = reviews;
	}

	// No passed params constructor
	public ReviewsAdapter() {
		movieReviews = new ArrayList<MovieReview>();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		// Declare items
		public TextView reviewAuthorTv;
		public TextView reviewContentTv;
		public ViewHolder(View v) {
			super(v);
			reviewAuthorTv = (TextView) v.findViewById(R.id.review_author);
			reviewContentTv = (TextView) v.findViewById(R.id.review_content);
			v.setTag(v);
		}

	}

	@Override
	public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_review, parent, false);

		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final MovieReview movieReview = movieReviews.get(position);
		// Set Text Values
		holder.reviewAuthorTv.setText(movieReview.getAuthor());
		holder.reviewContentTv.setText(movieReview.getContent());
	}

	@Override
	public int getItemCount() {
		return movieReviews.size();
	}
}
