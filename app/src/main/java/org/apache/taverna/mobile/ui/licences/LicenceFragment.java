package org.apache.taverna.mobile.ui.licences;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.model.licence.LicenceContent;
import org.apache.taverna.mobile.ui.adapter.LicenceRecyclerViewAdapter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A fragment representing a list of Licence Items.
 */
public class LicenceFragment extends Fragment {


    private List<LicenceContent> itemList;

    private Gson gson;

    public static LicenceFragment newInstance() {

        return new LicenceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_licence_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (view instanceof RecyclerView) {

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            Type type = new TypeToken<List<org.apache.taverna.mobile.data.model.licence
                    .LicenceContent>>() {
            }.getType();
            itemList = gson.fromJson(getString(R.string.licence_data), type);
            recyclerView.setAdapter(new LicenceRecyclerViewAdapter(itemList));
        }
    }


}
