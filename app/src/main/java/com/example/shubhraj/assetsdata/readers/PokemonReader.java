package com.example.shubhraj.assetsdata.readers;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.example.shubhraj.assetsdata.R;
import com.example.shubhraj.assetsdata.model.Pokemon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Shubhraj on 23-10-2017.
 */

public class PokemonReader {
    private static final String TAG = "PokemonReader";
    public static ArrayList<Pokemon> loadPokemonnFromAsset(Context context) throws IOException
    {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        final Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.items);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strings = TextUtils.split(line, ",");
                if (strings.length < 2)
                    continue;
                pokemons.add(new Pokemon(strings[0].trim(), strings[1].trim()));
            }
        }
        finally {
            reader.close();
        }
        return pokemons;
    }
}
