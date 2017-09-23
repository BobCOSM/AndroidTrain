package com.bobc.trainservice.contentprovider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TrainUriContract {
	protected static final String CONTENT_AUTHOR = "com.bobc.trainservice";
	protected static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHOR);
	protected static final String TABLE_NAME = "test";
	public static final String COLUME_NAME = "name";
	public static final String PATH_TEST = "test";
	public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEST).build();

	public static Uri bindUri(long id){
		return ContentUris.withAppendedId(CONTENT_URI, id);
	}
}
