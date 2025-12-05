import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day3 {
    private static int getMaxJoltage(String s) {
        int maxJoltage = 0;
        for (int i = 0; i < s.length(); i++) {
            for (int j = i+1; j < s.length(); j++) {
                int x = s.charAt(i) - '0';
                int y = s.charAt(j) - '0';
                int joltage = x * 10 + y;
                maxJoltage = Integer.max(maxJoltage, joltage);
            }        
        }
        return maxJoltage;
    }   

    private static boolean exist(String s, int targetDigit, int remainingDigits) {
        int idx = s.indexOf(targetDigit + '0');
        
        if (idx == -1 || idx + remainingDigits > s.length()) {
            return false;
        }

        return true;
    }

    private static long pow10(int remainingDigits) {
        long x = 1;
        for (int i = 1; i < remainingDigits; i++) {
            x *= 10;
        }
        return x;
    }
    private static long maxJoltageOf(String s, int remainingDigits) {
        if(remainingDigits == 0) return 0;
        for (int targetDigit = 9; targetDigit >= 0; targetDigit--) {
            if(exist(s, targetDigit, remainingDigits)) {
                int idx = s.indexOf(targetDigit + '0');
                long result = pow10(remainingDigits) * targetDigit;
                if(remainingDigits > 1) result += maxJoltageOf(s.substring(idx+1), remainingDigits-1)  ;
                return result;
            }
        }      
        return -1;
    }


    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);    
        // Input
        List<String> lines = new ArrayList<>();
        int cont = 0;
        while(in.hasNextLine()) {
            lines.add(in.nextLine());
        }

        int joltageSum = 0;
        long joltageSumV2 = 0;
        for (String s : lines) {
            joltageSum += getMaxJoltage(s);
            long tmp = maxJoltageOf(s,12);
            System.out.println(s + "-> " + tmp);
            joltageSumV2 += tmp;
        }

        System.out.println("Part 1: " + joltageSum);
        System.out.println("Part 2: " + joltageSumV2);
     

    }
}
