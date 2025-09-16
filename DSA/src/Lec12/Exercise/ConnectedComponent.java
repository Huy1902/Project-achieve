package Lec12.Exercise;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConnectedComponent {
  private static class Graph {
    private final int V;
    private int E;
    private List<Integer>[] adj;

    /**
     * Construct graph have vertices (numbered from 1 to V)
     *
     * @param V number of vertices
     */
    public Graph(int V) {
      this.V = V;
      this.E = 0;
      adj = new List[V + 1];
      for(int i = 1; i <= V; i++) {
        adj[i] = new ArrayList<>();
      }
    }

    public void addEdge(int v, int w) {
      adj[v].add(w);
      adj[w].add(v);
      this.E++;
    }

    public Iterable<Integer> adj(int v) {
      return adj[v];
    }

    public int degree(int v) {
      return adj[v].size();
    }
  }
  public static void dfs(Graph graph, int v, List<Boolean> marked) {
    for(int w : graph.adj(v)) {
      if(!marked.get(w)) {
        marked.set(w, true);
        dfs(graph, w, marked);
      }
    }
  }

  public static void main(String[] args) {
    int n, m;
    Scanner scanner = new Scanner(System.in);
    n = scanner.nextInt();
    m = scanner.nextInt();
    Graph graph = new Graph(n);
    for(int i = 0; i < m; i++) {
      graph.addEdge(scanner.nextInt(), scanner.nextInt());
    }
    // Construct graph done

    List<Boolean> marked = new ArrayList<>();
    for(int i = 0; i <= n; i++) {
      marked.add(false);
    }
    int count_connected_components = 0;
    for(int i = 1; i <= n; i++) {
      if(!marked.get(i)) {
        ++count_connected_components;
        dfs(graph, i, marked);
      }
    }

    System.out.println(count_connected_components);
  }
}
