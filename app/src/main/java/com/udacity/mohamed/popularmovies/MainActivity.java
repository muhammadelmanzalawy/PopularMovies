package com.udacity.mohamed.popularmovies;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.udacity.mohamed.popularmovies.model.Movie;
import com.udacity.mohamed.popularmovies.model.MovieReview;
import com.udacity.mohamed.popularmovies.reponse.MovieResponse;
import com.udacity.mohamed.popularmovies.reponse.MovieReviewResponse;
import com.udacity.mohamed.popularmovies.rest.ApiClient;
import com.udacity.mohamed.popularmovies.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String DETAILFRAGMENT_TAG = "DFTAG";
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new MoviesFragment())
//					.commit();
//		}

		if (findViewById(R.id.movie_details_container) != null) {
			mTwoPane = true;
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			if (savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.movie_details_container, new DetailFragment(), DETAILFRAGMENT_TAG)
						.commit();
			}
		} else {
			mTwoPane = false;
		}
	}

}
