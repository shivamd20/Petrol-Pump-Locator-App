package com.sstc.shivam.petrolpumplocator.startScreen;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sstc.shivam.petrolpumplocator.GPSTracker;
import com.sstc.shivam.petrolpumplocator.MainActivity;
import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;
import com.sstc.shivam.petrolpumplocator.startScreen.database.GetAllLocation;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListLocationFragFragment extends Fragment {

    // TODO: Customize parameter argument names

    private static final String ARG_COLUMN_COUNT = "column-count";
    public static MyListLocationFragRecyclerViewAdapter listLocationFragRecyclerViewAdapter;
    GPSTracker gps;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    // TODO: Customize parameter initialization
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public ListLocationFragFragment() {
    }

    public static ListLocationFragFragment newInstance(int columnCount,Location l) {

        ListLocationFragFragment fragment = new ListLocationFragFragment();
        fragment.location=l;
        Bundle args = new Bundle();
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

        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.linearLayoutMAINFRAG) ;

        linearLayout.removeView(view.findViewById(R.id.offlineWork));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        // Set the adapter
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if (mListener == null) {
                Toast.makeText(getActivity(), "mlisner is null", Toast.LENGTH_SHORT).show();
            }


        if (getActivity() instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) getActivity();
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnListFragmentInteractionListener");
            }


        listLocationFragRecyclerViewAdapter =
                    new MyListLocationFragRecyclerViewAdapter(GetAllLocation.ITEMS, mListener);

            recyclerView.setAdapter(listLocationFragRecyclerViewAdapter);



        recyclerView.setItemAnimator(new DefaultItemAnimator());

        showList(location);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Toast.makeText(getActivity(), "attach method called" + context.toString(), Toast.LENGTH_SHORT).show();

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    Location location;


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void showList(Location location) {
        GetAllLocation gAL = new GetAllLocation();
        gAL.setlistLocationFrag(this);
        gAL.execute(location, 0, 15);
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

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PetrolPumpItem item);
    }

}
