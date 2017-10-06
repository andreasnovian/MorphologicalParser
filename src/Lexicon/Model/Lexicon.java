package Lexicon.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to represent the lexicon used in Morphological Parser Lexicon keeps
 * root words in Bahasa Indonesia and it's morphological component
 *
 * @author Andreas Novian
 */
public class Lexicon {

    //each letter of a roots stored in a single node
    private final HashMap<Character, Node> roots;

    //to read from file
    private BufferedReader br;

    //to write into file
    private BufferedWriter bw;

    //the folder where the lxc file is in the project folder
    private final String folder;
    
    private final Combiner combiner;

    public Lexicon() throws FileNotFoundException, IOException {
        this.roots = new HashMap<>();
        this.folder = "lxc/";
        this.combiner = new Combiner();
        load();
    }

    /**
     * Method to initialize the lexicon tree based on roots in the "roots" file
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void load() throws FileNotFoundException, IOException {
        this.br = new BufferedReader(new FileReader(folder + "roots.lxc"));
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            if (!currentLine.equalsIgnoreCase("")) {
                this.insertToTree(currentLine);
            }
        }
        this.br.close();
    }

    /**
     * Method to write a String into a lexicon file (.lxc)
     *
     * @param fileName name of the lexicon file
     * @param key String to be entered
     *
     * @throws IOException
     */
    private void writeToFile(String fileName, String key) throws IOException {
        String path = folder + fileName + ".lxc";
        this.bw = new BufferedWriter(new FileWriter(path, true));
        if (!key.equalsIgnoreCase("")) {
            this.bw.write(key + "\n");
        }
        this.bw.flush();
        this.bw.close();
    }

    /**
     * Method to delete a String from a lexicon file
     *
     * @param fileName name of the lexicon file
     * @param key String to be deleted
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void deleteFromFile(String fileName, String key) throws FileNotFoundException, IOException {
        String content = "";
        this.br = new BufferedReader(new FileReader(folder + fileName + ".lxc"));
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            if (!currentLine.equalsIgnoreCase(key) && !currentLine.equalsIgnoreCase("")) {
                content += currentLine + "\n";
            }
        }
        this.br.close();
        content = content.trim();
        this.clearFile(fileName);
        this.writeToFile(fileName, content);
    }

    /**
     * Method to make a new lexicon file for a new root word entry
     *
     * @param fileName new root word entry, as the new lexicon file name
     *
     * @throws IOException
     */
    private void createFile(String fileName) throws IOException {
        String path = folder + fileName + ".lxc";
        this.bw = new BufferedWriter(new FileWriter(path));
        this.bw.flush();
        this.bw.close();
    }

    /**
     * Method to delete a lexicon file
     *
     * @param fileName root word, name of the lexicon file
     * @return true if and only if the file is successfully deleted
     */
    private boolean deleteFile(String fileName) {
        String path = folder + fileName + ".lxc";
        boolean status = new File(path).delete();
        if (status) {
            System.out.println("File " + fileName + " deleted");
        } else {
            System.out.println("Fail to delete " + fileName);
        }
        return status;
    }

    /**
     * Method to clear the content of a lexicon file
     *
     * @param fileName name of the lexicon file
     *
     * @throws IOException
     */
    private void clearFile(String fileName) throws IOException {
        String path = folder + fileName + ".lxc";
        this.bw = new BufferedWriter(new FileWriter(path, false));
        this.bw.write("");
        this.bw.flush();
        this.bw.close();
    }

    /**
     * Method to get all the morphological component from a single root
     *
     * @param root root word, name of the lexicon file
     * @return String containing all components from a single root
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String getComponent(String root) throws FileNotFoundException, IOException {
        String content = "";
        this.br = new BufferedReader(new FileReader(folder + root + ".lxc"));
        this.br.readLine();
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            content += currentLine + "\n";
        }
        content = content.trim();
        this.br.close();
        return content;
    }

    /**
     * Method to search if a key is already in the tree or not
     *
     * @param key root word as a key to searh in tree
     * @return true if and only if the root word is already in the tree
     */
    public boolean searchInTree(String key) {
        if (roots.containsKey(key.charAt(0))) {
            if (key.length() == 1 && roots.get(key.charAt(0)).endOfWord) {
                return true;
            }
            return searchRec(key.substring(1), roots.get(key.charAt(0)));
        } else {
            return false;
        }
    }

    /**
     * Private method to recursively search in the lexicon tree
     *
     * @param string root word
     * @param node discovered node
     * @return true whether the value of attribute endOfWord in param node
     */
    private boolean searchRec(String string, Node node) {
        if (string.length() == 0) {
            return node.endOfWord;
        }
        if (node.children.containsKey(string.charAt(0))) {
            return searchRec(string.substring(1), node.children.get(string.charAt(0)));
        } else {
            return false;
        }
    }

    /**
     * Method to search if a String key is in a file or not
     *
     * @param fileName name of the file
     * @param key String to search
     * @return true if and only if key String is in the file
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean searchInFile(String fileName, String key) throws FileNotFoundException, IOException {
        this.br = new BufferedReader(new FileReader(folder + fileName + ".lxc"));
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            if (currentLine.equalsIgnoreCase(key)) {
                return true;
            }
        }
        this.br.close();
        return false;
    }

    /**
     * Method to insert a new root word to lexicon, including creating a new
     * file for the entry
     *
     * @param root the root word to be entered
     * @throws IOException
     */
    public void insertRoot(String root) throws IOException {
        //only insert if the root word is not yet in the tree
        if (!this.searchInTree(root)) {
            this.insertToTree(root);

            //refresh the roots file
            this.clearFile("roots");
            String allWords = this.printAllWordInTree();
            this.writeToFile("roots", allWords);

            this.createFile(root);
            this.writeToFile(root, root);
        }
    }

