package location.in.unitedbyhcl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class chat_tab1 extends Fragment {

    public chat_tab1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab1, container, false);
        return rootView;
        // TextView textView = new TextView(getActivity());
       // textView.setText(R.string.hello_blank_fragment);
       // return textView;
    }



}
