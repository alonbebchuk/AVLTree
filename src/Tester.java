import java.util.Arrays;
import java.util.Random;
import java.util.function.IntPredicate;

public class Tester {

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        tree.insert(3, true);
        Printer.print(tree.getRoot());
        tree.insert(10, true);
        Printer.print(tree.getRoot());
        tree.insert(8, true);
        Printer.print(tree.getRoot());
        tree.insert(11, true);
        Printer.print(tree.getRoot());
        tree.insert(12, true);
        Printer.print(tree.getRoot());
        tree.insert(2, true);
        Printer.print(tree.getRoot());
        tree.insert(5, true);
        Printer.print(tree.getRoot());
        tree.insert(4, true);
        Printer.print(tree.getRoot());
        tree.insert(6, true);
        Printer.print(tree.getRoot());
        tree.insert(7, true);
        Printer.print(tree.getRoot());
    }
}


