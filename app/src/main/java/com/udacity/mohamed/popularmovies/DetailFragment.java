package com.udacity.mohamed.popularmovies;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.mohamed.popularmovies.adapter.ReviewsAdapter;
import com.udacity.mohamed.popularmovies.adapter.VideosAdapter;
import com.udacity.mohamed.popularmovies.model.Movie;
import com.udacity.mohamed.popularmovies.model.MovieReview;
import com.udacity.mohamed.popularmovies.model.MovieTable;
import com.udacity.mohamed.popularmovies.model.MovieVideos;
import com.udacity.mohamed.popularmovies.reponse.MovieReviewResponse;
import com.udacity.mohamed.popularmovies.reponse.MovieVideosResponse;
import com.udacity.mohamed.popularmovies.rest.ApiClient;
import com.udacity.mohamed.popularmovies.rest.ApiConfig;
import com.udacity.mohamed.popularmovies.rest.ApiInterface;
import com.udacity.mohamed.popularmovies.utilities.ViewsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed on 17/09/2016.
 */
public class DetailFragment extends Fragment {

	private static final String LOG_TAG = DetailFragment.class.getSimpleName();
	private Movie movie;
	private ReviewsAdapter rAdapter;
	private VideosAdapter vAdapter;
	private RecyclerView reviewsRecList, videosRecList;
	private TextView titleTextView, voteAverageTextView, releaseDateTextView, overviewTextView;
	private ImageView posterImageView, posterHeaderImageView;
	private View rootView;
	private Button favBtn;

	private ShareActionProvider mShareActionProvider;
	private String mYouTubeUrl;

