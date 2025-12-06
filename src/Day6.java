import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day6 {

    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);
        // Input
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine() + " ");
        }

        String[] operations = lines.getLast().split(" +");// Split prende una "regex", questa vuol dire "1 o più spazi"

        List<long[]> values = new ArrayList<>();
        for (int i = 0; i < lines.size() - 1; i++) {
            String[] rawValues = lines.get(i).split(" +");
            long[] rowOfValues = new long[operations.length];
            int cont = 0;
            for (String s : rawValues) {
                if (!s.isBlank()) {
                    rowOfValues[cont++] = Long.parseLong(s);
                }
            }
            values.add(rowOfValues);
        }
        long sum = 0;
        for (int i = 0; i < values.get(0).length; i++) { // i è un indice per le colonne
            String op = operations[i]; // L'operazione che devo fare
            long x = values.get(0)[i];
            for (int j = 1; j < values.size(); j++) {
                if (op.equals("+")) {
                    x += values.get(j)[i];
                } else {
                    x *= values.get(j)[i];
                }
            }
            sum += x;
        }
        System.out.println(sum);

        // Part 2
        lines.removeLast(); // rimuovo l'ultima riga
        List<String[]> parsed = parseLines(lines, operations.length);
        sum = 0;
        for (int i = 0; i < parsed.get(0).length; i++) { // i è un indice per le colonne
            List<Long> valuesV2 = extractValues(parsed, i);
            long tmp = calculate(valuesV2, operations[i]) ;
            System.out.println(tmp);
            sum += tmp;
            System.out.println("SUMMMM V2 " + sum);

        }

    }

    private static List<String[]> parseLines(List<String> lines, int numberOfColumns) {
        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            result.add(new String[numberOfColumns]);
        }

        List<Integer> separatorIndexes = new ArrayList<>();
        separatorIndexes.add(0);
        // For every "column" of characters

        for (int i = 0; i < lines.get(0).length(); i++) {
            int spacesCount = 0;
            for (int j = 0; j < lines.size(); j++) {
                spacesCount += (lines.get(j).charAt(i) == ' ') ? 1 : 0;
            }
            if(spacesCount == lines.size())
                separatorIndexes.add(i);
        }
        separatorIndexes.add(lines.get(0).length());


        System.out.println(separatorIndexes);
        int idx = 1;

        for (int i = 0; i < numberOfColumns; i++) {
            int from = separatorIndexes.get(idx - 1);
            int to = separatorIndexes.get(idx);
            // Step 1: find the idx of where to "cut the lines"
            // I can have N spaces, then N digits
            while(Character.isDigit(lines.get(0).charAt(to-1)) && lines.get(0).charAt(to) != ' ') {
                to++;
            }
            System.out.println("Breaking string from " +  from + " to " + to);
            for(int j = 0; j < lines.size(); j++) {
                System.out.println(lines.get(j).substring(from, to));

                result.get(j)[i] = lines.get(j).substring(from, to);
            }
            idx++;
        }
        return result;
    }

    private static List<Long> extractValues(List<String[]> values, int i) {
        List<String> v = new ArrayList<>();
        for (int j = 0; j < values.size(); j++) {
            v.add(values.get(j)[i]);
        }

        return verticalExtract(v);
    }

    private static List<Long> verticalExtract(List<String> v) {
        int l = findBiggestLength(v);
        List<Long> numbers = new ArrayList<>();
        System.out.println(v);
        for (int i = 0; i < l; i++) {
            String verticalS = "";
            for (String s : v) {
                if(s.charAt(i) != ' ') 
                    verticalS = verticalS + s.charAt(i); 
            }
            if(!verticalS.isBlank()) {
                System.out.println("Parsing " + verticalS);
                numbers.add(Long.parseLong(verticalS));
            }
            
        }
        return numbers;
    }


    private static int findBiggestLength(List<String> v) {
        int l = 0;
        for (String x : v) {
            l = Integer.max(l, x.length());
        }
        return l;
    }

    public static long calculate(List<Long> values, String operation) {
        long x = values.get(0);
        if (operation.equals("+")) {
            for (int i = 1; i < values.size(); i++) {
                x += values.get(i);
            }
        } else {
            for (int i = 1; i < values.size(); i++) {
                x *= values.get(i);
            }
        }
        return x;
    }

}
