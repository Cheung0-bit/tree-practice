import org.junit.Test;
import top.zhang0.RbTree;
import top.zhang0.TreeOperation;

/**
 * <红黑树测试>
 *
 * @Author Lin
 * @createTime 2022/7/25 11:48
 */
public class RbTreeTest {

    private static final Integer[] arrays = new Integer[]{3, 5, 8, 7, 15, 19, 18, 30, 33};

    @Test
    public void createRbTree() {
        RbTree<Integer> rbTree = new RbTree<>();
        for (Integer key : arrays) {
            rbTree.createTree(key);
        }
        TreeOperation.show(rbTree.root);
    }

}