	public DetailFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_detail, container, false);

		// RecyclerView Setup - Videos
		videosRecList = (RecyclerView) rootView.findViewById(R.id.recyclerview_movies_videos);

		videosRecList.setHasFixedSize(true);
		videosRecList.setItemAnimator(new DefaultItemAnimator());
		LinearLayoutManager llmVideos = new LinearLayoutManager(getActivity());
		llmVideos.setOrientation(LinearLayoutManager.HORIZONTAL);
		videosRecList.setLayoutManager(llmVideos);
		// Setting and empty adapter to the reclist
		videosRecList.setAdapter(new VideosAdapter());

		// RecyclerView Setup - Reviews
		reviewsRecList = (RecyclerView) rootView.findViewById(R.id.recyclerview_movies_reviews);

		reviewsRecList.setHasFixedSize(true);
		reviewsRecList.setItemAnimator(new DefaultItemAnimator());
		LinearLayoutManager llmReviews = new LinearLayoutManager(getActivity());
		llmReviews.setOrientation(LinearLayoutManager.VERTICAL);
		reviewsRecList.setLayoutManager(llmReviews);
		// Setting and empty adapter to the reclist
		reviewsRecList.setAdapter(new ReviewsAdapter());

		// Get data from intent
		Intent intent = getActivity().getIntent();
		if (intent != null && intent.hasExtra("movie")) {
			// Relieve object from Extras
			movie = intent.getParcelableExtra("movie");
		}

		if (movie != null) {
			// InitViews
			initViews();

			// Inflate Data
			inflateData();

			// Handling the onClick action of favorite button
			favBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (isFavorite(movie)){
						// The movie is already favorite then remove
						String[] selectionArgs = {String.valueOf(movie.getId())};
						String selection =  "col_int" + "=?";
						getActivity().getContentResolver().delete(MovieTable.CONTENT_URI
								, selection, selectionArgs);

						// Remove stored poster
						ContextWrapper cw = new ContextWrapper(getActivity());
						File directory = cw.getDir("com.udacity.popularmovies", getActivity().MODE_PRIVATE);
						File myImageFile = new File(directory, movie.getTitle());
						if (myImageFile.delete()) {
							Log.v("IMAGE DELETION" , "image on the disk deleted successfully!");
						}

						ViewsUtils.showToast(getResources().getString(R.string.favorite_removed_msg), getActivity());
						favBtn.setText(R.string.add_to_favorite);
					}
					else {
						// Insert Favorite movie details in the database

						// Saving Poster on Storage
						Target tr = new Target() {
							@Override
							public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										ContextWrapper cw = new ContextWrapper(getActivity());
										File directory = cw.getDir("com.udacity.popularmovies", getActivity().MODE_PRIVATE);
										File myImageFile = new File(directory, movie.getTitle()); // Create image file
										// Set the poster path to the storage's path
										movie.setPoster_path(myImageFile.getAbsolutePath());
										FileOutputStream fos = null;
										try {
											fos = new FileOutputStream(myImageFile);
											bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
										} catch (FileNotFoundException e) {
											e.printStackTrace();
										}
										try {
											fos.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}).start();
							}

							@Override
							public void onBitmapFailed(Drawable errorDrawable) {

							}

							@Override
							public void onPrepareLoad(Drawable placeHolderDrawable) {

							}
						};
						Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(tr);

						// Set Favorite attribute
						movie.setFavorite(true);

						// Movie model insertion in DB using Content Provider
						Uri insertionUri = getActivity().getContentResolver().insert(MovieTable.CONTENT_URI,MovieTable.getContentValues(movie,false));
						if (insertionUri != null){
							ViewsUtils.showToast(getResources().getString(R.string.favorite_added_msg), getActivity());
							favBtn.setText(R.string.remove_from_favorite);
						}
						else {
							ViewsUtils.showToast(getResources().getString(R.string.failure_msg), getActivity());
						}
					}
				}
			});

			// Set Action Bar Title
			getActivity().setTitle(movie.getTitle());
		}

		return rootView;
	}

	private void initViews() {
		// Detail fragment UI elements
		posterImageView        = (ImageView) rootView.findViewById(R.id.detail_poster);
		posterHeaderImageView  = (ImageView) rootView.findViewById(R.id.detail_poster_header);
		titleTextView          = (TextView) rootView.findViewById(R.id.detail_title);
		voteAverageTextView    = (TextView) rootView.findViewById(R.id.detail_voteAverage);
		releaseDateTextView    = (TextView) rootView.findViewById(R.id.detail_releaseDate);
		overviewTextView       = (TextView) rootView.findViewById(R.id.detail_overview);
		favBtn                 = (Button) rootView.findViewById(R.id.favBtn);
	}

	private void inflateData() {
		// Setup layout with variables
		titleTextView.setText(movie.getTitle());
		if (movie.getFavorite()) {
			ContextWrapper cw = new ContextWrapper(getActivity());
			File directory = cw.getDir("com.udacity.popularmovies", Context.MODE_PRIVATE);
			File myImageFile = new File(directory, movie.getTitle());
			Picasso.with(getActivity()).load(myImageFile).into(posterHeaderImageView);
			Picasso.with(getActivity()).load(myImageFile).into(posterImageView);
		}
		else{
			Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(posterHeaderImageView);
			Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(posterImageView);
		}
		voteAverageTextView.setText(String.valueOf(movie.getVote_average()));
		releaseDateTextView.setText(movie.getRelease_date());
		overviewTextView.setText(movie.getOverview());

		ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

		// Retrieve Movies Reviews and Initiate adapter
		fetchMovieVideos(apiService);

		// Retrieve Movies Reviews
		fetchMovieReviews(apiService);

		// Check whether the movie is in favorites or not
		// Setup Favorite Button
		if (isFavorite(movie)) {
			favBtn.setText("Remove From Favorites");
		}
		else {
			favBtn.setText("Add To Favorites");
		}
	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.detailfragment, menu);

		// Retrieve the share menu item
		MenuItem menuItem = menu.findItem(R.id.action_share);

		// Get the provider and hold onto it to set/change the share intent.
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
	}

	private Intent createShareForecastIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, mYouTubeUrl);
		return shareIntent;
	}

	// Fetch movie trailers and set VideosAdapter
	private void fetchMovieVideos (ApiInterface apiService) {
		Call<MovieVideosResponse> callVideos = apiService.getMovieVideos(movie.getId(), ApiConfig.API_KEY);
		callVideos.enqueue(new Callback<MovieVideosResponse>() {
			@Override
			public void onResponse(Call<MovieVideosResponse> call, Response<MovieVideosResponse> response) {
				List<MovieVideos> movieVideos = response.body().getResults();
				vAdapter = new VideosAdapter(movieVideos);
				videosRecList.setAdapter(vAdapter);

				// Set YouTube sharing intent with URL
				if (movieVideos.get(0) != null)
				mYouTubeUrl = "http://www.youtube.com/watch?v=" + movieVideos.get(0).getKey();
				// If onLoadFinished happens before this, we can go ahead and set the share intent now.
				if (mYouTubeUrl != null) {
					mShareActionProvider.setShareIntent(createShareForecastIntent());
				}
			}

			@Override
			public void onFailure(Call<MovieVideosResponse>call, Throwable t) {
				ViewsUtils.showToast(getResources().getString(R.string.videos_failure_msg), getActivity());
			}
		});
	}

	// Fetch movie reviews and set ReviewsAdapter
	private void fetchMovieReviews (ApiInterface apiService) {
		Call<MovieReviewResponse> callReviews = apiService.getMovieReviews(movie.getId(), ApiConfig.API_KEY);
		callReviews.enqueue(new Callback<MovieReviewResponse>() {
			@Override
			public void onResponse(Call<MovieReviewResponse> call, Response<MovieReviewResponse> response) {
				List<MovieReview> movieReviews = response.body().getResults();
				rAdapter = new ReviewsAdapter(movieReviews);
				reviewsRecList.setAdapter(rAdapter);
			}

			@Override
			public void onFailure(Call<MovieReviewResponse>call, Throwable t) {
				ViewsUtils.showToast(getResources().getString(R.string.reviews_failure_msg), getActivity());
			}
		});
	}

	private boolean isFavorite (Movie m) {
		// Check whether the movie is in favorites or not
		String[] selectionArgs = {String.valueOf(m.getId())};
		String selection =  "col_int" + "=?";
		Cursor c = getActivity().getContentResolver().query(MovieTable.CONTENT_URI,null,selection,selectionArgs,null);

		// Setup Favorite Button
		if (c.getCount() >=1) {
			return true;
		}
		else {
			return false;
		}
	}

	public void setMovie (Movie movie){
		this.movie = movie;
	}
}
