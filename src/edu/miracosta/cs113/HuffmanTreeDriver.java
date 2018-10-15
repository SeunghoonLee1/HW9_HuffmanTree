package edu.miracosta.cs113;

/**
 * You will be given some code that will retrieve text from a given website and output it to a file (see table at end for limited chars),
 * as well as the code for a Binary Tree. You are to use Java’s built in PriorityQueue, in conjunction with the given BinaryTree to build
 * a HuffmanTree which will encode the retrieved text file. Your HuffmanTree implementation will provide a lossless compression of text
 * which can then be sent (along with the Huffman tree) and decompressed with the same Huffman tree.
 *
 * Valid chars left over after cleaning webpage and storing into file:
 * A-Z (upper case letter) characters
 * a-z (lower case letter) characters
 * 0-9 (digit) characters
 * only allowed symbols: space ( ), tab (\t), newline (\n), exclamation mark (!), period (.), question mark (?)
 *
 *
 * @author Danny Lee
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class HuffmanTreeDriver {
    /*
     *Algorithm
     *1. Taking a URL and output text file name to create the original file, this file is used to create the Huffman tree
     *2. Using the built Huffman tree with the original file's website text to create an encoded file
     *3. Using the built Huffman tree with the encoded file to create a decoded file
     *4. Output the number of bits for each file, make sure to use 16 bits per character for the original and decoded files ( for encoded file simply count the number of 0's and 1's for the number of bits). Note that original and decoded files should have the exact same number of bits!
     *5. Output the percentage of compression (number of bits in original file / number of bits in encoded file).
     */

    public static void main(String[] args){

        HuffmanTree.HuffData[] huffDataArray = new HuffmanTree.HuffData[68];
        HuffmanTree huffmanTree = new HuffmanTree();
        BufferedReader reader = null;
        BufferedReader anotherReader = null;
        BufferedReader decodeReader = null;

        int[] lowerCaseWeights = huffmanTree.getLowerCaseWeights();
        int[] upperCaseWeights = huffmanTree.getUpperCaseWeights();
        int[] numberWeights = huffmanTree.getNumberWeights();
        int[] otherSymbolsWeights = huffmanTree.getOtherSymbolsWeights();

        String huffmanCode = "";

        //Taking a URL and output text file name to create the original file. This is used to create Huffman tree.
        TextFileGenerator generator = new TextFileGenerator();
        try{
            generator.makeCleanFile("http://michaelwflaherty.com/files/index.html","original");
        }catch(IOException e){
            System.out.println("IOException occured!");
            System.exit(0);
        }

        //Assign bufferedReaders to the original file.
        try{
            File originalFile = new File("original");
            reader = new BufferedReader(new FileReader(originalFile));
            anotherReader = new BufferedReader(new FileReader(originalFile));
        }catch(FileNotFoundException e){
            System.out.println("File not found!");
            System.exit(0);
        }

        //Using the bufferedReader, read each characters and build array.
        try{
            int nextSymbol = 0;
            while((nextSymbol = reader.read()) != -1){
                huffmanTree.buildArray(nextSymbol);
            }
        }catch(IOException e){
            System.out.println("IOException occured while reading using the BufferedReader.");
            System.exit(0);
        }

        /**TODO
         * Build huffDataArray based on the 4 arrays above.(lowerCaseWeights, uppercaseWeights,,,,)
         * */
        for(int i = 0; i < 26; i++){
            huffDataArray[i] = new HuffmanTree.HuffData(lowerCaseWeights[i], (char)(97 + i) + "");
        }
        for(int i = 26; i < 52; i++){
            huffDataArray[i] = new HuffmanTree.HuffData(upperCaseWeights[i - 26], (char)(65 + (i - 26)) + "");
        }
        for(int i = 52; i < 62; i++){
            huffDataArray[i] = new HuffmanTree.HuffData(numberWeights[i - 52], (char)(48 + (i - 52)) + "");
        }

        huffDataArray[62] = new HuffmanTree.HuffData(otherSymbolsWeights[0], (char)32 + "");
        huffDataArray[63] = new HuffmanTree.HuffData(otherSymbolsWeights[1], (char)9 + "");
        huffDataArray[64] = new HuffmanTree.HuffData(otherSymbolsWeights[2], "\n");
        huffDataArray[65] = new HuffmanTree.HuffData(otherSymbolsWeights[3], "!");
        huffDataArray[66] = new HuffmanTree.HuffData(otherSymbolsWeights[4], ".");
        huffDataArray[67] = new HuffmanTree.HuffData(otherSymbolsWeights[5], "?");

        //Build huffmanTree using the huffDataArray.
        huffmanTree.buildTree(huffDataArray);

        //Encode all the symbols
        huffmanTree.encode(huffmanCode, huffmanTree.getHuffTree());

        //Output encodedFile.
        try{
            PrintStream out = new PrintStream("encodedFile");
            int nextSymbol = 0;
            while((nextSymbol = anotherReader.read()) != -1){
                huffmanTree.encodeToFile(out, (char)nextSymbol + "");
            }
        }catch(IOException e){
            System.out.println("IOException occurred while reading using the another BufferedReader.");
            System.exit(0);
        }

        //Decode file
        try{
            File encodedFile = new File("encodedFile");
            decodeReader = new BufferedReader(new FileReader(encodedFile));

        }catch(FileNotFoundException e){
            System.out.println("File not found!");
            System.exit(0);
        }

        //Output the decodedResult to a file
        try{
            String nextLine = "";
            String decodedResult = "";
            // PrintWriter decodeOutputStream = new PrintWriter(new FileOutputStream("decodedFile"));
            PrintStream out = new PrintStream("decodedFile");
            while((nextLine = decodeReader.readLine()) != null){
                decodedResult = huffmanTree.decode(nextLine);
                out.print(decodedResult);
            }
        }catch(IOException e) {
            System.out.println("IOException occurred while reading the encodedFile using the decodeReader.");
        }

        //Display the results.
        System.out.println("Output the number of bits for each file.");
        System.out.println("original.txt : " + generator.getNumChars("original") * 16 + "bits.");
        System.out.println("encodedFile.txt : " + generator.getNumChars("encodedFile") + "bits. ");
        System.out.println("decodedFile.txt : " + generator.getNumChars("decodedFile")*16 + "bits.");

        double compressionRatio = ((double)(generator.getNumChars("original") * 16)) / (double)((generator.getNumChars("encodedFile")));
        System.out.printf("%s %.2f\n", "Percentage of compression  :", compressionRatio);

        //Close bufferedReader at the end.
        try{
            reader.close();
            anotherReader.close();
            decodeReader.close();
            System.out.println("\nClosing the BuffereddReader.");
        }catch(IOException e){
            System.out.println("IOException occurred while closing the BufferedReader.");
            System.exit(0);
        }

    }
}