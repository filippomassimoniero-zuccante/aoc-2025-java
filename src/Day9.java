import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day9 {
    public static void printMat(char[][] lines) {
        for (char[] cs : lines) {
            for (char c : cs) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static void insideMat(List<Integer> xCoords, List<Integer> yCoords) {
        for (int i = 0; i < 8; i++) {
            if (i == 0)
                System.out.println("-01234567890123");
            System.out.print(i);

            for (int j = 0; j < 12; j++) {
                if (isInsidePolygon(j, i, xCoords, yCoords)) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("./input.txt");
        Scanner in = new Scanner(myFile);
        // Input
        List<String[]> tiles = new ArrayList<>();
        while (in.hasNextLine()) {
            tiles.add(in.nextLine().split(","));
        }

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int i = 0; i < tiles.size(); i++) {
            int x = Integer.parseInt(tiles.get(i)[0]);
            int y = Integer.parseInt(tiles.get(i)[1]);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        List<Integer> xCoords = new ArrayList<>();
        List<Integer> yCoords = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            int x = Integer.parseInt(tiles.get(i)[0]) - 1;
            int y = Integer.parseInt(tiles.get(i)[1]) - 1;

            xCoords.add(x);
            yCoords.add(y);
        }

        long maxRect = Integer.MIN_VALUE;
        for (int i = 0; i < xCoords.size(); i++) {
            for (int j = i + 1; j < xCoords.size(); j++) {
                long rect = Math.abs(xCoords.get(i) - xCoords.get(j) + 1l)
                        * Math.abs(yCoords.get(i) - yCoords.get(j) + 1l);
                maxRect = Math.max(maxRect, rect);
            }
        }

        System.out.println(maxRect);
        System.out.println(areaPart2(xCoords, yCoords));
    }

    public static long areaPart2(List<Integer> xCoords, List<Integer> yCoords) {
        long maxRect = Integer.MIN_VALUE;
        for (int i = 0; i < xCoords.size(); i++) {
            for (int j = 0; j < xCoords.size(); j++) {
                if (i == j)
                    continue;
                long rect = (Math.abs(xCoords.get(i) - xCoords.get(j)) + 1l)
                        * (Math.abs(yCoords.get(i) - yCoords.get(j)) + 1l);

                if (rect > maxRect) {
                    if (isValidRectangle(xCoords, yCoords, i, j)) {
                        maxRect = rect;
                        System.out.println("New max is " + maxRect + " with " + i + " " + j);
                    }
                }
            }
        }
        return maxRect;
    }

    public static boolean isValidRectangle(List<Integer> xCoords, List<Integer> yCoords, int i, int j) {
        int minY = Math.min(yCoords.get(j), yCoords.get(i));
        int maxY = Math.max(yCoords.get(j), yCoords.get(i));
        int minX = Math.min(xCoords.get(j), xCoords.get(i));
        int maxX = Math.max(xCoords.get(j), xCoords.get(i));

        for (int k = 0; k < xCoords.size(); k++) {
            int xe1 = xCoords.get(k);
            int ye1 = yCoords.get(k);
            int xe2 = xCoords.get((k + 1) % xCoords.size());
            int ye2 = yCoords.get((k + 1) % yCoords.size());

            // horizontal edges
            if (ye1 == ye2) {
                int edgeMinX = Math.min(xe1, xe2);
                int edgeMaxX = Math.max(xe1, xe2);

                if (minY < ye1 && ye1 < maxY && edgeMaxX > minX && edgeMinX < maxX) {
                    if (!isEdgeValid(edgeMinX, edgeMaxX, ye1, true, xCoords, yCoords, minX, maxX)) {
                        return false;
                    }
                }
            }

            // Check vertical edges
            if (xe1 == xe2) {
                int edgeMinY = Math.min(ye1, ye2);
                int edgeMaxY = Math.max(ye1, ye2);

                if (minX < xe1 && xe1 < maxX && edgeMaxY > minY && edgeMinY < maxY) {
                    if (!isEdgeValid(edgeMinY, edgeMaxY, xe1, false, xCoords, yCoords, minY, maxY)) {
                        return false;
                    }
                }
            }
        }

        return isInsidePolygon((minX + maxX) / 2, (minY + maxY) / 2, xCoords, yCoords);
    }

    // Check if an edge is valid by testing points on both sides
    // isHorizontal: true for horizontal edges
    public static boolean isEdgeValid(int edgeMin, int edgeMax, int edgePos,
            boolean isHorizontal, List<Integer> xCoords,
            List<Integer> yCoords, int rectMin, int rectMax) {

        for (int pos = edgeMin; pos <= edgeMax; pos++) {
            if (pos < rectMin || pos > rectMax)
                continue;

            if (isHorizontal) {
                // test x=pos at y-1 and y+1
                boolean aboveInside = isInsidePolygon(pos, edgePos - 1, xCoords, yCoords);
                boolean belowInside = isInsidePolygon(pos, edgePos + 1, xCoords, yCoords);

                if (!aboveInside || !belowInside) {
                    return false;
                }
            } else {
                // test y=pos at x-1 and x+1
                boolean leftInside = isInsidePolygon(edgePos - 1, pos, xCoords, yCoords);
                boolean rightInside = isInsidePolygon(edgePos + 1, pos, xCoords, yCoords);

                if (!leftInside || !rightInside) {
                    return false;
                }
            }
        }

        return true;
    }

    // Ray cast algorithm
    public static boolean isInsidePolygon(int px, int py, List<Integer> xCoords, List<Integer> yCoords) {
        int n = xCoords.size();
        int cont = 0;

        for (int i = 0; i < n; i++) {
            int x1 = xCoords.get(i);
            int y1 = yCoords.get(i);
            int y2 = yCoords.get((i + 1) % n);

            if (Math.min(y1, y2) <= py && Math.max(y1, y2) >= py && px == x1) {
                return true;
            }
            if (y1 == y2) // skip horizontal edges
                continue;

            if ((py <= y1) != (py <= y2)) {
                if (px < x1)
                    cont++;
            }
        }
        return cont % 2 == 1;
    }
}