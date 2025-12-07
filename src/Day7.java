import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day7 {
    // Serve per la seconda parte, mi salvo i risultati intermedi
    public static long[][] memo;

    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);
        // Input
        List<char[]> lines = new ArrayList<>();
        List<char[]> copy = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine().toCharArray());
        }
        for (char[] c : lines) {
            copy.add(Arrays.copyOf(c, c.length));
        }
        List<Integer> tachyonsRays = new ArrayList<>();
        int origin = 0;
        for (int i = 0; i < lines.get(0).length; i++) {
            if (lines.get(0)[i] == 'S') {
                origin = i;
                tachyonsRays.add(i);
            }
        }
        int splitCount = 0;
        for (int height = 1; height < lines.size(); height++) {
            for (int x : tachyonsRays) {
                if (lines.get(height)[x] == '.') {
                    lines.get(height)[x] = '|';
                } else if (lines.get(height)[x] == '^') {
                    if (x > 0)
                        lines.get(height)[x - 1] = '|';
                    if (x < lines.get(0).length - 1)
                        lines.get(height)[x + 1] = '|';
                    splitCount++;
                }
            }
            tachyonsRays.clear();
            for (int i = 0; i < lines.get(0).length; i++) {
                if (lines.get(height)[i] == '|')
                    tachyonsRays.add(i);
            }
            // printMat(lines);
        }
        printMat(lines);

        System.out.println(splitCount);

        // Inizializzo memo, tutta a 0
        memo = new long[lines.size()][lines.get(0).length];
        System.out.println(numberOfWays(copy, 1, origin));

    }

    public static void printMat(List<char[]> lines) {
        for (char[] cs : lines) {
            for (char c : cs) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    // L'idea è ricorsiva. Il numero di strade possibili è 1 se sono arrivato alla
    // fine
    // Se non sono arrivato alla fine:
    // - Se è un '.' allora vado avanti, non cambio il numero di strade possibili
    // - Se è uno splitter, allora ritorno le strade possibili se la particella va a
    // sinistra + se va a destra
    // Visto che il numero di strade è veramente enorme, a volte capita che
    // "risolviamo lo stesso" sottoproblema
    // In questo caso usiamo una tecnica che si chiama "memoization"
    public static long numberOfWays(List<char[]> lines, int currentHeight, int tachyonPos) {
        // System.out.println("Day7.numberOfWays()");
        // printMat(lines);
        // System.out.println(currentHeight + "-" + tachyonPos);
        if (currentHeight == lines.size())
            return 1;
        if (memo[currentHeight][tachyonPos] != 0)
            return memo[currentHeight][tachyonPos];
        // System.out.println(lines.get(currentHeight)[tachyonPos]);
        if (lines.get(currentHeight)[tachyonPos] == '.') {
            return numberOfWays(lines, currentHeight + 1, tachyonPos);
        } else if (lines.get(currentHeight)[tachyonPos] == '^') {
            long tmp = 0;
            if (tachyonPos > 0)
                tmp += numberOfWays(lines, currentHeight + 1, tachyonPos - 1); // sinistra
            if (tachyonPos < lines.get(0).length - 1)
                tmp += numberOfWays(lines, currentHeight + 1, tachyonPos + 1); // destra
            memo[currentHeight][tachyonPos] = tmp;
            return tmp;
        }
        System.out.println("Shouldn't reach here");
        return -99999;
    }
}
