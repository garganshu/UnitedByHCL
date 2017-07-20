package location.in.unitedbyhcl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Deepanshi on 7/20/2017.
 */

public class Layout1_CHAT extends Fragment {
    View myview;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public  static final int RC_SIGN_IN=1;
    public static final int RC_PHOTO_PICKER=2;
    public  static final String ANONYMOUS = "anonymous";
    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebasAuUth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.chat_layout1, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) myview.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mFirebasAuUth= FirebaseAuth.getInstance();

        mAuthStateListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //  Toast.makeText(getActivity(), "You're now signed in. Welcome to FriendlyChat.", Toast.LENGTH_SHORT).show();
                    onSignedInIntialise(user.getDisplayName(),user.getEmail());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        return myview;
    }
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
        /**
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
            View rootView = inflater.inflate(R.layout.chat_tab1, container, false);
            return rootView;
        }
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {
                if (resultCode == RESULT_OK) {
                    // Sign-in succeeded, set up the UI
                    Toast.makeText(getActivity(), "Signed in!", Toast.LENGTH_SHORT).show();

                } else if (resultCode == RESULT_CANCELED) {
                    // Sign in was canceled by the user, finish the activity
                    Toast.makeText(getActivity(), "Sign in canceled", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
        @Override
        public void onResume() {
            super.onResume();
            if (mAuthStateListener != null) {
                mFirebasAuUth.addAuthStateListener(mAuthStateListener);
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            if (mAuthStateListener != null) {
                mFirebasAuUth.removeAuthStateListener(mAuthStateListener);
            }
            detachDatabaseListener();
        }
        private void onSignedInIntialise(String username,String userid) {
            mUsername = username;
            //name= username;
            //id=userid;

            if (mChildEventListener == null) {
                mChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
//                    mMessageAdapter.add(friendlyMessage);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
            }
        }
        private void onSignedOutCleanup(){
            mUsername= ANONYMOUS;

            detachDatabaseListener();
        }
        private void detachDatabaseListener(){
            if(mChildEventListener!=null) {
                mMessagesDatabaseReference.removeEventListener(mChildEventListener);
                mChildEventListener=null;
            }
            else{
                Toast.makeText(getContext(),"erorrrrrr",Toast.LENGTH_SHORT).show();
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
    //AMAN Code
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mFirebasAuUth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("TAG", "signInWithCredential:success");
//                    FirebaseUser user = mFirebasAuUth.getCurrentUser();
//                    mUsername = user.getUid();
//                    String userName;
//                    String emailRetreive = user.getEmail();
//                    userName = usernameExractor(emailRetreive);
//                    mDatabase.child("Users").child(uid).child("UserName").setValue(userName);
//                    //updateUI(user);
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("TAG", "signInWithCredential:failure", task.getException());
//                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
//                    //updateUI(null);
//                }
//            }
//
//        });
//    }
//    private String usernameExractor(String emailRetreive) {
//
//        String ch="";
//        int i;
//        for (i=0;i<emailRetreive.length();i++){
//            if(emailRetreive.charAt(i)!='@'){
//                ch = ch + emailRetreive.charAt(i);
//
//            }else
//                break;
//        }
//        return  ch;
//    }

}