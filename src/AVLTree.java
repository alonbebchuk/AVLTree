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
    private int size;

    /**
     * This constructor creates an empty AVLTree.
     */
    public AVLTree() {
        this.virtualNode = new AVLNode();
        this.root = virtualNode;
        this.size = 0;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return !this.root.isRealNode();
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
            } else {
                node = (k < node.getKey()) ? node.left : node.right;
            }
        }

        return null;
    }

    private void setFirstParentToSecondParent(AVLNode node1, AVLNode node2) {
        AVLNode node2Parent = node2.getParent();

        node1.setParent(node2Parent);

        if (node2Parent == null) {
            this.root = node1;
        } else if (node2 == node2Parent.left) {
            node2Parent.setLeft(node1);
        } else {
            node2Parent.setRight(node1);
        }
    }

    private void setFirstLeftChildToSecondRightChild(AVLNode node1, AVLNode node2) {
        AVLNode node2RightChild = node2.right;

        node1.setLeft(node2RightChild);

        if (node2RightChild.isRealNode()) {
            node2RightChild.setParent(node1);
        }
    }

    private void setFirstRightChildToSecondLeftChild(AVLNode node1, AVLNode node2) {
        AVLNode node2LeftChild = node2.left;

        node1.setRight(node2LeftChild);

        if (node2LeftChild.isRealNode()) {
            node2LeftChild.setParent(node1);
        }
    }

    private void setFirstRightChildToSecond(AVLNode node1, AVLNode node2) {
        node1.setRight(node2);
        node2.setParent(node1);
    }

    private void setFirstLeftChildToSecond(AVLNode node1, AVLNode node2) {
        node1.setLeft(node2);
        node2.setParent(node1);
    }

    private void singleRotationHeightXorUpdate(AVLNode node, AVLNode nodeChild) {
        node.updateHeight();
        nodeChild.updateHeight();

        node.updateXor();
        nodeChild.updateXor();
    }

    private void doubleRotationHeightXorUpdate(AVLNode node, AVLNode nodeChild, AVLNode nodeGrandChild) {
        node.updateHeight();
        nodeChild.updateHeight();
        nodeGrandChild.updateHeight();

        node.updateXor();
        nodeChild.updateXor();
        nodeGrandChild.updateXor();
    }

    public AVLNode rotation(AVLNode node) {
        int nodeBF = node.getBalanceFactor();
        AVLNode nodeChild = (nodeBF > 0) ? node.left : node.right, nodeGrandChild = null;
        int nodeChildBF = nodeChild.getBalanceFactor();

        if (nodeBF > 0) {
            if (nodeChildBF >= 0) {
                setFirstParentToSecondParent(nodeChild, node);
                setFirstLeftChildToSecondRightChild(node, nodeChild);
                setFirstRightChildToSecond(nodeChild, node);
                singleRotationHeightXorUpdate(node, nodeChild);
            } else {
                nodeGrandChild = nodeChild.right;

                setFirstParentToSecondParent(nodeGrandChild, node);
                setFirstRightChildToSecondLeftChild(nodeChild, nodeGrandChild);
                setFirstLeftChildToSecondRightChild(node, nodeGrandChild);
                setFirstLeftChildToSecond(nodeGrandChild, nodeChild);
                setFirstRightChildToSecond(nodeGrandChild, node);
                doubleRotationHeightXorUpdate(node, nodeChild, nodeGrandChild);
            }
        } else {
            if (nodeChildBF <= 0) {
                setFirstParentToSecondParent(nodeChild, node);
                setFirstRightChildToSecondLeftChild(node, nodeChild);
                setFirstLeftChildToSecond(nodeChild, node);
                singleRotationHeightXorUpdate(node, nodeChild);
            } else {
                nodeGrandChild = nodeChild.left;

                setFirstParentToSecondParent(nodeGrandChild, node);
                setFirstRightChildToSecondLeftChild(node, nodeGrandChild);
                setFirstLeftChildToSecondRightChild(nodeChild, nodeGrandChild);
                setFirstLeftChildToSecond(nodeGrandChild, node);
                setFirstRightChildToSecond(nodeGrandChild, nodeChild);
                doubleRotationHeightXorUpdate(node, nodeChild, nodeGrandChild);
            }
        }

        return (nodeGrandChild == null) ? nodeChild : nodeGrandChild;
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
                return -1;
            }

            parent = node;
            node = (k < node.getKey()) ? node.left : node.right;
        }
        size++;

        AVLNode newNode = new AVLNode(k, i);

        if (parent == null) {
            this.root = newNode;
        } else if (k < parent.getKey()) {
            setFirstLeftChildToSecond(parent, newNode);
        } else {
            setFirstRightChildToSecond(parent, newNode);
        }

        int numRotations = 0, prevParentHeight;
        boolean fixHeight = true, fixXor = i;

        while (parent != null) {
            if (fixXor) {
                parent.updateXor();
            }

            if (fixHeight) {
                prevParentHeight = parent.getHeight();
                parent.updateHeight();

                if (parent.getHeight() == prevParentHeight) {
                    fixHeight = false;
                } else if (Math.abs(parent.getBalanceFactor()) > 1) {
                    this.rotation(parent);
                    numRotations = 1;
                    fixHeight = false;
                }
            }

            if (!fixXor && !fixHeight) {
                break;
            }

            parent = parent.getParent();
        }

        return numRotations;
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
        AVLNode parent = null;

        while (node.isRealNode()) {
            if (node.getKey() == k) {
                parent = node.getParent();

                if (node.left.isRealNode() && node.right.isRealNode()) {
                    AVLNode successor = this.successor(node);
                    parent = successor.getParent();

                    if (successor == parent.left) {
                        parent.setLeft(this.virtualNode);
                    } else {
                        parent.setRight(this.virtualNode);
                    }

                    setFirstParentToSecondParent(successor, node);
                    setFirstLeftChildToSecond(successor, node.left);
                    setFirstRightChildToSecond(successor, node.right);
                } else if (node.left.isRealNode() || node.right.isRealNode()) {
                    setFirstParentToSecondParent(node.left.isRealNode() ? node.left : node.right, node);
                } else {
                    parent.setLeft(this.virtualNode);
                    parent.setRight(this.virtualNode);
                }

                node.setParent(null);
                node.setLeft(null);
                node.setRight(null);

                break;
            } else if (k < node.getKey()) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (!node.isRealNode()) {
            return -1;
        }
        size--;

        int numRotations = 0, prevParentHeight;
        boolean fixHeight = true;

        while (parent != null) {
            parent.updateXor();

            if (fixHeight) {
                prevParentHeight = parent.getHeight();
                parent.updateHeight();

                if (parent.getHeight() == prevParentHeight) {
                    fixHeight = false;
                } else if (Math.abs(parent.getBalanceFactor()) > 1) {
                    parent = this.rotation(parent);
                    numRotations++;
                }
            }

            parent = parent.getParent();
        }

        return numRotations;
    }

    public AVLNode minNode() {
        if (this.empty()) {
            return null;
        }

        AVLNode x = this.root;

        while (x.left.isRealNode()) {
            x = x.left;
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

        return x == null ? null : x.getValue();
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

        while (x.right.isRealNode()) {
            x = x.right;
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
            if (arr[i] == 44) {
                int a = 1;
            }
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
        boolean[] arr = new boolean[this.size()];

        AVLNode x = this.minNode();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = x.getValue();
            x = this.successor(x);
        }

        return arr;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.size;
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
        AVLNode node = this.root;
        int trueCount = 0;

        while (node.getKey() != k) {
            if (k < node.getKey()) {
                node = node.left;
            } else {
                trueCount += (node.value ? 1 : 0) + node.left.trueCount;
                node = node.right;
            }
        }

        trueCount += (node.value ? 1 : 0) + node.left.trueCount;

        return trueCount % 2 == 1;
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
        if (node.right.isRealNode()) {
            node = node.right;

            while (node.left.isRealNode()) {
                node = node.left;
            }

            return node;
        } else {
            while (node.getParent() != null) {
                if (node == node.getParent().left) {
                    return node.getParent();
                }

                node = node.getParent();
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
        AVLNode node = this.minNode();
        int trueCnt = 0;

        while (true) {
            if (node.getValue()) {
                trueCnt++;
            }

            if (node.getKey() == k) {
                break;
            }

            node = this.successor(node);
        }

        return trueCnt % 2 == 1;
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
        private int trueCount;

        public AVLNode() {
            this.key = -1;
            this.value = null;
            this.height = -1;
            this.trueCount = 0;
        }

        public AVLNode(int key, boolean value) {
            this.left = AVLTree.this.virtualNode;
            this.right = AVLTree.this.virtualNode;

            this.key = key;
            this.value = value;
            this.height = 0;
            this.trueCount = this.value ? 1 : 0;
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

        private void updateHeight() {
            this.height = 1 + Math.max(this.left.getHeight(), this.right.getHeight());
        }

        public int getBalanceFactor() {
            return this.left.height - this.right.height;
        }

        public boolean getXor() {
            return this.trueCount % 2 == 1;
        }

        private void updateXor() {
            this.trueCount = (this.value ? 1 : 0) + this.left.trueCount + this.right.trueCount;
        }
    }

}
