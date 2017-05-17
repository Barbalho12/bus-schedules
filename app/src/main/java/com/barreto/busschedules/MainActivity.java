package com.barreto.busschedules;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity{
//        implements NavigationView.OnNavigationItemSelectedListener {

    private static Itinerary itinerary;

    private static final String TAG = "TEST";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        MeuAsyncTask asyncTask = new MeuAsyncTask();
        asyncTask.execute();
    }

    static String mensagem = "";

    class MeuAsyncTask extends AsyncTask<Void, Void, String > {
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

                if(itLocal == null && itLocalSeach == null){

                    mensagem =  "Erro ao buscar Horários";
                    Log.v(TAG, "1");
                }else if(itLocal == null && itLocalSeach != null){
                    itinerary = itLocalSeach;
                    save(getApplicationContext(), "horarios.save", itLocalSeach);

                    mensagem =  "Os Horários foram Carregados com sucesso";
                    Log.v(TAG, "2");
                }else if(itLocalSeach == null){

                    itinerary = itLocal;
                    mensagem =  "Horários carregados localmente";
                    Log.v(TAG, "3");

                }else if(itLocal != null && itLocalSeach != null && itLocal.equals(itLocalSeach)){
                    itinerary = itLocal;
                    mensagem =  "Horários já atualizados";
                    Log.v(TAG, "4");

                }else if(itLocal != null && itLocalSeach != null && !itLocal.equals(itLocalSeach)){
                    itinerary = itLocalSeach;
                    save(getApplicationContext(), "Horarios.save", itLocalSeach);
                    mensagem =  "Os Horários foram atualizados";
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

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            TextView textView = (TextView) findViewById(R.id.section_label);
            textView.setText(result);

            Toast.makeText(getApplicationContext(),mensagem, Toast.LENGTH_LONG).show();
        }
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

    public void save(Context context, String fileName, Itinerary itinerary){

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

    public Itinerary load(Context context, String fileName){

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

//    void insere(){
//        BancoController crud = new BancoController(getBaseContext());
//        EditText titulo = (EditText)findViewById(R.id.editText);
//        EditText autor = (EditText)findViewById((R.id.editText2));
//        EditText editora = (EditText)findViewById(R.id.editText3);
//        String tituloString = titulo.getText().toString();
//        String autorString = autor.getText().toString();
//        String editoraString = editora.getText().toString();
//        String resultado;
//        resultado = crud.insereDado(tituloString,autorString,editoraString);
//        resultado = crud.insereDado(itinerary);
//        Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
//    }
//
//    void consutla(){
//        BancoController crud = new BancoController(getBaseContext());
//        Cursor cursor = crud.carregaDados();
//
//        String[] nomeCampos = new String[] {CriaBanco.ID, CriaBanco.TITULO};
//        int[] idViews = new int[] {R.id.idLivro, R.id.nomeLivro};
//
//        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(getBaseContext(),
//                R.layout.livros_layout,cursor,nomeCampos,idViews, 0);
//        lista = (ListView)findViewById(R.id.listView);
//        lista.setAdapter(adaptador);
//
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
