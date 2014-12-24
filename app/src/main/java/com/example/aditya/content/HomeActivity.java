package com.example.aditya.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class HomeActivity extends ActionBarActivity  implements  LoaderManager.LoaderCallbacks<Cursor>{

    private ListView mListView;
    private static final int EMPLOYEE_LOADER = 0;
    private static final String[] mProjection =
            {
                    DataContract.ROW_ID,
                    DataContract.EMPLOYEE_NAME_COLUMN,
            };

    public String[] mFromColumns = {

            DataContract.EMPLOYEE_NAME_COLUMN
    };
    public int[] mToFields = {
            android.R.id.text1
    };

    SimpleCursorAdapter mSimpleCursorAdaper;
    EditText mEmployeeName;
    EditText mEmployeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();

        mSimpleCursorAdaper = new SimpleCursorAdapter(
                this,                // Current context
                android.R.layout.simple_list_item_1,  // Layout for a single row
                null,                // No Cursor yet
                mFromColumns,        // Cursor columns to use
                mToFields,           // Layout fields to use
                0                    // No flags
        );
        mListView.setAdapter(mSimpleCursorAdaper);
        getSupportLoaderManager().initLoader(EMPLOYEE_LOADER,null,this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.listView);
        mEmployeeId = (EditText)findViewById(R.id.employee_id);
        mEmployeeName = (EditText) findViewById(R.id.employee_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSimpleCursorAdaper.changeCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSimpleCursorAdaper.changeCursor(data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case EMPLOYEE_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        this,   // Parent activity context
                        DataContract.EMPLOYEE_TABLE_CONTENT_URI,        // Table to query
                        mProjection,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    public void insertIntoDb(View v){
        ContentValues employeeValues = new ContentValues();
        employeeValues.put(DataContract.EMPLOYEE_ID, mEmployeeId.getText().toString().trim());
        employeeValues.put(DataContract.EMPLOYEE_NAME_COLUMN, mEmployeeName.getText().toString().trim());
        getContentResolver().insert(DataContract.EMPLOYEE_TABLE_CONTENT_URI,employeeValues);
    }

}
