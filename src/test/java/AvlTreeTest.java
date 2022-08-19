import org.junit.Test;
import top.zhang0.AvlTree;

/**
 * <AVL树测试>
 *
 * @Author Lin
 * @createTime 2022/7/23 18:03
 */
public class AvlTreeTest {

    private static final Integer[] arrays = new Integer[]{26, 21, 30, 50, 60, 66, 68, 70};
//    private static final Integer[] arrays = new Integer[]{1, 2, 3};

    @Test
    public void createAvlTree() {
        AvlTree<Integer> avlTree = new AvlTree<>();
        for (Integer data : arrays) {
            avlTree.createTree(data);
        }
        avlTree.printTree();
    }

}
