import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class SimpleMap {
    private List<List<Long>> keys;
    private List<List<Integer>> values;
    private int maxElementValue;
    public static int BUCKETS = 10000;

    public SimpleMap(int maxElementValue) {
        this.maxElementValue = maxElementValue;
        values = new ArrayList<>();
        keys = new ArrayList<>();

        for (int i = 0; i < BUCKETS; i++) {
            keys.add(new ArrayList<>());
            values.add(new ArrayList<>());
        }
    }

    public Integer getValue(int[] completeKey) {
        int bucket = bucketIndex(completeKey);
        List<Long> bucketKeys = keys.get(bucket);
        List<Integer> bucketValues = values.get(bucket);

        if (bucketKeys.isEmpty()) {
            return -1;
        }

        long longKey = intArrayToLongKey(completeKey);

        for (int i = 0; i < bucketKeys.size(); i++) {
            if (bucketKeys.get(i).equals(longKey)) {
                return bucketValues.get(i);
            }
        }

        return -1;
    }

    public void putValue(int[] key, int value) {
        int bucket = bucketIndex(key);
        keys.get(bucket).add(intArrayToLongKey(key));
        values.get(bucket).add(value);
    }

    private int bucketIndex(int[] key) {
        int hash = Arrays.hashCode(key);
        return (hash & 0x7fffffff) % BUCKETS;
    }

    private long intArrayToLongKey(int[] arr) {
        long key = 0;
        long base = maxElementValue + 1L;

        for (int v : arr) {
            key = key * base + v;
        }

        return key;
    }
    
    

}
public class Day10 {

    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);

        int totalPart1 = 0;
        int totalPart2 = 0;

        while (in.hasNextLine()) {
            String[] splitted = in.nextLine().split(" ");

            // Lights
            String stringTarget = splitted[0];
            boolean[] lightsTarget = new boolean[stringTarget.length() - 2];
            for (int i = 1; i < stringTarget.length() - 1; i++) {
                lightsTarget[i - 1] = stringTarget.charAt(i) == '#';
            }
            
            // Buttons
            List<List<Integer>> buttons = new ArrayList<>();
            for (int i = 1; i < splitted.length - 1; i++) {
                String button = splitted[i].substring(1, splitted[i].length() - 1);
                String[] values = button.split(",");
                List<Integer> b = new ArrayList<>();
                for (String v : values) {
                    b.add(Integer.parseInt(v));
                }
                buttons.add(b);
            }
            
            // Joltage
            String stringJoltage = splitted[splitted.length - 1];
            String[] joltageStrings = stringJoltage.substring(1, stringJoltage.length() - 1).split(",");
            int[] joltageTarget = new int[joltageStrings.length];
            int maxJoltageTarget = Integer.MIN_VALUE;
            for (int i = 0; i < joltageStrings.length; i++) {
                joltageTarget[i] = Integer.parseInt(joltageStrings[i]);
                maxJoltageTarget = Math.max(maxJoltageTarget, joltageTarget[i]);
            }
            SimpleMap map = new SimpleMap(maxJoltageTarget);
            // Part 1
            totalPart1 += solvePart1(lightsTarget, buttons);
            totalPart2 += solvePart2(joltageTarget, buttons, map);
            System.out.println(totalPart2);
        }

        System.out.println("Total Part 1: " + totalPart1);
        System.out.println("Total Part 2: " + totalPart2);
    }

    // Part 1: BFS 
    public static int solvePart1(boolean[] target, List<List<Integer>> buttons) {
        List<boolean[]> queue = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        
        boolean[] initial = new boolean[target.length];
        queue.add(initial);
        depths.add(0);
        
        int idx = 0;
        while (idx < queue.size()) {
            boolean[] current = queue.get(idx);
            int depth = depths.get(idx);
            idx++;
            
            if (Arrays.equals(current, target)) {
                return depth;
            }
            
            for (List<Integer> button : buttons) {
                boolean[] next = Arrays.copyOf(current, current.length);
                for (int light : button) {
                    next[light] = !next[light];
                }
                queue.add(next);
                depths.add(depth + 1);
            }
        }
        
        return -1; // Shouldn't reach here
    }

    public static int solvePart2(int[] joltageTarget, List<List<Integer>> buttons, SimpleMap memo) {
        // Base case: all targets are 0
        boolean allZero = true;
        for (int x : joltageTarget) {
            if (x != 0) {
                allZero = false;
                break;
            }
        }

        if (allZero) return 0;

        int memoValue = memo.getValue(joltageTarget);
        if( memoValue != -1) return memoValue; 

        // All possible combinations to get the correct "parity" 

        int minPresses = Integer.MAX_VALUE;
        
        // Try all 2^B possible combinations of buttons
        for (int mask = 0; mask < Math.pow(2, buttons.size()); mask++) {
            // Calculate what the joltages would be after pressing this combination
            int[] newJoltage = new int[joltageTarget.length];
            int buttonPresses = 0;
            
            for (int i = 0; i < buttons.size(); i++) {
                if ((mask & (1 << i)) != 0) { // bitmask
                    buttonPresses++;
                    for (int counter : buttons.get(i)) {
                        newJoltage[counter]++;
                    }
                }
            }

            if (!isArrayOkForSecondPart(newJoltage, joltageTarget)) continue; // not valid, skip to next bitmask
            
            int[] halfRemaining = new int[joltageTarget.length];
            for (int i = 0; i < joltageTarget.length; i++) {
                halfRemaining[i] = (joltageTarget[i] - newJoltage[i]) / 2;
            }

            int result = solvePart2(halfRemaining, buttons, memo);
            if(result != Integer.MAX_VALUE) // Might not be solvable
                minPresses = Math.min(minPresses, result * 2 + buttonPresses);
        }

        memo.putValue(joltageTarget, minPresses);

        return minPresses;
    }

    public static boolean isArrayOkForSecondPart(int[] newJoltage, int[] joltageTarget) {
        for (int i = 0; i < newJoltage.length; i++) {
            int diff = joltageTarget[i] - newJoltage[i];
            if( diff < 0 || diff % 2 == 1) return false;
        }

        return true;
    }

    public static int arrayToMemoIdx(int[] arr, int maxValue) {
        int value = 0;
        for (int i = 0; i < arr.length; i++) {
            value = value * (maxValue + 1) + arr[i]; 
        }
        return value;
    }

    
}