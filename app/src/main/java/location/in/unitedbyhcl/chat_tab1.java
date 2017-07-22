package location.in.unitedbyhcl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class chat_tab1 extends Fragment {

    Boolean flagDisp;


    public chat_tab1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSharedData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_tab1, container, false);
        return rootView;
    }

    //call this function anywhere to get flag value in this fragment

    public void getSharedData(){
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        flagDisp = pref.getBoolean("flagValue", false);


    }



}
