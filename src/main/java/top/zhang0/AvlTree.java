package top.zhang0;

/**
 * <二叉平衡树>
 *
 * @Author Lin
 * @createTime 2022/7/23 17:18
 */
public class AvlTree<T extends Comparable<T>> {

    private Node<T> root;

    /**
     * LL型调整
     *
     * @param avlNode
     * @return
     */
    private Node<T> llRotate(Node<T> avlNode) {
        Node<T> node = avlNode.leftChild;
        avlNode.leftChild = node.rightChild;
        node.rightChild = avlNode;
        return node;
    }

    /**
     * RR型调整
     *
     * @param avlNode
     * @return
     */
    private Node<T> rrRotate(Node<T> avlNode) {
        Node<T> node = avlNode.rightChild;
        avlNode.rightChild = node.leftChild;
        node.leftChild = avlNode;
        return node;
    }

    /**
     * LR型调整
     *
     * @param avlNode
     * @return
     */
    private Node<T> lrRotate(Node<T> avlNode) {
        avlNode.leftChild = rrRotate(avlNode.leftChild);
        return llRotate(avlNode);
    }

    /**
     * RL型调整
     *
     * @param avlNode
     * @return
     */
    private Node<T> rlRotate(Node<T> avlNode) {
        avlNode.rightChild = llRotate(avlNode.rightChild);
        return rrRotate(avlNode);
    }

    /**
     * 实现平衡
     *
     * @param node
     * @return
     */
    private Node<T> balance(Node<T> node) {
        // 左子树比右子树高度大于1以上
        if (getAvlTreeHeight(node.leftChild) - getAvlTreeHeight(node.rightChild) > 1) {
            if (getAvlTreeHeight(node.leftChild.leftChild) >= getAvlTreeHeight(node.leftChild.rightChild)) {
                // 执行LL型调整
                node = llRotate(node);
            } else {
                // 执行LR型调整
                node = lrRotate(node);
            }
        } else if (getAvlTreeHeight(node.rightChild) - getAvlTreeHeight(node.leftChild) > 1) {
            if (getAvlTreeHeight(node.rightChild.rightChild) >= getAvlTreeHeight(node.rightChild.leftChild)) {
                // 执行RR型调整
                node = rrRotate(node);
            } else {
                // 执行RL型调整
                node = rlRotate(node);
            }
        }
        return node;
    }

    /**
     * 建立二叉平衡树
     *
     * @param data
     */
    public void createTree(T data) {
        if (data == null) {
            return;
        }
        root = insert(root, data);
    }

    private Node<T> insert(Node<T> root, T data) {
        // 根节点为空，说明树为空，则创建一颗树
        if (root == null) {
            return new Node<>(data);
        } else {
            if (compare(root, data) > 0) {
                root.leftChild = insert(root.leftChild, data);
                root = balance(root);
            } else if (compare(root, data) < 0) {
                root.rightChild = insert(root.rightChild, data);
                root = balance(root);
            }
            return root;
        }
    }

    private int compare(Node<T> root, T data) {
        return root.data.compareTo(data);
    }

    /**
     * 计算高度
     *
     * @param node
     * @return
     */
    private int getAvlTreeHeight(Node<T> node) {
        if (node == null) {
            return 0;
        } else {
            int leftWeight = getAvlTreeHeight(node.leftChild);
            int rightWeight = getAvlTreeHeight(node.rightChild);
            return Math.max(leftWeight, rightWeight) + 1;
        }
    }

    public void printTree() {
        System.out.print("前序遍历： ");
        preOrder(root);
        System.out.println();
        System.out.print("中序遍历： ");
        inOrder(root);
        System.out.println();
        System.out.print("后序遍历： ");
        postOrder(root);
        System.out.println();
    }

    private void preOrder(Node<T> root) {
        if (root != null) {
            System.out.print(root.data);
            System.out.print(" ");
            preOrder(root.leftChild);
            preOrder(root.rightChild);
        }
    }

    private void inOrder(Node<T> root) {
        if (root != null) {
            inOrder(root.leftChild);
            System.out.print(root.data);
            System.out.print(" ");
            inOrder(root.rightChild);
        }
    }

    private void postOrder(Node<T> root) {
        if (root != null) {
            postOrder(root.leftChild);
            postOrder(root.rightChild);
            System.out.print(root.data);
            System.out.print(" ");
        }
    }
}
