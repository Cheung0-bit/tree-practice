## 前言
树是数据结构中必学的一种数据结构。在实际应用中，常见的树结构有二叉搜索树、B树、B+树、AVL树、红黑树、字典树等。
对应的用途列举如下：

- B/B+树：主要用于文件系统以及数据库中做索引
- AVL树：平衡二叉树，windows对进程地址空间的管理用到了AVL
- 红黑树：平衡二叉树的一种改进，广泛的应用在C++STL中，如map、set，以及JDK中的HashMap、TreeMap等
- Trie（字典树）：又经常叫做前缀树，主要用于字符串检索、文本预测、词频统计等

## 二叉搜索树（BST Binary Search Tree）
### 概述
二叉搜索树也叫做二叉排序树，二叉搜索树采用二分思维将数据按照规则组装在一个树形结构中，大大提高了数据检索的效率。对于一颗普通的二叉树进行中序遍历，即可获取一个有序的数序列
### 特点
- 非空左子树的所有键值小于根节点的值
- 非空右子树的所有键值大于根节点的值
- 左右子树都是二叉搜索树

### 局限性

![image-20220723142156081](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220723142156081.png)

从图中可见，在改变一串数列组内顺序后，会得到不同的二叉搜索树。最好的情况下，搜索数据的时间复杂度为O(logN)，最坏的情况下，该树会退化为线性表，导致时间复杂度变为O(N)，因此，在二叉搜索树的基础上，又衍生出了AVL树和红黑树。后者基于二叉搜索树，做出了更多的限制。

### 代码实现

首先，我们需要构建一颗二叉树。首当其冲，我们顶一个数的节点类

~~~java
public class Node<T extends Comparable<T>> {

    /**
     * 数据域
     */
    public T data;

    /**
     * 节点子树的高度
     */
    public int height;

    /**
     * 左孩子
     */
    public Node<T> leftChild;

    /**
     * 右孩子
     */
    public Node<T> rightChild;

    public Node(T data) {
        this(data, null, null);
    }

    public Node(T data, Node<T> leftChild, Node<T> rightChild) {
        this.data = data;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

}
~~~

data字段由于不限定传入的对象类型，在这里使用泛型可以很好的规范

#### 构建二叉树

~~~java
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
~~~

- `createTree`方法，每当要插入一个新节点时，便调用该函数。该函数会递归的搜索整棵树，确保找到一个合适的位置插入新的节点（比根节点小则插在左子树，大则插在右子树）
- `printTree`方法，用于打印建造完成的树。底层提供前序、中序、后序三种遍历方式
- `compare`方法用于比较两个节点的大小，这里简单使用`hashcode`来作比较。众所周知，`Integer`对象的`hashcode`即为本身

#### 删除节点

在二叉搜索树中，删除一个节点算是相当复杂的一份工作。主要面临三种情况

- 节点为叶子节点，则直接删除
- 只包含左子树或者右子树的节点，则将父节点指向待删除的节点的唯一直接子节点
- 包含左右子树的节点，则寻找右子树最小值或左子树最大值替换该节点

示例代码：

~~~java
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
~~~

其中，`getMin`方法用于寻找树中的最小节点。在一颗二叉排序树中，一目了然的，最小节点就是这棵树最靠左的节点

~~~java
 private T getMin(Node<T> root) {
        Node<T> temp = root;
        while (temp.leftChild != null) {
            temp = temp.leftChild;
        }
        return temp.data;
 }
~~~

### 测试检验

编写单元测试，检验结果：

~~~java
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
~~~

其中`createBsTree`方法建立的二叉搜索树为：

~~~bash
前序遍历： 50 26 21 30 66 60 70 68 
中序遍历： 21 26 30 50 60 66 68 70 
后序遍历： 21 30 26 60 68 70 66 50 
~~~

`deleteNode`方法测试结果为：

~~~bash
前序遍历： 60 26 21 30 66 70 68 
中序遍历： 21 26 30 60 66 68 70 
后序遍历： 21 30 26 68 70 66 60 
~~~

观察得到，中序遍历的结果就是一组升序排列的数列

## 平衡二叉树（AVL）

### 概述

为解决二叉搜索树退化成一张链表的情况，改进出了AVL（取名与作者`G.M.Adelson-Velsky`和`E.M.Landis`）

一颗AVL具备的条件：

- 必须是一颗BST
- 每个节点的左右子树高度至多相差1

AVL树的查找、插入、删除等操作在平均和最坏的情况下都是O(logN)，得益于其一直在动态的维护平衡性

### 相关参数

- 平衡因子

