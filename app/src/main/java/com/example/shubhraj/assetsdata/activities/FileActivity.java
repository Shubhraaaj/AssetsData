package com.example.shubhraj.assetsdata.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shubhraj.assetsdata.R;
import com.example.shubhraj.assetsdata.adapters.RecyclerViewAdapter;
import com.example.shubhraj.assetsdata.model.Pokemon;
import com.example.shubhraj.assetsdata.readers.PokemonReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Shubhraj on 23-10-2017.
 */
public class FileActivity extends AppCompatActivity
{
    private static final String FILE_NAME = "pokemons.txt";
    private Button pokemonButton;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<Pokemon> pokemons;
    private ProgressBar progressBar;
    private ArrayList<Pokemon> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemons = new ArrayList<>();

        pokemonList = new ArrayList<>();
        pokemonList.add(new Pokemon("Ashu","Fire"));
        pokemonList.add(new Pokemon("Shubhraj","Water"));
        pokemonList.add(new Pokemon("SP","Rock"));

        pokemonButton = (Button) findViewById(R.id.load_data_button);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(pokemons);
        recyclerView.setAdapter(adapter);
        new WritePokemonDataToFileOnBackground().execute();
        pokemonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReadPokemonDataFromFileOnBackground().execute();
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
                pokemonList.addAll(PokemonReader.loadPokemonnFromAsset(FileActivity.this));
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

    private class ReadPokemonDataFromFileOnBackground extends AsyncTask<Void, Integer, ArrayList<Pokemon>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Pokemon> doInBackground(Void... voids)
        {
            ArrayList<Pokemon> filePokemon = new ArrayList<>();
            try {
                FileInputStream fileInputStream = openFileInput(FILE_NAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, ",");
                    if (strings.length < 2)
                        continue;
                    filePokemon.add(new Pokemon(strings[0].trim(), strings[1].trim()));
                }
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return filePokemon;
        }

        @Override
        protected void onPostExecute(ArrayList<Pokemon> filePokemons) {
            super.onPostExecute(filePokemons);
            pokemons.clear();
            pokemons.addAll(filePokemons);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }

    private class WritePokemonDataToFileOnBackground extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
                FileOutputStream fileout=openFileOutput(FILE_NAME, MODE_PRIVATE);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                for (Pokemon pokemon:pokemonList)
                {
                    outputWriter.write(pokemon.getName() + "," + pokemon.getType() + "\n");
                }
                outputWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
