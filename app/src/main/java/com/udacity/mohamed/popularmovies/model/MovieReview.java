package com.udacity.mohamed.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed on 17/09/2016.
 */
public class MovieReview implements Parcelable {
	private String id;
	private String author;
	private String content;
	private String url;

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	protected MovieReview(Parcel in) {
		id = in.readString();
		author = in.readString();
		content = in.readString();
		url = in.readString();
	}

	public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
		@Override
		public MovieReview createFromParcel(Parcel in) {
			return new MovieReview(in);
		}

		@Override
		public MovieReview[] newArray(int size) {
			return new MovieReview[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(author);
		dest.writeString(content);
		dest.writeString(url);
	}
}