  左子树高度减去右子树高度的值称为该节点的平衡因子BF(Balance Factor)。若BF的绝对值大于1，则表明需要进行调整

- 最小不平衡子树

  距离插入节点最近的，且平衡因子的绝对值大于1的节点为根的子树

### 调整方式

- LL型

  ![image-20220723165252254](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220723165252254.png)

- RR型

  ![image-20220723165416390](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220723165416390.png)

- LR型

  ![image-20220723165429874](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220723165429874.png)

- RL型

  ![image-20220723165447301](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220723165447301.png)

其中LL和RR型就可以看成将中间的节点”拎起来“

LR和RL型就先将拐出来的部分旋转为LL和RR型，再进行对应的操作

### 代码实现

#### LL型平衡

~~~java
private Node<T> llRotate(Node<T> avlNode) {
        Node<T> node = avlNode.leftChild;
        avlNode.leftChild = node.rightChild;
        node.rightChild = avlNode;
        return node;
}
~~~

#### RR型平衡

~~~java
private Node<T> rrRotate(Node<T> avlNode) {
        Node<T> node = avlNode.rightChild;
        avlNode.rightChild = node.leftChild;
        node.leftChild = avlNode;
        return node;
}
~~~

#### LR型平衡

~~~java
private Node<T> lrRotate(Node<T> avlNode) {
        avlNode.leftChild = rrRotate(avlNode.leftChild);
        return llRotate(avlNode);
}
~~~

#### RL型平衡

~~~java
private Node<T> rlRotate(Node<T> avlNode) {
        avlNode.rightChild = llRotate(avlNode.rightChild);
        return rrRotate(avlNode);
}
~~~

#### 平衡实现逻辑

~~~java
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
~~~

#### 建立AVL逻辑

~~~java
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
~~~

### 功能测试

~~~java
private static final Integer[] arrays = new Integer[]{26, 21, 30, 50, 60, 66, 68, 70};

@Test
public void createAvlTree() {
    AvlTree<Integer> avlTree = new AvlTree<>();
    for (Integer data : arrays) {
        avlTree.createTree(data);
    }
    avlTree.printTree();
}
~~~

~~~bash
前序遍历： 26 21 66 50 30 60 68 70 
中序遍历： 21 26 30 50 60 66 68 70 
后序遍历： 21 30 60 50 70 68 66 26 
~~~

### 代价分析

- 查找：效率很好，平均情况和最坏情况都是O(logN)
- 插入：每插入一个节点至多需要旋一次旋转。总体时间复杂度为O(logN)
- 删除：每一次删除最多需要O(logN)次旋转，复杂度为O(logN)

AVL树的结构相当的稳定，故查询效率相当的高。但每次插入或删除节点时都会进行动态的维护平衡，带来了不小i的时间成本。所以当查询和删除频率不高时，采用AVL树可以带来极高的查询效率。但当插入和删除频率较高时，AVL的性能并不理想，此时，我们选择采用红黑树

## 红黑树（Red Black Tree）

在AVL中提到了，当插入和删除频率较高时，我们选择红黑树来降低因不断的维护平衡带来的时间损耗。在诸多地方（比如JDK1.8的HashMap……）得到了广泛的应用。那么，什么是红黑树，为什么就这么牛逼？我们一起来解开其神秘的面纱~

### 什么是红黑树

![](https://0-bit.oss-cn-beijing.aliyuncs.com/Red-black_tree_example.svg.png)

> 红黑树是一种特定类型的[二叉树](https://baike.baidu.com/item/二叉树)，它是在计算机科学中用来组织数据比如数字的块的一种结构。 [4] 
>
> 红黑树是一种平衡二叉查找树的变体，它的左右子树高差有可能大于 1，所以红黑树不是严格意义上的[平衡二叉树](https://baike.baidu.com/item/平衡二叉树/10421057)（AVL），但 对之进行平衡的代价较低， 其平均统计性能要强于 AVL 。 [2] 
>
> 由于每一棵红黑树都是一颗[二叉排序树](https://baike.baidu.com/item/二叉排序树/10905079)，因此，在对红黑树进行查找时，可以采用运用于普通二叉排序树上的查找算法，在查找过程中不需要颜色信息。

以上是百度百科的介绍。我简单总结就是

- 一种二叉树
- 具备平衡效果
- 由红黑两种颜色的节点组成
- 增删改差性能很强👍

那为什么叫红黑树呢，难道红色和黑色有什么魔力吗？是这样的，红黑树的发明者鲁道夫·贝尔非常喜欢红色和黑色~

### 红黑树的性质

* 性质1：每个节点非黑即红
* 性质2：根节点是黑色
* 性质3：每个叶子节点（NULL）是黑色
* 性质4：每个红色节点的两个子节点一定都是黑色。也就是说不存在两个红色节点相连
* 性质5：任意节点到每个叶子节点的路径都包含相同数量的黑节点，也就是所谓的黑色完美平衡
  * 性质5.1：可能推导出如果一个节点拥有一个黑色节点，那么该节点肯定有两个子节点

> 啊！条条框框的好多呀！

不要被这么多性质吓到，先思考一个问题，红黑树到底是怎么产生的，为什么刚好用两种颜色来描述节点，不能是三种？难道因为我们都是玩二进制的男人吗？

首先，我们要了解另一种树：2-3树（念二三树）

### 先说一说2-3树

2-3树不是一种严格由一个节点只有一个数据域的节点构成的树，其存在2叉节点和3叉节点

![image-20220725162751893](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220725162751893.png)

其中，只有一个数据域的就是2叉节点：左叉指向小与该节点数据域的节点，右叉则是相反的情况

有两个数据域的就是3叉节点，左指向小于该节点中较小数的节点，中间是介于该节点两个数据域的节点，右边则大于较大者的节点

来看一下1-7是如何构建一颗2-3树的过程体会一下：

![image-20220725163525671](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220725163525671.png)

可以看到只要在建树的过程中满足2-3树的限制，就可以保证这棵树保证一定的平衡度。

2-3树跟红黑树之间就像是在面向对象编程过程中接口跟具体实现类的关系，红黑树就是针对2-3树的一种实现

2-3树的概念十分模糊，虽然我们通过示意图可以很简单的体会到2-3树想要表达的意思，但是编程就相当的难实现。于是就有智者创建了红黑树，通过红与黑两种颜色在创建树的过程中加以约束，以满足2-3树的性质

### 2-3树与红黑树的比对

那么，2-3树跟红黑树有什么联系，我们来看看2-3树与红黑树之间是怎么转化的：

![image-20220725165737960](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220725165737960.png)

我个人的理解（仅提供一个看待红黑树的角度）：

- 红色节点可看作是黑色父节点的附庸，是一种辅助的展示手段。因为红色节点会合成到黑色节点中成为2-3树中的3叉节点
- 也因此，不可能有两个相连的红色节点，否则无法还原成一个3叉节点。这样就解释了性质4
- 当一颗红黑树还原成2-3树时，就是一颗”完美二叉树“！（肥肥胖胖，完美稳定） 此时也就是江湖上说的**黑色平衡**。此时也理所当然满足了性质5任意节点到每个叶子节点的路径都包含相同数量的黑节点
- 当满足所有条件性质时，最长的一条路径就不会超过最短的一条路径的2倍。使得查找的时间复杂最坏不会超过O(2logN)

### 红黑树插入节点的分类讨论

#### 三种基本操作

- 变色

  - 顾名思义，红-黑的颜色转换

- 左旋

  - 对应AVL中的概念，为达到黑色平衡所做的操作
  - 示例代码

  ~~~java
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
  ~~~

- 右旋

  - 对应AVL中的概念，为达到黑色平衡所做的操作
  - 示例代码

  ~~~java
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
  ~~~

**红黑树的构建并不复杂，只要将所有可能出现的情况讨论到位，代码按部就班的写就行了**

#### 情况1

为空树，不废话直接插入。但注意根节点为黑色

#### 情况2

插入的Key已经存在。搜索到对应的节点，更新数据值

#### 情况3

插入的节点的父节点为黑节点，那么直接上红色节点，不会破坏黑色平衡

#### 情况4

父亲节点为红色，讨论叔叔节点的颜色

- 情况4.1

  - 叔叔节点存在且为红色节点

  - 处理方案

    - 父辈节点变为黑色
    - 爷爷节点变为红色
    - 将爷爷节点设为当前节点，进行后续操作

    ![image-20220725174927748](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220725174927748.png)

- 情况4.2

  叔叔节点不存在或为空

  - 4.2.1

    - 新插入的节点为父亲的左孩子
    - 先变色，后右旋

    ![image-20220725175039510](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220725175039510.png)

  - 4.2.2

    - 新插入的节点为父亲的右孩子
    - 先左旋，再变色，后右旋

    ![image-20220725175758098](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20220725175758098.png)

- 4.3 

​	4.2情况的对称操作

代码示例

```java
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
```

其中，红黑树节点类代码：

~~~java
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
~~~

### 引入展示工具类

从网上借鉴了一个工具类，可以形象的展示一颗红黑树

~~~java
public class TreeOperation {

      /*
    树的结构示例：
              1
            /   \
          2       3
         / \     / \
        4   5   6   7
    */

    /**
     * 用于获得树的层数
     *
     * @param root
     * @return
     */
    public static int getTreeDepth(RbNode<?> root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.leftChild), getTreeDepth(root.rightChild)));
    }

    private static void writeArray(RbNode<?> currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currNode == null) {
            return;
        }
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = String.format("%s-%s ", currNode.key, "true".equals(String.valueOf(currNode.color)) ? "R" : "B");

        // 计算当前位于树的第几层
        int currLevel = ((rowIndex + 1) / 2);
        // 若到了最后一层，则返回
        if (currLevel == treeDepth) {
            return;
        }
        // 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
        int gap = treeDepth - currLevel - 1;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
        if (currNode.leftChild != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currNode.leftChild, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
        if (currNode.rightChild != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currNode.rightChild, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }


    public static void show(RbNode<?> root) {
        if (root == null) {
            System.out.println("EMPTY!");
        }
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        // 最后一行的宽度为2的（n - 1）次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
        // res[0][(arrayWidth + 1)/ 2] = (char)(root.val + '0');
        writeArray(root, 0, arrayWidth / 2, res, treeDepth);

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        for (String[] line : res) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length; i++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2 : line[i].length() - 1;
                }
            }
            System.out.println(sb.toString());
        }
    }

}
~~~

### 测试与总结

~~~java
private static final Integer[] arrays = new Integer[]{3, 5, 8, 7, 15, 19, 18, 30, 33};

@Test
public void createRbTree() {
    RbTree<Integer> rbTree = new RbTree<>();
    for (Integer key : arrays) {
        rbTree.createTree(key);
    }
    TreeOperation.show(rbTree.root);
}
~~~

~~~bash
            8-B          
         /     \         
      5-R         18-R     
    /   \       /   \    
  3-B     7-B 15-B      30-B 
                     / \ 
                    19-R  33-R 
~~~

关于红黑树节点的删除面临的情况较多，比插入要复杂的多，这里就不再进行讨论了。本质也是讨论出所有的情况，将每种情况写出对应的操作代码，再组装起来，就可以了，只不过复杂度很高，也相当烧脑。能够牢固的把握和理解红黑树的构建过程和与2-3树的转化的概念，就不错啦！
