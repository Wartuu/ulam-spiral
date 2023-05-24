import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Vector;

public class Ulam {


    private static boolean isPrime(long value) {
        if (value <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(value); i++) {
            if (value % i == 0) {
                return false;
            }
        }

        return true;
    }


    public static Vector<Vector<Boolean>> checkForPrimary(Vector<Vector<Long>> values) {

        Vector<Vector<Boolean>> out = new Vector<>();

        for (Vector<Long> y : values) {
            Vector<Boolean> mapY = new Vector<>();
            for (long x : y) {
                mapY.add(isPrime(x));
            }
            out.add(mapY);
        }

        return out;
    }

    public static Vector<Vector<Long>> generate2DSquare(int size) {
        Vector<Vector<Long>> map = new Vector<>(size*size);

        long iter = 1;
        for (int y = 0; y < size; y++) {
            Vector<Long> xMap = new Vector<>(size);
            for (int x = 0; x < size; x++) {
                xMap.add(iter);
                iter++;
            }

            map.add(xMap);
        }


        return map;
    }

    public static Vector<Vector<Long>> generate2DSpiral(int size) {
        Vector<Vector<Long>> spiral = new Vector<>(size);

        int[][] directions = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } }; // Right, Down, Left, Up
        int directionIndex = 0;

        int x = size / 2;
        int y = size / 2;

        int steps = 1;
        int stepsCount = 0;

        long iter = 1;

        for (int i = 0; i < size; i++) {
            spiral.add(new Vector<>(size));
            for (int j = 0; j < size; j++) {
                spiral.get(i).add(0L);
            }
        }

        spiral.get(y).set(x, iter++);

        while (true) {
            int[] direction = directions[directionIndex];
            for (int i = 0; i < steps; i++) {
                x += direction[0];
                y += direction[1];

                if (x >= 0 && x < size && y >= 0 && y < size) {
                    spiral.get(y).set(x, iter++);
                } else {
                    return spiral;
                }
            }

            stepsCount++;
            if (stepsCount == 2) {
                steps++;
                stepsCount = 0;
            }

            directionIndex = (directionIndex + 1) % 4;
        }
    }

    public static String generate2DSquareString(int size) {
        Vector<Vector<Long>> map = generate2DSquare(size);

        StringBuilder out = new StringBuilder();

        for (Vector<Long> y : map) {
            for (long x : y) {
                out.append(x + " ");
            }
            out.append("\n");
        }

        return out.toString();
    }
}
