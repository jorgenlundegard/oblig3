package no.oslomet.cs.algdat.Oblig3;

import java.util.Comparator;

public class main {
    //klasse for testing etc. TODO: slett før levering
    public static void main(String[] args){
        System.out.println("Hei");

        //oppg. 0:
        ObligSBinTre<Character> charTre = new ObligSBinTre<>(Comparator.naturalOrder());
        System.out.println(charTre.antall());

        ObligSBinTre<Character> intTre = new ObligSBinTre<>(Comparator.naturalOrder());
        System.out.println(charTre.antall());

        ObligSBinTre<Character> stringTre = new ObligSBinTre<>(Comparator.naturalOrder());
        System.out.println(charTre.antall());

        //oppg. 1:
        Integer[] a = {4,7,2,9,5,10,8,1,3,6};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi: a){tre.leggInn(verdi);}
        System.out.println(tre.antall());
    }
}
