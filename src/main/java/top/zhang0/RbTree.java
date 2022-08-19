package top.zhang0;

/**
 * <红黑树>
 *
 * @Author Lin
 * @createTime 2022/7/25 10:31
 */
public class RbTree<T extends Comparable<T>> {

    /**
     * 根结点指针
     */
    public RbNode<T> root;

    /**
     * 红色标记
     */
    private static final boolean RED = true;

    /**
     * 黑色标记
     */
    private static final boolean BLACK = false;

    /**
     * 建树
     *
     * @param key
     */
    public void createTree(T key) {
        if (key == null) {
            return;
        }
        if (this.root == null) {
            this.root = new RbNode<>(BLACK, key);
        } else {
            insert(root, key);
        }
    }

    /**
     * 插入节点
     *
     * @param node
     * @param key
     * @return
     */
    private void insert(RbNode<T> node, T key) {
        if (compare(node, key) > 0) {
            if (node.leftChild == null) {
                node.leftChild = new RbNode<>(RED, key);
                node.leftChild.parent = node;
                insertFixUp(node.leftChild);
            } else {
                insert(node.leftChild, key);
            }
        } else if (compare(node, key) < 0) {
            if (node.rightChild == null) {
                node.rightChild = new RbNode<>(RED, key);
                node.rightChild.parent = node;
                insertFixUp(node.rightChild);
            } else {
                insert(node.rightChild, key);
            }
        }
    }

    /**
     * 修复
     *
     * @param node
     */
    private void insertFixUp(RbNode<T> node) {
        RbNode<T> parent, grandParent, uncle;
        //父结点不是根，且为红色，才进行修复
        while ((parent = node.parent) != null && parent.color == RED) {
            grandParent = parent.parent;
            if (parent == grandParent.leftChild) {
                //叔叔为红色 对应4.1情况 直接变色
                if ((uncle = grandParent.rightChild) != null && uncle.color == RED) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    grandParent.color = RED;
                    node = grandParent;
                    // 将祖父当成新插入结点继续修复, 知道黑色完美平衡
                    continue;
                }
                // 对应 4.2.2 情况 先进行旋转操作 转化为 4.2.1 情况
                if (parent.rightChild == node) {
                    leftRotate(parent);
                    // 旋转后node和parent的指向关系已经发生了变化
                    // 但恢复到情况4.2.1 需要重新调整node和parent的实际关系
                    // 因此调换两个指针的指向
                    RbNode<T> tmp = parent;
                    parent = node;
                    node = tmp;
                }
                // 对应 4.2.1 情况 先变色 后旋转
                parent.color = BLACK;
                grandParent.color = RED;
                rightRotate(grandParent);
            } else {
                //对称操作 就不多废话了
                if ((uncle = grandParent.leftChild) != null && uncle.color == RED) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    grandParent.color = RED;
                    node = grandParent;
                    continue;
                }
                if (parent.leftChild == node) {
                    rightRotate(parent);
                    RbNode<T> tmp = parent;
                    parent = node;
                    node = tmp;
                }
                parent.color = BLACK;
                grandParent.color = RED;
                leftRotate(grandParent);
            }
        }
        this.root.color = BLACK;
    }

    private int compare(RbNode<T> root, T key) {
        return root.key.compareTo(key);
    }

    @SuppressWarnings("all")
    private void leftRotate(RbNode<T> x) {
        RbNode<T> y = x.rightChild;
        x.rightChild = y.leftChild;
        // 若左孩子不为空，应完善该节点的父亲指针指向
        if (y.leftChild != null) {
            y.leftChild.parent = x;
        }
        // 交换父亲域
        y.parent = x.parent;
        if (x.parent != null) {
            // 寻找正确的子节点指向
            if (x.parent.leftChild == x) {
                x.parent.leftChild = y;
            } else {
                x.parent.rightChild = y;
            }
        } else {
            this.root = y;
        }
        y.leftChild = x;
        x.parent = y;
    }

    /**
     * 右旋操作
     *
     * @param x
     */
    @SuppressWarnings("all")
    private void rightRotate(RbNode<T> x) {
        RbNode<T> y = x.leftChild;
        x.leftChild = y.rightChild;
        // 若右孩子不为空，应完善该节点的父亲指针指向
        if (y.rightChild != null) {
            y.rightChild.parent = x;
        }
        // 交换父亲域
        y.parent = x.parent;
        if (x.parent != null) {
            // 寻找正确的子节点指向
            if (x.parent.leftChild == x) {
                x.parent.leftChild = y;
            } else {
                x.parent.rightChild = y;
            }
        } else {
            this.root = y;
        }
        y.rightChild = x;
        x.parent = y;
    }

}
