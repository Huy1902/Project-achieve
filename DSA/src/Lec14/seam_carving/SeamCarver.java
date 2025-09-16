package Lec14.seam_carving;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SeamCarver {

  private Picture picture;

  private static final double BORDER_ENERGY = 1000d;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    if (picture == null) {
      throw new IllegalArgumentException("Picture cannot be null");
    }
    this.picture = new Picture(picture);
  }

  private boolean validateX(int x) {
    return x >= 0 && x < picture.width();
  }

  private boolean validateY(int y) {
    return y >= 0 && y < picture.height();
  }

  private boolean validate(int x, int y) {
    return validateX(x) && validateY(y);
  }

  // current picture
  public Picture picture() {
    return new Picture(picture);
  }

  // width of current picture
  public int width() {
    return picture.width();
  }

  // height of current picture
  public int height() {
    return picture.height();
  }

  private double square_diff(double num1, double num2) {
    return (num1 - num2) * (num1 - num2);
  }

  private double gradient(Color color1, Color color2) {
    return square_diff(color1.getRed(), color2.getRed()) + square_diff(color1.getGreen(), color2.getGreen())
            + square_diff(color1.getBlue(), color2.getBlue());
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    if (!validate(x, y)) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    if (x == 0 || y == 0 || x == picture.width() - 1 || y == picture.height() - 1) {
      return BORDER_ENERGY;
    }

    double gradient_x = gradient(picture.get(x - 1, y), picture.get(x + 1, y));
    double gradient_y = gradient(picture.get(x, y - 1), picture.get(x, y + 1));

    return Math.sqrt(gradient_x + gradient_y);
  }

  private int encodeCoordinate(int x, int y) {
    return y * width() + x;
  }

  private Coordinate decodeCoordinate(int code) {
    return new Coordinate(code % width(), code / width());
  }

  private static class Coordinate {
    public int x;
    public int y;
    public int code;

    public Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  private java.util.List<java.util.List<Double>> energyTable() {
    java.util.List<List<Double>> energyTable = new ArrayList<>();
    for (int y = 0; y < height(); ++y) {
      energyTable.add(new ArrayList<>());
      for (int x = 0; x < width(); ++x) {
        energyTable.get(y).add(energy(x, y));
      }
    }
    return energyTable;
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    int start = width() * height() + 1;
    int end = width() * height() + 2;
    EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(end + 1);

    List<List<Double>> energyTable = energyTable();
//    for (int row = 0; row < height(); row++) {
//      for (int col = 0; col < width(); col++)
//        StdOut.printf("%7.2f ", energyTable.get(row).get(col));
//      StdOut.println();
//    }
    for (int y = 0; y < height(); ++y) {
      for (int x = 0; x < width(); ++x) {
        if (0 <= x && x < width() - 1) {
          if (validate(x + 1, y)) {
            edgeWeightedDigraph.addEdge(new DirectedEdge(encodeCoordinate(x, y),
                    encodeCoordinate(x + 1, y), energyTable.get(y).get(x + 1)));
          }
          if (validate(x + 1, y + 1)) {
            edgeWeightedDigraph.addEdge(new DirectedEdge(encodeCoordinate(x, y),
                    encodeCoordinate(x + 1, y + 1), energyTable.get(y + 1).get(x + 1)));
          }
          if (validate(x + 1, y - 1)) {
            edgeWeightedDigraph.addEdge(new DirectedEdge(encodeCoordinate(x, y),
                    encodeCoordinate(x + 1, y - 1), energyTable.get(y - 1).get(x + 1)));
          }
        }
        if (x == 0) {
          edgeWeightedDigraph.addEdge(new DirectedEdge(start, encodeCoordinate(x, y), energyTable.get(y).get(x)));
        }
        if (x == width() - 1) {
          edgeWeightedDigraph.addEdge(new DirectedEdge(encodeCoordinate(x, y), end, energyTable.get(y).get(x)));
        }
      }
    }

    DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, start);
    Iterable<DirectedEdge> iterable = dijkstraSP.pathTo(end);

    int[] result = new int[width()];
    int index = 0;
    for (DirectedEdge edge : iterable) {
      if (edge.to() != end) {
        result[index++] = decodeCoordinate(edge.to()).y;
      }
    }
    return result;

  }


  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    int start = width() * height() + 1;
    int end = width() * height() + 2;
    EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(end + 1);

    List<List<Double>> energyTable = energyTable();
