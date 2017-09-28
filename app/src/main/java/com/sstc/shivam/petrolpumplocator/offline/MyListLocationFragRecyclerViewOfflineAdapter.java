package com.sstc.shivam.petrolpumplocator.offline;

import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;
import com.sstc.shivam.petrolpumplocator.startScreen.ListLocationFragFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PetrolPumpItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListLocationFragRecyclerViewOfflineAdapter extends RecyclerView.Adapter{

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final List<PetrolPumpItem> mValues;
    private final ListLocationFragFragmentOffline.OnListFragmentOfflineInteractionListener mListener;
    Location location;


    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    @Override
    public int getItemViewType(int position) {
        return mValues.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public  void setLocation(Location mlocation){
        location=mlocation;
    }

    public MyListLocationFragRecyclerViewOfflineAdapter(List<PetrolPumpItem> items, final ListLocationFragFragmentOffline.OnListFragmentOfflineInteractionListener listener,
                                                        RecyclerView recyclerView) {
        mValues = items;
        mListener = listener;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                loading = true;
                            }
                        }
                    });
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.fragment_listlocationfrag, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if ((holder instanceof ViewHolder)) {

           ViewHolder listViewHolder =(ViewHolder) holder;

        final PetrolPumpItem petrolPumpItem = mValues.get(position);
        listViewHolder.mContentView.setText(mValues.get(position).pname);
        listViewHolder.mDistanceView.setText(mValues.get(position).distance);
        listViewHolder.mTimeView.setText(mValues.get(position).duration);
        listViewHolder.mAddressView.setText(mValues.get(position).address);

        listViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.

                    Log.i("holder nfo", petrolPumpItem.pname);
                    mListener.onListFragmentOfflineInteraction(petrolPumpItem);
                } else {
                    //  Toast.makeText(null,"listner clickd",Toast.LENGTH_SHORT).show();
                }
            }
        });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

//        if(petrolPumpItem!=null)
       /// setAllButtons(holder, position, petrolPumpItem);

    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder){
        super.onViewAttachedToWindow(holder);

    }

    void setAllButtons( ViewHolder holder,   int position,  PetrolPumpItem item)
    {
        if(item!=null) {
            if (item.rest_room == null) {
                holder.restroom.setText("AVAILABLE");
            }


            if (item.water == null) {
                holder.water.setText("AVAILABLE");
            }

            if (item.toilets == null) {

                holder.toilet.setText("AVAILABLE");
            }

            if (true) {
                //  holder.shop.setEnabled(false);

                // holder.linearLayout.removeView( holder.shop);
            }
            if (item.card_accepted == null) {

                holder.card_pay.setText("AVAILABLE");
            }

            if (item.air == null) {

                holder.air.setText("AVAILABLE");
            }

            if (item.atm == null || (item.atm.compareTo("0") == 0)) {

                holder.atm.setText("AVAILABLE");
            }
            if (item.first_aid == null) {

                holder.first_aid.setText("AVAILABLE");
            }
            if (item.petrol == null) {

                holder.petrol.setText("AVAILABLE");
            }
            if (item.disel == null) {

                holder.disel.setText("AVAILABLE");
            }
        }
        else
        {
            Log.e("","item is null");
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

        LinearLayout linearLayout;
        
        TextView restroom,water,toilet,shop,card_pay,air,atm,first_aid,petrol,disel;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);

            mTimeView = (TextView) view.findViewById(R.id.time);

            mDistanceView = (TextView) view.findViewById(R.id.distance);

            mAddressView = (TextView) view.findViewById(R.id.address);
            
            getAllFacButton(view);

        }

        public void addItem(PetrolPumpItem item) {
            mValues.add(item);
            notifyDataSetChanged();
        }

        void getAllFacButton(View view)
        {
            /*
            restroom=(TextView)view.findViewById(R.id.rest_room);
            water=(TextView)view.findViewById(R.id.water);
            toilet=(TextView)view.findViewById(R.id.toilet);
            shop=(TextView)view.findViewById(R.id.shop);
            card_pay=(TextView)view.findViewById(R.id.card_pay);
            air=(TextView)view.findViewById(R.id.air);
            atm=(TextView)view.findViewById(R.id.atm);
            first_aid=(TextView)view.findViewById(R.id.first_aid);
            petrol=(TextView)view.findViewById(R.id.petrol);
            disel=(TextView)view.findViewById(R.id.diesel);
            linearLayout=(LinearLayout) view.findViewById(R.id.linearLayoutFront);
            */
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
