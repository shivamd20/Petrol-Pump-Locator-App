package com.sstc.shivam.petrolpumplocator.offline;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sstc.shivam.petrolpumplocator.GPSTracker;
import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.offline.database.GetAllLocationAsyncTask;
import com.sstc.shivam.petrolpumplocator.offline.database.GetDataFromSQLite;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentOfflineInteractionListener}
 * interface.
 */
public class ListLocationFragFragmentOffline extends Fragment {

    // TODO: Customize parameter argument names

    private static final String ARG_COLUMN_COUNT = "column-count";
    public static MyListLocationFragRecyclerViewOfflineAdapter listLocationFragRecyclerOfflineViewAdapter;
  GPSTracker gps;
    ProgressDialog progressDialog;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    // TODO: Customize parameter initialization
    private OnListFragmentOfflineInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */


    public ListLocationFragFragmentOffline() {
    }

    public static ListLocationFragFragmentOffline newInstance(int columnCount,Location l) {
        ListLocationFragFragmentOffline fragment = new ListLocationFragFragmentOffline();
        Bundle args = new Bundle();
        fragment.location=l;
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listlocationfrag_list, container, false);

        FrameLayout frameLayout=(FrameLayout)view.findViewById(R.id.frame_layout_frag);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }



        if (getActivity() instanceof OnListFragmentOfflineInteractionListener) {
            mListener = (OnListFragmentOfflineInteractionListener) getActivity();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }


        listLocationFragRecyclerOfflineViewAdapter =
                new MyListLocationFragRecyclerViewOfflineAdapter(GetDataFromSQLite.ITEMS, mListener); //TODO

        recyclerView.setAdapter(listLocationFragRecyclerOfflineViewAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        showList(location);
        return view;
    }

    Location location;

   public void  getLocationFromGps()
    {
        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            location=gps.getLocation();
            showList(location);

        } else {

            gps.showSettingsAlert();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void showList(Location location) {

        GetDataFromSQLite dataFromSQLite = new GetDataFromSQLite(this.getActivity());
        Cursor cur = dataFromSQLite.getLocations(location,0,200);

        GetAllLocationAsyncTask getAllLocationAsyncTask = new GetAllLocationAsyncTask(this.getActivity());

        getAllLocationAsyncTask.execute(location, 0, 200);

        try {
            getAllLocationAsyncTask.get();
            listLocationFragRecyclerOfflineViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("showlist", e.toString());
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("loading");
        progressDialog.show();
        progressDialog.dismiss();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnListFragmentOfflineInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentOfflineInteraction(PetrolPumpItem item);
    }

}
