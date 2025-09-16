package Lec8.JavaPriorityQueue;
import java.rmi.StubNotFoundException;
import java.util.*;

/*
 * Create the Student and Priorities classes here.
 */
class StudentCompare implements Comparator<Student> {

  @Override
  public int compare(Student o1, Student o2) {

    if (o1.cgpa != o2.cgpa) {
      return Double.compare(o2.cgpa, o1.cgpa);
    } else if (!o1.name.equals(o2.name)) {
      return o1.name.compareTo(o2.name);
    } else {
      return Integer.compare(o1.id, o2.id);
    }
  }
}
class Priorities {
  private PriorityQueue<Student> students = new PriorityQueue<>(1000, new StudentCompare());
  public List<Student> getStudents(List<String> events) {
    for(String event: events) {
      String[] parts = event.split(" ");
      if(parts[0].equals("ENTER")) {
        students.add(new Student(Integer.parseInt(parts[3]), parts[1], Double.parseDouble(parts[2])));
      } else {
        students.poll();
      }
    }
    List<Student> ans = new ArrayList<>();
    while (!students.isEmpty()) {
      ans.add(students.poll());
    }
    return ans;
  }

}
class Student {
  int id;
  String name;
  double cgpa;
  Student(int id, String name, double cgpa) {
    this.id = id;
    this.name = name;
    this.cgpa = cgpa;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getCgpa() {
    return cgpa;
  }
}
public class Solution {

  private final static Scanner scan = new Scanner(System.in);

  private final static Priorities priorities = new Priorities();

  public static void main(String[] args) {
    int totalEvents = Integer.parseInt(scan.nextLine());
    List<String> events = new ArrayList<>();

    while (totalEvents-- != 0) {
      String event = scan.nextLine();
      events.add(event);
    }

    List<Student> students = priorities.getStudents(events);

    if (students.isEmpty()) {
      System.out.println("EMPTY");
    } else {
      for (Student st: students) {
        System.out.println(st.getName());
      }
    }
  }
}