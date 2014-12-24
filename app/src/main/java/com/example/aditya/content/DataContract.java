package com.example.aditya.content;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aditya on 24/12/14.
 */
public class DataContract {

    private DataContract() { }

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "com.example.aditya.content";

    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    /**
     *  The MIME type for a content URI that would return multiple rows
     *  <P>Type: TEXT</P>
     */
    public static final String MIME_TYPE_ROWS =
            "vnd.android.cursor.dir/vnd.com.example.aditya.content";

    /**
     * The MIME type for a content URI that would return a single row
     *  <P>Type: TEXT</P>
     *
     */
    public static final String MIME_TYPE_SINGLE_ROW =
            "vnd.android.cursor.item/vnd.com.example.aditya.content";
    /**
     * Employee key column name
     */
    public static final String ROW_ID = BaseColumns._ID;

    /**
     * Employee table name
     */
    public static final String EMPLOYEE_TABLE_NAME = "employee_data";

    /**
     * Picture table content URI
     */
    public static final Uri EMPLOYEE_TABLE_CONTENT_URI =
            Uri.withAppendedPath(CONTENT_URI, EMPLOYEE_TABLE_NAME);


    public static final String EMPLOYEE_NAME_COLUMN = "employee_name";
    public static final String EMPLOYEE_ID = "employee_id";

    // The content provider database name
    public static final String DATABASE_NAME = "EmployeeDB";

    // The starting version of the database
    public static final int DATABASE_VERSION = 1;

}
