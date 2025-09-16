package Lec13.WordNet;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;



public class Outcast {

  private WordNet wordnet;

  public Outcast(WordNet wordnet)         // constructor takes a WordNet object
  {
    this.wordnet = wordnet;
  }

  public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
  {
    String outcast = null;

    int max = 0;

    for(String noun1 : nouns) {
      int distance = 0;
      for(String noun2 : nouns) {
        if(!noun1.equals(noun2)) {
          distance += wordnet.distance(noun1, noun2);
        }
      }
      if(distance > max) {
        max = distance;
        outcast = noun1;
      }
    }
    return outcast;
  }

  public static void main(String[] args) {
    WordNet wordnet = new WordNet("src/Lec12/WordNet/synsets.txt", "src/Lec12/WordNet/hypernyms.txt");
    Outcast outcast = new Outcast(wordnet);
    while(!StdIn.isEmpty()) {
      String file = StdIn.readString();
      In in = new In(file);

      String[] nouns = in.readAllStrings();
      StdOut.println(file + ": " + outcast.outcast(nouns));
    }
  }  // see test client below
}