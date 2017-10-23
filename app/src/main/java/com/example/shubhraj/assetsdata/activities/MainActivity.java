package com.example.shubhraj.assetsdata.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.shubhraj.assetsdata.R;
import com.example.shubhraj.assetsdata.adapters.RecyclerViewAdapter;
import com.example.shubhraj.assetsdata.model.Pokemon;
import com.example.shubhraj.assetsdata.readers.PokemonReader;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private Button pokemonButton;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<Pokemon> pokemons;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemons = new ArrayList<>();

        pokemonButton = (Button) findViewById(R.id.load_data_button);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(pokemons);
        recyclerView.setAdapter(adapter);
        pokemonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReadPokemonDataOnBackground().execute();
            }
        });

    }
    private class ReadPokemonDataOnBackground extends AsyncTask<Void, Integer, ArrayList<Pokemon>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Pokemon> doInBackground(Void... voids)
        {
            ArrayList<Pokemon> pokemonList = new ArrayList<>();
            try {
                pokemonList.addAll(PokemonReader.loadPokemonnFromAsset(MainActivity.this));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pokemonList;
        }


        @Override
        protected void onPostExecute(ArrayList<Pokemon> pokemonsList) {
            super.onPostExecute(pokemonsList);
            pokemons.clear();
            pokemons.addAll(pokemonsList);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }
}
