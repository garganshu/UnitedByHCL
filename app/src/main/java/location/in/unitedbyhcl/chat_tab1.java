package location.in.unitedbyhcl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_OK;




public class chat_tab1 extends Fragment {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    Boolean flagDisp;
    TextView t1,t2,t3;
    ImageView img;
    Button b2,b3,b4;
    private DatabaseReference mDatabase;
    private ChildEventListener mChildEventListener;
    static String question;
    static int i=1;
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
        t1=(TextView)rootView.findViewById(R.id.textView5);
        t2=(TextView)rootView.findViewById(R.id.ques2);
        t3=(TextView)rootView.findViewById(R.id.ans2);
        b2=(Button)rootView.findViewById(R.id.button2);
        b3=(Button)rootView.findViewById(R.id.button3);
        b4=(Button)rootView.findViewById(R.id.button4);
        img=rootView.findViewById(R.id.smiley);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            // initializeView();
            rootView.findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().startService(new Intent(getActivity(),FloatingViewService.class));
                  //  getActivity().finish();
                }
            });
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("questions");

        fetch_q();
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                t2.setVisibility(View.VISIBLE);

                t2.setText(question);
                t3.setVisibility(View.VISIBLE);

                t3.setText("Yes");
                i++;
                if(i==4){
                    b2.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    b4.setVisibility(View.INVISIBLE);
                    t2.setVisibility(View.INVISIBLE);
                    t3.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                }
                if(i==5)
                {  i=1;}
                fetch_q();

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t2.setText(question);
                t3.setText("No");
                i++;
                if(i==4){
                    b2.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    b4.setVisibility(View.INVISIBLE);
                    t2.setVisibility(View.INVISIBLE);
                    t3.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);

                }
                if (i == 5) {
                    i = 1;
                }
                fetch_q();

            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t2.setText(question);
                t3.setText("Dont Know");
                i++;
                if(i==4){
                    b2.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    b4.setVisibility(View.INVISIBLE);
                    t2.setVisibility(View.INVISIBLE);
                    t3.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);

                }
                if(i==5)
                {  i=1;}
                fetch_q();

            }
        });
        return rootView;
    }
public void fetch_q() {
    String k = Integer.toString(i);
    mDatabase.child("mainapp").child(k).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            question = (String) dataSnapshot.getValue();
            t1.setText(question);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    });
}
    //call this function anywhere to get flag value in this fragment

    public void getSharedData(){
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        flagDisp = pref.getBoolean("flagValue", false);


    }



    private void initializeView() {
        getView().findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startService(new Intent(getActivity(),FloatingViewService.class));
                getActivity().finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(getActivity(),
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                getActivity().finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
