package com.sstc.shivam.petrolpumplocator.offline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by shiva on 25-03-2017.
 */

public class DeleteOfflineMapDilogue extends DialogFragment {
    ArrayList mSelectedItems = new ArrayList();
    FireMissilesDialogFragmentLisner mLisner;

    ArrayList<String> listStates;

    public static DeleteOfflineMapDilogue getInstance(ArrayList<String> listStates, FireMissilesDialogFragmentLisner ml)
    {
        DeleteOfflineMapDilogue f=new DeleteOfflineMapDilogue();
        f.listStates=listStates;
        f.mLisner=ml;
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("select states")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(arryListtoArray(listStates), null,
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
                .setPositiveButton("delete selected state", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        ArrayList<String>  list=new ArrayList<String>();

                        for(Object X:mSelectedItems)
                        {
                            Integer a=(Integer) X;
                            list.add(listStates.get(a.intValue()));
                        }
                            mLisner.onFireMissilesDialogFragmentOkAction(list);

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

    public interface FireMissilesDialogFragmentLisner{

        public void onFireMissilesDialogFragmentOkAction(ArrayList<String> args);
    }}
