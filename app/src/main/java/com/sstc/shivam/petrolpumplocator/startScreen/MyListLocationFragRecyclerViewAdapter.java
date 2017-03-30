package com.sstc.shivam.petrolpumplocator.startScreen;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.offline.MyListLocationFragRecyclerViewOfflineAdapter;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;
import com.sstc.shivam.petrolpumplocator.startScreen.ListLocationFragFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListLocationFragRecyclerViewAdapter extends RecyclerView.Adapter<MyListLocationFragRecyclerViewAdapter.ViewHolder> {

    private final List<PetrolPumpItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyListLocationFragRecyclerViewAdapter(List<PetrolPumpItem> items, OnListFragmentInteractionListener listener) {
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
    public void onBindViewHolder(final MyListLocationFragRecyclerViewAdapter.ViewHolder holder, final int position) {
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

                    Log.i("holder nfo", holder.mItem.pname);
                    mListener.onListFragmentInteraction(holder.mItem);
                } else {
                    //  Toast.makeText(null,"listner clickd",Toast.LENGTH_SHORT).show();
                }
            }
        });

        setAllButtons(holder, position, mValues.get(position));

    }

    void setAllButtons(final ViewHolder holder, final  int position, final PetrolPumpItem item)
    {
        if(item.rest_room==null)
        {
            holder.restroom.setEnabled(false);
            holder.linearLayout.removeView( holder.restroom);
        }
        if(item.water==null)
        {
            holder.water.setEnabled(false);
            holder.linearLayout.removeView( holder.water);
        }
        if(item.toilets==null)
        {
            holder.toilet.setEnabled(false);

            holder.linearLayout.removeView( holder.toilet);
        }
        if(true)
        {
            holder.shop.setEnabled(false);

            holder.linearLayout.removeView( holder.shop);
        }
        if(item.card_accepted==null)
        {
            holder.card_pay.setEnabled(false);

            holder.linearLayout.removeView( holder.card_pay);
        }

        if(item.air==null)
        {
            holder.air.setEnabled(false);

            holder.linearLayout.removeView( holder.air);
        }
        if(item.atm==null)
        {
            holder.atm.setEnabled(false);

            holder.linearLayout.removeView( holder.atm);
        }
        if(item.first_aid==null)
        {
            holder.first_aid.setEnabled(false);

            holder.linearLayout.removeView( holder.first_aid);
        }
        if(item.petrol==null)
        {
            holder.petrol.setEnabled(false);

            holder.linearLayout.removeView( holder.petrol);
        }
        if(item.disel==null)
        {
            holder.disel.setEnabled(false);

            holder.linearLayout.removeView( holder.disel);
        }
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

        LinearLayout linearLayout;

        ImageView restroom,water,toilet,shop,card_pay,air,atm,first_aid,petrol,disel;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);

            mTimeView = (TextView) view.findViewById(R.id.time);

            mDistanceView = (TextView) view.findViewById(R.id.distance);

            mAddressView = (TextView) view.findViewById(R.id.address);

            getAllFacButton(view);

        }
        void getAllFacButton(View view)
        {
            restroom=(ImageView)view.findViewById(R.id.rest_room);
            water=(ImageView)view.findViewById(R.id.water);
            toilet=(ImageView)view.findViewById(R.id.toilet);
            shop=(ImageView)view.findViewById(R.id.shop);
            card_pay=(ImageView)view.findViewById(R.id.card_pay);
            air=(ImageView)view.findViewById(R.id.air);
            atm=(ImageView)view.findViewById(R.id.atm);
            first_aid=(ImageView)view.findViewById(R.id.first_aid);
            petrol=(ImageView)view.findViewById(R.id.petrol);
            disel=(ImageView)view.findViewById(R.id.diesel);
            linearLayout=(LinearLayout) view.findViewById(R.id.linearLayoutFront);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

    }
}
