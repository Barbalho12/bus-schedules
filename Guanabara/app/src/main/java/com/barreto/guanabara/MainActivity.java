package com.barreto.guanabara;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView et_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_show = (TextView) findViewById(R.id.et_show);

//        String texto = "";
//        new DownloadFilesTask().execute(texto, texto, texto);

        et_show.setText("Aguardando Horários...");
        et_show.setTextSize(20);
        MeuAsyncTask asyncTask = new MeuAsyncTask();
        asyncTask.execute();

    }

//    private class DownloadFilesTask extends AsyncTask<String, String, String> {
//
//        protected String doInBackground( String ... texto) {
//            GuanabaraService guanabaraService = new GuanabaraService();
//
//            try {
//                texto[0] = guanabaraService.search();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return texto[0];
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
////            setProgressPercent(progress[0]);
//        }
//
//        protected void onPostExecute(String texto) {
////            showDialog("Downloaded " + result + " bytes");
//            et_show.setText(texto);
//        }
//
//    }

    class MeuAsyncTask extends AsyncTask<Void, Void, String >{
        @Override
        protected String doInBackground(Void... params) {
            GuanabaraService guanabaraService = new GuanabaraService();

            String text = "";
            try {
                text = guanabaraService.search();
            } catch (IOException e) {
                e.printStackTrace();
                text = "error";
            }
            return text;
        }
        @Override
        protected void onPostExecute(String result) {
            et_show.setText(result);
//            super.onPostExecute(result);
        }
    }
}
