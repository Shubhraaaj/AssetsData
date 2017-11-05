package com.example.shubhraj.assetsdata.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.shubhraj.assetsdata.data.PokemonContract.PokemonEntry.TABLE_NAME;

/**
 * Created by Shubhraj on 24-10-2017.
 */

public class PokemonProvider extends ContentProvider
{
    public static final String TAG = "PokemonProvider";

    public static final int POKEMONS = 100;

    public static final int POKEMON_ID = 101;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        sUriMatcher.addURI(PokemonContract.CONTENT_AUTHORITY, PokemonContract.PATH_POKEMONS, POKEMONS);
        sUriMatcher.addURI(PokemonContract.CONTENT_AUTHORITY, PokemonContract.PATH_POKEMONS + "/#", POKEMON_ID);
    }

    private PokemonDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new PokemonDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projections, @Nullable String selections,
                        @Nullable String[] selectionArgs, @Nullable String sortByOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match)
        {
            case POKEMONS:
                cursor = database.query(TABLE_NAME, projections, selections, selectionArgs,
                        null, null, sortByOrder);
                break;
            case POKEMON_ID:
                selections = PokemonContract.PokemonEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME, projections, selections, selectionArgs,
                        null, null, sortByOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POKEMONS:
                return PokemonContract.PokemonEntry.CONTENT_LIST_TYPE;
            case POKEMON_ID:
                return PokemonContract.PokemonEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case POKEMONS:
                return insertPokemon(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPokemon(Uri uri, ContentValues values) {
        String pokemonName = values.getAsString(PokemonContract.PokemonEntry.POKEMON_NAME);

        if (pokemonName == null) {
            throw new IllegalArgumentException("Pokemon requires a name");
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(PokemonContract.PokemonEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POKEMONS:
                rowsDeleted = database.delete(TABLE_NAME, selection,
                        selectionArgs);
                break;
            case POKEMON_ID:
                selection = PokemonContract.PokemonEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POKEMONS:
                return updatePokemon(uri, contentValues, selection, selectionArgs);
            case POKEMON_ID:
                selection = PokemonContract.PokemonEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePokemon(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePokemon(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(PokemonContract.PokemonEntry.POKEMON_NAME))
        {
            String name = values.getAsString(PokemonContract.PokemonEntry.POKEMON_NAME);
            if (name == null)
            {
                throw new IllegalArgumentException("Pokemon requires a name");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsUpdated = database.update(PokemonContract.PokemonEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}