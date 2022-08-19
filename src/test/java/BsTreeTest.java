import org.junit.Test;
import top.zhang0.BsTree;

/**
 * <二叉搜索树测试>
 *
 * @Author Lin
 * @createTime 2022/7/23 14:58
 */
public class BsTreeTest {

    private static final Integer[] arrays = new Integer[]{50, 66, 60, 26, 21, 30, 70, 68};

    @Test
    public void createBsTree() {
        BsTree<Integer> bsTree = new BsTree<>();
        for (Integer data : arrays) {
            bsTree.createTree(data);
        }
        bsTree.printTree();
    }

    @Test
    public void deleteNode() {
        BsTree<Integer> bsTree = new BsTree<>();
        for (Integer data : arrays) {
            bsTree.createTree(data);
        }
        bsTree.deleteNode(50);
        bsTree.printTree();
    }


}
