import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day11 {
    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);

        List<String> keys = new ArrayList<>();
        List<Integer> nodes = new ArrayList<>();

        List<List<Integer>> adj = new ArrayList<>(); // adjacency list

        List<String[]> inputWords = new ArrayList<>();
        int first = 0, last;
        while (in.hasNextLine()) {
            String s = in.nextLine();
            String[] words = s.split(" ");

            String from = words[0].substring(0, words[0].length() - 1);

            keys.add(from);
            nodes.add(keys.size());

            // if (from.equals("you")) // part 1
            if (from.equals("svr"))
                first = keys.size();

            inputWords.add(words);
        }
        keys.add("out");
        last = keys.size();

        nodes.add(last);

        for(int k = 0; k < inputWords.size(); k++) {
            String[] words = inputWords.get(k);
            List<Integer> destinations = new ArrayList<>();
            for (int i = 1; i < words.length; i++) {
                // look for the correct node
                for (int j = 0; j < keys.size(); j++) {
                    if (keys.get(j).equals(words[i])) {
                        destinations.add(nodes.get(j));
                        break;
                    }
                }

            }
            adj.add(destinations);
            System.out.println(destinations);
        }

        List<List<Long>> memo = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            List<Long> tmp = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                tmp.add(-1L);
            }
            memo.add(tmp);
        }
        // System.out.println("First part:" + dfs(adj, first, last));
        System.out.println("Second part: " + secondDfs(adj, first, last, keys, false, false, memo));

    }


    public static long secondDfs(List<List<Integer>> adj, int node, int last, List<String> keys, boolean dacOk, boolean fftOk, List<List<Long>> memo) {
        if(node == last) return (dacOk && fftOk) ? 1 : 0;

        int idx = (dacOk) ? 1 : 0;
        idx += (fftOk) ? 2 : 0;
        
        long value = memo.get(node).get(idx);
        if(value != -1) return value;

        if(keys.get(node - 1).equals("fft")) {
            fftOk = true;
        } 
        if(keys.get(node - 1).equals("dac")) {
            dacOk = true;
        } 
        long cont = 0;
        for(int x : adj.get(node-1)) {
            cont += secondDfs(adj, x, last, keys, dacOk, fftOk, memo);
        }
        System.out.println("Visited " + node);
        memo.get(node).set(idx, cont);

        return cont;
    }


    public static int dfs(List<List<Integer>> adj, int node, int last) {
        // System.out.println("Visiting " + node);
        if(node == last) return 1;

        int cont = 0;
        for(int x : adj.get(node-1)) {
            cont += dfs(adj, x, last);
        }
        return cont;
    }
}
