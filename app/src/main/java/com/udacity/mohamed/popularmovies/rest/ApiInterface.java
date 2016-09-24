package com.udacity.mohamed.popularmovies.rest;

import com.udacity.mohamed.popularmovies.model.MovieReview;
import com.udacity.mohamed.popularmovies.reponse.MovieResponse;
import com.udacity.mohamed.popularmovies.reponse.MovieReviewResponse;
import com.udacity.mohamed.popularmovies.reponse.MovieVideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mohamed on 17/09/2016.
 */
public interface ApiInterface {
	@GET("movie/top_rated")
	Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

	@GET("movie/popular")
	Call<MovieResponse> getMostPopularMovies(@Query("api_key") String apiKey);

	@GET("movie/{id}/videos")
	Call<MovieVideosResponse> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);

	@GET("movie/{id}/reviews")
	Call<MovieReviewResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
