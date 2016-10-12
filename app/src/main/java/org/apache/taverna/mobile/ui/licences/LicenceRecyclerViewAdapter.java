package org.apache.taverna.mobile.ui.licences;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.ui.licences.LicenceFragment.OnListFragmentInteractionListener;
import org.apache.taverna.mobile.ui.licences.licence.LicenceContent.LicenceItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LicenceItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class LicenceRecyclerViewAdapter extends RecyclerView.Adapter<LicenceRecyclerViewAdapter.ViewHolder> {

    private final List<LicenceItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public LicenceRecyclerViewAdapter(List<LicenceItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_licence, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mLicenceNameView.setText(mValues.get(position).getName());
        holder.mLicenceVersionView.setText(mValues.get(position).getVersion());
        holder.mLicenceLicenceView.setText("Unknown");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mLicenceNameView;
        public final TextView mLicenceVersionView;
        public final TextView mLicenceLicenceView;
        public LicenceItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLicenceNameView = (TextView) view.findViewById(R.id.licence_name);
            mLicenceVersionView = (TextView) view.findViewById(R.id.licence_version);
            mLicenceLicenceView = (TextView) view.findViewById(R.id.licence_licence);
        }

        @Override
        public String toString() {
            return super.toString() + " " + mLicenceNameView.getText() + " " + mLicenceVersionView.getText() + " " + mLicenceLicenceView.getText();
        }
    }
}