    /**
     * Method to insert a word into the tree
     *
     * @param word word to be entered
     */
    private void insertToTree(String word) {
        if (!roots.containsKey(word.charAt(0))) {
            roots.put(word.charAt(0), new Node());
        }
        insertRec(word.substring(1), roots.get(word.charAt(0)));
    }

    /**
     * Private method to recursively insert a word to the tree
     *
     * @param word word to be entered
     * @param node discovered node
     */
    private void insertRec(String word, Node node) {
        final Node nextChild;
        if (node.children.containsKey(word.charAt(0))) {
            nextChild = node.children.get(word.charAt(0));
        } else {
            nextChild = new Node();
            nextChild.parent = node;
            node.children.put(word.charAt(0), nextChild);
        }
        if (word.length() == 1) {
            nextChild.endOfWord = true;
        } else {
            insertRec(word.substring(1), nextChild);
        }
    }

    /**
     * Method to write morphological component of a root word into the lexicon
     * file
     *
     * @param root root word, also the name of the lexicon file
     * @param component morphological component to be entered
     *
     * @throws IOException
     */
    public void insertComponent(String root, String component) throws IOException {
        if (!this.searchInFile(root, component)) {
            this.writeToFile(root, component);
        }
    }

    /**
     * Method to delete a root word from lexicon Delete root from the tree and
     * from the file
     *
     * @param root root word to be deleted
     *
     * @throws IOException
     */
    public void deleteRoot(String root) throws IOException {
        this.deleteFile(root);
        deleteRec(root.substring(1), roots.get(root.charAt(0)));

        this.clearFile("roots");
        String allWords = this.printAllWordInTree();
        this.writeToFile("roots", allWords);

    }

    /**
     * Private method to recursively delete a root word from the tree
     *
     * @param word word to be deleted
     * @param node discovered node
     */
    private void deleteRec(String word, Node node) {
        final Node nextChild;
        nextChild = node.children.get(word.charAt(0));
        if (word.length() == 1) {
            nextChild.endOfWord = false;
        } else {
            deleteRec(word.substring(1), nextChild);
        }
    }

    /**
     * Method to delete morphological component of a root word from the file
     *
     * @param root root word, also name of the file
     * @param component morphological component to be deleted
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void deleteComponent(String root, String component) throws FileNotFoundException, IOException {
        this.deleteFromFile(root, component);
    }

    /**
     * Method to update a root word
     *
     * @param oldRoot root to update
     * @param newRoot new updated root
     *
     * @throws IOException
     */
    public void updateRoot(String oldRoot, String newRoot) throws IOException {
        //can't update to a root thats already in the tree
        if (this.searchInTree(newRoot)) {
            return;
        }

        this.insertRoot(newRoot);
        String content = "";
        this.br = new BufferedReader(new FileReader(folder + oldRoot + ".lxc"));
        this.br.readLine();
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            if (!currentLine.equalsIgnoreCase("")) {
                content += currentLine + "\n";
            }
        }
        this.br.close();

        content = content.trim();
        this.writeToFile(newRoot, content);

        this.deleteRoot(oldRoot);
    }

    /**
     * Method to update morphological component from a root word
     *
     * @param root root to update
     * @param oldComponent morphological component to update
     * @param newComponent new updated morphological component
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void updateComponent(String root, String oldComponent, String newComponent) throws FileNotFoundException, IOException {
        String content = "";
        this.br = new BufferedReader(new FileReader(folder + root + ".lxc"));
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            if (currentLine.equalsIgnoreCase(oldComponent)) {
                content += newComponent + "\n";
            } else if (!currentLine.equalsIgnoreCase("")) {
                content += currentLine + "\n";
            }
        }
        this.br.close();
        content = content.trim();
        this.clearFile(root);
        this.writeToFile(root, content);
    }

    /**
     * Method to print all root word in the tree
     *
     * @return String containing all root word separated with new line char
     */
    public String printAllWordInTree() {
        ArrayList<String> words = new ArrayList<>();
        String result = "";
        Node node;
        String word;
        int letterInt;
        char letterChar;

        for (int ascii = 97; ascii < 123; ascii++) {
            if (roots.containsKey((char) ascii)) {
                node = roots.get((char) ascii);
                word = "" + (char) ascii;
                letterInt = 97;
                do {
                    letterChar = (char) letterInt;
                    if (node.children.containsKey(letterChar)) {
                        word += letterChar;
                        node = node.children.get(letterChar);
                        letterInt = 97;
                        if (node.endOfWord) {
                            words.add(word);
                        }
                    } else {
                        if (letterInt < 122) {
                            letterInt++;
                        } else {
                            char prevLetter = word.charAt(word.length() - 1);
                            letterInt = (int) prevLetter;
                            letterInt++;
                            word = word.substring(0, word.length() - 1);
                            node = node.parent;
                        }
                    }
                } while (node != null);
            }
        }

        for (int i = 0; i < words.size(); i++) {
            result += words.get(i) + "\n";
        }
        result = result.trim();
        return result;
    }

    public String convertToWord(String rootWord, String component) {
        return combiner.convertToWord(rootWord, component);
    }
}
