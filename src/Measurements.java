import java.util.*;
import java.lang.Math;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Measurements {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            double[][] results = compareXors2();
            print2D(results);
            System.out.println();
//        compareTrees();
        }
    }

    public static void print2D(double mat[][])
    {
        // Loop through all rows
        for (double[] row : mat)

            // converting each row as string
            // and then printing in a separate line
            System.out.println(Arrays.toString(row));
    }

    private static double[][] compareXors2() {
        double results[][] = new double[5][];

        List<Integer> inputs = IntStream.rangeClosed(1, 100000).boxed().collect(Collectors.toList());
        Collections.shuffle(inputs);
        Random rand = new Random();

        AVLTree avlTree = new AVLTree();

        long start, end;
        double totalPrefixXor, totalFirst100PrefixXor, totalSuccPrefixXor, totalFirst100SuccPrefixXor;

        for (int i = 1; i <= 5; i++) {
            for (int input : inputs.subList(500 * (i-1), 500 * i)) {
                avlTree.insert(input, rand.nextBoolean());
            }

            totalPrefixXor = totalFirst100PrefixXor = totalSuccPrefixXor = totalFirst100SuccPrefixXor = 0;

            int[] keys = avlTree.keysToArray();

            for (int j = 0; j < 500 * i; j++) {
                System.nanoTime();
                System.nanoTime();

                start = System.nanoTime();
                avlTree.prefixXor(keys[j]);
                end = System.nanoTime();

                totalPrefixXor += end - start;
                if (j < 100) {
                    totalFirst100PrefixXor += end - start;
                }

                System.nanoTime();
                System.nanoTime();

                start = System.nanoTime();
                avlTree.succPrefixXor(keys[j]);
                end = System.nanoTime();

                totalSuccPrefixXor += end - start;
                if (j < 100) {
                    totalFirst100SuccPrefixXor += end - start;
                }
            }

            results[i-1] = new double[4];
            results[i-1][0] = totalPrefixXor / (500 * i);
            results[i-1][1] = totalFirst100PrefixXor / 100;
            results[i-1][2] = totalSuccPrefixXor / (500 * i);
            results[i-1][3] = totalFirst100SuccPrefixXor / 100;
        }

        return results;
    }

    public static int[] createArr(int size) {
        int[] arr = new int[5000];
        int i = 13;
        int counter = 0;
        while (true) {
            for (int j = 0; j < (int) Math.pow(2, 13 - i + 1); j++) {
                if (counter == size - 1) {
                    break;
                }
                if (j % 2 == 0) {
                    arr[counter] = (int) Math.ceil(Math.pow(2, i - 1) * (j + 1));
                    counter++;
                }
            }
            i--;
            if (i == 0) {
                break;
            }
        }
        return Arrays.copyOf(arr, size);
    }

    public static void compareTrees() {
        Random rand = new Random();
        NonAVLTree normalTree = new NonAVLTree();
        AVLTree avlTree = new AVLTree();
        long first, second;
        double avlArithmatic = 0, normalArithmatic = 0, avlBalanced = 0, normalBalanced = 0, avlRandom = 0, normalRandom = 0;
        for (int i = 0; i < 5; i++) {
            System.out.println("For " + 1000 * (i + 1) + " elements");
            for (int j = 0; j < 1000 * (i + 1); j++) {
                first = System.currentTimeMillis();
                normalTree.insert(j, false);
                second = System.currentTimeMillis();
                normalArithmatic += second - first;
                first = System.currentTimeMillis();
                avlTree.insert(j, false);
                second = System.currentTimeMillis();
                avlArithmatic += second - first;
            }
            avlArithmatic /= (i + 1) * 1000;
            normalArithmatic /= (i + 1) * 1000;
            System.out.println("Average time per insert for normal tree arithmetic sequence: " + normalArithmatic + " milliseconds");
            System.out.println("Average time per insert for avl tree arithmetic sequence: " + avlArithmatic + " milliseconds");
            for (int j = 0; j < 1000 * (i + 1); j++) {
                first = System.currentTimeMillis();
                normalTree.insert(rand.nextInt(), false);
                second = System.currentTimeMillis();
                normalRandom += second - first;
                first = System.currentTimeMillis();
                avlTree.insert(rand.nextInt(), false);
                second = System.currentTimeMillis();
                avlRandom += second - first;
            }
            avlRandom /= (i + 1) * 1000;
            normalRandom /= (i + 1) * 1000;
            System.out.println("Average time per insert for normal tree random sequence: " + normalRandom + " milliseconds");
            System.out.println("Average time per insert for avl tree random sequence: " + avlRandom + " milliseconds");
            int[] balancedArr = createArr((i + 1) * 1000);
            for (int j = 0; j < 1000 * (i + 1); j++) {
                first = System.currentTimeMillis();
                normalTree.insert(balancedArr[j], false);
                second = System.currentTimeMillis();
                normalRandom += second - first;
                first = System.currentTimeMillis();
                avlTree.insert(balancedArr[j], false);
                second = System.currentTimeMillis();
                avlRandom += second - first;
            }
            avlBalanced /= (i + 1) * 1000;
            normalBalanced /= (i + 1) * 1000;
            System.out.println("Average time per insert for normal tree balanced sequence: " + normalBalanced + " milliseconds");
            System.out.println("Average time per insert for avl tree balanced sequence: " + avlBalanced + " milliseconds");
        }
    }
}
