package com.sstc.shivam.petrolpumplocator.offline;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.offline.database.GetAllStateNamesFromServer;
import com.sstc.shivam.petrolpumplocator.offline.database.GetDataFromSQLite;
import com.sstc.shivam.petrolpumplocator.offline.database.OfflineLocationDownloadTask;
import com.sstc.shivam.petrolpumplocator.offline.database.PrefSingleton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfflineFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfflineFrag#newInstance} factory method to
 * create an instance of this fragment.
 */

public class OfflineFrag extends Fragment implements DeleteOfflineMapDilogue.FireMissilesDialogFragmentLisner,DownloadMapDilogue.onDownloadOffllineMapsLisner{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OfflineFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OfflineFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static OfflineFrag newInstance(String param1, String param2) {
        OfflineFrag fragment = new OfflineFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    }

    boolean checkForOffline() {
        return PrefSingleton.getInstance().getPreference().getBoolean(PrefSingleton.keyOffline, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


      final  View view = inflater.inflate(R.layout.fragment_offline, container, false);
        Button btn=(Button)view.findViewById(R.id.execute);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OfflineLocationDownloadTask offlineLocationDownloadTask=OfflineLocationDownloadTask.getInstance(OfflineFrag.this
                ,null);
                offlineLocationDownloadTask.execute();

            }
        });


        Button deleteSelectedbtn=(Button)view.findViewById(R.id.deleteSelectedBtn);

        deleteSelectedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteOfflineMapDilogue deleteOfflineMapDilogue = DeleteOfflineMapDilogue.getInstance(
                         new GetDataFromSQLite(OfflineFrag.this.getActivity())
                        .getAllStateList(),OfflineFrag.this);

                deleteOfflineMapDilogue.show(OfflineFrag.this.getChildFragmentManager(),"Delete Selected Masp");
            }
        });

        Button deletebtn=(Button)view.findViewById(R.id.deleteAllMaps);

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataFromSQLite dataFromSQLite=new GetDataFromSQLite(OfflineFrag.this.getActivity());
                dataFromSQLite.deleteOfflineMap();

                showAlert("All Stored Maps deleted");
            }
        });


        final Switch  switchh =(Switch)view.findViewById(R.id.offlineSwitch);


        if(checkForOffline())
        {
            switchh.setChecked(true);
        }

        switchh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (switchh.isChecked()) {

                    Snackbar.make(view, "  Offline Work ", Snackbar.LENGTH_SHORT).setDuration(200).show();

                    PrefSingleton.getInstance().writePreference(PrefSingleton.keyOffline, true);
                } else {

                    Snackbar.make(view, "  Offline Work Disabled", Snackbar.LENGTH_SHORT).setDuration(200).show();

                    PrefSingleton.getInstance().writePreference(PrefSingleton.keyOffline, false);

                }
            }});

        Button downloadMap=(Button)view.findViewById(R.id.downloadMap);

        downloadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                GetAllStateNamesFromServer gs=new GetAllStateNamesFromServer(OfflineFrag.this);
                gs.execute();
            }
        });



        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onFireMissilesDialogFragmentOkAction(ArrayList<String> args){
        new GetDataFromSQLite(this.getActivity()).deleteStateFromSQlite(args);
    }

    public void onDownloadOffllineMapsokAction(ArrayList<String> args){
        for(String X:args)
        {
            OfflineLocationDownloadTask offlineLocationDownloadTask
                    =OfflineLocationDownloadTask.getInstance  ( this,   X );
            offlineLocationDownloadTask.execute();

        }
    }

   public void showAlert(String msg)
    {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Messege")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {  }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //  void onFragmentInteraction(Uri uri);
    }

}



