import java.util.*;
import java.lang.Math;


public class Measurements {

    public static void main(String[] args) {
        System.out.println("Compare Xors Test");
        compareXorsTest();
        System.out.println("Compare Trees Test");
        compareTreesTest();
    }

    private static List<Integer> randomUniqueList(int desiredSize) {
        List<Integer> randomUniqueList = new ArrayList<>();
        Set<Integer> inventory = new HashSet<>();
        Random rand = new Random();

        int counter = 0, randValue;

        while (desiredSize > counter++) {
            randValue = rand.nextInt();

            if (inventory.add(randValue)) {
                randomUniqueList.add(randValue);
            }
        }

        return randomUniqueList;
    }

    private static double[][] compareXors() {
        double result[][] = new double[5][4];

        List<Integer> inputs = randomUniqueList(2500);
        Random rand = new Random();

        AVLTree avlTree = new AVLTree();

        long start, end;
        double totalPrefixXor, totalFirst100PrefixXor, totalSuccPrefixXor, totalFirst100SuccPrefixXor;
        int[] keys;

        for (int i = 1; i <= 5; i++) {
            totalPrefixXor = totalFirst100PrefixXor = totalSuccPrefixXor = totalFirst100SuccPrefixXor = 0;

            for (int input : inputs.subList(500 * (i - 1), 500 * i)) {
                avlTree.insert(input, rand.nextBoolean());
            }

            keys = avlTree.keysToArray();

            for (int j = 0; j < 500 * i; j++) {
                start = System.nanoTime();
                avlTree.prefixXor(keys[j]);
                end = System.nanoTime();

                totalPrefixXor += end - start;
                if (j < 100) {
                    totalFirst100PrefixXor += end - start;
                }

                start = System.nanoTime();
                avlTree.succPrefixXor(keys[j]);
                end = System.nanoTime();

                totalSuccPrefixXor += end - start;
                if (j < 100) {
                    totalFirst100SuccPrefixXor += end - start;
                }
            }

            result[i - 1][0] = totalPrefixXor / (500 * i);
            result[i - 1][1] = totalFirst100PrefixXor / 100;
            result[i - 1][2] = totalSuccPrefixXor / (500 * i);
            result[i - 1][3] = totalFirst100SuccPrefixXor / 100;
        }

        return result;
    }

    public static void print2D(double mat[][]) {
        for (double[] row : mat) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void compareXorsTest() {
        double[][] averageResult = new double[5][4], result;
        int attempts = 1000;

        // warm-up
        for (int i = 0; i < 50; i++) {
            compareXors();
        }

        for (int i = 0; i < attempts; i++) {
            result = compareXors();

            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 4; k++) {
                    averageResult[j][k] += result[j][k];
                }
            }
        }

        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 4; k++) {
                averageResult[j][k] /= attempts;
            }
        }

        print2D(averageResult);
    }

    public static List<Integer> balancedList(int desiredSize) {
        List<Integer> balancedList = new ArrayList<>();

        for (int i = 13; i >= 0; i--) {
            for (int j = 0; j < (int) Math.pow(2, 13 - i + 1); j++) {
                if (j % 2 == 0) {
                    balancedList.add((int) Math.ceil(Math.pow(2, i - 1) * (j + 1)));
                    if (balancedList.size() == desiredSize) {
                        return balancedList;
                    }
                }
            }
        }

        return null;
    }

    public static double[][] compareTrees() {
        double result[][] = new double[5][6];

        AVLTree avlTree;
        NonAVLTree nonAVLTree;

        Random rand = new Random();

        long start;
        double avlArithmatic, normalArithmatic, avlRandom, normalRandom, avlBalanced, normalBalanced;
        List<Integer> inputs;

        for (int i = 1; i <= 5; i++) {
            // arithmetic
            avlTree = new AVLTree();
            nonAVLTree = new NonAVLTree();

            start = System.nanoTime();
            for (int j = 0; j < 1000 * i; j++) {
                avlTree.insert(j, rand.nextBoolean());
            }
            avlArithmatic = System.nanoTime() - start;

            start = System.nanoTime();
            for (int j = 0; j < 1000 * i; j++) {
                nonAVLTree.insert(j, rand.nextBoolean());
            }
            normalArithmatic = System.nanoTime() - start;

            result[i - 1][0] = avlArithmatic / (1000 * i);
            result[i - 1][1] = normalArithmatic / (1000 * i);

            // balanced
            inputs = balancedList(1000 * i);

            avlTree = new AVLTree();
            nonAVLTree = new NonAVLTree();

            start = System.nanoTime();
            for (int input : inputs) {
                avlTree.insert(input, rand.nextBoolean());
            }
            avlBalanced = System.nanoTime() - start;

            start = System.nanoTime();
            for (int input : inputs) {
                nonAVLTree.insert(input, rand.nextBoolean());
            }
            normalBalanced = System.nanoTime() - start;

            result[i - 1][2] = avlBalanced / (1000 * i);
            result[i - 1][3] = normalBalanced / (1000 * i);

            // random
            avlTree = new AVLTree();
            nonAVLTree = new NonAVLTree();

            inputs = randomUniqueList(1000 * i);

            start = System.nanoTime();
            for (int input : inputs) {
                avlTree.insert(input, rand.nextBoolean());
            }
            avlRandom = System.nanoTime() - start;

            start = System.nanoTime();
            for (int input : inputs) {
                nonAVLTree.insert(input, rand.nextBoolean());
            }
            normalRandom = System.nanoTime() - start;

            result[i - 1][4] = avlRandom / (1000 * i);
            result[i - 1][5] = normalRandom / (1000 * i);
        }

        return result;
    }

    public static void compareTreesTest() {
        double[][] averageResult = new double[5][6], result;
        int attempts = 1000;

        // warm-up
        for (int i = 0; i < 50; i++) {
            compareTrees();
        }

        for (int i = 0; i < attempts; i++) {
            result = compareTrees();

            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 6; k++) {
                    averageResult[j][k] += result[j][k];
                }
            }
        }

        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 6; k++) {
                averageResult[j][k] /= attempts;
            }
        }

        print2D(averageResult);
    }
}
