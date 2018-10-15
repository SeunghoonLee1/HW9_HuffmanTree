package edu.miracosta.cs113;

import java.io.Serializable;

public class BinaryTree<E> implements Serializable {
    protected Node<E> root;

    /**
     * Default constructor to build empty BinaryTree
     */
    public BinaryTree() {
        root = null;
    }

    /**
     * Node constructor to build empty BinaryTree with parameter as root
     * @param root shallow copied and stored as root
     */
    protected BinaryTree(Node<E> root) {
        this.root = root;
    }

    /**
     * Full constructor to build BinaryTree from provided data (for root)
     * and left+right children/subtrees
     *
     * @param data to be stored for root node
     * @param leftTree left subtree of root (null allowed)
     * @param rightTree right subtree of root (null allowed)
     */
    public BinaryTree(E data, BinaryTree<E> leftTree, BinaryTree<E> rightTree) {
        root = new Node<E>(data);
        if(leftTree != null) {
            root.left = leftTree.root;
        } else {
            root.left = null;
        }

        if(rightTree != null) {
            root.right = rightTree.root;
        } else {
            root.right = null;
        }
    }

    /**
     * Return the left subtree
     * @return The left subtree or null if either the root or left subtree is null
     */
    public BinaryTree<E> getLeftSubtree() {
        if(root != null && root.left != null) {
            return new BinaryTree<E>(root.left);
        } else {
            return null;
        }
    }

    /**
     * Return the right subtree
     * @return The right subtree or null if either the root or left subtree is null
     */
    public BinaryTree<E> getRightSubtree() {
        if(root != null && root.right != null){
            return new BinaryTree<E>(root.right);
        } else {
            return null;
        }
    }

    /** Determine whether this tree is a leaf.
     * @return true if the root has no children
     */
    public boolean isLeaf() {
        return (root.left == null && root.right == null);
    }

    /**
     * Perform a preorder traversal.
     * @param node The local root
     * @param depth The depth
     * @param sb The string buffer to save the output
     */
    private void preOrderTraverse(Node<E> node, int depth, StringBuilder sb) {
        for(int i = 1; i < depth; i++) {
            sb.append(" ");
        }
        if(node == null) {
            sb.append("null\n");
        } else {
            sb.append(node.toString());
            sb.append("\n");
            preOrderTraverse(node.left, depth+1, sb);
            preOrderTraverse(node.right, depth+1, sb);
        }
    }

    /**
     * Returns the data in the root
     * @return Root's data
     */
    public E getData() {
        return root.data;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        preOrderTraverse(root, 1, sb);
        return sb.toString();
    }

    /**
     * The inner class for the BinaryTree, a specialized node.
     * @param <E> Generic to hold any data type
     */
    protected static class Node<E> implements Serializable {
        protected E data;
        protected Node<E> left;
        protected Node<E> right;

        /**
         * Constructor which takes in a generic data and holds it
         * @param data The data to hold within the node
         */
        public Node(E data) {
            this.data = data;
            left = null;
            right = null;
        }

        public String toString() {
            return data.toString();
        }
    }
}

