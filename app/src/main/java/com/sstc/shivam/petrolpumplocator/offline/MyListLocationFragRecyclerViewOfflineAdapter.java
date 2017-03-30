package com.sstc.shivam.petrolpumplocator.offline;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.offline.database.GetDataFromSQLite;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;
import com.sstc.shivam.petrolpumplocator.startScreen.ListLocationFragFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PetrolPumpItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListLocationFragRecyclerViewOfflineAdapter extends RecyclerView.Adapter<MyListLocationFragRecyclerViewOfflineAdapter.ViewHolder> {

    private final List<PetrolPumpItem> mValues;
    private final ListLocationFragFragmentOffline.OnListFragmentOfflineInteractionListener mListener;

    public MyListLocationFragRecyclerViewOfflineAdapter(List<PetrolPumpItem> items, ListLocationFragFragmentOffline.OnListFragmentOfflineInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void removeitem(int i) {
        mValues.remove(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_listlocationfrag, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).pname);
        holder.mDistanceView.setText(mValues.get(position).distance);
        holder.mTimeView.setText(mValues.get(position).duration);
        holder.mAddressView.setText(mValues.get(position).address);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.

                    Log.i("holder nfo",holder.mItem.pname);
                    mListener.onListFragmentOfflineInteraction(holder.mItem);
                } else {
                    //  Toast.makeText(null,"listner clickd",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mContentView;
        public final TextView mTimeView;
        public final TextView mDistanceView;
        public final TextView mAddressView;
        public PetrolPumpItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);

            mTimeView = (TextView) view.findViewById(R.id.time);

            mDistanceView = (TextView) view.findViewById(R.id.distance);

            mAddressView = (TextView) view.findViewById(R.id.address);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

    }
}
