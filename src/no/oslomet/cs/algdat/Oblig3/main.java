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

        //oppgave 2
        Integer[] aa = {4,7,2,9,4,10,8,7,4,6};

        ObligSBinTre<Integer> tre2 = new ObligSBinTre<>(Comparator.naturalOrder());
        for ( int verdi : aa ) tre2.leggInn(verdi);



        //Oppgave 3, 5
        Integer[] b = {5,1,7,2,5,4,9,3};
        ObligSBinTre<Integer> btre = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi: b){btre.leggInn(verdi);}
        System.out.println(btre.antall());
        System.out.println(btre);

        btre.fjernAlle(5);
        btre.nullstill();
        System.out.println(btre);
        System.out.println(btre.antall());

        btre.leggInn(1);

    }
}
