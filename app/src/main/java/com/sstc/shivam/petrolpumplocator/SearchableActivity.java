package com.sstc.shivam.petrolpumplocator;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.sstc.shivam.petrolpumplocator.offline.database.Contract;
import com.sstc.shivam.petrolpumplocator.offline.database.GetDataFromSQLite;

public class SearchableActivity extends ListActivity {

    boolean isOffline;
    public static String OFFLINE="offline";
Cursor mCursor;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_searchable);

            // Get the intent, verify the action and get the query
            Intent intent = getIntent();

            String query="vcbjkvhjbvcjbhcvbjcvhbvcjbhcbkcjhbc";

            Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
            if (appData != null) {
                 isOffline = appData.getBoolean(OFFLINE);
            }
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                 query = intent.getStringExtra(SearchManager.QUERY);
                doSearch(query);
                Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getBaseContext(),""+isOffline,Toast.LENGTH_SHORT).show();


            mCursor = new GetDataFromSQLite(this).searchInSqlliteDatabase(query);

            Toast.makeText(this,mCursor.getCount()+"",Toast.LENGTH_SHORT).show();

            ListAdapter adapter = new SimpleCursorAdapter(
                    this, // Context.
                    R.layout.row_search,  // Specify the row template to use (here, two columns bound to the two retrieved cursor
                    mCursor,                                              // Pass in the cursor to bind to.
            new String[] {Contract.PetrolPumpDetail.PNAME, Contract.PetrolPumpDetail.ADDRESS},           // Array of cursor columns to bind to.
                    new int[] {R.id.pname, R.id.address});  // Parallel array of which template objects to bind to those columns.

            // Bind to our new adapter.
            setListAdapter(adapter);
        }


    private void doSearch(String query){
        Bundle data = new Bundle();
        data.putString("query", query);

    }

}