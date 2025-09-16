package Lec14.seam_carving;/* *****************************************************************************
 *  Compilation:  javac-algs4 PrintEnergy.java
 *  Execution:    java-algs4 PrintEnergy input.png
 *  Dependencies: SeamCarver.java
 *                
 *
 *  Read image from file specified as command line argument.
 *  Print energy of each pixel as calculated by SeamCarver object. 
 * 
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;

public class PrintEnergy {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Picture picture = new Picture(scanner.nextLine());
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");        

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%7.2f ", sc.energy(col, row));
            StdOut.println();
        }
    }

}
