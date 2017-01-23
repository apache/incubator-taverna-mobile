package org.apache.taverna.mobile.ui.tavernaserver.inputs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.data.model.Input;
import org.apache.taverna.mobile.data.model.Inputs;
import org.apache.taverna.mobile.ui.workflowrun.WorkflowRunActivity;
import org.apache.taverna.mobile.utils.Constants;

import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TavernaServerInputsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TavernaServerInputsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TavernaServerInputsFragment extends ListFragment implements TavernaServerInputsMvpView, View.OnFocusChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private DataManager dataManager;

    private TavernaServerInputsPresenter tavernaServerInputsPresenter;

    private OnFragmentInteractionListener mListener;

    public TavernaServerInputsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TavernaServerInputsFragment.
     */
    public static TavernaServerInputsFragment newInstance() {
        Bundle args = new Bundle();

        TavernaServerInputsFragment fragment = new TavernaServerInputsFragment();
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
        dataManager = new DataManager(new PreferencesHelper(getContext()));
        tavernaServerInputsPresenter = new TavernaServerInputsPresenter(dataManager);
        ArrayAdapter<Input> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.input_port, new Input[]{});
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_taverna_server_inputs, container, false);
        ButterKnife.bind(this, rootView);
        tavernaServerInputsPresenter.attachView(this);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void showError(int stringID) {

    }

    @Override
    public void showCredentialError() {

    }

    @Override
    public void setInputs(Inputs inputs) {
        // Add inputs to the view
        //LayoutInflater vi = (LayoutInflater) getActivity().getLayoutInflater().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View v = vi.inflate(R.layout.input_port, null);
        //for (Input input: inputs.getInputs()) {
        //    input.getDepth();
        //    TextView portNameView = (TextView) v.findViewById(R.id.port_name);
        //    portNameView.setText(input.getName());
        //    ViewGroup insertPoint = (ViewGroup) getView().findViewById(R.id.inputs_area);
        //    insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //}
    }

    public void fetchInputs(String username, String password, String runLocation) {
        tavernaServerInputsPresenter.workflowInputs(username, password, runLocation);
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
        void onFragmentInteraction(Inputs inputs);
    }

}
