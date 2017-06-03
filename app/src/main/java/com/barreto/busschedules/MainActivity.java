package com.barreto.busschedules;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//        implements NavigationView.OnNavigationItemSelectedListener {

    private static Itinerary itinerary;
    private static List<Integer> informationsTime;

    private static final String TAG = "TEST";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show);
////       DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////       ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
////       this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////       drawer.setDrawerListener(toggle);
////       toggle.syncState();

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MeuAsyncTask asyncTask = new MeuAsyncTask();
        asyncTask.execute();

    }

    static String mensagem = "";

    class MeuAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            GuanabaraService guanabaraService = new GuanabaraService();
            String text = "";
            Itinerary itLocal = load(getApplicationContext(), "horarios.save");
            Itinerary itLocalSeach = null;

            try {
                itLocalSeach = guanabaraService.search();
                Log.v(TAG, itLocalSeach.getString(Itinerary.UTIL_DAY) + " -- ");
                Log.v(TAG, itLocalSeach.getString(Itinerary.SATURDAY) + " -- ");
                Log.v(TAG, itLocalSeach.getString(Itinerary.SUNDAY) + " -- ");
            } catch (IOException e) {
                text = "Sem conexão";
            }

            try {
                if (itLocal == null && itLocalSeach == null) {
                    mensagem = "Erro ao buscar Horários";
                    Log.v(TAG, "1");
                } else if (itLocal == null && itLocalSeach != null) {
                    itinerary = itLocalSeach;
                    save(getApplicationContext(), "horarios.save", itLocalSeach);

                    mensagem = "Os Horários foram Carregados com sucesso";
                    Log.v(TAG, "2");
                } else if (itLocalSeach == null) {

                    itinerary = itLocal;
                    mensagem = "Horários carregados localmente";
                    Log.v(TAG, "3");

                } else if (itLocal != null && itLocalSeach != null && itLocal.equals(itLocalSeach)) {
                    itinerary = itLocal;
                    mensagem = "Horários já atualizados";
                    Log.v(TAG, "4");

                } else if (itLocal != null && itLocalSeach != null && !itLocal.equals(itLocalSeach)) {
                    itinerary = itLocalSeach;
                    save(getApplicationContext(), "Horarios.save", itLocalSeach);
                    mensagem = "Os Horários foram atualizados";
                    Log.v(TAG, "5");
                }
                text = itinerary.getString(Itinerary.UTIL_DAY);
            } catch (Exception e) {
                text = "Sem conexão";
            }
            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            informationsTime = updateTime();

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
        }
    }

    public static String getDay(int dia) {
        String diaS;
        if (dia == 1) {
            diaS = Itinerary.SUNDAY;
        } else if (dia == 7) {
            diaS = Itinerary.SATURDAY;
        } else {
            diaS = Itinerary.UTIL_DAY;
        }
        return diaS;
    }

    public List<Integer> updateTime() {
        int dia = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        List<Time> listTimes = itinerary.getSlotTimes(getDay(dia));
        int horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutos = Calendar.getInstance().get(Calendar.MINUTE);
        int tempoAtual = horas * 60 + minutos;
        int time1, time2;

        List<Integer> times = new ArrayList<>(4);

        Time last = listTimes.get(listTimes.size() - 1);
        Time first = listTimes.get(0);
        int lastTime = last.getHours() * 60 + last.getMinutes();
        int firstTime = first.getHours() * 60 + first.getMinutes();
        if (tempoAtual > lastTime || tempoAtual < firstTime) {

            times.add(0, dia);
            times.add(1, listTimes.size() - 1);
            times.add(2, 0);

            if (tempoAtual < lastTime) {
                times.add(3, (1440 - lastTime) + tempoAtual);
                times.add(4, firstTime - tempoAtual);
            } else {
                times.add(3, tempoAtual - lastTime);
                times.add(4, (1440 - tempoAtual) + firstTime);
            }
        }
        for (int i = 0; i < listTimes.size() - 1; i++) {
            Time nPrev = listTimes.get(i);
            Time nNext = listTimes.get(i + 1);
            time1 = nPrev.getHours() * 60 + nPrev.getMinutes();
            time2 = nNext.getHours() * 60 + nNext.getMinutes();
            if (tempoAtual >= time1 && tempoAtual <= time2) {
                times.add(0, dia);
                times.add(1, i);
                times.add(2, i + 1);
                times.add(3, tempoAtual - time1);
                times.add(4, time2 - tempoAtual);
            }
        }
        return times;
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.nav_util_day) {
//            if(itinerary != null) {
////                et_show.setText(itinerary.getString("util_day"));
////                et_title.setText("Dia Útil");
//            }
//        } else if (id == R.id.nav_saturday) {
//            if(itinerary != null) {
////                et_show.setText(itinerary.getString("saturday"));
////                et_title.setText("Sábado");
//            }
//        } else if (id == R.id.nav_sunday) {
//            if(itinerary != null) {
////                et_show.setText(itinerary.getString("sunday"));
////                et_title.setText("Domingo");
//            }
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    public void save(Context context, String fileName, Itinerary itinerary) {

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(itinerary);
            os.close();
            fos.close();
            Log.v(TAG, "Salvo");
        } catch (FileNotFoundException e) {
            Log.v(TAG, "Error salvar");
            e.printStackTrace();
        } catch (IOException e) {
            Log.v(TAG, "Error salvar");
            e.printStackTrace();
        }

    }

    public Itinerary load(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            Itinerary itinerary = (Itinerary) is.readObject();
            is.close();
            fis.close();
            Log.v(TAG, "Lido");
            return itinerary;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Error leitura");
        return null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

//        private static TextView textView;
        private static GridView gridview;
//        private static int pos;

//        private static RelativeLayout rlNextInfo;

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

//            textView = (TextView) rootView.findViewById(R.id.section_label);

            gridview = (GridView) rootView.findViewById(R.id.gridview);
            RelativeLayout rlNextInfo = (RelativeLayout) rootView.findViewById(R.id.rl_next_info);
            TextView tvNextHour = (TextView) rootView.findViewById(R.id.tv_hour_next);
            TextView tvNextHourWait = (TextView) rootView.findViewById(R.id.tv_time_next);

            int n = getArguments().getInt(ARG_SECTION_NUMBER);

            if (itinerary != null) {

                gridview.setAdapter(new ImageAdapter(getActivity(), itinerary.getString(n)));
                if (itinerary.getString(n).equals(getDay(informationsTime.get(0)))) {
                    rlNextInfo.setVisibility(View.VISIBLE);

                    tvNextHour.setText(itinerary.getSlotTimes(itinerary.getString(n)).get(informationsTime.get(2)).getTimeText());
                    tvNextHourWait.setText("Faltam " + informationsTime.get(4) + "min");
                }

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Toast.makeText(getContext(), "" + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });
//                textView.setText(itinerary.getString(n));
            } else {
//                textView.setText("Aguardando...");
            }

            return rootView;
        }

        public class ImageAdapter extends BaseAdapter {
            private Context mContext;

            private String day;

            public ImageAdapter(Context c, String day) {
                mContext = c;
                this.day = day;
            }

            public int getCount() {
                return itinerary.getSlotTimes(day).size();
            }

            public Object getItem(int position) {
                return null;
            }

            public long getItemId(int position) {
                return 0;
            }

            // create a new ImageView for each item referenced by the Adapter
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView;

                if (convertView == null) {
                    textView = new TextView(mContext);
                    textView.setLayoutParams(new GridView.LayoutParams(100, 55));
                    textView.setPadding(1, 1, 1, 1);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }

                    textView.setTextSize(18);
                    textView.setTextColor(Color.WHITE);

                    if (day.equals(getDay(informationsTime.get(0)))) {
                        if (position == informationsTime.get(1)) {
                            textView.setBackgroundColor(Color.RED);
                        } else if (position == informationsTime.get(2)) {
                            textView.setBackgroundColor(Color.rgb(7, 138, 0));
                        } else {
                            textView.setBackgroundColor(Color.DKGRAY);
                        }
                    } else {
                        textView.setBackgroundColor(Color.DKGRAY);
                    }
                } else {
                    textView = (TextView) convertView;
                }
//                Log.v(TAG, day);
                textView.setText(itinerary.getSlotTimes(day).get(position).getTimeText());
                return textView;
            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
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
