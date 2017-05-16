package com.barreto.busschedules;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "TEST";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static Itinerary itinerary;

    private ViewPager mViewPager;

//    private static RelativeLayout wait_info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Create Activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);

        MeuAsyncTask asyncTask = new MeuAsyncTask();
        asyncTask.execute();
    }

    class MeuAsyncTask extends AsyncTask<Void, Void, String > {


        @Override
        protected String doInBackground(Void... params) {
            GuanabaraService guanabaraService = new GuanabaraService();
            String text = "";
            try {

                itinerary = guanabaraService.search();
                text = itinerary.getString("util_day");
            } catch (IOException e) {
                e.printStackTrace();
                text = "error";
            }
            return text;
        }
        @Override
        protected void onPostExecute(String result) {

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            TextView textView = (TextView) findViewById(R.id.section_label);
            textView.setText(result);

        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main2, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static TextView textView;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
            Log.v(TAG, "PlaceholderFragment");
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

            textView = (TextView) rootView.findViewById(R.id.section_label);

            int n = getArguments().getInt(ARG_SECTION_NUMBER);

            if(itinerary != null) {
                textView.setText(itinerary.getString(n));
            }else{
                textView.setText("Aguardando...");
            }

            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            Log.v(TAG, "getItem " + position);
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            Log.v(TAG, "getPageTitle");
            switch (position) {
                case 0:
                    return "Dia útil";
                case 1:
                    return "Sábado";
                case 2:
                    return "Domingo";
                case 3:
                    return "Feriado";
            }
            return null;
        }
    }
}
