package Lec4;

import java.util.*;

public class SimpleTextEditor {
  private class Operation {
    int type = 0;
    String delete = "" ;
    int numChar = 0;
    Operation(int type, String delete, int numChar) {
      this.type = type;
      this.delete = delete;
      this.numChar = numChar;
    }
  }
  public class Editor {
    StringBuilder s = new StringBuilder();
    Stack<Operation> stack = new Stack<>();
    public void append(String add, boolean isUpon){
      s.append(add);
      if(!isUpon) {
        stack.push(new Operation(1, "", add.length()));
      }
    }
    public void delete(int k, boolean isUpon) {
      if(!isUpon) {
        stack.push(new Operation(2, s.substring(s.length() - k), 0));
      }
      s.delete(s.length() - k, s.length());
    }
    public void print(int k) {
      System.out.println(s.charAt(k - 1));
    }
    public void undo() {
      if(stack.isEmpty()) {
        return;
      }
      Operation prev = stack.peek();
      stack.pop();
      if(prev.type == 1) {
        delete(prev.numChar, true);
      } else {
        append(prev.delete, true);
      }
    }
  }
  public static void main(String[] args) {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    Scanner scanner = new Scanner(System.in);
    int query = scanner.nextInt();
    SimpleTextEditor solution = new SimpleTextEditor();
    SimpleTextEditor.Editor editor = solution.new Editor();
    for(int i = 0; i < query; ++i) {
      int type = scanner.nextInt();
      switch(type) {
        case 1:
        {
          String add = scanner.next();
          editor.append(add, false);
          break;
        }
        case 2: {
          int k = scanner.nextInt();
          editor.delete(k, false);
          break;
        }
        case 3:
        {
          int k = scanner.nextInt();
          editor.print(k);
          break;
        }
        case 4: {
          editor.undo();
          break;
        }
      }
    }
    scanner.close();
  }
}