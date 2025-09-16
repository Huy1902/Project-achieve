package Lec14.seam_carving;/* *****************************************************************************
 *  Compilation:  javac-algs4 ShowEnergy.java
 *  Execution:    java-algs4 ShowEnergy input.png
 *  Dependencies: SeamCarver.java SCUtility.java
 *                
 *  Read image from file specified as command-line argument. Show original
 *  image (useful only if image is large enough).
 *
 *  % java-algs4 ShowEnergy HJoceanSmall.png
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;

public class ShowEnergy {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Picture picture = new Picture(scanner.nextLine());
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);

    }

}
