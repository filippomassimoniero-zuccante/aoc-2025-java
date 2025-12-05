import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day4 {

    public static char[][] deepClone(char[][] mat) {
        char[][] newMat = new char[mat.length][mat[0].length];

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                newMat[i][j] = mat[i][j];
            }
        }
        return newMat;

    }

    public static int countNeighbours(char[][] mat, int i, int j) {
        int cont = -1; // Conto sempre la posizione stessa
        for (int k = -1; k <= 1; k++) {
            for (int w = -1; w <= 1; w++) {
                if (i + k < mat.length && i + k >= 0 && j + w < mat[0].length && j + w >= 0) {
                    if (mat[i + k][j + w] == '@')
                        cont++;
                }
            }
        }
        return cont;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);
        // Input
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }

        char[][] mat = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            mat[i] = lines.get(i).toCharArray();
        }

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                System.out.print(mat[i][j]);
            }
            System.out.println();
        }

        // Part 1
        int cont = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (mat[i][j] == '@') {
                    int tmp = countNeighbours(mat, i, j);
                    if (tmp < 4) {
                        cont++;
                    }
                }
            }
        }

        System.out.println("Part 1 : " + cont);

        // Part 2
        int totRemoved = 0;
        while (cont > 0) {
            cont = 0;
            char[][] nextMat = deepClone(mat);
            for (int i = 0; i < mat.length; i++) {
                for (int j = 0; j < mat[0].length; j++) {
                    if (mat[i][j] == '@') {
                        int tmp = countNeighbours(mat, i, j);
                        if (tmp < 4) {
                            // I can remove it!
                            cont++;
                            nextMat[i][j] = 'x';
                        }
                    }
                }
            }
            mat = nextMat;
            totRemoved += cont;
            System.out.println("Removed " + cont);
        }
        System.out.println("Total removed : " + totRemoved);

    }
}
