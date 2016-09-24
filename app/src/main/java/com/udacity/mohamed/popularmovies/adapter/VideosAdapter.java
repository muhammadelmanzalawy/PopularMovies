package com.udacity.mohamed.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.mohamed.popularmovies.DetailActivity;
import com.udacity.mohamed.popularmovies.R;
import com.udacity.mohamed.popularmovies.model.MovieVideos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mohamed on 18/09/2016.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

	private List<MovieVideos> movieVideos = Collections.emptyList();

	// Basic Constructor
	public VideosAdapter(List<MovieVideos> movieVideos) {
		this.movieVideos = movieVideos;
	}

	// No passed params constructor
	public VideosAdapter() {
		this.movieVideos = new ArrayList<MovieVideos>();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
		// Declare items
		private ItemClickListener clickListener;
		public ImageView videoImage;
		public ViewHolder(View v) {
			super(v);
			videoImage = (ImageView) v.findViewById(R.id.video_iv);
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

	@Override
	public VideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_video, parent, false);

		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Context context = holder.videoImage.getContext();
		final MovieVideos video = movieVideos.get(position);
		Picasso.with(context).load("http://img.youtube.com/vi/" + video.getKey() + "/hqdefault.jpg").into(holder.videoImage);

		// Handling click Event to Launch the trailer
		holder.setClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position, boolean isLongClick) {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return movieVideos.size();
	}
}
