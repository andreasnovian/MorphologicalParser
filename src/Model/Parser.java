package Model;

import Lexicon.Model.Lexicon;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Andreas Novian
 */
public class Parser {

    //an object of a lexicon
    public Lexicon lexicon;

    private String rootWord;

    public Parser() throws IOException {
        this.lexicon = new Lexicon();
        this.rootWord = "";
    }

    /**
     * Method to search whether a word is a valid root word in lexicon tree or
     * not
     *
     * @param word a word to check
     * @return true if and only if word is a valid root word in lexicon tree
     */
    public boolean isRootWord(String word) {
        return lexicon.searchInTree(word.toLowerCase());
    }

    public String convertToWord(String rootWord, String component) {
        this.rootWord = rootWord;
        String result = rootWord;
        String[] comp = component.split("\\+");
        char symbol;

        for (String i : comp) {
            symbol = i.charAt(0);
            i = i.substring(1);

            switch (symbol) {
                case '@':
                    result += " " + i;
                    break;
                case '^':
                    result = duplikasi(result, i);
                    break;
                case '[':
                    result = prefiksasi(result, i);
                    break;
                case ']':
                    result = sufiksasi(result, i);
                    break;
                case '#':
                    result = konfiksasi(result, i);
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    private String prefiksasi(String rootWord, String prefiks) {
        String result = "";
        char c1 = rootWord.charAt(0);
        char c2 = rootWord.charAt(1);

        //to determine whether rootWord is one syllable or not
        boolean oneSyl = false;
        int countSyl = 0;
        char temp;
        for (int i = 0; i < rootWord.length(); i++) {
            temp = rootWord.charAt(i);
            if (temp == 'a' || temp == 'i' || temp == 'u' || temp == 'e' || temp == 'o') {
                countSyl++;
            }
        }
        if (countSyl == 1) {
            oneSyl = true;
        }

        if (prefiks.equalsIgnoreCase("me")) {
            switch (c1) {
                case 'b':
                case 'f':
                case 'v':
                    result = "mem" + rootWord;
                    break;
                case 'c':
                case 'd':
                case 'j':
                    result = "men" + rootWord;
                    break;
                case 's':
                    if (c2 == 'y') {
                        result = "men" + rootWord;
                    } else {
                        if (result.equalsIgnoreCase(this.rootWord)) {
                            result = "meny" + rootWord.substring(1);
                        } else {
                            result = "meny" + rootWord;
                        }
                    }
                    break;
                case 'g':
                case 'h':
                case 'a':
                case 'i':
                case 'u':
                case 'e':
                case 'o':
                case 'q':
                    result = "meng" + rootWord;
                    break;
                case 'k':
                    if (c2 == 'h') {
                        result = "meng" + rootWord;
                    } else {
                        if (result.equalsIgnoreCase(this.rootWord)) {
                            result = "meng" + rootWord.substring(1);
                        } else {
                            result = "meng" + rootWord;
                        }
                    }
                    break;
                case 'p':
                    if (result.equalsIgnoreCase(this.rootWord)) {
                        result = "mem" + rootWord.substring(1);
                    } else {
                        result = "mem" + rootWord;
                    }
                    break;
                case 't':
                    if (result.equalsIgnoreCase(this.rootWord)) {
                        result = "men" + rootWord.substring(1);
                    } else {
                        result = "men" + rootWord;
                    }
                    break;
                default:
                    result = "me" + rootWord;
                    break;
            }
            if (oneSyl) {
                result = "menge" + rootWord;
            }
        } else if (prefiks.equalsIgnoreCase("pe")) {
            switch (c1) {
                case 'b':
                case 'f':
                case 'v':
                    result = "pem" + rootWord;
                    break;
                case 'c':
                case 'd':
                case 'j':
                    result = "pen" + rootWord;
                    break;
                case 's':
                    if (c2 == 'y') {
                        result = "pen" + rootWord;
                    } else {
                        if (result.equalsIgnoreCase(this.rootWord)) {
                            result = "peny" + rootWord.substring(1);
                        } else {
                            result = "peny" + rootWord;
                        }
                    }
                    break;
                case 'g':
                case 'h':
                case 'a':
                case 'i':
                case 'u':
                case 'e':
                case 'o':
                case 'q':
                    result = "peng" + rootWord;
                    break;
                case 'k':
                    if (c2 == 'h') {
                        result = "peng" + rootWord;
                    } else {
                        if (result.equalsIgnoreCase(this.rootWord)) {
                            result = "peng" + rootWord.substring(1);
                        } else {
                            result = "peng" + rootWord;
                        }
                    }
                    break;
                case 'p':
                    if (result.equalsIgnoreCase(this.rootWord)) {
                        result = "pem" + rootWord.substring(1);
                    } else {
                        result = "pem" + rootWord;
                    }
                    break;
                case 't':
                    if (result.equalsIgnoreCase(this.rootWord)) {
                        result = "pen" + rootWord.substring(1);
                    } else {
                        result = "pen" + rootWord;
                    }
                    break;
                default:
                    result = "pe" + rootWord;
                    break;
            }
            if (oneSyl) {
                result = "penge" + rootWord;
            }
        } else if (prefiks.equalsIgnoreCase("per")) {
            String syl = rootWord.substring(0, 3);
            boolean erSyl = syl.substring(1).equalsIgnoreCase("er");
            switch (c1) {
                case 'r':
                    result = "pe" + rootWord;
                    break;
                default:
                    result = "per" + rootWord;
                    break;
            }
            if (rootWord.equalsIgnoreCase("ajar")) {
                result = "pelajar";
            }
            if (erSyl) {
                result = "pe" + rootWord;
            }
        } else if (prefiks.equalsIgnoreCase("ber")) {
            String syl = rootWord.substring(0, 3);
            boolean erSyl = syl.substring(1).equalsIgnoreCase("er");
            switch (c1) {
                case 'r':
                    result = "be" + rootWord;
                    break;
                default:
                    result = "ber" + rootWord;
                    break;
            }
            if (rootWord.equalsIgnoreCase("ajar")) {
                result = "belajar";
            }
            if (erSyl) {
                result = "be" + rootWord;
            }
        } else if (prefiks.equalsIgnoreCase("ter")) {
            switch (c1) {
                case 'r':
                    result = "te" + rootWord;
                    break;
                default:
                    result = "ter" + rootWord;
                    break;
            }
        } else if (prefiks.equalsIgnoreCase("memper")) {
            String syl = rootWord.substring(0, 3);
            boolean erSyl = syl.substring(1).equalsIgnoreCase("er");

            if (erSyl) {
                result = "mempe" + rootWord;
            } else {
                result = "memper" + rootWord;
            }
        } else if (prefiks.equalsIgnoreCase("diper")) {
            String syl = rootWord.substring(0, 3);
            boolean erSyl = syl.substring(1).equalsIgnoreCase("er");

            if (erSyl) {
                result = "dipe" + rootWord;
            } else {
                result = "diper" + rootWord;
            }
        }

        //for prefix other than specified above
        if (result.equalsIgnoreCase("")) {
            result = prefiks + rootWord;
        }

        return result;
    }

    private String sufiksasi(String rootWord, String sufiks) {
        String result = rootWord;

        result = result + sufiks;

        return result;
    }

    private String konfiksasi(String rootWord, String konfiks) {
        String[] comp = konfiks.split("-");

        if (rootWord.contains(" ")){
            rootWord = rootWord.replace(" ", "");
        }
        
        String result = prefiksasi(rootWord, comp[0]);
        result = sufiksasi(result, comp[1]);

        return result;
    }

    private String duplikasi(String rootWord, String duplikasi) {
        String result = rootWord;

        if (duplikasi.equalsIgnoreCase("2")) {
            result = result + "-" + rootWord;
        } else {
            result = result + "-" + duplikasi;
        }

        return result;
    }

    public ArrayList<String> process(String text) {
        ArrayList<String> result = new ArrayList<>();
        String word[] = text.split(" ");
        String parseResult;

        for (String i : word) {
            parseResult = parse(i);
            result.add(parseResult);
        }

        return result;
    }

    /**
     * Method to perform parse operation of a word
     *
     * @param word word to parse
     * @return all the possible parse result of the word
     */
    private String parse(String word) {
        String parseResult = "";
        String rootWord = "";

        //if word is found in lexicon
        if (isRootWord(word)) {
            parseResult = word;
            rootWord = word;
        }

        //if word is a reduplication
        if (word.contains("-")) {

        }

        return parseResult;
    }
}
