package edu.miracosta.cs113;

/**
 * HuffmanTree.java : Huffman code uses different numbers of bits to encode the letters. It uses fewer bits for more
 * common letters and more bits for less common letters. On average, using Huffman codes to encode text files should
 * give you files with fewer bits than you would get using other codes. To determine the code for a letter, you form a
 * binary string by tracing the path from the root node to that letter. Each time you go left, append a 0, and each time
 * you go right, append a 1.
 *
 * @author Danny Lee
 * @version 1.0
 */


import java.io.Serializable;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;
import java.util.LinkedList;
import java.io.PrintStream;

public class HuffmanTree extends BinaryTree implements Serializable{

    //Nested Class
    /**A datum in the Huffman Tree*/
    public static class HuffData implements Serializable{
        //Data fields
        private int weight = 0;
        private String symbol = "";
        private String huffCode = "";

        /**
         * Full constructor
         * @param weight the frequency of the letter appeared in the text file.
         * @param symbol the character or symbol that appeared in the text file.
         */
        public HuffData(int weight, String symbol){
            this.weight = weight;
            this.symbol = symbol;
        }

        /**
         * Full constructor
         * @param symbol the character or symbol that appeared in the text file
         * @param huffCode a binary string of 0s and 1s that is used for encoding.
         */
        public HuffData(String symbol, String huffCode){
            this.symbol = symbol;
            this.huffCode = huffCode;
        }

        /**
         * Prints out the symbol and its weight.
         * @return string result
         */
        public String toString(){
            return ("Symbol : " + this.symbol + " weight : " + this.weight);
        }


        //getter methods.
        public String getHuffCode(){
            return huffCode;
        }


        public String getSymbol(){
            return symbol;
        }

    }

    //Nested Class
    /**A Comparator for Huffman trees; nested class. */
    private static class CompareHuffmanTrees implements Comparator<BinaryTree<HuffData>>{
        /**
         * Compare two objects
         * @param treeLeft The left-hand object
         * @param treeRight The right-hand object
         * @return -1 if left less than right, 0 if left equals right, and +1 if left greater than right.
         */
        public int compare(BinaryTree<HuffData> treeLeft, BinaryTree<HuffData> treeRight){
            int leftWeight = treeLeft.getData().weight;
            int rightWeight = treeRight.getData().weight;
            return (leftWeight - rightWeight);
        }
    }

    //Data fields
    /**A reference to the completed Huffman Tree*/
    private BinaryTree<HuffData> huffTree;
    private LinkedList<HuffData> huffDataList = new LinkedList<HuffData>();
    private int[] numberWeights = new int[10];
    private int[] lowerCaseWeights = new int[26];
    private int[] upperCaseWeights = new int[26];
    private int[] otherSymbolsWeights = new int[6]; //space, tab, newline, exclamation mark, period, question mark


    /**
     * Builds the Huffman tree using the given alphabet and weights.
     * @param symbols An array of HuffData objects
     */
    public void buildTree(HuffData[] symbols){
        Queue<BinaryTree<HuffData>> theQueue = new PriorityQueue<BinaryTree<HuffData>>(symbols.length, new CompareHuffmanTrees());
        // Load the queue with the leaves.
        for(HuffData nextSymbol : symbols){
            BinaryTree<HuffData> aBinaryTree = new BinaryTree<HuffData>(nextSymbol, null, null);
            theQueue.offer(aBinaryTree);
        }
        //Build the tree.
        while(theQueue.size() > 1){
            BinaryTree<HuffData> left = theQueue.poll();
            BinaryTree<HuffData> right = theQueue.poll();
            int leftWeight = left.getData().weight;
            int rightWeight = right.getData().weight;
            HuffData sum = new HuffData((leftWeight + rightWeight), null);
            BinaryTree<HuffData> newTree = new BinaryTree<HuffData>(sum, left, right);
            theQueue.offer(newTree);
        }
        //The queue should now contain only one item.
        huffTree = theQueue.poll();
    }

