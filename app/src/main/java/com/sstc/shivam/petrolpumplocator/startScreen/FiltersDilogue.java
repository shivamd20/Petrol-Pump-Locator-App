package com.sstc.shivam.petrolpumplocator.startScreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.sstc.shivam.petrolpumplocator.offline.database.GetDataFromSQLite;

import java.util.ArrayList;


public class FiltersDilogue extends DialogFragment {
    ArrayList mSelectedItems = new ArrayList();
    onFilterSelectionListner mLisner;

    ArrayList<String> listStates;

    public static FiltersDilogue getInstance(ArrayList<String> listStates, onFilterSelectionListner ml)
    {
        FiltersDilogue f=new FiltersDilogue();
        f.listStates=listStates;
        f.mLisner=ml;
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ArrayList<String> tempdb = new GetDataFromSQLite(getActivity())
                .getAllStateList();

        boolean[] checkedItems = new boolean[listStates.size()];

        new GetDataFromSQLite(getActivity()).deleteOfflineMap();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("select states to download maps")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(arryListtoArray(listStates), checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons

                .setPositiveButton("download states data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        ArrayList<String>  list=new ArrayList<String>();

                        GetDataFromSQLite dataFromSQLite = new GetDataFromSQLite(FiltersDilogue.this.getActivity());
                        dataFromSQLite.deleteOfflineMap();

                        for(Object X:mSelectedItems)
                        {
                            Integer a=(Integer) X;
                            list.add(listStates.get(a.intValue()));
                        }
                            mLisner.onFilterSelection(list);

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    String[] arryListtoArray(ArrayList<String> arrayList)
    {

        String[] tenp=new String[arrayList.size()];

        int i=0;
        for(String X:arrayList)
        {
            tenp[i]=X;
         i++;
        }

        return tenp;
    }

    public interface onFilterSelectionListner {

        void onFilterSelection(ArrayList<String> args);
    }}
