public class NonAVLTree {
    private NonAVLNode virtualNode;
    private NonAVLNode root;

    public NonAVLTree() {
        this.virtualNode = new NonAVLNode();
        this.root = virtualNode;
    }

    private void setFirstRightChildToSecond(NonAVLNode node, NonAVLNode rightChild) {
        node.setRight(rightChild);

        if (rightChild.isRealNode()) {
            rightChild.setParent(node);
        }
    }

    private void setFirstLeftChildToSecond(NonAVLNode node, NonAVLNode leftChild) {
        node.setLeft(leftChild);

        if (leftChild.isRealNode()) {
            leftChild.setParent(node);
        }
    }

    public int insert(int k, boolean i) {
        NonAVLNode parent = null;
        NonAVLNode node = this.root;

        while (node.isRealNode()) {
            if (node.getKey() == k) {
                return -1;
            }

            parent = node;
            node = (k < node.getKey()) ? node.getLeft() : node.getRight();
        }

        NonAVLNode newNode = new NonAVLNode(k, i);

        if (parent == null) {
            this.root = newNode;
        } else if (k < parent.getKey()) {
            this.setFirstLeftChildToSecond(parent, newNode);
        } else {
            this.setFirstRightChildToSecond(parent, newNode);
        }

        return 1;
    }

    public class NonAVLNode {
        protected NonAVLNode parent;
        protected NonAVLNode left;
        protected NonAVLNode right;

        protected int key;
        protected Boolean value;

        public NonAVLNode() {
            this.key = -1;
            this.value = null;
        }

        public NonAVLNode(int key, boolean value) {
            this.left = NonAVLTree.this.virtualNode;
            this.right = NonAVLTree.this.virtualNode;

            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return this.key;
        }

        public void setLeft(NonAVLNode node) {
            this.left = node;
        }

        public NonAVLNode getLeft() {
            return this.left;
        }

        public void setRight(NonAVLNode node) {
            this.right = node;
        }

        public NonAVLNode getRight() {
            return this.right;
        }

        public void setParent(NonAVLNode node) {
            this.parent = node;
        }

        public boolean isRealNode() {
            return this.getKey() != -1;
        }
    }
}