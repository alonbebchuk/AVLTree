import java.util.Arrays;


/**
 * PLEASE ADD THE FOLLOWING FUNCTION TO AVLTREE CLASS SO THE TESTER WORKS!!!!
 * <p>
 * public AVLNode find(int k) {
 * //Search for node k - go down the tree in the correct directions as seen in class, until node with key k is found (or null returned if k not in tree).
 * if (this.empty()) {
 * return null;
 * }
 * AVLNode currNode = this.root;
 * while (currNode != this.externalLeaf){
 * if (k<currNode.key) {
 * currNode = currNode.leftSon;
 * }
 * else if (k>currNode.key) {
 * currNode = currNode.rightSon;
 * }
 * else if (k==currNode.key) {
 * return currNode;
 * }
 * }
 * return null;
 * }
 *
 * @author Edan
 */


public class EdansTester {
    public static AVLTree.AVLNode find(AVLTree t, int k) {
        //Search for node k - go down the tree in the correct directions as seen in class, until node with key k is found (or null returned if k not in tree).
        AVLTree.AVLNode currNode = t.getRoot();
        while (currNode != null) {
            if (k < currNode.getKey()) {
                currNode = currNode.getLeft();
            } else if (k > currNode.getKey()) {
                currNode = currNode.getRight();
            } else if (k == currNode.getKey()) {
                return currNode;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        int[] arr1 = {3, 8, 9, 2, 0, 4, 7, 11, 22, 33, 44, 55, 66, 77, 88, 99, 100, 23, 24, 25, 26};
        AVLTree t1 = new AVLTree();
        if (t1.empty() != true) {
            System.out.println("empty func false negative");
        }
        if (t1.search(3) != null) {
            System.out.println("Problem with search func");
        }
        for (int i = 0; i < arr1.length; i++) {
            int temp = arr1[i];
            if (temp % 2 == 1) {
                t1.insert(temp, false);
            } else {
                t1.insert(temp, true);
            }
            BTreePrinter.printNode(t1.getRoot());
        }
        if (t1.getRoot().getKey() != 8) {
            System.out.println("Problem with insert/balance: root should be 8 here");
        }
        //Check height tests
        int[] a = {0, 7, 9, 22, 24, 26, 44, 66, 88, 100};
        for (int i = 0; i < a.length; i++) {
            int temp = a[i];
            if (find(t1, temp).getHeight() != 0) {
                System.out.println("Hight problem in height 0 leaves");
            }
        }
        int[] b = {2, 4, 11, 25, 55, 99};
        for (int i = 0; i < b.length; i++) {
            int temp = b[i];
            if (find(t1, temp).getHeight() != 1) {
                System.out.println("Hight problem in height 1 nodes");
            }
        }
        int[] c = {3, 23, 77};
        for (int i = 0; i < c.length; i++) {
            int temp = c[i];
            if (find(t1, temp).getHeight() != 2) {
                System.out.println("Hight problem in height 2 nodes");
            }
        }
        if (find(t1, 33).getHeight() != 3) {
            System.out.println("Hight problem in height 3 nodes");
        }
        if (find(t1, 8).getHeight() != 4) {
            System.out.println("Hight problem in height 4 nodes");
        }
        if (t1.min() != true || t1.max() != true) {
            System.out.println("Min/Max problem");
        }

        //Check insert and rebalances
        t1.insert(101, false);
        BTreePrinter.printNode(t1.getRoot());
        if (t1.getRoot().getKey() != 33) {
            System.out.println("Problem with insert/balance: root should be 33 here");
        }

        if (t1.min() != true || t1.max() != false) {
            System.out.println("Min/Max problem");
        }

        //Check height tests
        int[] d = {0, 7, 9, 22, 24, 26, 44, 66, 88, 101};
        for (int i = 0; i < d.length; i++) {
            int temp = d[i];
            if (find(t1, temp).getHeight() != 0) {
                System.out.println("Hight problem in height 0 leaves");
            }
        }
        int[] e = {2, 4, 11, 25, 55, 100};
        for (int i = 0; i < e.length; i++) {
            int temp = e[i];
            if (find(t1, temp).getHeight() != 1) {
                System.out.println("Hight problem in height 1 nodes: height of" + temp + "is" + find(t1, temp).getHeight());
            }
        }
        int[] f = {3, 23, 99};
        for (int i = 0; i < f.length; i++) {
            int temp = f[i];
            if (find(t1, temp).getHeight() != 2) {
                System.out.println("Hight problem in height 2 nodes");
            }
        }
        if (find(t1, 8).getHeight() != 3 || find(t1, 77).getHeight() != 3) {
            System.out.println("Hight problem in height 3 nodes");
        }
        if (find(t1, 33).getHeight() != 4) {
            System.out.println("Hight problem in height 4 nodes");
        }
        if (t1.getRoot().getKey() != 33) {
            System.out.println("root problem");
        }
//		t1.print(t1.getRoot());
//		System.out.println("\nNow delete 33\n");
        t1.delete(33);
        BTreePrinter.printNode(t1.getRoot());
        if (t1.getRoot().getKey() != 44) {
            System.out.println("Root should be succesor of deleted node: should be 44");
        }
//		t1.print(t1.getRoot());
        t1.insert(102, true);
        BTreePrinter.printNode(t1.getRoot());
        t1.insert(103, false);
        BTreePrinter.printNode(t1.getRoot());
        if (find(t1, 101).getParent() != find(t1, 77)) {
            System.out.println("Problem with rebalance");
        }
        int[] g = {0, 2, 3, 4, 7, 8, 9, 11, 22, 23, 24, 25, 26, 44, 55, 66, 77, 88, 99, 100, 101, 102, 103};
        int[] gt = t1.keysToArray();
        if (g.length != gt.length) {
            System.out.println("Keys to array error");
            System.out.println("Keys to array supposed to be" + Arrays.toString(g));
            System.out.println("Keys to array is            " + Arrays.toString(gt));
        } else {
            int len = Math.max(g.length, gt.length);
            for (int i = 0; i < len; i++) {
                if (g[i] != gt[i]) {
                    System.out.println("Keys to array error: meant to be " + g[i] + " but is" + gt[i]);
                }
                if (g[i] % 2 == 1) {
                    if (t1.search(g[i]) != false) {
                        System.out.println("Problem with search or get key for " + g[i]);
                    }
                } else {
                    if (t1.search(g[i]) != true) {
                        System.out.println("Problem with search or get key for " + g[i]);
                    }
                }
            }
        }
        boolean[] h = {true, true, false, true, false, true, false, false, true, false, true, false, true, true, false, true, false, true, false, true, false, true, false};
        boolean[] ht = t1.infoToArray();
        if (h.length != ht.length) {
            System.out.println("info to array error");
            System.out.println("info to array supposed to be" + Arrays.toString(g));
            System.out.println("info to array is            " + Arrays.toString(gt));
        } else {
            int len = Math.max(h.length, ht.length);
            for (int i = 0; i < len; i++) {
                if (h[i] != ht[i]) {
                    System.out.println("Keys to array error: meant to be " + h[i] + " but is" + ht[i]);
                }
            }
        }
        if (t1.size() != g.length) {
            System.out.println("Size problem");
        }
        if (t1.successor(find(t1, 26)).getKey() != 44 || t1.successor(find(t1, 55)).getKey() != 66 || t1.successor(find(t1, 100)).getKey() != 101) {
            System.out.println("Successor error");
        }

        for (int i = 0; i < g.length; i++) {
            if (t1.succPrefixXor(g[i]) != t1.prefixXor(g[i])) {
                System.out.println("Problem with succPrefixXor or PrefixXor");
            }
        }

        if (t1.prefixXor(4) != true || t1.prefixXor(3) != false || t1.prefixXor(2) != false || t1.prefixXor(8) != false || t1.prefixXor(23) != true) {
            System.out.println("PrefixXor problem");
        }
        if (t1.succPrefixXor(4) != true || t1.succPrefixXor(3) != false || t1.succPrefixXor(2) != false || t1.succPrefixXor(8) != false || t1.succPrefixXor(23) != true) {
            System.out.println("succPrefixXor problem");
        }
//		AVLTree.print(t1.getRoot());
        for (int i = 0; i < 100; i++) {
            t1.insert(500 + i, false);
        }
//		AVLTree.print(t1.getRoot());
        if (t1.getRoot().getHeight() != 6) {
            System.out.println("height of root supposed to be 6, is " + t1.getRoot().getHeight());
        }
        int[] j = t1.keysToArray();
        for (int i = 0; i < j.length; i++) {
            if (t1.succPrefixXor(j[i]) != t1.prefixXor(j[i])) {
                System.out.println("Problem with succPrefixXor or PrefixXor");
            }
        }
        System.out.println("Done running. If no errors printed - great! If errors printed, fix as needed");


    }
}
