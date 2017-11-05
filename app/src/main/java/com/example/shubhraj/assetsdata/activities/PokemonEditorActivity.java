package com.example.shubhraj.assetsdata.activities;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shubhraj.assetsdata.R;
import com.example.shubhraj.assetsdata.data.PokemonContract;

public class PokemonEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int EXISTING_LOADER = 0;
    private Uri mCurrentUri;
    private EditText nameEdit, typeEdit;
    private ImageView typeImage;
    private Button actionButton;

    private boolean mDataHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDataHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_editor);
        actionButton = (Button) findViewById(R.id.editor_button);
        mCurrentUri = getIntent().getData();
        if(mCurrentUri==null)
        {
            setTitle(getString(R.string.add_a_pokemon_title));
            actionButton.setText(R.string.add_pokemon);
            invalidateOptionsMenu();
        }
        else
        {
            setTitle(getString(R.string.edit_pokemon_title));
            actionButton.setText(R.string.update_pokemon);
            getLoaderManager().initLoader(EXISTING_LOADER, null, this);
        }
        nameEdit = (EditText) findViewById(R.id.pokemon_name);
        typeEdit = (EditText) findViewById(R.id.pokemon_type);
        typeImage = (ImageView) findViewById(R.id.pokemon_type_image);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePokemon();
                finish();
            }
        });

        nameEdit.setOnTouchListener(mTouchListener);
        typeEdit.setOnTouchListener(mTouchListener);
    }

    private void savePokemon()
    {
        String nameString = nameEdit.getText().toString().trim();
        String typeString = typeEdit.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(PokemonContract.PokemonEntry.POKEMON_NAME, nameString);
        values.put(PokemonContract.PokemonEntry.POKEMON_TYPE, typeString);

        if (mCurrentUri == null) {
            Uri newUri = getContentResolver().insert(PokemonContract.PokemonEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:
                if (!mDataHasChanged) {
                    NavUtils.navigateUpFromSameTask(PokemonEditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(PokemonEditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mDataHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePokemon();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePokemon() {
        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projections = new String[]{
                PokemonContract.PokemonEntry._ID,
                PokemonContract.PokemonEntry.POKEMON_NAME,
                PokemonContract.PokemonEntry.POKEMON_TYPE};
        return new CursorLoader(this, mCurrentUri, projections,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(PokemonContract.PokemonEntry.POKEMON_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PokemonContract.PokemonEntry.POKEMON_TYPE);

            String name = cursor.getString(nameColumnIndex);
            String breed = cursor.getString(breedColumnIndex);

            nameEdit.setText(name);
            typeEdit.setText(breed);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameEdit.setText("");
        typeEdit.setText("");
    }
}