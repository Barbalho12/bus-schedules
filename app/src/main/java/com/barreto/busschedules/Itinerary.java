package com.barreto.busschedules;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Barreto on 12/05/2017.
 */

public class Itinerary implements Serializable{

    public static final String UTIL_DAY = "UTIL_DAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";

    String util_days;
    String saturday;
    String sunday;

    Hashtable<String, List<Time>> slots;

    public Itinerary(){
        slots = new Hashtable<>();
    }

    void createSlot(String name, List<Time> hors){
        slots.put(name, hors);
    }

//    void createSlot(String TYPE, List<Time> hors){
//
//        String texto = "";
//        boolean init = true;
//        for (Time time : hors) {
//            if(init) {
//                texto += time.getTimeText();
//                init = false;
//            }else{
//                texto += "      " + time.getTimeText();
//            }
//        }
//        if(TYPE.equals(UTIL_DAY)){
//            util_days = texto;
//        }else if(TYPE.equals(SATURDAY)){
//            saturday = texto;
//        }else if(TYPE.equals(SUNDAY)){
//            sunday = texto;
//        }
//    }

//    void createSlot(String TYPE, String slot){
//        if(TYPE.equals(UTIL_DAY)){
//            util_days = slot;
//        }else if(TYPE.equals(SATURDAY)){
//            saturday = slot;
//        }else if(TYPE.equals(SUNDAY)){
//            sunday = slot;
//        }
////        slots.put(name, hors);
//    }

    List<Time> getSlotTimes(String name){
        return slots.get(name);
    }

//    String getSlotTimes(String TYPE){
//        if(TYPE.equals(UTIL_DAY)){
//            return util_days;
//        }else if(TYPE.equals(SATURDAY)){
//            return saturday;
//        }else if(TYPE.equals(SUNDAY)){
//            return sunday;
//        }
//        return sunday;
//    }

//    String getString(String TYPE){
//        if(TYPE.equals(UTIL_DAY)){
//            return util_days;
//        }else if(TYPE.equals(SATURDAY)){
//            return saturday;
//        }else if(TYPE.equals(SUNDAY)){
//            return sunday;
//        }
//        return sunday;
//    }

    public String getString(String name){
        String texto = "";
        boolean init = true;
        for (Time time : getSlotTimes(name)) {
            if(init) {
                texto += time.getTimeText();
                init = false;
            }else{
                texto += "  " + time.getTimeText();
            }
        }
        return texto;
    }

    public String getString(int sec_name){
        String  name = "";
        switch (sec_name){
            case 0:
                name = UTIL_DAY;
                break;
            case 1:
                name = SATURDAY;
                break;
            case 2:
                name = SUNDAY;
                break;
            default:
                name = SUNDAY;
                break;
        }
//        return sec_name + "";
        return getString(name);
    }

    @Override
    public boolean equals(Object object) {
        if(! (object instanceof Itinerary)){
            return false;
        }
        if(object  == null){
            return false;
        }
        Itinerary otherIt = (Itinerary) object;
        if((util_days == null || saturday == null || sunday == null) && (otherIt.util_days == null || otherIt.saturday == null || otherIt.sunday == null)){
            return true;
        }/*else  if((util_days == null || saturday == null || sunday == null) || (otherIt.util_days == null || otherIt.saturday == null || otherIt.sunday == null)){
            return false;
        }*/
        if(getString(UTIL_DAY).equals(otherIt.getString(UTIL_DAY))
                && getString(SATURDAY).equals(otherIt.getString(SATURDAY))
                && getString(SUNDAY).equals(otherIt.getString(SUNDAY))){
            return true;
        }
        return false;
    }
}
