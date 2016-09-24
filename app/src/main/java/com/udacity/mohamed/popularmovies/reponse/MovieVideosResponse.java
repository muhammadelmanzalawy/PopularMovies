package com.udacity.mohamed.popularmovies.reponse;

import com.udacity.mohamed.popularmovies.model.MovieVideos;

import java.util.List;

/**
 * Created by Mohamed on 17/09/2016.
 */
public class MovieVideosResponse {

	private Integer id;
	private List<MovieVideos> results;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<MovieVideos> getResults() {
		return results;
	}

	public void setResults(List<MovieVideos> results) {
		this.results = results;
	}
}

