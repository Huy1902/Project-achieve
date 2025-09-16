import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

public class UFClient2 {
  public static void main(String[] args) {
    int N = StdIn.readInt();
    UF uf = new UF(N);

    int count_pair = 0;
    boolean all_connected = true;

    while (!StdIn.isEmpty()) {
      int p = StdIn.readInt();
      int q = StdIn.readInt();
      if (!uf.connected(p, q)) {
        uf.union(p, q);
      }
      ++count_pair;
      all_connected = true;
      for(int i = 1; i < N - 1; ++i) {
        if(!uf.connected(0, i)) {
          all_connected = false;
          break;
        }
      }

      if(all_connected) {
        StdOut.println(count_pair);
        break;
      }
    }
    while (!StdIn.isEmpty()) {
      //nothing just take input
    }
    if(!all_connected) {
      StdOut.println("FAILED");
    }
  }
}