package no.oslomet.cs.algdat.Oblig3;

//Gruppemedlemmer:
//Mads Andreas Lundbye - s331353
//Jorgen Lundegaard - s331385


////////////////// ObligSBinTre /////////////////////////////////

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{
  private static final class Node<T>   // en indre nodeklasse
  {
    private T verdi;                   // nodens verdi
    private Node<T> venstre, høyre;    // venstre og hoyre barn
    private Node<T> forelder;          // forelder

    // konstruktor
    private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
    {
      this.verdi = verdi;
      venstre = v; høyre = h;
      this.forelder = forelder;
    }

    private Node(T verdi, Node<T> forelder)  // konstruktor
    {
      this(verdi, null, null, forelder);
    }

    @Override
    public String toString() { return "" + verdi;}

  } // class Node

  private Node<T> rot;                            // peker til rotnoden
  private int antall;                             // antall noder
  private int endringer;                          // antall endringer

  private final Comparator<? super T> comp;       // komparator

  public ObligSBinTre(Comparator<? super T> c)    // konstruktor
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

    // p er naa null, dvs. ute av treet, q er den siste vi passerte

    p = new Node<T>(verdi, q);               // oppretter en ny node

    if (q == null) rot = p;                  // p blir rotnode
    else if (cmp < 0) q.venstre = p;         // venstre barn til q
    else q.høyre = p;                        // hoyre barn til q

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

      Node<T> p = rot, q = null;   // q skal vaere forelder til p

      while (p != null)            // leter etter verdi
      {
          int cmp = comp.compare(verdi,p.verdi);      // sammenligner
          if (cmp < 0) { q = p; p = p.venstre; }      // gaar til venstre
          else if (cmp > 0) { q = p; p = p.høyre; }   // gaar til hoyre
          else break;    // den sokte verdien ligger i p
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

      antall--;   // det er naa én node mindre i treet
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
      endringer = endringer + antallFjernet;
      return antallFjernet;
  }
  
  @Override
  public int antall()
  {
      return antall;
  }
  
  public int antall(T verdi)
  {

      // LoSNING: kjorer en iterativ inorder bruker en stack. hver gang den gaar gjennom en med onskede value -> antall++
      if (!inneholder(verdi)) {return 0;}       // Dersom verdien ikke finnes er det ingen grunn til aa se hvor mange.
      int antall = 0;                           // En teller for antall verdier.
      Node<T> noden = rot;                      // Starter med roten av treet

      if (noden == null) return 0;              // Returnerer med 0 dersom verdien ikke finnes i treet  | dersom treet er tomt?
      Stack<Node> stack = new Stack<Node>();    // Lager en stack som lagrer nodene
      while (true) {                            // En lokke som bare gir ture helt til man finner en tom node og en tom stack
          if (noden != null) {
              stack.push(noden);                // Legger inn noden i stacken
              noden = noden.venstre;            // Nodens venstre barn blir valgt og kjorer gjennom lokken igjen

          } else {
              if (stack.isEmpty()){break;}      // Naar stacken er tom avsluttes lokken
              noden = stack.pop();              // Popper overste verdi i stcken ut, men lagrer den som "noden"
              if (noden.verdi == verdi) {antall++;} // Hvis verdien er den man leter etter oker antall
              noden = noden.høyre;              // Fortsetter lokken med noden sitt hoyre barn
          }
      } // while

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
    //finner forste node i inorden
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
      endringer = 0;
  }
  
  private static <T> Node<T> nesteInorden(Node<T> p)
  {
    if(p.høyre != null){
      p = p.høyre;
      while(p.venstre != null){p = p.venstre;}
      return p;             //Hvis noden har et hoyre subtre, er neste i inorden den nederste venstre noden i dette.
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
    while(node.venstre != null){node = node.venstre; }  // finner forste node i inorden
    StringBuilder s = new StringBuilder();
    s.append("[");s.append(node.verdi);                 //Legger til forste verdi i stringbuilder
    while(true) {                                       // lokke som fortsetter til break;
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
      // Skal loses iterativt med inverse inorder. Skal bruke hjelpe stack. OBS ikke foreldrepekere?

      String utskrift = "[";                    // Utskriften begynner med "["
      Node<T> noden = rot;                      // Starter med roten av treet

      if (noden == null) return utskrift += "]";// Returnerer med "]" dersom verdier ikke finnes i treet
      Stack<Node> stack = new Stack<Node>();    // Lager en stack som lagrer nodene
      while (true) {                            // En lokke som bare gir ture helt til man finner en tom node og en tom stack
          if (noden != null) {
              stack.push(noden);                // Legger inn noden i stacken
              noden = noden.høyre;              // Nodens hoyre barn blir valgt og kjorer gjennom lokken igjen

          } else {
              if (stack.isEmpty()){break;}      // Naar stacken er tom avsluttes lokken
              noden = stack.pop();              // Popper overste verdi i stcken ut, men lagrer den som "noden"
              if (!utskrift.equals("[")) {utskrift += ", ";}
              utskrift += noden.verdi;          // Hvis verdien er den man leter etter oker antall
              noden = noden.venstre;            // Fortsetter lokken med noden sitt venstre barn
          }
      } // while

      return utskrift += "]";
  }
  
  public String høyreGren()
  {
      if(tom()) return("[]");
      String[] grener = grener();
      if (grener.length == 1) {return grener[0];}
      return grener[grener.length - 1];
  }
  
  public String lengstGren()
  {
      if (tom()) return("[]");
      String[] grener = grener();
      String lengstGren = grener[0];
      for(int i = 1; i<grener.length; i++){
          if(grener[i].length()>grener[i-1].length()) lengstGren = grener[i];
      }
      return lengstGren;
  }

  
  public String[] grener()
  {
      if(tom()){
          String[] tomListe;
          tomListe = new String[0];
          return tomListe;
      }
      ArrayList<Node<T>> bladNoder = new ArrayList<>();
      //finne bladnoder ved inorden traversering. Finner forste i inorden:
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
          if(bladNoder.size()==0){     //spesialtilfelle der treet har en gren, og forste i inorden er bladnoden.
              node = rot;
              while (node.venstre != null) node = node.venstre;
              bladNoder.add(node);
          }
      }

      String[] grener = new String[bladNoder.size()];
      int index = 0;

      //traverser opp via foreldreref. fra hver bladnode og legger i streng.
      for(Node<T> bladNode : bladNoder){
          StringBuilder s = new StringBuilder();
          s.append("[");
          while(true){
              s.insert(1, bladNode.verdi);
              if(bladNode.forelder!=null) {
                  bladNode = bladNode.forelder;
                  s.insert(1, ", ");
              }else break;
          }
          s.append("]");
          grener[index] = s.toString();
          index ++;
      }
      return grener;
  }
  private void getBladNoder(Node<T> node, StringBuilder s)
  {
      if(node==null) return;

      if(node.venstre==null && node.høyre==null){
          s.insert(0, node.verdi).insert(node.verdi.toString().length(),", ");  //legger til verdien i stringbuilder hvis noden er en bladnode
      }
      //kaller metoden igjen paa barna
      getBladNoder(node.høyre, s);
      getBladNoder(node.venstre, s);
  }
  public String bladnodeverdier()
  {
      if(tom()) return "[]";                    //tom liste skal returnere "[]"
      StringBuilder s = new StringBuilder();
      getBladNoder(rot,s);
      s.insert(0,"[").delete(s.length()-2, s.length()).insert(s.length(),"]");  //setter klammer rundt og sletter overflodige komma.
      return s.toString();
  }
  
  public String postString()
  {
      String utskrift = "[";
      Node<T> node = rot;
      if (node == null) return "[]";
      Stack<Node> stack1 = new Stack<Node>();
      Stack<Node> stack2 = new Stack<Node>();
      stack1.push(node);

      while (!stack1.isEmpty()){
          node = stack1.pop();
          stack2.push(node);
          if (node.venstre != null) stack1.push(node.venstre);
          if (node.høyre != null) stack1.push(node.høyre);
      }

      while (!stack2.isEmpty()) {
          if (utskrift != "[") {utskrift += ", ";}
          node = stack2.pop();
          //System.out.println(node.verdi);
          utskrift += node;
      }

      return utskrift + "]";
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
    
    private BladnodeIterator()  // konstruktor
    {
        if(antall > 1){
            while(p.venstre!=null){     //finner forste i inorden
                p = p.venstre;
            }
            while(true){                //traverserer inorden til forste bladnode er funnet.
                if(p.venstre!=null || p.høyre!=null){
                    p = nesteInorden(p);
                }else break;            //baade venstre og hoyre barn er null, da er p en bladnode.
            }
        }
    }
    
    @Override
    public boolean hasNext()
    {
      return p != null;  // Denne skal ikke endres!
    }
    
    @Override
    public T next() {
        if (iteratorendringer != endringer)
            throw new ConcurrentModificationException();
        if (tom() || p==null) throw new NoSuchElementException();
        q = p;
        while(true){
            p = nesteInorden(p);
            if(p == null) break;
            if(p.venstre==null && p.høyre==null){
                break;
            }
        }
        removeOK = true;
        return q.verdi;
    }
    
    @Override
    public void remove() {

        if(iteratorendringer!=endringer) throw new ConcurrentModificationException();
        if(!removeOK) throw new IllegalStateException();

        Node<T> t = rot; //pekeren t traverserer ned fra rot til den er lik q.

        while (t != null)            // leter etter q
        {
            int cmp = comp.compare(q.verdi,t.verdi);      // sammenligner
            if (cmp < 0) t = t.venstre;    // gaar til venstre
            else if (cmp > 0) t = t.høyre;   // gaar til hoyre
            else break;    // t = q
        }
        if(t==null) throw new NoSuchElementException();


        Node<T> b = t.venstre != null ? t.venstre : t.høyre;  // b for barn
        if (t == rot) {
            rot = b;
        }
        else if (t == t.forelder.venstre) t.forelder.venstre = b;
        else t.forelder.høyre = b;
        if(b != null) b.forelder = t.forelder;


        removeOK = false;
        antall--;   // det er naa én node mindre i treet
        iteratorendringer++;
        endringer ++;
    }
  } // BladnodeIterator
} // ObligSBinTre
