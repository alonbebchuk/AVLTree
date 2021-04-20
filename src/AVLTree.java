/**
 * public class AVLNode
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 * <p>
 * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
 * arguments. Changing these would break the automatic tester, and would result in worse grade.
 * <p>
 * However, you are allowed (and required) to implement the given functions, and can add functions of your own
 * according to your needs.
 */

public class AVLTree {
    private AVLNode virtualNode;
    private AVLNode root;

    /**
     * This constructor creates an empty AVLTree.
     */
    public AVLTree() {
        this.virtualNode = new AVLNode();
        this.root = virtualNode;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return this.root.isRealNode();
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public Boolean search(int k) {
        AVLNode node = this.root;

        while (node.isRealNode()) {
            if (node.getKey() == k) {
                return node.getValue();
            }
            node = (k < node.getKey()) ? node.getLeft() : node.getRight();
        }

        return null;
    }

    private void switchNodesParent(AVLNode node1, AVLNode node2) {
        if (node1.getParent() == null) {
            this.root = node2;
            node2.setParent(null);
        } else {
            if (node1.getKey() < node1.getParent().getKey()) {
                node1.getParent().setLeft(node2);
            } else {
                node1.getParent().setRight(node2);
            }
            node2.setParent(node1.getParent());
        }
    }

    private void switchNodesRightChild(AVLNode node1, AVLNode node2) {
        node1.setLeft(node2.getRight());
        node2.getRight().setParent(node1);
    }

    private void switchNodesLeftChild(AVLNode node1, AVLNode node2) {
        node1.setRight(node2.getLeft());
        node2.getLeft().setParent(node1);
    }

    private void switchNodesRight(AVLNode node1, AVLNode node2) {
        node1.setRight(node2);
        node2.setParent(node1);
    }

    private void switchNodesLeft(AVLNode node1, AVLNode node2) {
        node1.setLeft(node2);
        node2.setParent(node1);
    }

    private void singleRotationHeightUpdate(AVLNode node, AVLNode nodeChild) {
        nodeChild.setHeight(node.getHeight());
        node.setHeight(1 + Math.max(node.getLeft().getKey(), node.getRight().getKey()));
    }

    private void doubleRotationHeightUpdate(AVLNode node, AVLNode nodeChild, AVLNode nodeGrandChild) {
        nodeGrandChild.setHeight(node.getHeight());
        nodeChild.setHeight(nodeChild.getHeight() - 1);
        node.setHeight(1 + Math.max(node.getLeft().getKey(), node.getRight().getKey()));
    }

    public void rotation(AVLNode node) {
        int nodeBF = node.getBalanceFactor();
        AVLNode nodeChild = nodeBF > 0 ? node.getLeft() : node.getRight(), nodeGrandChild, leftoverL, leftoverR;
        int nodeChildBF = nodeChild.getBalanceFactor();

        if (nodeBF > 0) {
            if (nodeChildBF >= 0) {
                switchNodesParent(node, nodeChild);
                switchNodesRightChild(node, nodeChild);
                switchNodesRight(nodeChild, node);
                singleRotationHeightUpdate(node, nodeChild);
            } else {
                nodeGrandChild = nodeChild.getRight();
                switchNodesParent(node, nodeGrandChild);
                switchNodesRightChild(node, nodeGrandChild);
                switchNodesLeftChild(nodeChild, nodeGrandChild);
                switchNodesLeft(nodeGrandChild, nodeChild);
                switchNodesRight(nodeGrandChild, node);
                doubleRotationHeightUpdate(node, nodeChild, nodeGrandChild);
            }
        } else {
            if (nodeChildBF <= 0) {
                switchNodesParent(node, nodeChild);
                switchNodesLeftChild(node, nodeChild);
                switchNodesLeft(nodeChild, node);
                singleRotationHeightUpdate(node, nodeChild);
            } else {
                nodeGrandChild = nodeChild.getRight();
                switchNodesParent(node, nodeGrandChild);
                switchNodesRightChild(node, nodeGrandChild);
                switchNodesLeftChild(nodeChild, nodeGrandChild);
                switchNodesLeft(nodeGrandChild, nodeChild);
                switchNodesRight(nodeGrandChild, node);
                doubleRotationHeightUpdate(node, nodeChild, nodeGrandChild);
            }
        }
    }

    /**
     * public int insert(int k, boolean i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
     * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, boolean i) {
        AVLNode parent = null;
        AVLNode node = this.root;

        while (node.isRealNode()) {
            if (node.getKey() == k) {
                while (parent != null) {
                    parent.setHeight(parent.getHeight() - 1);
                    parent = parent.getParent();
                }
                return -1;
            }

            node.setHeight(node.getHeight() + 1);

            parent = node;
            node = (k < node.getKey()) ? node.getLeft() : node.getRight();
        }

        AVLNode newNode = new AVLNode(k, i);

        if (parent == null) {
            this.root = newNode;
        } else if (parent.getKey() > k) {
            switchNodesLeft(parent, newNode);
        } else {
            switchNodesRight(parent, newNode);
        }

        while (parent != null) {
            if (Math.abs(parent.getBalanceFactor()) > 1) {
                this.rotation(parent);
                return 1;
            } else {
                parent = parent.getParent();
            }
        }

        return 0;
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        AVLNode node = this.root;

        while (node.isRealNode()) {
            if (node.getKey() > k) {
                node = node.getLeft();
            } else if (node.getKey() < k) {
                node = node.getRight();
            } else {
                if (node.getLeft().isRealNode() && node.getRight().isRealNode()) {
                    AVLNode successor = this.successor(node);
                    switchNodesParent(successor, node);
                    switchNodesLeft(successor, node);
                    switchNodesRight(successor, node);
                } else if (node.getLeft().isRealNode() || node.getRight().isRealNode()) {
                    AVLNode child = node.getLeft().isRealNode() ? node.getLeft() : node.getRight();
                    switchNodesParent(child, node);
                } else {
                    node.getParent().setLeft(this.virtualNode);
                    node.getParent().setRight(this.virtualNode);
                }

                AVLNode parent = node.getParent();
                while (parent != null) {
                    parent.setHeight(parent.getHeight() - 1);
                }
            }
        }

        if (node == null) {
            return -1;
        }

        AVLNode parent = node.getParent();
        int rotationCnt = 0;

        while (parent != null) {
            if (Math.abs(parent.getBalanceFactor()) > 1) {
                this.rotation(parent);
                rotationCnt++;
            }
            parent = parent.getParent();
        }

        return rotationCnt;    // to be replaced by student code
    }

    public AVLNode minNode() {
        if (this.empty()) {
            return null;
        }
        AVLNode x = this.root;
        while (x.getLeft().isRealNode()) {
            x = x.getLeft();
        }
        return x;
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public Boolean min() {
        AVLNode x = this.minNode();
        return x != null ? x.getValue() : null;
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public Boolean max() {
        if (this.empty()) {
            return null;
        }
        AVLNode x = this.root;
        while (x.getRight().isRealNode()) {
            x = x.getRight();
        }
        return x.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size()];
        AVLNode x = this.minNode();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = x.getKey();
            x = this.successor(x);
        }
        return arr;
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.size()]; // to be replaced by student code
        AVLNode x = this.minNode();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = x.getValue();
            x = this.successor(x);
        }
        return arr;                    // to be replaced by student code
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.root.getSize();
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public AVLNode getRoot() {
        return this.root.isRealNode() ? this.root : null;
    }

    /**
     * public boolean prefixXor(int k)
     * <p>
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     * <p>
     * precondition: this.search(k) != null
     */
    public boolean prefixXor(int k) {
        return false;
    }

    /**
     * public AVLNode successor
     * <p>
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     */
    public AVLNode successor(AVLNode node) {
        if (node.getRight().isRealNode()) {
            node = node.getRight();
            while (node.getLeft().isRealNode()) {
                node = node.getLeft();
            }
            return node;
        } else {
            while (node.getParent().isRealNode()) {
                if (node == node.getParent().getLeft()) {
                    return node.getParent();
                }
            }
        }
        return null;
    }

    /**
     * public boolean succPrefixXor(int k)
     * <p>
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     * <p>
     * precondition: this.search(k) != null
     */
    public boolean succPrefixXor(int k) {
        return false;
    }


    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     * <p>
     * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
     * arguments. Changing these would break the automatic tester, and would result in worse grade.
     * <p>
     * However, you are allowed (and required) to implement the given functions, and can add functions of your own
     * according to your needs.
     */
    public class AVLNode {
        private AVLNode left;
        private AVLNode parent;
        private AVLNode right;

        private int key;
        private Boolean value;
        private int height;
        private int size;

        public AVLNode() {
            this.key = -1;
            this.value = null;
            this.height = -1;
        }

        public AVLNode(int key, boolean value) {
            this.left = AVLTree.this.virtualNode;
            this.right = AVLTree.this.virtualNode;

            this.key = key;
            this.value = value;
            this.height = 0;
        }

        //returns node's key (for virtual node return -1)
        public int getKey() {
            return this.key;
        }

        //returns node's value [info] (for virtual node return null)
        public boolean getValue() {
            return this.value;
        }

        //sets left child
        public void setLeft(AVLNode node) {
            this.left = node;
        }

        //returns left child (if there is no left child return null)
        public AVLNode getLeft() {
            return this.left.isRealNode() ? this.left : null;
        }

        //sets right child
        public void setRight(AVLNode node) {
            this.right = node;
        }

        //returns right child (if there is no right child return null)
        public AVLNode getRight() {
            return this.right.isRealNode() ? this.right : null;
        }

        //sets parent
        public void setParent(AVLNode node) {
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        public AVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            return this.key != -1;
        }

        // sets the height of the node
        public void setHeight(int height) {
            this.height = height;
        }

        // Returns the height of the node (-1 for virtual nodes)
        public int getHeight() {
            return this.height;
        }

        public int getBalanceFactor() {
            return this.left.height - this.right.height;
        }

        public int getSize() {
            return this.size;
        }
    }

}
