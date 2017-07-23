package location.in.unitedbyhcl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static location.in.unitedbyhcl.SignUpPage.uid;

/**
 * Created by AnshulGarg on 7/22/2017.
 */

public class Layout1_CHAT extends Fragment implements LocationListener {
    View myview;


    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat, geoadd,geodistance;
    String lat;
    String provider;
    Double latitude, longitude,lat1 = -82.862751 ,lon1 = 135.0000;
    float distance = 0;
    protected boolean gps_enabled, network_enabled;


    private static final String TAG = "MainActivity";
    public String name;
    public String id;
    Boolean flag ;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static final int RC_SIGN_IN = 1;
    public static final int RC_PHOTO_PICKER = 2;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private TextView temp;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserLcationDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebasAuUth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.chat_layout1, container, false);

        txtLat = (TextView) myview.findViewById(R.id.latlong);
        geoadd = (TextView) myview.findViewById(R.id.address);
        geodistance = (TextView) myview.findViewById(R.id.distance) ;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseStorage=FirebaseStorage.getInstance();
       // mUserLcationDatabaseReference=mFirebaseDatabase.getReference().child("usercurrentlocation");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) myview.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        temp= (TextView) myview.findViewById(R.id.texttemp);

        TabLayout tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return myview;
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;

        public PlaceholderFragment() {
        }

        /**
         *
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.chat_tab1, container, false);
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0: {
                    chat_tab1 tab_1 = new chat_tab1();
                    return tab_1;
                }
                case 1: {
                    chat_tab2 tab_2 = new chat_tab2();
                    return tab_2;
                }

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Chat";
                case 1:
                    return "Nearby Updates";
            }
            return null;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        txtLat = (TextView) myview.findViewById(R.id.latlong);
        geoadd = (TextView) myview.findViewById(R.id.address);
        geodistance = (TextView) myview.findViewById(R.id.distance);


        latitude = location.getLatitude();
        longitude = location.getLongitude();
       // mDatabase.child("users").child("usercurrentlocation").setValue(latitude);
        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());

        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            int i = addresses.get(0).getMaxAddressLineIndex();
            String full_add[] = new String[i];
            for (int j = 0; j < i; j++) {
                full_add[j] = addresses.get(0).getAddressLine(j);
            }
            geoadd.setText(Arrays.toString(full_add).replaceAll("\\[|\\]", ""));
            aboveTenkms();
            String str2 = Float.toString(distance);
            geodistance.setText("Distance :"+str2);
            sharedata();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude", "disable");
    }

    //code to send latitude and longitude to firebase only if distance between current latitude and longitde
    //and the old one fetched from database is more than 10kms....Loc1 to be fetched from db
    //code to convert latitude and longitude into the distance..........
    //begins here.
    public void aboveTenkms() {
        flag = false;
        distance = distFrom(lat1,lon1,latitude,longitude);

        if (distance >= 5000.00) {
            flag = true;
           // mUserLcationDatabaseReference.push().setValue(loc2);
            mDatabase.child("users").child("usercurrentlocation").child("latitude").setValue(latitude);
            mDatabase.child("users").child("usercurrentlocation").child("longitude").setValue(longitude);
//            lat1 = latitude;
//            lon1 = longitude;
            //temp.setText(latitude+""+longitude+"");

            //push loc2 to firebase and update loc1 there
        }


    }


    public float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    //function to set shared preference (flag value only)
    public void sharedata(){

        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("flagValue",flag);
        edit.commit();
    }


}