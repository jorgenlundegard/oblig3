package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{
  private static final class Node<T>   // en indre nodeklasse
  {
    private T verdi;                   // nodens verdi
    private Node<T> venstre, høyre;    // venstre og høyre barn
    private Node<T> forelder;          // forelder

    // konstruktør
    private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
    {
      this.verdi = verdi;
      venstre = v; høyre = h;
      this.forelder = forelder;
    }

    private Node(T verdi, Node<T> forelder)  // konstruktør
    {
      this(verdi, null, null, forelder);
    }

    @Override
    public String toString(){ return "" + verdi;}

  } // class Node

  private Node<T> rot;                            // peker til rotnoden
  private int antall;                             // antall noder
  private int endringer;                          // antall endringer

  private final Comparator<? super T> comp;       // komparator

  public ObligSBinTre(Comparator<? super T> c)    // konstruktør
  {
    rot = null;
    antall = 0;
    comp = c;
  }
  
  @Override
  public boolean leggInn(T verdi)
  {
    Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

    Node<T> p = rot, q = null;               // p starter i roten
    int cmp = 0;                             // hjelpevariabel

    while (p != null)                        // fortsetter til p er ute av treet
    {
      q = p;                                 // q er forelder til p
      cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
      p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
    }

    // p er nå null, dvs. ute av treet, q er den siste vi passerte

    p = new Node<T>(verdi, q);               // oppretter en ny node

    if (q == null) rot = p;                  // p blir rotnode
    else if (cmp < 0) q.venstre = p;         // venstre barn til q
    else q.høyre = p;                        // høyre barn til q

    antall++;                                // én verdi mer i treet
    endringer++;
    return true;                             // vellykket innlegging
  }
  
  @Override
  public boolean inneholder(T verdi)
  {
    if (verdi == null) return false;

    Node<T> p = rot;

    while (p != null)
    {
      int cmp = comp.compare(verdi, p.verdi);
      if (cmp < 0) p = p.venstre;
      else if (cmp > 0) p = p.høyre;
      else return true;
    }

    return false;
  }
  
  @Override
  public boolean fjern(T verdi)
  {
      if (verdi == null) return false;  // treet har ingen nullverdier

      Node<T> p = rot, q = null;   // q skal være forelder til p

      while (p != null)            // leter etter verdi
      {
          int cmp = comp.compare(verdi,p.verdi);      // sammenligner
          if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
          else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
          else break;    // den søkte verdien ligger i p
      }
      if (p == null) return false;   // finner ikke verdi

      if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
      {
          Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
          if (p == rot) {
              rot = b;
          }
          else if (p == q.venstre) q.venstre = b;
          else q.høyre = b;
          if(b != null) b.forelder = q;
      }
      else  // Tilfelle 3)
      {
          Node<T> r = nesteInorden(p), s = Objects.requireNonNull(r).forelder;;   // finner neste i inorden
          p.verdi = r.verdi;   // kopierer verdien i r til p

          if (!s.equals(p)) {
              s.venstre = r.høyre;
          }
          else s.høyre = r.høyre;
      }

      antall--;   // det er nå én node mindre i treet
      endringer ++;
      return true;
  }
  
  public int fjernAlle(T verdi)
  {
      if(tom()) return 0;
      int antallFjernet = 0;
      while(true){
        if(!fjern(verdi)) break;
        antallFjernet++;
      }
      return antallFjernet;
  }
  
  @Override
  public int antall()
  {
      return antall;
  }
  
  public int antall(T verdi)
  {

      // LØSNING: kjør en iterativ inorder, skal prøve på det med en stack/deque. hver gang den går gjennom en med ønskede value -> antall++
      // For å få den mest effektiv finn første ved inneholder(), deretter inorder frem til p.value>value.

    // Har klassene 'antall', 'inneholder' og 'tom' til disposisjon.
    // Må finne en måte å teste alle nodene for gitt verdi. Hvis true antall++.
      int antall = 0;
      int runder = 0;


      // If true kjor gjennom hele for aa finne antallet.

    if (inneholder(verdi)) {
        if (verdi == null) return 0;

        Node<T> forelder = rot;
        Node<T> venstre = rot;
        Node<T> hoyre = rot;

        while (forelder != null && runder < antall())
        {
            runder++;

            if (forelder.verdi == verdi) {antall++;}




            /*
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else antall++;*/
        }
    }

    // If false gi 0.

    return antall;
  }
  
  @Override
  public boolean tom()
  {
    return antall == 0;
  }
  
  @Override
  public void nullstill()
  {
      if(antall == 0) return;
    //finner første node i inorden
      ArrayList<Node<T>> noder = new ArrayList<>();
      Node<T> nodeSkalNulles = rot;
      while(nodeSkalNulles.venstre != null){nodeSkalNulles = nodeSkalNulles.venstre; }
      Node<T> node = nesteInorden(nodeSkalNulles);

      for(int i = 0; i<antall; i++){
          noder.add(nodeSkalNulles);
          nodeSkalNulles = node;
          if(node != null) node = nesteInorden(node);
      }
      for(Node<T> a : noder){
          a.høyre = null;
          a.venstre = null;
          a.forelder = null;
          a.verdi = null;
          antall--;
      }
      rot = null;
  }
  
  private static <T> Node<T> nesteInorden(Node<T> p)
  {
    if(p.høyre != null){
      p = p.høyre;
      while(p.venstre != null){p = p.venstre;}
      return p;             //Hvis noden har et høyre subtre, er neste i inorden den nederste venstre noden i dette.
    }
    else{
        // Ellers gaar vi oppover i treet til gjeldende node er venstre barn til sin foreldernode.
        // Dette er neste i inorden.
        while(true){
          if(p.forelder == null){return null;}  //Hvis gjeldende node er siste i inorden, returneres null.
          if(p.equals(p.forelder.venstre)){return p.forelder;}
          p = p.forelder;
        }
    }
  }
  
  @Override
  public String toString()
  {
    if(tom()){return "[]";}
    Node<T> node = rot;                                 // "gjeldende" node
    while(node.venstre != null){node = node.venstre; }  // finner første node i inorden
    StringBuilder s = new StringBuilder();
    s.append("[");s.append(node.verdi);                 //Legger til første verdi i stringbuilder
    while(true) {                                       // løkke som fortsetter til break;
      node = nesteInorden(node);
      if(node == null){break;}                    //Hvis nesteInorden(node) er null er node siste verdi. Lokken brytes.
      s.append(", ");
      s.append(node.verdi);
    }
    s.append("]");
    return s.toString();
  }
  
  public String omvendtString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String høyreGren()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String lengstGren()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String[] grener()
  {
      if(tom()){
          String[] tomListe;
          tomListe = new String[0];
          return tomListe;
      }
      ArrayList<Node<T>> bladNoder = new ArrayList<>();
      //finne bladnoder ved inorden traversering. Finner første i inorden:
      Node<T> node = rot;// "gjeldende" node
      while (node.venstre != null) node = node.venstre;
      if(antall==1) bladNoder.add(node);
      else {
          while (nesteInorden(node) != null) {
              node = nesteInorden(node);
              if (Objects.requireNonNull(node).høyre == null && node.venstre == null) {
                  bladNoder.add(node);
              }
          }
      }

      String[] grener = new String[bladNoder.size()];
      int index = 0;

      //traverser opp via foreldreref. fra hver bladnode og legger i streng.
      for(Node<T> bladNode : bladNoder){
          StringBuilder s = new StringBuilder();
          s.append("[");
          while(!Objects.requireNonNull(bladNode).equals(rot)){
              bladNode = bladNode.forelder;
              assert bladNode != null;
              s.insert(1, bladNode.verdi);
              if(bladNode.forelder!=null) s.insert(1, ", ");
          }
          s.append("]");
          grener[index] = s.toString();
          index ++;
      }
      return grener;
  }
  
  public String bladnodeverdier()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String postString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  @Override
  public Iterator<T> iterator()
  {
    return new BladnodeIterator();
  }
  
  private class BladnodeIterator implements Iterator<T>
  {
    private Node<T> p = rot, q = null;
    private boolean removeOK = false;
    private int iteratorendringer = endringer;
    
    private BladnodeIterator()  // konstruktør
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public boolean hasNext()
    {
      return p != null;  // Denne skal ikke endres!
    }
    
    @Override
    public T next()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

  } // BladnodeIterator

} // ObligSBinTre
