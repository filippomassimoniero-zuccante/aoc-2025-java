    import java.io.File;
    import java.io.FileNotFoundException;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;
    import java.util.Scanner;

    class Point3D {
        private final long x,y,z;

        

        public Point3D(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public long getX() {
            return x;
        }

        public long getY() {
            return y;
        }

        public long getZ() {
            return z;
        }

        public long getDistance(Point3D point) {
            return  (point.x-x)*(point.x-x)+(point.y-y)*(point.y-y)+(point.z-z)*(point.z-z);
        }

    }
    public class Day8 {

        public static void printMat(int[][] lines) {
            for (int[] cs : lines) {
                for (int c : cs) {
                    System.out.print(c + " ");
                }
                System.out.println();
            }
        }

        public static int[] findSmallestDistance(long[][] dist) {
            int[] res = new int[2];
            long minimumFound = Integer.MAX_VALUE;
            int minimumI = -1;
            int minimumJ = -1;

            for (int i = 0; i < dist.length; i++) {
                for(int j = i+1; j < dist.length; j++) {
                    if(dist[i][j] < minimumFound) {
                        minimumFound = dist[i][j];
                        minimumI = i;
                        minimumJ = j;
                    }
                }   
            }

            res[0] = minimumI;
            res[1] = minimumJ;
            return res;
        } 

        public static boolean isAllSameCircuit(int[] circuits) {
            int firstCircuit = circuits[0];
            if(firstCircuit == Integer.MAX_VALUE) return false;

            for (int circuit : circuits) {
                if (circuit != firstCircuit) {
                    return false;
                }
            }
            
            return true;
        }
        
        public static void main(String[] args) throws FileNotFoundException {
            File myFile = new File("./input.txt");
            Scanner in = new Scanner(myFile);
            // Input
            List<Point3D> points = new ArrayList<>();
            while (in.hasNextLine()) {
                String s = in.nextLine(); 
                System.out.println(s);
                String[] splitted = s.split(",");
                points.add(new Point3D(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])));
            }

            // Matrice di distanze tra punto i e j
            long[][] dist= new long[points.size()][points.size()];
            for(long[] arr: dist) {
                Arrays.fill(arr, -1);
            }
            for (int i = 0; i < points.size(); i++) {
                for(int j = i+1; j < points.size(); j++) {
                    dist[i][j] = points.get(i).getDistance(points.get(j));
                }   
            }

            // printMat(dist);

            int connectionsAmount = (points.size() > 700) ? 1000 : 10;

            // Ogni volta che "creo" un circuito, gli metto un ID: 1000, 1001 ecc.
            // Se collego due point di circuiti diversi, setto l'id al minimo dei due e "propago" il cambiamento: 
            // Per esempio se provo a collegare due punti, uno del circuito 1000 cocn uno del circuito 1050 allora
            // tutti i point che erano nel circuito 1050 diventano del circuito 1000!
            int[] circuits = new int[points.size()];
            Arrays.fill(circuits, Integer.MAX_VALUE);
            int circuitId = 1000; // 1000 è stato scelto a caso, volendo li possiamo far partire da 0

            // PART 1, togli il commento dal for e mettilo nel while
            // for (int i = 0; i < connectionsAmount; i++) {
            while(!isAllSameCircuit(circuits)) { // PART 2: la risposta è l'ultima "Risposta parte 2: " che viene stampata 
                int[] coords = findSmallestDistance(dist);
                int x = coords[0];
                int y = coords[1];

                dist[x][y] = Long.MAX_VALUE;

                // collega points[x] e points[y]
                if(Math.min(circuits[x], circuits[y]) == Integer.MAX_VALUE) { // non erano collegati a nessun circuito
                    circuits[x] = circuitId;
                    circuits[y] = circuitId++;
                } else {
                    int correctId = Math.min(circuits[x], circuits[y]);
                    int otherId = Math.max(circuits[x], circuits[y]);

                    if(otherId == Integer.MAX_VALUE) {
                        // In questo caso, aggiungo un nuovo punto a un circuito esistente. 
                        circuits[x] = correctId;
                        circuits[y] = correctId;
                    } else {
                        // Unisco due circuiti. In questo caso gestisco anche il discorso "unisco 2 punti che sono nello stesso circuito"
                        for (int j = 0; j < circuits.length; j++) {
                            if(circuits[j] == otherId) {
                                circuits[j] = correctId;
                            }
                        }
                    }
                }

                System.out.println("Risposta parte 2: " + points.get(x).getX() * points.get(y).getX());

            }

            System.out.println(Arrays.toString(circuits));
            
            // Conto le frequenze all'interno di circuits
            Integer[] freq = new Integer[circuitId-1000]; // uso integer per il sort
            Arrays.fill(freq, 0);
            System.out.println(Arrays.toString(freq));

            for (int q : circuits) {
                if(q != Integer.MAX_VALUE)
                    freq[q-1000]++;
            }

            System.out.println(Arrays.toString(freq));

            Arrays.sort(freq,Collections.reverseOrder());

            System.out.println(Arrays.toString(freq));

            System.out.println(freq[0]*freq[1] * freq[2]);
        }
    }