    /**
     * Completes the array by assigning given ascii code to the appropriate array.
     * @param ascii givne ascii code of a character or symbol.
     */
    public void buildArray(int ascii){
        int temp = 0;
        if(ascii >= 48 && ascii <= 57){//when it's a number
            temp = numberWeights[ascii - 48];
            temp ++;
            numberWeights[ascii - 48] = temp;
        }else if(ascii >= 65 && ascii <= 90){//when it's a upper case letter.
            temp = upperCaseWeights[ascii - 65];
            temp ++;
            upperCaseWeights[ascii - 65] = temp;
        }else if(ascii >= 97 && ascii <= 122){//when it's a lower case letter.
            temp = lowerCaseWeights[ascii -97];
            temp ++;
            lowerCaseWeights[ascii - 97] = temp;
        }else{//when it's a symbol
            if(ascii == 32){//when it's a space
                temp = otherSymbolsWeights[0];
                temp ++;
                otherSymbolsWeights[0] = temp;
            }else if(ascii == 9){//when it's a tab
                temp = otherSymbolsWeights[1];
                temp ++;
                otherSymbolsWeights[1] = temp;
            }else if(ascii == 13){//when it's a newline.    //ascii for \n == 13 10??
                temp = otherSymbolsWeights[2];
                temp ++;
                otherSymbolsWeights[2] = temp;
            }else if(ascii == 33){//when it's an exclamation mark
                temp = otherSymbolsWeights[3];
                temp ++;
                otherSymbolsWeights[3] = temp;
            }else if(ascii == 46){//when it's a period.
                temp = otherSymbolsWeights[4];
                temp ++;
                otherSymbolsWeights[4] = temp;
            }else if(ascii == 63){//when it's a question mark.
                temp = otherSymbolsWeights[5];
                temp ++;
                otherSymbolsWeights[5] = temp;
            }
        }

    }

    /**
     * Encodes to a Huffman Code.
     * @param code encoded Huffman code
     * @param tree Huffman tree being used to encode.
     */
    public void encode(String code, BinaryTree<HuffData> tree){
        HuffData theData = tree.getData();
        if(theData.symbol != null){//when it reaches to leaf node
            if(theData.symbol.equals(" ")){
                huffDataList.add(new HuffData(" ", code));
            }else if(theData.symbol.equals("\t")){
                huffDataList.add(new HuffData("\t", code));
            }else if(theData.symbol.equals("\n")){
                huffDataList.add(new HuffData("\n", code));
            }else{
                huffDataList.add(new HuffData(theData.symbol, code));
            }
        }else{
            encode(code + "0", tree.getLeftSubtree());
            encode(code + "1", tree.getRightSubtree());
        }
    }

    /**
     * Prints the encoded Huffman code to the external text file
     * @param out PrintStream used to print out to a file.
     * @param symbol used to find the encoded string of this symbol.
     */
    public void encodeToFile(PrintStream out, String symbol){
        for(int i = 0; i < huffDataList.size(); i++){
            if(huffDataList.get(i).getSymbol().equals(symbol)){
                out.print(huffDataList.get(i).getHuffCode());
                break;
            }
        }
    }

    /**
     * Method to decode a message that is input as string of digit characters 0 or 1.
     * @param codedMessage The input message as a String of zeros and ones.
     * @return The decoded message as a String.
     */
    public String decode(String codedMessage){
        StringBuilder result = new StringBuilder();
        BinaryTree<HuffData> currentTree = huffTree;
        for(int i = 0; i < codedMessage.length(); i++){
            if(codedMessage.charAt(i) == '1'){
                currentTree = currentTree.getRightSubtree();
            }else{
                currentTree = currentTree.getLeftSubtree();
            }
            if(currentTree.isLeaf()){
                HuffData theData = currentTree.getData();
                result.append(theData.symbol);
                currentTree = huffTree;
            }
        }
        return result.toString();
    }


    //getter methods.
    public BinaryTree<HuffData> getHuffTree(){
        return huffTree;
    }

    public LinkedList<HuffData> getHuffDataList() {
        return huffDataList;
    }

    public int[] getNumberWeights(){
        return numberWeights;
    }

    public int[] getLowerCaseWeights(){
        return lowerCaseWeights;
    }

    public int[] getUpperCaseWeights(){
        return upperCaseWeights;
    }

    public int[]getOtherSymbolsWeights(){
        return otherSymbolsWeights;
    }

}