package com.barreto.busschedules;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Barreto on 12/05/2017.
 */

public class Itinerary implements Serializable{

    Hashtable<String, List<Time>> slots;

    public Itinerary(){
        slots = new Hashtable<>();
    }

    void createSlot(String name, List<Time> hors){
        slots.put(name, hors);
    }

    List<Time> getSlotTimes(String name){
        return slots.get(name);
    }

    public String getString(String name){
        String texto = "";
        boolean init = true;
        for (Time time : getSlotTimes(name)) {
            if(init) {
                texto += time.getTimeText();
                init = false;
            }else{
                texto += "      " + time.getTimeText();
            }
        }
        return texto;
    }

    public String getString(int sec_name){
        String  name = "";
        switch (sec_name){
            case 0:
                name = "util_day";
                break;
            case 1:
                name = "saturday";
                break;
            case 2:
                name = "sunday";
                break;
            default:
                name = "sunday";
                break;
        }
//        return sec_name + "";
        return getString(name);
    }
}
