/**
 * id: 314023516
 * name: Alon Bebchuk
 * username: alonbebchuk
 * <p>
 * id: 328634373
 * name: Aryeh Gorun
 * username: aryehgorun
 * <p>
 * <p>
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
    private AVLNode min;
    private AVLNode max;

    /**
     * This constructor creates an empty AVLTree.
     * <p>
     * Complexity - O(1)
     */
    public AVLTree() {
        this.virtualNode = new AVLNode();
        this.root = virtualNode;
        this.size = 0;
        this.min = null;
        this.max = null;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     * <p>
     * Complexity - O(1)
     */
    public boolean empty() {
        return !this.root.isRealNode();
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     * <p>
     * Complexity - O(log n)
     */
    public Boolean search(int k) {
        AVLNode node = this.root;

        while (node.isRealNode()) {
            if (node.getKey() == k) {
                return node.getValue();
            } else {
                node = (k < node.getKey()) ? node.getLeft() : node.getRight();
            }
        }

        return null;
    }

    /**
     * private void setFirstParentToSecondParent(AVLNode node1, AVLNode node2)
     * <p>
     * sets the parent of node1 to be the parent of node2 and also updates the reciprocal connection
     * <p>
     * Complexity - O(1)
     */
    private void setFirstParentToSecondParent(AVLNode node1, AVLNode node2) {
        AVLNode node2Parent = node2.getParent();

        node1.setParent(node2Parent);

        if (node2Parent == null) {
            this.root = node1;
        } else if (node2 == node2Parent.getLeft()) {
            node2Parent.setLeft(node1);
        } else {
            node2Parent.setRight(node1);
        }
    }

    /**
     * private void setFirstRightChildToSecond(AVLNode node, AVLNode rightChild)
     * <p>
     * sets the right child of node to be rightChild and also updates the reciprocal connection
     * <p>
     * Complexity - O(1)
     */
    private void setFirstRightChildToSecond(AVLNode node, AVLNode rightChild) {
        node.setRight(rightChild);

        if (rightChild.isRealNode()) {
            rightChild.setParent(node);
        }
    }

    /**
     * private void setFirstLeftChildToSecond(AVLNode node, AVLNode leftChild)
     * <p>
     * sets the left child of node to be leftChild and also updates the reciprocal connection
     * <p>
     * Complexity - O(1)
     */
    private void setFirstLeftChildToSecond(AVLNode node, AVLNode leftChild) {
        node.setLeft(leftChild);

        if (leftChild.isRealNode()) {
            leftChild.setParent(node);
        }
    }

    /**
     * private void rotationHeightXorUpdate(AVLNode node, AVLNode nodeChild, AVLNode nodeGrandChild)
     * <p>
     * updates height and xor of nodes whose values may have changed after a rotation
     * <p>
     * Complexity - O(1)
     */
    private void rotationHeightXorUpdate(AVLNode node, AVLNode nodeChild, AVLNode nodeGrandChild) {
        node.updateHeight();
        nodeChild.updateHeight();
        if (nodeGrandChild != null) {
            nodeGrandChild.updateHeight();
        }

        node.updateTrueCnt();
        nodeChild.updateTrueCnt();
        if (nodeGrandChild != null) {
            nodeGrandChild.updateTrueCnt();
        }
    }

    /**
     * private AVLNode rotation(AVLNode node)
     * <p>
     * given an AVL criminal node, performs matching rotation and
     * returns the node which is now in the criminal's original location
     * <p>
     * Complexity - O(1)
     */
    private AVLNode rotation(AVLNode node) {
        int nodeBF = node.getBalanceFactor();
        AVLNode nodeChild = (nodeBF > 0) ? node.getLeft() : node.getRight(), nodeGrandChild = null;
        int nodeChildBF = nodeChild.getBalanceFactor();

        if (nodeBF > 0) {
            if (nodeChildBF >= 0) {
                // right rotation
                this.setFirstParentToSecondParent(nodeChild, node);
                this.setFirstLeftChildToSecond(node, nodeChild.getRight());
                this.setFirstRightChildToSecond(nodeChild, node);
            } else {
                nodeGrandChild = nodeChild.getRight();

                // left then right rotation
                this.setFirstParentToSecondParent(nodeGrandChild, node);
                this.setFirstRightChildToSecond(nodeChild, nodeGrandChild.getLeft());
                this.setFirstLeftChildToSecond(node, nodeGrandChild.getRight());
                this.setFirstLeftChildToSecond(nodeGrandChild, nodeChild);
                this.setFirstRightChildToSecond(nodeGrandChild, node);
            }
        } else {
            if (nodeChildBF <= 0) {
                // left rotation
                this.setFirstParentToSecondParent(nodeChild, node);
                this.setFirstRightChildToSecond(node, nodeChild.getLeft());
                this.setFirstLeftChildToSecond(nodeChild, node);
            } else {
                nodeGrandChild = nodeChild.getLeft();

                // right then left rotation
                this.setFirstParentToSecondParent(nodeGrandChild, node);
                this.setFirstRightChildToSecond(node, nodeGrandChild.getLeft());
                this.setFirstLeftChildToSecond(nodeChild, nodeGrandChild.getRight());
                this.setFirstLeftChildToSecond(nodeGrandChild, node);
                this.setFirstRightChildToSecond(nodeGrandChild, nodeChild);
            }
        }

        this.rotationHeightXorUpdate(node, nodeChild, nodeGrandChild);

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
     * <p>
     * Complexity - O(log n)
     */
    public int insert(int k, boolean i) {
        AVLNode parent = null;
        AVLNode node = this.root;

        while (node.isRealNode()) {
            if (node.getKey() == k) {
                return -1;
            }

            parent = node;
            node = (k < node.getKey()) ? node.getLeft() : node.getRight();
        }

        AVLNode newNode = new AVLNode(k, i);

        // update min/max pointers
        if (this.min == null || newNode.getKey() < this.min.getKey()) {
            this.min = newNode;
        }
        if (this.max == null || newNode.getKey() > this.max.getKey()) {
            this.max = newNode;
        }

        // add newNode to tree in proper location and update predecessors and successors of affected nodes
        if (parent == null) {
            this.root = newNode;
        } else if (k < parent.getKey()) {
            this.setFirstLeftChildToSecond(parent, newNode);

            newNode.predecessor = parent.predecessor;
            newNode.successor = parent;
            if (parent.predecessor != null) {
                parent.predecessor.successor = newNode;
            }
            parent.predecessor = newNode;
        } else {
            this.setFirstRightChildToSecond(parent, newNode);

            newNode.predecessor = parent;
            newNode.successor = parent.successor;
            if (parent.successor != null) {
                parent.successor.predecessor = newNode;
            }
            parent.successor = newNode;
        }

        // following path from inserted node to root fixing criminals and updating height and trueCnt values of nodes
        // and counting number of rotations and height updates made
        int fixPathLen = 1, prevParentHeight;
        boolean fixHeight = true, fixTrueCnt = i;

        while (parent != null) {
            if (fixTrueCnt) {
                parent.trueCnt++;
            }

            if (fixHeight) {
                if (Math.abs(parent.getBalanceFactor()) >= 2) {
                    parent = this.rotation(parent);
                    fixPathLen++;
                    fixHeight = false;
                } else {
                    prevParentHeight = parent.getHeight();
                    parent.updateHeight();

                    if (parent.getHeight() == prevParentHeight) {
                        fixHeight = false;
                    } else {
                        fixPathLen++;
                    }
                }
            }

            if (!fixTrueCnt && !fixHeight) {
                break;
            }

            parent = parent.getParent();
        }

        size++;

        return fixPathLen;
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     * <p>
     * Complexity - O(log n)
     */
    public int delete(int k) {
        AVLNode parent = null;
        AVLNode node = this.root;
        AVLNode successor = null;

        // update min/max pointers
        if (this.min != null && k == this.min.getKey()) {
            node = this.min;
            this.min = this.min.getParent();
        }
        if (this.max != null && k == this.max.getKey()) {
            node = this.max;
            this.max = this.max.getParent();
        }

        while (node.isRealNode()) {
            if (node.getKey() == k) {
                // deleting node if it has two children
                if (node.getLeft().isRealNode() && node.getRight().isRealNode()) {
                    successor = this.successor(node);

                    if (successor == node.getRight()) {
                        parent = successor;
                    } else {
                        parent = successor.getParent();
                        this.setFirstLeftChildToSecond(parent, successor.getRight());
                        this.setFirstRightChildToSecond(successor, node.getRight());
                    }

                    this.setFirstParentToSecondParent(successor, node);
                    this.setFirstLeftChildToSecond(successor, node.getLeft());
                } else {
                    parent = node.getParent();

                    // deleting node if it has one child
                    if (node.getLeft().isRealNode() || node.getRight().isRealNode()) {
                        this.setFirstParentToSecondParent(
                                node.getLeft().isRealNode() ? node.getLeft() : node.getRight(), node
                        );
                    }
                    // deleting node if it is leaf
                    else {
                        if (this.size() == 1) {
                            this.root = this.virtualNode;
                        } else if (node == parent.getLeft()) {
                            parent.setLeft(this.virtualNode);
                        } else {
                            parent.setRight(this.virtualNode);
                        }
                    }
                }

                node.setParent(null);
                node.setLeft(null);
                node.setRight(null);

                // update predecessors and successors of affected nodes
                if (node.predecessor != null) {
                    node.predecessor.successor = node.successor;
                }
                if (node.successor != null) {
                    node.successor.predecessor = node.predecessor;
                }

                break;
            } else {
                node = (k < node.getKey()) ? node.getLeft() : node.getRight();
            }
        }

        if (!node.isRealNode()) {
            return -1;
        }

        // following path from parent of physically deleted node to root
        // fixing criminals and updating height and trueCnt values of nodes
        // and counting number of rotations and height updates made
        int fixPathLen = 0, prevParentHeight;
        boolean fixHeight = true, fixTrueCnt = node.getValue() || (successor == null ? false : successor.getValue());

        while (parent != null) {
            if (fixTrueCnt) {
                parent.updateTrueCnt();
            }

            if (fixHeight) {
                if (Math.abs(parent.getBalanceFactor()) >= 2) {
                    parent = this.rotation(parent);
                    fixPathLen++;
                } else {
                    prevParentHeight = parent.getHeight();
                    parent.updateHeight();

                    if (parent.getHeight() == prevParentHeight) {
                        fixHeight = false;
                    } else {
                        fixPathLen++;
                    }
                }
            }

            if (!fixTrueCnt && !fixHeight) {
                break;
            }

            parent = parent.getParent();
        }

        size--;

        return fixPathLen;
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     * <p>
     * Complexity - O(1)
     */
    public Boolean min() {
        return this.min == null ? null : this.min.getValue();
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     * <p>
     * Complexity - O(1)
     */
    public Boolean max() {
        return this.max == null ? null : this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     * <p>
     * Complexity - O(n)
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size()];

        AVLNode x = this.min;

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
     * <p>
     * Complexity - O(n)
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.size()];

        AVLNode x = this.min;

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
     * <p>
     * Complexity - O(1)
     */
    public int size() {
        return this.size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * <p>
     * Complexity - O(1)
     */
    public AVLNode getRoot() {
        return this.empty() ? null : this.root;
    }

    /**
     * public boolean prefixXor(int k)
     * <p>
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     * <p>
     * precondition: this.search(k) != null
     * <p>
     * Complexity - O(log n)
     */
    public boolean prefixXor(int k) {
        int trueCount = 0;
        AVLNode node = this.root;

        while (true) {
            if (k < node.getKey()) {
                node = node.getLeft();
            } else {
                trueCount += node.getLeft().trueCnt + (node.value ? 1 : 0);

                if (k == node.getKey()) {
                    break;
                } else {
                    node = node.getRight();
                }
            }
        }


        return trueCount % 2 == 1;
    }

    /**
     * public AVLNode successor
     * <p>
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     * <p>
     * Complexity - O(1)
     */
    public AVLNode successor(AVLNode node) {
        return node.successor;
    }

    /**
     * public boolean succPrefixXor(int k)
     * <p>
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     * <p>
     * precondition: this.search(k) != null
     * <p>
     * Complexity - O(n)
     */
    public boolean succPrefixXor(int k) {
        int trueCnt = 0;
        AVLNode node = this.min;

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
        private AVLNode parent;
        private AVLNode left;
        private AVLNode right;

        private AVLNode predecessor;
        private AVLNode successor;

        private int key;
        private Boolean value;
        private int height;
        private int trueCnt;

        //constructor for virtual node
        public AVLNode() {
            this.parent = null;
            this.left = null;
            this.right = null;

            this.predecessor = null;
            this.successor = null;

            this.key = -1;
            this.value = null;
            this.height = -1;
            this.trueCnt = 0;
        }

        //constructor for real node
        public AVLNode(int key, boolean value) {
            this.parent = null;
            this.left = AVLTree.this.virtualNode;
            this.right = AVLTree.this.virtualNode;

            this.predecessor = null;
            this.successor = null;

            this.key = key;
            this.value = value;
            this.height = 0;
            this.trueCnt = this.value ? 1 : 0;
        }

        //returns node's key (for virtual node return -1)
        public int getKey() {
            return this.key;
        }

        //returns node's value [info] (for virtual node return null)
        public Boolean getValue() {
            return this.value;
        }

        //sets left child
        public void setLeft(AVLNode node) {
            this.left = node;
        }

        //returns left child
        //if called for virtual node, return value is ignored.
        public AVLNode getLeft() {
            return this.left;
        }

        //sets right child
        public void setRight(AVLNode node) {
            this.right = node;
        }

        //returns right child
        //if called for virtual node, return value is ignored.
        public AVLNode getRight() {
            return this.right;
        }

        //sets parent
        public void setParent(AVLNode node) {
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        public AVLNode getParent() {
            return this.parent;
        }

        //returns true if this is a non-virtual AVL node
        public boolean isRealNode() {
            return this.getKey() != -1;
        }

        //sets the height of the node
        public void setHeight(int height) {
            this.height = height;
        }

        //returns the height of the node (-1 for virtual nodes)
        public int getHeight() {
            return this.height;
        }

        //updates the height of the node
        private void updateHeight() {
            this.setHeight(1 + Math.max(this.getLeft().getHeight(), this.getRight().getHeight()));
        }

        //returns the balance factor of the node
        private int getBalanceFactor() {
            return this.getLeft().getHeight() - this.getRight().getHeight();
        }

        //updates the trueCnt of the node
        private void updateTrueCnt() {
            this.trueCnt = (this.value ? 1 : 0) + this.getLeft().trueCnt + this.getRight().trueCnt;
        }
    }

}
