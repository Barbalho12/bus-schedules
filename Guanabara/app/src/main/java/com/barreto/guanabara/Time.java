/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barreto.guanabara;

/**
 *
 * @author barreto
 */
public class Time {
    private int hours;
    private int minutes;
    
    public Time(){
        
    }
    public Time(int hora, int minutes){
        
    }
    public Time(String hora, String minutes){
        
    }
    /**
     * 
     * @param horaWithminutes "00:00"
     */
    public Time(String horaWithminutes){
        try{
            int h = Integer.parseInt(horaWithminutes.split(":")[0]);
            int m = Integer.parseInt(horaWithminutes.split(":")[1]);
            if ((h < 24 && h >= 0) && (m >= 0 && m < 60)){
                this.hours = h;
                this.minutes = m;
            }else{
                throw new Exception(horaWithminutes+" possui tempo inválido");
            }
        }catch(Exception erro){
            erro.printStackTrace();
            //System.err.println("[Time Constructor format 'hh:mm' ERROR] "+erro.getCause());
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    
    public String getTimeText() {
        if(hours>=10 && minutes>=10){
            return hours+":"+minutes;
        }else if(hours<10 && minutes<10){
            return "0"+hours+":0"+minutes;
        }else if(hours<10){
            return "0"+hours+":"+minutes;
        }else{
            return hours+":0"+minutes;
        }
    }
    
    public int getAbsolutTime() {
        return ((hours*60)+minutes);
    }
    
}
