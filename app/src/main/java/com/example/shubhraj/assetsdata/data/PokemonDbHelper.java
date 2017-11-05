package com.example.shubhraj.assetsdata.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.shubhraj.assetsdata.data.PokemonContract.PokemonEntry;

/**
 * Created by Shubhraj on 24-10-2017.
 */

public class PokemonDbHelper extends SQLiteOpenHelper
{
    private static final String TAG = "PokemonDbHelper";

    private static final String DATABASE_NAME = "pokemonlist.db";

    private static final int DATABASE_VERSION = 1;

    public PokemonDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + PokemonEntry.TABLE_NAME
                + " (" + PokemonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PokemonEntry.POKEMON_NAME + " TEXT NOT NULL, "
                + PokemonEntry.POKEMON_TYPE + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}