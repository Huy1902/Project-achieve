import edu.princeton.cs.algs4.*;
import java.util.Hashtable;

class TwoSum {

  public static void main(String[] args) {
    In in = new In("C:\\Users\\lymst\\IdeaProjects\\DSA\\8Kints.txt"); // tạo luồng đọc từ file
    int[] a = in.readAllInts();  // đọc toàn bộ file vào mảng a
    // xử lý dữ liệu trong mảng a
    for(int i = 0; i < a.length; ++i) {
      for(int j = i + 1; j < a.length; ++j) {
        if( (a[i] + a[j]) == 0) {
          System.out.printf("%d %d \n", a[i], a[j]);
        }
      }
    }
    //StdArrayIO.print(a); // in mảng ra màn hình
  }
}