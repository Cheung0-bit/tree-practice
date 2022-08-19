package top.zhang0;

/**
 * <红黑树节点>
 * 性质1：每个节点非黑即红
 * 性质2：根节点是黑色
 * 性质3：每个叶子节点（NULL）是黑色
 * 性质4：每个红色节点的两个子节点一定都是黑色。也就是说不存在两个红色节点相连
 * 性质5：任意节点到每个叶子节点的路径都包含相同数量的黑节点，也就是所谓的黑色完美平衡
 * 性质5.1：可能推导出如果一个节点拥有一个黑色节点，那么该节点肯定有两个子节点
 *
 * @Author Lin
 * @createTime 2022/7/25 10:11
 */
public class RbNode<T extends Comparable<T>> {

    /**
     * 节点颜色
     */
    public boolean color;

    /**
     * 节点键值
     */
    public T key;

    /**
     * 左孩子指针
     */
    public RbNode<T> leftChild;

    /**
     * 右孩子指针
     */
    public RbNode<T> rightChild;

    /**
     * 父结点指针，红黑树经常涉及到兄弟，叔叔，侄子，有个父结点指针方便操作。
     */
    public RbNode<T> parent;

    public RbNode(boolean color, T key) {
        this(color, key, null, null, null);
    }

    public RbNode(boolean color, T key, RbNode<T> leftChild, RbNode<T> rightChild, RbNode<T> parent) {
        this.color = color;
        this.key = key;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
    }
}
