package com.udacity.mohamed.popularmovies.reponse;

import com.udacity.mohamed.popularmovies.model.MovieReview;

import java.util.List;

/**
 * Created by Mohamed on 17/09/2016.
 */
public class MovieReviewResponse {
	private Integer id;
	private Integer page;
	private List<MovieReview> results;
	private Integer total_pages;
	private Integer total_results;

	// Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<MovieReview> getResults() {
		return results;
	}

	public void setResults(List<MovieReview> results) {
		this.results = results;
	}

	public Integer getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(Integer total_pages) {
		this.total_pages = total_pages;
	}

	public Integer getTotal_results() {
		return total_results;
	}

	public void setTotal_results(Integer total_results) {
		this.total_results = total_results;
	}
}
