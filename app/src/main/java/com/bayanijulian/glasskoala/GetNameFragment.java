package com.bayanijulian.glasskoala;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class GetNameFragment extends Fragment {

    public static final String NAME_KEY = "GET_NAME_KEY";
    private Listener listener;
    private Button nextBtn;
    public static final int PAGER_NUMBER = 0;
    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;

    public GetNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static GetNameFragment getInstance() {
        GetNameFragment fragment = new GetNameFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_name, container, false);
        nextBtn = view.findViewById(R.id.fragment_get_name_btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        firstNameInput = view.findViewById(R.id.fragment_get_name_input_first_name);
        lastNameInput = view.findViewById(R.id.fragment_get_name_input_last_name);

        return view;
    }

    private void next() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        if (checkInput(firstName, lastName)) {
            Bundle bundle = new Bundle();

            String fullName = firstName + " " + lastName;
            bundle.putString(NAME_KEY, fullName);

            bundle.putInt(SignUpActivity.PAGER_NUMBER_KEY, PAGER_NUMBER);

            listener.onNext(bundle);
        }
    }

    private boolean checkInput(String firstName, String lastName) {
        boolean isInputEmpty = firstName.isEmpty() || lastName.isEmpty();
        return !isInputEmpty;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
    public interface Listener {
        void onNext(Bundle bundle);
    }
}
