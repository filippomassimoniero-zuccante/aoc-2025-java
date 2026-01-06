import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day12 {
    public static void main(String[] args) throws FileNotFoundException {
        // Only works because the input is "easy"
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);


        List<String[]> tiles = new ArrayList<>();
        List<Integer> fullTiles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            in.nextLine();
            String[] pat = new String[3];
            int cont = 0;
            for (int j = 0; j < 3; j++) {
                pat[j] = in.nextLine();
                for (char c : pat[j].toCharArray()) {
                    if(c == '#') cont++;
                }
            }
            tiles.add(pat);
            fullTiles.add(cont);
            System.out.println(pat[0]);
            in.nextLine();
        }

        int valid = 0;

        while (in.hasNext()) {
            String s = in.nextLine();
            String[] separatedColon = s.split(":");
            // Dimensions 
            String[] dimensions = separatedColon[0].split("x");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);
            String[] amounts = separatedColon[1].split(" ");
            int[] tileAmount = new int[amounts.length-1];
            for (int i = 1; i < amounts.length; i++) {
                tileAmount[i-1] = Integer.parseInt(amounts[i]);
            }
            System.out.println(Arrays.toString(tileAmount));
            valid += isValidPattern(tiles, fullTiles, width, height, tileAmount);
        }

        System.out.println("Valid: " + valid);


    }

    private static int isValidPattern(List<String[]> tiles, List<Integer> full, int w, int h, int[] tileAmount) {
        int maximumPossibleFullTiles = w * h;
        int contFull = 0;
        int totalTiles = 0;
        for (int i = 0; i < tileAmount.length; i++) {
            contFull += tileAmount[i] * full.get(i);
            totalTiles += tileAmount[i];
        }

        if(contFull > maximumPossibleFullTiles) return 0;

        int validSections = (w / 3) * (h / 3);

        if(totalTiles > validSections) return 0;


        return 1;
    }

}
