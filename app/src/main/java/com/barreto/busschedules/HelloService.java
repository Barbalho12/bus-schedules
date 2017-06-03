package com.barreto.busschedules;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HelloService /*extends Service*/ {
//    private Looper mServiceLooper;
//    private ServiceHandler mServiceHandler;
//
//    // Handler that receives messages from the thread
//    private final class ServiceHandler extends Handler {
//        public ServiceHandler(Looper looper) {
//            super(looper);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            // Normally we would do some work here, like download a file.
//            // For our sample, we just sleep for 5 seconds.
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                // Restore interrupt status.
//                Thread.currentThread().interrupt();
//            }
//            // Stop the service using the startId, so that we don't stop
//            // the service in the middle of handling another job
//            stopSelf(msg.arg1);
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        // Start up the thread running the service.  Note that we create a
//        // separate thread because the service normally runs in the process's
//        // main thread, which we don't want to block.  We also make it
//        // background priority so CPU-intensive work will not disrupt our UI.
//        HandlerThread thread = new HandlerThread("ServiceStartArguments",Thread.NORM_PRIORITY);
//        thread.start();
//
//        // Get the HandlerThread's Looper and use it for our Handler
//        mServiceLooper = thread.getLooper();
//        mServiceHandler = new ServiceHandler(mServiceLooper);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
////        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//
//        int dia = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
//
////        if(dia )
//        int horas = Calendar.getInstance().get(Calendar.HOUR);
//        int minutos = Calendar.getInstance().get(Calendar.MINUTE);
//
//        Toast.makeText(this, dia+" -> "+horas+"h"+minutos, Toast.LENGTH_SHORT).show();
//        // For each start request, send a message to start a job and deliver the
//        // start ID so we know which request we're stopping when we finish the job
//        Message msg = mServiceHandler.obtainMessage();
//        msg.arg1 = startId;
//        mServiceHandler.sendMessage(msg);
//
//        // If we get killed, after returning from here, restart
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        Itinerary itinerary = (Itinerary) intent.getExtras().get("KEY");
//
//        int dia = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
//
//        List<Time> listTimes;
//
//        if(dia == 1){
//            listTimes = itinerary.getSlotTimes(Itinerary.SATURDAY);
//        }else if(dia == 7){
//            listTimes = itinerary.getSlotTimes(Itinerary.SUNDAY);
//        }else{
//            listTimes = itinerary.getSlotTimes(Itinerary.UTIL_DAY);
//        }
//
//        int horas = Calendar.getInstance().get(Calendar.HOUR);
//        int minutos = Calendar.getInstance().get(Calendar.MINUTE);
//
//        int tempoAtual = horas*60 + minutos;
//
//        int time1, time2;
//
//        List<Integer> times = new ArrayList<>(4);
//
//        for (int i = 0; i < listTimes.size()-1; i++) {
//            Time nPrev = listTimes.get(i);
//            Time nNext = listTimes.get(i+1);
//
//            time1 = nPrev.getHours()*60 + nPrev.getMinutes();
//            time2 = nNext.getHours()*60 + nNext.getMinutes();
//
//            if(tempoAtual >= time1 && tempoAtual <= time2){
//                times.add(0, dia);
//                times.add(1, i);
//                times.add(2, i+1);
//                times.add(3, tempoAtual-time1);
//                times.add(4, time2-tempoAtual);
//            }
//        }
//
////        Binder a = new Binder();
////        a.
//        Toast.makeText(this, dia+" -> "+horas+"h"+minutos, Toast.LENGTH_SHORT).show();
////        now.getHours();
//
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
//    }
}
