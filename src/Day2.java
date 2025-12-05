import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day2 {
    
    public static boolean isInvalid(long x) {
        int half = Long.toString(x).length() / 2; 
        long pow = (long)Math.pow(10, half);
        return x / pow == x % pow;
    }

    public static long sumOfInvalidIds(long low, long high) {
        long sum = 0;
        for (long i = low; i <= high; i++) {
            if(isInvalid(i))
                sum += i;
        }        
        // System.out.println("Sum of current range: " + sum);
        return sum;
    }

    public static List<Integer> possibleLengths(int l) {
        List<Integer> divisors = new ArrayList<>();
        for (int i = 1; i <= l/2; i++) {
            if(l % i == 0) divisors.add(i);
        }
        return divisors;
    }

    public static boolean isInvalidV2(long x) {
        int l = Long.toString(x).length(); 
        List<Integer> possibleLenghts = possibleLengths(l);
        String s = "" + x;
        for (int length : possibleLenghts) {
            String sub = s.substring(0, length);
            // System.out.println("Trying with the sub " + sub);
            
            String check = "";
            for (int i = 0; i < l / length; i++) {
                check += sub;
            }

            if(check.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static long sumOfInvalidIdsV2(long low, long high) {
        long sum = 0;
        for (long i = low; i <= high; i++) {
            if(isInvalidV2(i))
                sum += i;
        }        
        System.out.println("Sum of current range: " + sum);
        return sum;
    }




    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("./input.txt");
        Scanner in = new Scanner(inputFile);  

        String input = in.nextLine();
        in.close();
        String[] ranges = input.split(",");

        System.out.println(Arrays.toString(ranges));

        // Uso i long perché i numeri in input sono molto grandi
        List<Long> low = new ArrayList<>();
        List<Long> high = new ArrayList<>();

        for (String range : ranges) {
            // Prendi la prima parte
            String firstPart = range.substring(0,range.indexOf("-"));
            String secondPart = range.substring(firstPart.length() + 1);
            low.add(Long.parseLong(firstPart));
            high.add(Long.parseLong(secondPart));
        }
        long answerPart1 = 0;
        long answerPart2 = 0;
        for (int i = 0; i < low.size(); i++) {
            answerPart1 += sumOfInvalidIds(low.get(i), high.get(i));
            answerPart2 += sumOfInvalidIdsV2(low.get(i), high.get(i));
            // System.out.println(i + ": " + answerPart1);
            System.out.println(i + ": " + answerPart2);
        }
        
    }
}
