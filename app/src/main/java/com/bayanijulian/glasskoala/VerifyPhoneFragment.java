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

public class VerifyPhoneFragment extends Fragment {
    public static final int PAGER_NUMBER = 2;
    private Listener listener;
    private Button nextBtn;
    private TextInputEditText codeInput;
    public static final String CODE_KEY = "GET_CODE_KEY";

    public VerifyPhoneFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static VerifyPhoneFragment getInstance() {
        VerifyPhoneFragment fragment = new VerifyPhoneFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_phone, container, false);
        nextBtn = view.findViewById(R.id.fragment_verify_phone_btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        codeInput = view.findViewById(R.id.fragment_verify_phone_input_code);

        return view;
    }

    private void next() {
        String code = codeInput.getText().toString();
        if (checkCode(code)) {
            Bundle bundle = new Bundle();
            bundle.putString(CODE_KEY, code);
            bundle.putInt(SignUpActivity.PAGER_NUMBER_KEY, PAGER_NUMBER);
            listener.onNext(bundle);
        }
    }

    private boolean checkCode(String code) {
        if (code.isEmpty()) {
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
        void onNext(Bundle bundle);
    }
}