//    for (int row = 0; row < height(); row++) {
//      for (int col = 0; col < width(); col++)
//        StdOut.printf("%7.2f ", energyTable.get(row).get(col));
//      StdOut.println();
//    }

    for (int x = 0; x < width(); x++) {
      for (int y = 0; y < height(); y++) {
        if (0 <= y && y < height() - 1) {
          if (validate(x, y + 1)) {
            edgeWeightedDigraph.addEdge(new DirectedEdge(encodeCoordinate(x, y), encodeCoordinate(x, y + 1), energyTable.get(y + 1).get(x)));
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(encodeCoordinate(x, y)).append(" ").append(encodeCoordinate(x, y + 1));
//            System.out.println(stringBuilder.toString());
          }
          if (validate(x - 1, y + 1)) {
            edgeWeightedDigraph.addEdge(new DirectedEdge(encodeCoordinate(x, y), encodeCoordinate(x - 1, y + 1), energyTable.get(y + 1).get(x - 1)));
          }
          if (validate(x + 1, y + 1)) {
            edgeWeightedDigraph.addEdge(new DirectedEdge(encodeCoordinate(x, y), encodeCoordinate(x + 1, y + 1), energyTable.get(y + 1).get(x + 1)));
          }
        }
        if (y == 0) {
          edgeWeightedDigraph.addEdge(new DirectedEdge(start, encodeCoordinate(x, y), energyTable.get(y).get(x)));
        }
        if (y == height() - 1) {
          edgeWeightedDigraph.addEdge(new DirectedEdge(encodeCoordinate(x, y), end, energyTable.get(y).get(x)));
        }
      }
    }

    DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, start);
    Iterable<DirectedEdge> iterable = dijkstraSP.pathTo(end);

    int[] result = new int[height()];
    int index = 0;
    for (DirectedEdge edge : iterable) {
      if (edge.to() != end) {
        result[index++] = decodeCoordinate(edge.to()).x;
      }
    }
    return result;
  }

  private void validateSeam(int[] seam, boolean isVertical) {
    if (seam == null) {
      throw new IllegalArgumentException("null");
    }

    if ((isVertical && seam.length != picture.height())
            || (!isVertical && seam.length != picture.width())) {
      throw new IllegalArgumentException("length");
    }

    for (int i = 0, range = seam.length - 1; i < range; i++) {
      if (Math.abs(seam[i] - seam[i + 1]) > 1) {
        throw new IllegalArgumentException("abs");
      }
    }

    for(int i = 0; i < seam.length; ++i) {
      int pixel = seam[i];
      if ((isVertical && !validateX(pixel))
              || (!isVertical && !validateY(pixel)) ) {
        throw new IllegalArgumentException("validateXY: " + pixel);
      }
    }
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    validateSeam(seam, false);

    Picture seamedPicture = new Picture(width(), height() - 1);

    for (int x = 0; x < picture.width(); ++x) {
      int skipRow = 0;
      for (int y = 0; y < picture.height() - 1; ++y) {
        if (seam[x] == y) {
          skipRow = 1;
        }
        seamedPicture.set(x, y, picture.get(x, y + skipRow));
      }
    }
    picture = seamedPicture;
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    validateSeam(seam, true);

    Picture seamedPicture = new Picture(width() - 1, height());

    for (int y = 0; y < picture.height(); ++y) {
      int skipCol = 0;
      for (int x = 0; x < picture.width() - 1; ++x) {
        if (seam[y] == x) {
          skipCol = 1;
        }
        seamedPicture.set(x, y, picture.get(x + skipCol, y));
      }
    }

    picture = seamedPicture;
  }

}