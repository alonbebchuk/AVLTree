import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class AVLTreeTest {
    private AVLTree tree = new AVLTree();

    private void insert(Integer... integers) {
        for (Integer i : integers)
            tree.insert(i, true);
    }

    private boolean checkBalanceOfTree(AVLTree.AVLNode current) {

        boolean balancedRight = true, balancedLeft = true;
        int leftHeight = 0, rightHeight = 0;

        if (current.getRight().isRealNode()) {
            balancedRight = checkBalanceOfTree(current.getRight());
            rightHeight = getDepth(current.getRight());
        }

        if (current.getLeft().isRealNode()) {
            balancedLeft = checkBalanceOfTree(current.getLeft());
            leftHeight = getDepth(current.getLeft());
        }

        return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
    }

    private int getDepth(AVLTree.AVLNode n) {
        int leftHeight = 0, rightHeight = 0;

        if (n.getRight().isRealNode())
            rightHeight = getDepth(n.getRight());
        if (n.getLeft().isRealNode())
            leftHeight = getDepth(n.getLeft());

        return Math.max(rightHeight, leftHeight) + 1;
    }

    private boolean checkOrderingOfTree(AVLTree.AVLNode current) {
        if (current.getLeft().isRealNode()) {
            if (current.getLeft().getKey() > current.getKey())
                return false;
            else
                return checkOrderingOfTree(current.getLeft());
        } else if (current.getRight().isRealNode()) {
            if (current.getRight().getKey() < current.getKey())
                return false;
            else
                return checkOrderingOfTree(current.getRight());
        } else if (!current.getLeft().isRealNode() && !current.getRight().isRealNode())
            return true;

        return true;
    }

    @Test
    public void testRemove() {
        assertTrue(tree.empty());

        insert(16, 24, 36, 19, 44, 28, 61, 74, 83, 64, 52, 65, 86, 93, 88);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));

        tree.delete(88);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(88) == null);

        tree.delete(19);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(19) == null);

        tree.delete(16);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(16) == null);

        tree.delete(28);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(28) == null);

        tree.delete(24);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(24) == null);

        tree.delete(36);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(36) == null);

        tree.delete(52);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(52) == null);

        tree.delete(93);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(93) == null);

        tree.delete(86);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(86) == null);

        tree.delete(83);
        assertTrue(checkBalanceOfTree(tree.getRoot()));
        assertTrue(checkOrderingOfTree(tree.getRoot()));
        assertTrue(tree.search(83) == null);
    }
}