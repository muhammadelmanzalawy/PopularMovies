package com.udacity.mohamed.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(table = "Movie", provider = "MovieProvider")
public class Movie implements Parcelable{

	// Movie Properties
	@SimpleSQLColumn("col_int")
	private int id;

	@SimpleSQLColumn("col_str")
	private String title;

	@SimpleSQLColumn("col_str2")
	private String overview;

	@SimpleSQLColumn("col_str3")
	private String poster_path;

	@SimpleSQLColumn("col_str4")
	private String release_date;

	@SimpleSQLColumn("col_bool")
	private boolean favorite;

	@SimpleSQLColumn("col_float")
	private float vote_average;

	// Constructor
	public Movie(int id, String title, String overview, String poster_path, String release_date, Boolean favorite, long vote_average) {
		this.vote_average = vote_average;
		this.setId(id);
		this.setTitle(title);
		this.setOverview(overview);
		this.setPoster_path(poster_path);
		this.setRelease_date(release_date);
		this.setFavorite(favorite);
		this.setVote_average(vote_average);
	}

	public Movie () {}

	protected Movie(Parcel in) {
		id = in.readInt();
		title = in.readString();
		overview = in.readString();
		poster_path = in.readString();
		release_date = in.readString();
		favorite = in.readByte()!=0;
		vote_average = in.readFloat();
	}

	public static final Creator<Movie> CREATOR = new Creator<Movie>() {
		@Override
		public Movie createFromParcel(Parcel in) {
			return new Movie(in);
		}

		@Override
		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};

	// Setters and Getters
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getOverview() {
		return overview;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	public String getRelease_date() {
		return release_date;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(overview);
		dest.writeString(poster_path);
		dest.writeString(release_date);
		dest.writeByte((byte) (favorite ? 1 : 0));
		dest.writeFloat(vote_average);
	}

	public boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public void setVote_average(float vote_average) {
		this.vote_average = vote_average;
	}

	public float getVote_average() {
		return vote_average;
	}
}
