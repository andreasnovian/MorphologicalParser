/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Lexicon.Model.Combiner;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Andreas Novian
 */
public class Tester {

    public static void main(String[] args) throws IOException {
        Parser p = new Parser();
        Combiner c = new Combiner();
//        MorphologicalParser mp = new MorphologicalParser();

//        System.out.println(p.convertToWord("punya", "#me-i"));
//        System.out.println(p.process("anak-beranak bersayur-mayur bersalam-salaman"));
//        System.out.println(p.process("berlari-larian"));
//        System.out.println(p.process("kaumakan"));
//        System.out.println(p.process("bersamamu"));

//        System.out.println(c.convertToWord("kejar", "[ber+^(kejar+]an)"));
//          System.out.println(p.process("berkejar-kejaran"));
//System.out.println(p.process("bertanggungjawaban bertanggung jawab"));
System.out.println(p.process("setengah"));
//System.out.println(p.process("bertanggungjawaban"));
//System.out.println(p.process("makanan kaleng"));
//          System.out.println(c.convertToWord("pesona", "[me"));
//        MorphologicalParser mp = new MorphologicalParser();
//        String text = "Andreas sedang melakukan tari-menari";
//        String word[] = text.split(" ");
//        ArrayList<String> parseResult;
//
//        for (String i : word) {
//            parseResult = mp.getMorphologic(i);
//            System.out.println(i + ": "+ parseResult.toString());
//            System.out.println("");
//        }
    }
}
