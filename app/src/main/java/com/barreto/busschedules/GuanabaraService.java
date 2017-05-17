package com.barreto.busschedules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;

public class GuanabaraService {

    public Itinerary search() throws IOException {
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

        Itinerary itinerary = new Itinerary();

        ArrayList domingoTimes = new ArrayList<>();
        for (String time : domingo) {
            domingoTimes.add(new Time(time));
        }
        itinerary.createSlot(Itinerary.SUNDAY, domingoTimes);


        ArrayList sabadoTimes = new ArrayList<>();
        for (String time : sabado) {
            sabadoTimes.add(new Time(time));
        }
        itinerary.createSlot(Itinerary.SATURDAY, sabadoTimes);


        ArrayList diaUtilTimes = new ArrayList<Time>();
        for (String time : diaUtil) {
            diaUtilTimes.add(new Time(time));
        }
        itinerary.createSlot(Itinerary.UTIL_DAY, diaUtilTimes);

        return itinerary;
    }

}
