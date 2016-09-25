package org.apache.taverna.mobile.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.taverna.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DownloadingFragment extends Fragment {

    private static final String ARGS_MESSAGE = "args_message";

    String message;

    @BindView(R.id.tvMessage)
    TextView tvMessage;

    public static DownloadingFragment newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE, message);
        DownloadingFragment fragment = new DownloadingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = getArguments().getString(ARGS_MESSAGE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_downloading, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvMessage.setText(message);
    }

}
