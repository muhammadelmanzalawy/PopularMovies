package com.udacity.mohamed.popularmovies.utilities;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Mohamed on 19/09/2016.
 */

public class ViewsUtils {
	public static void showToast(String message, Context context) {
		showMessageInToast(message, context);
	}

	private static void showMessageInToast(String message, Context ctx) {
		if (ctx != null)
			Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
	}
}
