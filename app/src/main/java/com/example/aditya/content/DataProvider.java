package com.example.aditya.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

/**
 * Created by aditya on 24/12/14.
 */
public class DataProvider extends ContentProvider {

    // Indicates that the incoming query is for a employee name URL
    public static final int EMPLOYEE_NAME_QUERY = 1;

    // Indicates an invalid content URI
    public static final int INVALID_URI = -1;

    // Constants for building SQLite tables during initialization
    private static final String TEXT_TYPE = "TEXT";
    private static final String PRIMARY_KEY_TYPE = "INTEGER PRIMARY KEY";
    private static final String INTEGER_TYPE = "INTEGER";


    // Defines an SQLite statement that builds the Employee table
    private static final String CREATE_EMPLOYEE_TABLE_SQL = "CREATE TABLE" + " " +
            DataContract.EMPLOYEE_TABLE_NAME + " " +
            "(" + " " +
            DataContract.ROW_ID + " " + PRIMARY_KEY_TYPE + " ," +
            DataContract.EMPLOYEE_ID + " " + INTEGER_TYPE + " ," +
            DataContract.EMPLOYEE_NAME_COLUMN + " " + TEXT_TYPE  +
            ")";
    // Identifies log statements issued by this component
    public static final String TAG = DataProvider.class.getSimpleName();

    // Defines an helper object for the backing database
    private SQLiteOpenHelper mHelper;

    // Defines a helper object that matches content URIs to table-specific parameters
    private static final UriMatcher sUriMatcher;

    // Stores the MIME types served by this provider
    private static final SparseArray<String> sMimeTypes;


    static {

        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(0);

        /*
         * Sets up an array that maps content URIs to MIME types, via a mapping between the
         * URIs and an integer code. These are custom MIME types that apply to tables and rows
         * in this particular provider.
         */
        sMimeTypes = new SparseArray<String>();

        // Adds a URI "match" entry that maps employee name to a numeric code
        sUriMatcher.addURI(
                DataContract.AUTHORITY,
                DataContract.EMPLOYEE_TABLE_NAME,
                EMPLOYEE_NAME_QUERY);

        // Specifies a custom MIME type for the Employee URL table
        sMimeTypes.put(
                EMPLOYEE_NAME_QUERY,
                "vnd.android.cursor.dir/vnd." +
                        DataContract.AUTHORITY + "." +
                        DataContract.EMPLOYEE_TABLE_NAME);


    }

    // Closes the SQLite database helper class, to avoid memory leaks
    public void close() {
        mHelper.close();
    }


    /**
     * Defines a helper class that opens the SQLite database for this provider when a request is
     * received. If the database doesn't yet exist, the helper creates it.
     */
    private class DataProviderHelper extends SQLiteOpenHelper {
        /**
         * Instantiates a new SQLite database using the supplied database name and version
         *
         * @param context The current context
         */
        DataProviderHelper(Context context) {
            super(context,
                    DataContract.DATABASE_NAME,
                    null,
                    DataContract.DATABASE_VERSION);
        }


        /**
         * Executes the queries to drop all of the tables from the database.
         *
         * @param db A handle to the provider's backing database.
         */
        private void dropTables(SQLiteDatabase db) {

            // If the table doesn't exist, don't throw an error
            db.execSQL("DROP TABLE IF EXISTS " + DataContract.EMPLOYEE_TABLE_NAME);

        }

        /**
         * Does setup of the database. The system automatically invokes this method when
         * SQLiteDatabase.getWriteableDatabase() or SQLiteDatabase.getReadableDatabase() are
         * invoked and no db instance is available.
         *
         * @param db the database instance in which to create the tables.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG,"On Create of SQLite");
            // Creates the tables in the backing database for this provider
            db.execSQL(CREATE_EMPLOYEE_TABLE_SQL);


        }

        /**
         * Handles upgrading the database from a previous version. Drops the old tables and creates
         * new ones.
         *
         * @param db The database to upgrade
         * @param version1 The old database version
         * @param version2 The new database version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
            Log.w(TAG,
                    "Upgrading database from version " + version1 + " to "
                            + version2 + ", which will destroy all the existing data");

            // Drops all the existing tables in the database
            dropTables(db);

            // Invokes the onCreate callback to build new tables
            onCreate(db);
        }
        /**
         * Handles downgrading the database from a new to a previous version. Drops the old tables
         * and creates new ones.
         * @param db The database object to downgrade
         * @param version1 The old database version
         * @param version2 The new database version
         */
        @Override
        public void onDowngrade(SQLiteDatabase db, int version1, int version2) {
            Log.w(DataProviderHelper.class.getName(),
                    "Downgrading database from version " + version1 + " to "
                            + version2 + ", which will destroy all the existing data");

            // Drops all the existing tables in the database
            dropTables(db);

            // Invokes the onCreate callback to build new tables
            onCreate(db);

        }


    }





    /**
     * Initializes the content provider. Notice that this method simply creates a
     * the SQLiteOpenHelper instance and returns. You should do most of the initialization of a
     * content provider in its static initialization block or in SQLiteDatabase.onCreate().
     */
    @Override
    public boolean onCreate() {
        Log.d(TAG,"On Create od DataProvider");
        // Creates a new database helper object
        mHelper = new DataProviderHelper(getContext());

        return true;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }
    /**
     * Returns the mimeType associated with the Uri (query).
     * @see android.content.ContentProvider#getType(Uri)
     * @param uri the content URI to be checked
     * @return the corresponding MIMEtype
     */
    @Override
    public String getType(Uri uri) {

        return sMimeTypes.get(sUriMatcher.match(uri));
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    /**
     *
     * Insert a single row into a table
     * @see android.content.ContentProvider#insert(Uri, ContentValues)
     * @param uri the content URI of the table
     * @param values a {@link android.content.ContentValues} object containing the row to insert
     * @return the content URI of the new row
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Decode the URI to choose which action to take
        switch (sUriMatcher.match(uri)) {

            // For the modification date table
            case EMPLOYEE_NAME_QUERY:

                // Creates a writeable database or gets one from cache
                SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();

                // Inserts the row into the table and returns the new row's _id value
                long id = localSQLiteDatabase.insert(
                        DataContract.EMPLOYEE_TABLE_NAME,
                        null,
                      values
                );

                // If the insert succeeded, notify a change and return the new row's content URI.
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {

                    throw new SQLiteException("Insert error:" + uri);
                }

        }

        return null;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // Decodes the content URI and maps it to a code
        switch (sUriMatcher.match(uri)) {

            // If the query is for a Employee
            case EMPLOYEE_NAME_QUERY:
                // Does the query against a read-only version of the database
                Cursor returnCursor = db.query(
                        DataContract.EMPLOYEE_TABLE_NAME,
                        projection,
                        null, null, null, null, null);

                // Sets the ContentResolver to watch this content URI for data changes
                returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return returnCursor;


            case INVALID_URI:

                throw new IllegalArgumentException("Query -- Invalid URI:" + uri);
        }

        return null;
    }
}
