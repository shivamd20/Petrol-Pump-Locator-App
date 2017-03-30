package com.sstc.shivam.petrolpumplocator.startScreen;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.detailsScreen.PetrolPumpDetailsActivity;
import com.sstc.shivam.petrolpumplocator.offline.database.Contract;
import com.sstc.shivam.petrolpumplocator.offline.database.GetAllLocationAsyncTask;
import com.sstc.shivam.petrolpumplocator.offline.database.GetDataFromSQLite;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartScreenFrag.OnStartFragIntersectionLisner} interface
 * to handle interaction events.
 * Use the {@link StartScreenFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartScreenFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private SimpleCursorAdapter mAdapter;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };

    // You must implements your logic to get data using OrmLite


    private void populateAdapter(String query) {


        mAdapter.changeCursor( new GetDataFromSQLite(this.getActivity()).searchInSqlliteDatabase(query));

        /*


        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);

        */
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button locationBtn;

    ImageButton mapBtn;

    SearchView searchBarBtn;

    private OnStartFragIntersectionLisner mListener;

    public StartScreenFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StartScreenFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static StartScreenFrag newInstance(String param1, OnStartFragIntersectionLisner mListener) {
        StartScreenFrag fragment = new StartScreenFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.mListener=mListener;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        final String[] from = new String[] {Contract.PetrolPumpDetail.PNAME,Contract.PetrolPumpDetail.ADDRESS,Contract.PetrolPumpDetail.PID};
        final int[] to = new int[] {android.R.id.text1,android.R.id.text2};
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view= inflater.inflate(R.layout.fragment_start_screen, container, false);

        locationBtn=(Button)view.findViewById(R.id.locationBtn);

        mapBtn=(ImageButton)view.findViewById(R.id.mapBtn);


        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchBarBtn=(SearchView) view.findViewById(R.id.searchBtn);
        searchBarBtn.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchBarBtn.setIconifiedByDefault(false);
        searchBarBtn.setSubmitButtonEnabled(true);
        searchBarBtn.setQueryRefinementEnabled(true);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocationButtonPressed();
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMapButtonPressed();
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocationButtonPressed();
            }
        });

        searchBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mListener.onLocationButtonPressed();
            }
        });

        searchBarBtn.setSuggestionsAdapter(mAdapter);
        searchBarBtn.setIconifiedByDefault(false);
        // Getting selected (clicked) item suggestion
        searchBarBtn.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
               //TODO
                Toast.makeText(StartScreenFrag.this.getActivity(),"Sugclick"+
                      mAdapter.getCursor().getString(0)  ,Toast.LENGTH_SHORT).show();

              PetrolPumpItem pitem= GetAllLocationAsyncTask.rowToObject(mAdapter.getCursor());

                PetrolPumpDetailsActivity bottomSheetDialogFragment = new PetrolPumpDetailsActivity();

                Intent i2 = new Intent(StartScreenFrag.this.getActivity(), PetrolPumpDetailsActivity.class);

                //TODO

                i2.putExtra(PetrolPumpItem.EXTRASTRING,pitem);
                startActivity(i2);
                StartScreenFrag.this.getActivity().overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right );


                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                Toast.makeText(StartScreenFrag.this.getActivity(),"Sugselected",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        searchBarBtn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });

        //searchBarBtn.setSuggestionsAdapter();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStartFragIntersectionLisner {

        public void onSearchButtonPressed();

        public void onMapButtonPressed();

        public void onLocationButtonPressed();
    }


}
