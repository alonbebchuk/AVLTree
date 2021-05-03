import java.util.Arrays;
import java.util.Random;
import java.util.function.IntPredicate;


public class Tester {

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();

        // tree empty on init
        if (!tree.empty()) {
            System.out.println("tree not empty on init");
        }

        tree.insert(3, true);

        // tree not empty after add
        if (tree.empty()) {
            System.out.println("tree empty after insert");
        }

        tree.insert(10, true);
        tree.insert(8, true);

        // -2 +1 rotation
        if (tree.getRoot().getKey() != 8) {
            System.out.println("insert - wrong -2 +1 rotation");
        }

        tree.insert(11, true);
        tree.insert(12, true);

        // -2 -1 rotation
        if (tree.getRoot().getRight().getKey() != 11) {
            System.out.println("insert - wrong -2 -1 rotation");
        }

        tree.insert(2, true);
        tree.insert(5, true);
        tree.insert(4, true);
        tree.insert(6, true);
        tree.insert(7, true);

        // +2 -1 rotation
        if (tree.getRoot().getKey() != 5) {
            System.out.println("insert - wrong +2 -1 rotation");
        }
    }
}


