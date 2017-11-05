package com.example.shubhraj.assetsdata.activities;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shubhraj.assetsdata.R;
import com.example.shubhraj.assetsdata.adapters.PokemonCursorAdapter;
import com.example.shubhraj.assetsdata.data.PokemonContract;

public class PokemonListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int POKEMON_LOADER = 0;
    private static final String TAG = "PokemonListActivity";
    private PokemonCursorAdapter mAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PokemonListActivity.this, PokemonEditorActivity.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mAdapter = new PokemonCursorAdapter(this, null);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent editIntent = new Intent(PokemonListActivity.this, PokemonEditorActivity.class);
                Uri contentUri = ContentUris.withAppendedId(PokemonContract.PokemonEntry.CONTENT_URI, id);
                editIntent.setData(contentUri);
                startActivity(editIntent);
            }
        });

        getLoaderManager().initLoader(POKEMON_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_all)
        {
            deleteAllPokemons();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPokemons()
    {
        int rowsDeleted = getContentResolver().delete(PokemonContract.PokemonEntry.CONTENT_URI,
                null, null);
        Log.d(TAG, "Rows deleted: "+rowsDeleted);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections = {PokemonContract.PokemonEntry._ID,
                                PokemonContract.PokemonEntry.POKEMON_NAME,
                                PokemonContract.PokemonEntry.POKEMON_TYPE};
        return new CursorLoader(this, PokemonContract.PokemonEntry.CONTENT_URI,projections,null,
                null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
