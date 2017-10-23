package com.example.shubhraj.assetsdata.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shubhraj.assetsdata.R;
import com.example.shubhraj.assetsdata.model.Pokemon;

import java.util.ArrayList;

/**
 * Created by Shubhraj on 23-10-2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<Pokemon> pokemons;

    public RecyclerViewAdapter(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_item,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pokemon pokemon = pokemons.get(position);
        holder.pokemonNameText.setText(pokemon.getName());
        holder.pokemonTypeText.setText(pokemon.getType());
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pokemonNameText, pokemonTypeText;
        public ViewHolder(View itemView) {
            super(itemView);
            pokemonNameText = (TextView) itemView.findViewById(R.id.pokemon_name);
            pokemonTypeText = (TextView) itemView.findViewById(R.id.pokemon_type);
        }
    }
}
