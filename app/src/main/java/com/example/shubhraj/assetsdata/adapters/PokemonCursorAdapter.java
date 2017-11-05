package com.example.shubhraj.assetsdata.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shubhraj.assetsdata.R;
import com.example.shubhraj.assetsdata.data.PokemonContract;

/**
 * Created by Shubhraj on 24-10-2017.
 */

public class PokemonCursorAdapter extends CursorAdapter
{

    public PokemonCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.pokemon_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView typeTextView = (TextView) view.findViewById(R.id.summary);
        ImageView typeImageView = (ImageView) view.findViewById(R.id.pokemon_type_image);

        int nameColumnIndex = cursor.getColumnIndex(PokemonContract.PokemonEntry.POKEMON_NAME);
        int typeColumnIndex = cursor.getColumnIndex(PokemonContract.PokemonEntry.POKEMON_TYPE);

        String pokemonName = cursor.getString(nameColumnIndex);
        String pokemonType = cursor.getString(typeColumnIndex);

        if (TextUtils.isEmpty(pokemonType)) {
            pokemonType = context.getString(R.string.unknown_breed);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                typeImageView.setImageDrawable(context.getDrawable(R.drawable.dragon_type_pokemon));
            }
            else
            {
                typeImageView.setImageResource(R.drawable.dragon_type_pokemon);
            }
        }
        else
        {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (pokemonType.equals("Electric"))
                       typeImageView.setImageDrawable(context.getDrawable(R.drawable.electric_type_pokemon));
                    else if (pokemonType.equals("Water"))
                        typeImageView.setImageDrawable(context.getDrawable(R.drawable.water_pokemon_badge));
                    else if (pokemonType.equals("Ground"))
                        typeImageView.setImageDrawable(context.getDrawable(R.drawable.rock_type_pokemon));
                    else if (pokemonType.equals("Grass"))
                        typeImageView.setImageDrawable(context.getDrawable(R.drawable.grass_pokemon_badge));
                    else if (pokemonType.equals("Fire"))
                        typeImageView.setImageDrawable(context.getDrawable(R.drawable.fire_pokemon_badge));
                }
                else
                {
                    if (pokemonType.equals("Electric"))
                        typeImageView.setImageResource(R.drawable.electric_type_pokemon);
                    else if (pokemonType.equals("Water"))
                        typeImageView.setImageResource(R.drawable.water_pokemon_badge);
                    else if (pokemonType.equals("Ground"))
                        typeImageView.setImageResource(R.drawable.rock_type_pokemon);
                    else if (pokemonType.equals("Grass"))
                        typeImageView.setImageResource(R.drawable.grass_pokemon_badge);
                    else if (pokemonType.equals("Fire"))
                        typeImageView.setImageResource(R.drawable.fire_pokemon_badge);
                }
        }

        nameTextView.setText(pokemonName);
        typeTextView.setText(pokemonType);
    }
}
