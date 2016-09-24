package com.udacity.mohamed.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Mohamed on 23/09/2016.
 */

public class NetworkUtils {

	public static boolean isNetworkConnected(Context context){
		try{
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			return netInfo != null;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
