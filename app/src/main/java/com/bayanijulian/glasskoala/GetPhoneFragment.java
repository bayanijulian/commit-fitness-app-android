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


public class GetPhoneFragment extends Fragment {

    private Listener listener;
    private Button nextBtn;
    public static final int PAGER_NUMBER = 1;
    public static final String PHONE_KEY = "GET_PHONE_KEY";
    private TextInputEditText phoneInput;

    public GetPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static GetPhoneFragment getInstance() {
        GetPhoneFragment fragment = new GetPhoneFragment();
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_phone, container, false);
        nextBtn = view.findViewById(R.id.fragment_get_phone_btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        phoneInput = view.findViewById(R.id.fragment_get_phone_input_phone);

        return view;
    }

    private void next() {
        String phoneNumber = phoneInput.getText().toString();
        if (checkPhoneNumber(phoneNumber)) {
            Bundle bundle = new Bundle();
            bundle.putString(PHONE_KEY, phoneNumber);
            bundle.putInt(SignUpActivity.PAGER_NUMBER_KEY, PAGER_NUMBER);
            listener.onNext(bundle);
        }
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            return false;
        }
        return true;
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
        // TODO: Update argument type and name
        void onNext(Bundle bundle);
    }
}
