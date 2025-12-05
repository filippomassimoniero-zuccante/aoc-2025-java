import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day5 {

    public static boolean isFresh(long x, List<Long> low, List<Long> high) {
        for (int i = 0; i < low.size(); i++) {
            if (low.get(i) <= x && x <= high.get(i))
                return true;
        }
        return false;
    }

    public static int indexOfMinimum(List<Long> list) {
        long minimumFound = Long.MAX_VALUE;
        int minimumIdx = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < minimumFound) {
                minimumFound = list.get(i);
                minimumIdx = i;
            }
        }
        return minimumIdx;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);
        // Input
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        int rangesLength = 0;
        rangesLength = lines.indexOf("");
        // while(!lines.get(rangesLength++).equals(""));

        List<String> ranges = lines.subList(0, rangesLength);
        List<String> ids = lines.subList(rangesLength + 1, lines.size());

        List<Long> low = new ArrayList<>();
        List<Long> high = new ArrayList<>();
        for (String range : ranges) {
            String firstPart = range.substring(0, range.indexOf("-"));
            String secondPart = range.substring(firstPart.length() + 1);
            low.add(Long.parseLong(firstPart));
            high.add(Long.parseLong(secondPart));

            System.out.println(range);
        }

        int cont = 0;
        for (String s : ids) {
            long x = Long.parseLong(s);
            if (isFresh(x, low, high)) {
                cont++;
            }
        }
        System.out.println("Fresh ingredients: " + cont);

        // Parte 2
        // In teoria dovrei ordinare i range
        // In alternativa... Prendo il range più a sinistra
        long countOfRanges = 0;

        while (low.size() > 1) {
            int smallestIdx = indexOfMinimum(low);
            long currentLow = low.get(smallestIdx);
            long currentHigh = high.get(smallestIdx);

            low.remove(smallestIdx);
            high.remove(smallestIdx);

            int secondSmallestIdx = indexOfMinimum(low);
            long secondLow = low.get(secondSmallestIdx);
            long secondHigh = high.get(secondSmallestIdx);

            

            // I range si sovrapponono?
            if(secondLow <= currentHigh) {
                low.remove(secondSmallestIdx);
                high.remove(secondSmallestIdx);

                low.add(currentLow);
                high.add(Long.max(currentHigh, secondHigh));
            } else {
                countOfRanges += currentHigh - currentLow + 1;
            }

            System.out.println(countOfRanges);
        }

        countOfRanges += high.get(0) - low.get(0) + 1;
        System.out.println(countOfRanges);

    }

}
