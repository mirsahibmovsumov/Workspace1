package com.exchange;

import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

	private static final String DB_NAME = "storeXML";

	private static final String TABLE_NAME = "Valute";

	private static final int DB_VERSION = 1;

	private static final String NAME = "name";
	private static final String VALUE = "value";

	public DBHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + "("
				+ "ID INTEGER PRIMARY KEY   AUTOINCREMENT ," + NAME + " TEXT,"
				+ VALUE + " real)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("Drop table if is exist " + TABLE_NAME);
		onCreate(db);
	}

	public void addValute(Map<String, String> valute) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (String key : valute.keySet()) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(NAME, key);
			contentValues.put(VALUE, valute.get(key));
			db.insert(TABLE_NAME, null, contentValues);
		}
		db.close();
	}

	public double getValue(String string) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME,
				new String[] { "ID", NAME, VALUE }, NAME + " = ? ",
				new String[] { string }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Valute valute = new Valute(cursor.getString(1), Double.valueOf(cursor
				.getString(2)));
		return valute.getValue();
	}

}
