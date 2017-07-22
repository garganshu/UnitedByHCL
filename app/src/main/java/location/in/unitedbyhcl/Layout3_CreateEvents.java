package location.in.unitedbyhcl;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import static android.R.attr.data;
import static android.R.attr.name;
import static android.app.Activity.RESULT_OK;
import static java.lang.Integer.valueOf;
import static location.in.unitedbyhcl.R.id.date;

/**
 * Created by Dell on 7/20/2017.
 */

public class Layout3_CreateEvents extends android.support.v4.app.Fragment {
    View myview;
    public static int RESULT_LOAD_IMAGE=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myview = inflater.inflate(R.layout.createevents_layout3, container, false);
        final EditText et_name = (EditText) myview.findViewById(R.id.editText4);
        final EditText et_date = (EditText) myview.findViewById(R.id.editText10);
        final EditText et_time = (EditText) myview.findViewById(R.id.editText9);
        final EditText et_venue = (EditText) myview.findViewById(R.id.editText3);
        final EditText et_info = (EditText) myview.findViewById(R.id.editText11);
        final EditText et_url = (EditText) myview.findViewById(R.id.editText6);
        final EditText et_spons = (EditText) myview.findViewById(R.id.editText7);
        final EditText et_fee = (EditText) myview.findViewById(R.id.editText8);
       // final ImageView et_img =(ImageView)myview.findViewById(R.id.imageView2);
        Button bt_Add = (Button) myview.findViewById(R.id.button);
        bt_Add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String name = String.valueOf(et_name.getText().toString());
                String info = String.valueOf(et_info.getText().toString());
                String venue = String.valueOf(et_venue.getText().toString());
                String date = String.valueOf(et_date.getText().toString());
                String time = String.valueOf(et_time.getText().toString());
                String url = String.valueOf(et_url.getText().toString());
                String Spons = String.valueOf(et_spons.getText().toString());
                Integer fee = Integer.valueOf(et_fee.getText().toString());
                //TODO String img path of image from gallery from et_img
                String img =new String();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    events e1=new events( name, info, venue, date,  time, img, url, Spons,fee);
                }
                et_name.setText("");
                et_date.setText("");
                et_time.setText("");
                et_fee.setText("");
                et_info.setText("");
                et_venue.setText("");
                et_spons.setText("");
                et_url.setText("");
                et_time.setText("");
            //TODO Add event info data to the database

            }

        });

        return myview;

    }




}
