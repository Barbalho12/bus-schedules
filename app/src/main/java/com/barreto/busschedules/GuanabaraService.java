
package com.barreto.busschedules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/**
 *
 * @author barreto
 */
public class GuanabaraService {
    public String search() throws IOException {
        String testeGuanabara = "http://www.transportesguanabara.com.br/ajax.linhas?linha=88";
        
        org.jsoup.nodes.Document doc = Jsoup.connect(testeGuanabara).get();

        Element span = doc.select("span").last();
        span.select("strong").remove();
        String valor = span.text();
        valor = valor.replaceAll("Horários previstos de saída do Terminal de Pajuçara", "").trim();

        valor = valor.replaceAll("\\*", "");
        valor = valor.replaceAll("Viagens pelo Campus da UFRN", "");

        valor = valor.replaceAll("\\(Dias Úteis\\):", "");
        valor = valor.replaceAll("\\(Sábados\\):", "*");
        valor = valor.replaceAll("\\(Domingos\\):", "*");
        valor = valor.replaceAll(" ", "");
        String catHora [] = valor.split("\\*");

        String diaUtil [] = catHora[0].split("/");
        String sabado [] = catHora[1].split("/");
        String domingo [] = catHora[2].split("/");


        Hashtable<String, List<Time>> intinerario = new Hashtable<>();

        ArrayList domingoTimes = new ArrayList<>();
        for (String time : domingo) {
            domingoTimes.add(new Time(time));
        }
        intinerario.put("domingo", domingoTimes);


        ArrayList sabadoTimes = new ArrayList<>();
        for (String time : sabado) {
            sabadoTimes.add(new Time(time));
        }
        intinerario.put("sabado", sabadoTimes);


        ArrayList diaUtilTimes = new ArrayList<Time>();
        for (String time : diaUtil) {
            diaUtilTimes.add(new Time(time));
        }
        intinerario.put("diaUtil", diaUtilTimes);

//        print(intinerario);
        return getString(intinerario);
//        return "";
    }

    public void print(Hashtable<String, List<Time>> intinerario){
        System.out.print("\nDia útil: ");
        for (Time time : intinerario.get("diaUtil")) {
            System.out.print(".");
        }
        System.out.print("\nSábado: ");
        for (Time time : intinerario.get("sabado")) {
            System.out.print(".");
        }
        System.out.print("\nDomingo: ");
        for (Time time : intinerario.get("domingo")) {
            System.out.print(".");
        }
        System.out.print("\n");
    }

    public String getString(Hashtable<String, List<Time>> intinerario){
        String texto = "";
//        texto += "\nDia útil: ";
        boolean init = true;
        for (Time time : intinerario.get("diaUtil")) {
//            System.out.print(".");
            if(init) {
                texto += time.getTimeText();
                init = false;
            }else{
                texto += "      " + time.getTimeText();
            }

        }
        return texto;
    }
    
}
