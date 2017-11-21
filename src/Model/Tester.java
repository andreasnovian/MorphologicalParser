/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Lexicon.Model.Combiner;
import Lexicon.Model.Lexicon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andreas Novian
 */
public class Tester {

    public static void main(String[] args) {
        try {
            Parser p = new Parser(new Lexicon());
            Combiner c = new Combiner();
            Lexicon l = new Lexicon();

            for (int t = 1; t <= 3; t++) {
                l.insertRoot("andreas");
                long a = System.currentTimeMillis();
                l.deleteRoot("andreas");
                long b = System.currentTimeMillis();

                System.out.println("waktu " + t + " : " + (b - a));
            }
//        MorphologicalParser mp = new MorphologicalParser();

            //        System.out.println(p.convertToWord("punya", "#me-i"));
//        System.out.println(p.process("anak-beranak bersayur-mayur bersalam-salaman"));
//        System.out.println(p.process("berlari-larian"));
//        System.out.println(p.process("kaumakan"));
//        System.out.println(p.process("bersamamu"));
//        System.out.println(c.convertToWord("kejar", "[ber+^(kejar+]an)"));
//          System.out.println(p.process("berkejar-kejaran"));
//System.out.println(p.process("bertanggungjawaban bertanggung jawab"));
//System.out.println(p.processFromText("memperindah",true,true));
//            System.out.println(p.readFile("tester.txt"));
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
        } catch (IOException ex) {
            System.out.println("hahaha");
        }
    }
}
