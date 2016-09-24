package com.udacity.mohamed.popularmovies;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.udacity.mohamed.popularmovies.adapter.MoviesAdapter;
import com.udacity.mohamed.popularmovies.model.Movie;
import com.udacity.mohamed.popularmovies.model.MovieTable;
import com.udacity.mohamed.popularmovies.reponse.MovieResponse;
import com.udacity.mohamed.popularmovies.rest.ApiClient;
import com.udacity.mohamed.popularmovies.rest.ApiConfig;
import com.udacity.mohamed.popularmovies.rest.ApiInterface;
import com.udacity.mohamed.popularmovies.utilities.NetworkUtils;
import com.udacity.mohamed.popularmovies.utilities.ViewsUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements MovieListener{

	private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
	private MoviesAdapter mAdapter;
	private RecyclerView recList;
	private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
	private GridLayoutManager glm;
	private List<Movie> mMovies = new ArrayList<>();

	private Parcelable mPosition;

	private static final String SELECTED_KEY = "selected_position";

	private MaterialDialog materialDialog;

	public MoviesFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.moviesfragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here.
		int id = item.getItemId();
		if (id == R.id.action_most_popular) {
			fetchMostPopularMovies(apiService);
			return true;
		}
		else if (id == R.id.action_top_rated) {
			fetchTopRatedMovies(apiService);
			return true;
		}
		else if (id == R.id.action_favorites) {
			fetchFavorites();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

		// RecyclerView Setup
		recList = (RecyclerView) rootView.findViewById(R.id.recyclerview_movies);

		recList.setHasFixedSize(true);
		recList.setItemAnimator(new DefaultItemAnimator());
		glm = new GridLayoutManager(getActivity(), 2);
		recList.setLayoutManager(glm);

		mAdapter = new MoviesAdapter(mMovies);
		mAdapter.setMovieListener(this);
		recList.setAdapter(mAdapter);

		if (mAdapter.getItemCount() == 0) {
			fetchMostPopularMovies(apiService);
		}

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save list state
		mPosition = glm.onSaveInstanceState();
		outState.putParcelable(SELECTED_KEY, mPosition);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mPosition != null) {
			glm.onRestoreInstanceState(mPosition);
		}
	}

	// Fetch top rated movies and set MoviesAdapter
	private void fetchTopRatedMovies (ApiInterface apiService) {
		if (NetworkUtils.isNetworkConnected(getActivity())) {
			showProgressDialog();
			Call<MovieResponse> callMovies = apiService.getTopRatedMovies(ApiConfig.API_KEY);
			callMovies.enqueue(new Callback<MovieResponse>() {
				@Override
				public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
					hideProgressDialog();
					List<Movie> movies = response.body().getResults();
					// update Adapter
					updateAdapter(movies);
				}

				@Override
				public void onFailure(Call<MovieResponse>call, Throwable t) {
					hideProgressDialog();
					ViewsUtils.showToast(getResources().getString(R.string.failure_msg), getActivity());
				}
			});
		}
		else {
			ViewsUtils.showToast(getResources().getString(R.string.no_internet), getActivity());
		}
	}

	// Fetch most popular movies and set MoviesAdapter
	private void fetchMostPopularMovies (ApiInterface apiService) {
		if (NetworkUtils.isNetworkConnected(getActivity())) {
			showProgressDialog();
			Call<MovieResponse> callMovies = apiService.getMostPopularMovies(ApiConfig.API_KEY);
			callMovies.enqueue(new Callback<MovieResponse>() {
				@Override
				public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
					hideProgressDialog();
					List<Movie> movies = response.body().getResults();
					// update Adapter
					updateAdapter(movies);
				}

				@Override
				public void onFailure(Call<MovieResponse> call, Throwable t) {
					hideProgressDialog();
					ViewsUtils.showToast(getResources().getString(R.string.failure_msg), getActivity());
				}
			});
		}
		else {
			ViewsUtils.showToast(getResources().getString(R.string.no_internet), getActivity());
		}
	}

	private void updateAdapter(List<Movie> movies){
		mMovies.clear();
		mMovies.addAll(movies);
		mAdapter.notifyDataSetChanged();
	}

	private void fetchFavorites() {
		Cursor cursor = getActivity().getContentResolver().query(MovieTable.CONTENT_URI,null,null,null,null);
		List<Movie> movies = MovieTable.getRows(cursor,false);
		// updateAdapter
		updateAdapter(movies);
	}

	@Override
	public void onMovieSelected(Movie movie) {
		if (getView().getRootView().findViewById(R.id.movie_details_container) != null){
			DetailFragment df = new DetailFragment();
			df.setMovie(movie);
			getFragmentManager().beginTransaction().replace(R.id.movie_details_container, df).commit();
		}
		else{
			Intent intent = new Intent(getActivity(), DetailActivity.class)
					.putExtra("movie", (Parcelable) movie);
			startActivity(intent);
		}
	}

	public void showProgressDialog() {
		materialDialog = new MaterialDialog.Builder(getActivity())
				.content(getResources().getString(R.string.waiting))
				.cancelable(true)
				.progress(true, 0)
				.show();
	}

	public void hideProgressDialog() {
		materialDialog.dismiss();
	}
}
