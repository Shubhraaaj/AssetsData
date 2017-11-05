package com.example.shubhraj.assetsdata.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Shubhraj on 24-10-2017.
 */

public final class PokemonContract
{
    public PokemonContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.shubhraj.assetsdata";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POKEMONS = "pokemons";

    public static final class PokemonEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_POKEMONS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POKEMONS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POKEMONS;

        public static final String TABLE_NAME = "pokemons";

        public static final String _ID = BaseColumns._ID;

        public static final String POKEMON_NAME = "name";

        public static final String POKEMON_TYPE = "type";
    }
}
