/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexicon.Model;

import java.io.IOException;

/**
 *
 * @author Andreas Novian
 */
public class Combiner {

    private String rootWord;

    public Combiner() throws IOException {
        this.rootWord = "";
    }

    public String convertToWord(String rootWord, String component) {
        this.rootWord = rootWord;
        String result = rootWord;
        String redup = "";
        String postKomposisi = "";

        if (component.contains("^(")) {
            redup = component.substring(component.indexOf("^") + 1, component.indexOf(")") + 1);
            component = component.replace("+^" + redup, "+^");
            component = component.replace("^" + redup, "^");
        }

        if (!component.equalsIgnoreCase("")) {
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
                        if (redup.equalsIgnoreCase("")) {
                            result = duplikasi(result, i);
                        } else {
                            result = duplikasi(result, redup);
                        }
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
                    case '&':
                        result += " " + i;
                        break;
                    default:
                        break;
                }
            }
        }

        return result;
    }

    private String prefiksasi(String rootWord, String prefiks) {
        String result = rootWord;
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

                    if (rootWord.equalsIgnoreCase("punya") || rootWord.equalsIgnoreCase("perkara")) {
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
        if (result.equalsIgnoreCase(rootWord)) {
            result = prefiks + rootWord;
        }

        return result;
    }

    private String sufiksasi(String rootWord, String sufiks) {
        if (rootWord.contains(" ")) {
            rootWord = rootWord.replace(" ", "");
        }

        String result = rootWord;

        result = result + sufiks;

        return result;
    }

    private String konfiksasi(String rootWord, String konfiks) {
        String[] comp = konfiks.split("-");

        if (rootWord.contains(" ")) {
            rootWord = rootWord.replace(" ", "");
        }

        String result = prefiksasi(rootWord, comp[0]);
        result = sufiksasi(result, comp[1]);

        return result;
    }

    private String duplikasi(String rootWord, String duplikasi) {
        String result = rootWord;

        //must convert element in between (..) for reduplication
        if (duplikasi.charAt(0) == '(') {
            duplikasi = duplikasi.substring(1, duplikasi.length() - 1);
            String[] temp = duplikasi.split("\\+");
            rootWord = temp[0];
            duplikasi = "";
            for (int j = 1; j < temp.length; j++) {
                if (temp[j].charAt(0) != '$') {
                    duplikasi += temp[j] + "+";
                }
            }
            if (!duplikasi.equalsIgnoreCase("")) {
                duplikasi = duplikasi.substring(0, duplikasi.length() - 1);
            }
            result = result + "-" + this.convertToWord(rootWord, duplikasi);
        } else if (duplikasi.equalsIgnoreCase("2")) {
            result = result + "-" + rootWord;
        } else {
            result = result + "-" + duplikasi;
        }

        return result;
    }
}
