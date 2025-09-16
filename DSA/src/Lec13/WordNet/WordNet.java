package Lec13.WordNet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordNet {
  private Digraph digraph;
  private SAP sap;

  /**
   * noun and synset ids contain that noun
   */
  private Map<String, ArrayList<Integer>> nouns = new HashMap<String, ArrayList<Integer>>();

  /**
   * synset id and its list of nouns
   */
  private Map<Integer, String> synsets = new HashMap<>();

  /**
   * edge in graph
   */
  private Map<Integer, ArrayList<Integer>> hasHypernyms = new HashMap<>();

  private int length_of_graph = 0;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    processSynsets(synsets);
    processHypernums(hypernyms);

    this.digraph = new Digraph(length_of_graph);

    for(Map.Entry<Integer, ArrayList<Integer>> entry : hasHypernyms.entrySet()) {
      for (Integer hypernym : entry.getValue()) {
        //entry.getKey() is a hyponym
        this.digraph.addEdge(entry.getKey(), hypernym);
      }
    }
    // Construct graph done

    DirectedCycle dc = new DirectedCycle(digraph);
    if(dc.hasCycle()) {
      throw new IllegalArgumentException("Cycle found");
    }
    // Check cycle done

    int rooted = 0;
    for(int i = 0; i < this.digraph.V(); ++i) {
      // to understand this, check diagram about what a WordNet like
      if(!this.digraph.adj(i).iterator().hasNext()) {
        ++rooted;
      }
    }
    if(rooted !=  1) {
      throw new IllegalArgumentException("Rooted vertex does not exist");
    }
    // Check root done

    sap = new SAP(digraph);
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return this.nouns.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    return this.nouns.containsKey(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if(!this.nouns.containsKey(nounA) || !this.nouns.containsKey(nounB)) {
      throw new IllegalArgumentException("Noun does not exist");
    }

    return sap.length(nouns.get(nounA), nouns.get(nounB));
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
      if(!this.nouns.containsKey(nounA) || !this.nouns.containsKey(nounB)) {
        throw new IllegalArgumentException("Noun does not exist");
      }

      int ancestor = this.sap.ancestor(nouns.get(nounA), nouns.get(nounB));

      return synsets.get(ancestor);
  }

  private void processSynsets(String synsets) {
    In input = new In(synsets);
    String line = null;

    ArrayList<Integer> currentNounsList = null;
    String currentSynsetNouns = null;

    while((line = input.readLine()) != null) {
      if(line.equals("")) {
        continue;
      }

      String[] lineElements = line.split(",");
      int synsetID = Integer.parseInt(lineElements[0]);
      String[] nounsElements = lineElements[1].split(" ");

      for(String noun : nounsElements) {
        currentNounsList = this.nouns.getOrDefault(noun, new ArrayList<>());

        currentNounsList.add(synsetID);
        currentSynsetNouns = lineElements[1];

        this.nouns.put(noun, currentNounsList);
        this.synsets.put(synsetID, currentSynsetNouns);
      }
      ++this.length_of_graph;
    }
  }

  private void processHypernums(String hypernyms) {
    In input = new In(hypernyms);
    String line = null;
    ArrayList<Integer> hypernymsList;

    while((line = input.readLine()) != null) {
      if(line.equals("")) {
        continue;
      }

      String[] lineElements = line.split(",");
      hypernymsList = new ArrayList<>();
      Integer hyponym = Integer.parseInt(lineElements[0]);

      for(int i = 1; i < lineElements.length; i++) {
        hypernymsList.add(Integer.parseInt(lineElements[i]));
      }
//      System.out.println(hyponym);
      this.hasHypernyms.put(hyponym, hypernymsList);
    }
  }

  // do unit testing of this class
  public static void main(String[] args) {
    WordNet wordNet = new WordNet("src/Lec12/WordNet/synsets.txt", "src/Lec12/WordNet/hypernyms.txt");

    System.out.println(wordNet.sap("Rameses_the_Great", "Henry_Valentine_Miller"));
    System.out.println(wordNet.distance("Rameses_the_Great", "Henry_Valentine_Miller"));
  }
}
