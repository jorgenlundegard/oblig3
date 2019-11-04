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
        Integer[] aa = {4,7,2,9,4,10,8,7,4,6,1};
        ObligSBinTre<Integer> tree = new ObligSBinTre<>(Comparator.naturalOrder());
        for ( int verdi : aa) tree.leggInn(verdi);

        System.out.println("\nOppgave2:");
        System.out.println(tree.antall());           //10
        System.out.println(tree.antall(5));    //0
        System.out.println(tree.antall(4));    //3
        System.out.println(tree.antall(7));    //2
        System.out.println(tree.antall(10));   //1

        System.out.println("\nOppgave4:");
        System.out.println(tree.omvendtString());

        //Oppgave 3, 5
        Integer[] b = {4,7,2,9,4,10,8,7,4,6};
        ObligSBinTre<Integer> btre = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi: b){btre.leggInn(verdi);}
        System.out.println(btre.antall());
        System.out.println(btre);

        //Oppgave6
        System.out.println(btre.høyreGren());

        //Oppgave 7
        Integer[] c = {4, 1, 6, 3, 5, 8, 2, 7, 9};
        ObligSBinTre<Integer> treOppg7 = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi: c){treOppg7.leggInn(verdi);}
        System.out.println("oppgave7");
        for(String gren : treOppg7.grener()) System.out.println(gren);

        //Oppgave8
        System.out.println(btre.postString());
    }
}
