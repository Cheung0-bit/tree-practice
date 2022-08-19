package top.zhang0;

/**
 * <二叉搜索树>
 *
 * @Author Lin
 * @createTime 2022/7/23 14:37
 */
public class BsTree<T extends Comparable<T>> {

    private Node<T> root;

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
            } else if (compare(root, data) < 0) {
                root.rightChild = insert(root.rightChild, data);
            }
            return root;
        }
    }

    public void deleteNode(T data) {
        if (data == null) {
            return;
        }
        root = delete(root, data);
    }

    private Node<T> delete(Node<T> root, T data) {
        if (compare(root, data) < 0) {
            root.rightChild = delete(root.rightChild, data);
        } else if (compare(root, data) > 0) {
            root.leftChild = delete(root.leftChild, data);
        } else {
            // 该节点拥有左右子树
            if (root.leftChild != null && root.rightChild != null) {
                T temp = getMin(root.rightChild);
                root.data = temp;
                root.rightChild = delete(root.rightChild, temp);
            } else {
                if (root.leftChild != null) {
                    root = root.leftChild;
                } else {
                    root = root.rightChild;
                }
            }
        }
        return root;
    }

    private T getMin(Node<T> root) {
        Node<T> temp = root;
        while (temp.leftChild != null) {
            temp = temp.leftChild;
        }
        return temp.data;
    }

    private T getMax(Node<T> root) {
        Node<T> temp = root;
        while (temp.rightChild != null) {
            temp = temp.rightChild;
        }
        return temp.data;
    }


    private int compare(Node<T> root, T data) {
        return root.data.compareTo(data);
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
