package Lexicon.Model;

import java.util.HashMap;

/**
 *
 * @author Andreas Novian
 */
public class Node {

    public Boolean endOfWord;
    public HashMap<Character, Node> children;
    public Node parent;

    public Node() {
        this.endOfWord = false;
        this.children = new HashMap<>();
    }
}
