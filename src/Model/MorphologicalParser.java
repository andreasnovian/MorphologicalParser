package Model;


import Lexicon.Model.Lexicon;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Anton,Michael,Devan,Ricky Suryadi,Ricky Ky
 */
public class MorphologicalParser {
    
    public Lexicon lexicon;
    public ArrayList<String> hasil;
    public String kataDasar;

    public MorphologicalParser() throws IOException {
        this.lexicon = new Lexicon();
        this.hasil = new ArrayList<>();
    }
    
    public ArrayList<String> getMorphologic(String input){
        hasil.clear();
        if(getKata(input)){ //jika kata masukan ditemukan di dalam kamus.
            hasil.add(input);
            this.kataDasar = input;
        }
        
        
        //untuk mengecek jika sisipannya dihilangkan ada di dalam kamus maka hasil ditambah.
        String sementara;
        if(input.contains("in")){
            sementara = sisipanIn(input);
            if(sementara!=null){
                hasil.add(sementara);
            }
        }
       if(input.contains("el")){
            sementara = sisipanEl(input);
            if(sementara!=null){
                hasil.add(sementara);
            }
        }
        if(input.contains("em")){
            sementara = sisipanEm(input);
            if(sementara!=null){
                hasil.add(sementara);
            }
        }
        if(input.contains("er")){
            sementara = sisipanEr(input);
            if(sementara!=null){
                hasil.add(sementara);
            }
        }
        if(input.contains("ah")){
            sementara = sisipanAh(input);
            if(sementara!=null){
                hasil.add(sementara);
            }
        }
        
        
        
        ArrayList<String> temp = new ArrayList<String>();
        
        //Cek untuk imbuhan awalan dan gabungan
        if("be".equals(input.substring(0,2))){
            temp.addAll(aturanBer(input.substring(2)));
        }
        else if("me".equals(input.substring(0,2))){
            temp.addAll(aturanMe(input.substring(2)));
        }
        else if("di".equals(input.substring(0,2))){
            temp.addAll(aturanDi(input.substring(2)));
        }
        else if("ke".equals(input.substring(0,2))){
            temp.addAll(aturanKe(input.substring(2)));
        }
        else if("ku".equals(input.substring(0,2))){
            temp.addAll(aturanKu(input.substring(2)));
        }
        else if("se".equals(input.substring(0,2))){
            temp.addAll(aturanSe(input.substring(2)));
        }
        else if("pe".equals(input.substring(0,2))){
            temp.addAll(aturanPe(input.substring(2)));
        }
        else if("per".equals(input.substring(0,3))){
            temp.addAll(aturanPer(input.substring(3)));
        }
        else if("ter".equals(input.substring(0,3))){
            temp.addAll(aturanTer(input.substring(3)));
        }
        else if("kau".equals(input.substring(0,3))){
            temp.addAll(aturanKau(input.substring(3)));
        }
        
        
        //cek untuk imbuhan akhiran
        if(temp.isEmpty()){ //jika bukan gabungan maka di cek lagi akhirannnya saja
            if("kan".equals(input.substring(input.length()-3))){
                temp.addAll(akhiranKan(input.substring(0,input.length()-3)));
            }
            else if("an".equals(input.substring(input.length()-2))){
                temp.addAll(akhiranAn(input.substring(0,input.length()-2)));
            }
            else if("i".equals(input.substring(input.length()-1))){
                temp.addAll(akhiranI(input.substring(0,input.length()-1)));
            }
            else if("lah".equals(input.substring(input.length()-3))){
                temp.addAll(akhiranLah(input.substring(0,input.length()-3)));
            }
            else if("nya".equals(input.substring(input.length()-3))){
                temp.addAll(akhiranNya(input.substring(0,input.length()-3)));
            }
        }
        //System.out.println(temp.isEmpty());
        
        //untuk kasus pengulangan
        //untuk mengecek jika ada pengulangan kata menggunakan simbol "-" misal melayang-layangkan,
        //maka memanggil method getMorphologic ini untuk kata melayang dan layangkan
        if(input.contains("-")){
            ArrayList<String> hasilKata1 = new ArrayList<String>();
            ArrayList<String> hasilKata2 = new ArrayList<String>();

            String[] hasilSplit = input.split("-");
            hasilKata1.addAll(getMorphologic(hasilSplit[0]));
            String kataDasarTemp = this.kataDasar;
            hasilKata2.addAll(getMorphologic(hasilSplit[1]));

            hasil.clear();
            if(kataDasarTemp.equals(this.kataDasar)){
                for(int i=0;i<hasilKata1.size()&&i<hasilKata2.size();i++){
                    temp.add(hasilKata1.get(0) +" [dan] "+hasilKata2.get(0));
                }
            }
            
        }
        
        hasil.addAll(temp);
        return hasil;
    }
    
    /**
     *  Aturan-aturan imbuhan awalan & gabungan
     *  @param input yang sudah dihilangkan awalannya misal bertelur inputnya berarti telur
     *  @return semua string yang valid
     */
    public ArrayList<String> aturanBer(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add("ber -"+input);
            this.kataDasar = input;
        }
            //jika ada akhiran an (ber - xxx - an) misal: ber-anak-an
        if(input.charAt(0) == 'r'){
            if("an".equals(input.substring(input.length()-2)) && getKata(input.substring(1,input.length()-2))){
                result.add("ber - "+ input.substring(1,input.length()-2) + "- an");
                this.kataDasar = input.substring(1,input.length()-2);
            }
            else if (input.length()>3 && "kan".equals(input.substring(input.length()-3)) && getKata(input.substring(1,input.length()-3))){
                result.add("ber - "+ input.substring(1,input.length()-3) + "- kan");
                this.kataDasar = input.substring(1,input.length()-3);
            }
            if(getKata(input.substring(1))){ 
                result.add("ber - " + input.substring(1));}
                this.kataDasar = input.substring(1);
        }
        else if (input.contains("er")){
            if(getKata(input)){
                result.add("ber - " + input);
                this.kataDasar = input;
            }
        }
            
        if(null != input)
        //Aturan khusus untuk belunjur dan belajar
        //Aturan khusus untuk belunjur dan belajar
        switch (input) {
            case "lajar":
                result.add("ber - ajar");
                this.kataDasar = "ajar";
                break;
            case "lunjur":
                result.add("ber - unjur");
                this.kataDasar = "unjur";
                break;
        }
        
        return result;
    }
    
    public ArrayList<String> aturanTer(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input))
            result.add("ter -"+input);

            //jika ada akhiran kan atau i (Ter - xxx - kan) (Ter - xxx - i) mis Tersiramkan atau terlukai
            if("kan".equals(input.substring(input.length()-3)) && getKata(input.substring(0,input.length()-3))){
                    result.add("ter - "+ input.substring(0,input.length()-3) + " kan");
                    this.kataDasar = input.substring(0,input.length()-3);
                }
            else if(input.charAt(input.length()-1)=='i' && getKata(input.substring(0,input.length()-1))){
                    result.add("ter - "+ input.substring(0,input.length()-1) + " i");
                    this.kataDasar = input.substring(0,input.length()-1);
                }
            
            if(input.charAt(0) == 'r'){
                if(getKata(input.substring(1))){ 
                    result.add("ter - " + input.substring(1));
                    this.kataDasar = input;
                }
                if (getKata(input)){
                    result.add("ter - " + input);
                    this.kataDasar = input;
                }
            }
            else if (input.contains("er")){
                if(getKata(input)){
                    result.add("ter - " + input);
                    this.kataDasar = input;
                }
            }
        
        return result;
    }
    
    public ArrayList<String> aturanMe(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            //untuk kasus kata awal l/m/n/r/w/y/q lukis <- melukis ,masak <- memasak, nyanyi <- menyanyi rasa <- merasa
            result.add("me - "+input);
            this.kataDasar = input;
        }
        //Untuk Kasus kata awal /a/e/g/h/i/k*/o/u diubah menjadi meng-
        if("ng".equals(input.substring(0,2))){
            //untuk kasus /a/e/g/h/i/o/u misal (ambil <- mengambil | emis <- mengemis | gali <- menggali)
            //sudah termaksud k* yang huruf keduanya konsonan misal klarifikasi <- mengklarifikasi
            if( getKata( input.substring(2) ) ){
                result.add("meng - "+input.substring(2));
                this.kataDasar = input.substring(2);
            }
            //untuk kasus k* yang huruf keduanya vokal misal (kurang <- mengurang)
            else if ( getKata("k"+input.substring(2)) ){
                result.add("meng - "+"k"+input.substring(2));
                this.kataDasar = "k"+input.substring(2);
            }
        }
        //untuk kasus kata awal /b/, /f/, /p*/, /v/ maka diubah menjadi mem-
        if(input.charAt(0)=='m'){
            //untuk kasus /b/, /f/, /v/ misal bantu <- membantu, fitnah <- memfitnah
            //sudah termaksud p* yang huruf keduanya konsonan misal proses <- memproses
            if( getKata( input.substring(1) ) ){
                result.add("mem - "+input.substring(1));
                this.kataDasar = input.substring(1);
            }
            //untuk kasus /p*/ misal panggil <- memanggil
            else if ( getKata("p"+input.substring(1)) ){
                result.add("mem - "+"p"+input.substring(1));
                this.kataDasar = "p"+input.substring(1);
            }
            /**
             * pengecualian /p*
             * perkara -> memperkara[i] (bukan memerkarai) 
             * punya - -> mempunya[i] (bukan memunyai)
             */
            else if("perkarai".equals(input)){
                result.add("mem - "+"perkara"+" - i");
                this.kataDasar = "perkara";
            }
            else if("punyai".equals(input)){
                result.add("mem - "+"punya"+" - i");
                this.kataDasar = "punya";
            }
        }
        //untuk kasus kata awal /c/, /d/, /j/, /t*/ maka diubah menjadi men-
        if(input.charAt(0)=='n'){
            //untuk kasus /c/, /d/, /j/ misal campur <- mencampur, duduk <- menduduk, jenguk <- menjenguk
            //sudah termaksud t* yang huruf keduanya konsonan misal transfer <- mentransfer
            if( getKata( input.substring(1) ) ){
                result.add("men - "+input.substring(1));
                this.kataDasar = input.substring(1);
            }
            //untuk kasus /t*/ yang huruf keduanya vokal misal tari <- menari
            else if ( getKata("t"+input.substring(1)) ){
                result.add("men - "+"t"+input.substring(1));
                this.kataDasar = "t"+input.substring(1);
            }
        }
        //untuk kasus kata awal /s*/ maka kata awal luluh dan diubah menjadi meny-
        if("ny".equals(input.substring(0,2))){
            //misal sontek <- menyontek
            if ( getKata("s"+input.substring(2)) ){
                result.add("meny - "+"s"+input.substring(2));
                this.kataDasar = "s"+input.substring(2);
            }
        }
        //untuk kasus  kata yang bersuku kata satu diubah menjadi menge-
        if("nge".equals(input.substring(0,3))){
            //misal bom <- mengebom | cat -> mengecat | klik -> mengeklik | lap -> mengelap | tik -> mengetik
            if ( getKata(input.substring(3)) ){
                result.add("menge - "+input.substring(3));
                this.kataDasar = input.substring(3);
            }
        }
        //Tidak dilebur jika kata dasar merupakan kata asing yang belum diserap secara sempurna. 
        //Contoh: me-hash me-transfer dsb.. (sudah ditangani tinggal dikamus saja ditambahin arti hash/transfer)
        
        //Aturan untuk imbuhan gabungan me-kan dan me-i, misal malu <- memalukan 
        if("kan".equals(input.substring(input.length()-3)) && getKata(input.substring(0,input.length()-3))){
                    result.add("me - "+ input.substring(0,input.length()-3) + " - kan");
                    this.kataDasar = input.substring(0,input.length()-3);
        }
        //misal | warna <- mewarnai
        else if(input.charAt(input.length()-1)=='i' && getKata(input.substring(0,input.length()-1))){
                    result.add("me - "+ input.substring(0,input.length()-1) + " - i");
                    this.kataDasar = input.substring(0,input.length()-1);
        }
        //misal | baju <- membajui
        else if(input.charAt(0)=='m' && input.charAt(input.length()-1)=='i' && getKata(input.substring(1,input.length()-1))){
                    result.add("mem - "+ input.substring(1,input.length()-1) + " - i");
                    this.kataDasar = input.substring(1,input.length()-1);
        }
        //misal | siksa <- menyiksai
        else if("ny".equals(input.substring(0,2)) && input.charAt(input.length()-1)=='i' && getKata("s"+input.substring(2,input.length()-1))){
                    result.add("meny - "+ "s"+input.substring(2,input.length()-1) + " - i");
                    this.kataDasar = "s"+input.substring(2,input.length()-1);
        }
        //misal | ikhlas <- mengikhlaskan
        else if("ng".equals(input.substring(0,2)) && "kan".equals(input.substring(input.length()-3)) && getKata(input.substring(2,input.length()-3))){
                    result.add("meng - "+ input.substring(2,input.length()-3) + " - kan");
                    this.kataDasar = input.substring(2,input.length()-3);
        }
        //Aturan untuk imbuhan gabungan memper-kan dan memper-i, misal malu <- mempermalukan 
        else if(input.length()>3 && "mper".equals(input.substring(0,4)) && "kan".equals(input.substring(input.length()-3)) && getKata(input.substring(4,input.length()-3))){
                result.add("memper - "+ input.substring(4,input.length()-3) + " - kan");
                this.kataDasar = input.substring(4,input.length()-3);
        }
        // misal | perangkat <- memperangkati
        else if(input.length()>3 &&"mper".equals(input.substring(0,4)) && input.charAt(input.length()-1)=='i' && getKata(input.substring(4,input.length()-1))){    
            result.add("memper - "+ input.substring(4,input.length()-1) + " - i");
            this.kataDasar = input.substring(4,input.length()-1);
        }
        
        return result;
    }
    
    public ArrayList<String> aturanDi(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add("di - "+input);//tambah space
            this.kataDasar = input;
        }
        
        //untuk kasus di-kan & di-i misal diberikan atau dikeringi
        if("kan".equals(input.substring(input.length()-3)) && getKata(input.substring(0,input.length()-3))){
                    result.add("di - "+ input.substring(0,input.length()-3) + " - kan");//-2 di ganti -3 sama di tambah -
                    this.kataDasar = input.substring(0,input.length()-3);
        }
        else if(input.charAt(input.length()-1)=='i' && getKata(input.substring(0,input.length()-1))){
                    result.add("di - "+ input.substring(0,input.length()-1) + " - i");
                    this.kataDasar = input.substring(0,input.length()-1);
        }
        //untuk kasus diper-kan & diper-i misal diperkenankan atau diperangi
        else if("per".equals(input.substring(0,3)) && "kan".equals(input.substring(input.length()-3)) && getKata(input.substring(3,input.length()-3))){
                result.add("di - per - "+ input.substring(3,input.length()-3) + " - kan");
                this.kataDasar = input.substring(3,input.length()-3);
        }
        else if("per".equals(input.substring(0,3)) && input.charAt(input.length()-1)=='i' && getKata(input.substring(3,input.length()-1))){    
            result.add("di - per - "+ input.substring(3,input.length()-1) + " - i");
            this.kataDasar = input.substring(3,input.length()-1);
        }
        
        return result;
    }
    
    public ArrayList<String> aturanKe(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add("ke -"+input);
            this.kataDasar = input;
        }
        //untuk kasus imbuhan gabungan ke-an & ke-i misal kelurahan | (contoh ke-i belum tahu)
        if("an".equals(input.substring(input.length()-2)) && getKata(input.substring(0,input.length()-2))){
                    result.add("ke - "+ input.substring(0,input.length()-2) + " - an");
                    this.kataDasar = input.substring(0,input.length()-2);
        }
        else if(input.charAt(input.length()-1)=='i' && getKata(input.substring(0,input.length()-1))){
                    result.add("ke - "+ input.substring(0,input.length()-1) + " - i");
                    this.kataDasar = input.substring(0,input.length()-1);
        }
        //untuk kasus ke-ter-xxx-an misal ketersiksaan, keterbukaan dll
        if("ter".equals(input.substring(0,3)) && "an".equals(input.substring(input.length()-2)) && getKata(input.substring(3,input.length()-2))){
                    result.add("ke - ter -"+ input.substring(3,input.length()-2) + " - an");
                    this.kataDasar = input.substring(3,input.length()-2);
        }
        
        return result;
    }
     
    public ArrayList<String> aturanKu(String input){
        ArrayList<String> result = new ArrayList<>();
        /**
         * merupakan kependekan dari "aku me-" misal kudapat
         */
        if(getKata(input)){
            result.add("ku -"+input);
            this.kataDasar = input;
        }
        //untuk kasus imbuhan gabungan ku-kan & ku-i misal ku-dapat-kan | (contoh ku-dapat-i)
        if(input.length()>3 && "kan".equals(input.substring(input.length()-3)) && getKata(input.substring(0,input.length()-3))){
                    result.add("ku - "+ input.substring(0,input.length()-3) + " kan");
                    this.kataDasar = input.substring(0,input.length()-3);
        }
        else if(input.charAt(input.length()-1)=='i' && getKata(input.substring(0,input.length()-1))){
                    result.add("ku - "+ input.substring(0,input.length()-1) + " - i");
                    this.kataDasar = input.substring(0,input.length()-1);
        }
        
        return result;
    }
    
    public ArrayList<String> aturanKau(String input){
        ArrayList<String> result = new ArrayList<>();
        /**
         * merupakan kependekan dari "engkau me-" misal kaudapat
         */
        if(getKata(input)){
            result.add("kau - "+input);//tambah space "Kau -"
            this.kataDasar = input;
        }
        //untuk kasus imbuhan gabungan kau-kan & kau-i misal kau-dapat-kan | (contoh kau-dapat-i)
        if("kan".equals(input.substring(input.length()-3)) && getKata(input.substring(0,input.length()-3))){
                    result.add("kau - "+ input.substring(0,input.length()-3) + " - kan");//tambah space " kan"
                    this.kataDasar = input.substring(0,input.length()-3);
        }
        else if(input.charAt(input.length()-1)=='i' && getKata(input.substring(0,input.length()-1))){
                    result.add("kau - "+ input.substring(0,input.length()-1) + " - i");
                    this.kataDasar = input.substring(0,input.length()-1);
        }
        
        return result;
    }
       
    public ArrayList<String> aturanPe(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            //untuk kasus kata awal l/m/n/r/w/y/q lukis -> pelukis | nyanyi -> penyanyi | rasa -> perasa | waris -> pewaris
            result.add("pe - "+input);
            this.kataDasar = input;
        }
        //Untuk Kasus kata awal /a/e/g/h/i/k*/o/u diubah menjadi peng-
       if("ng".equals(input.substring(0,2))){
            //untuk kasus /a/e/g/h/i/o/u misal (ambil <- pengambil | emis <- pengemis | gali <- penggali)
            //sudah termaksud k* yang huruf keduanya konsonan misal klarifikasi <- pengklarifikasi
            if( getKata( input.substring(2,input.length()) ) ){
                result.add("peng - "+input.substring(2));
                this.kataDasar = input.substring(2);
            }
            //untuk kasus k* yang huruf keduanya vokal misal (kurang <- pengurang)
            else if ( getKata("k"+input.substring(2)) ){
                result.add("peng - "+"k"+input.substring(2));
                this.kataDasar = "k"+input.substring(2);
            }
        }
        //untuk kasus kata awal /b/, /f/, /p*/, /v/ maka diubah menjadi pem-
        if(input.charAt(0)=='m'){
            //untuk kasus /b/, /f/, /v/ misal bantu <- pembantu, fitnah <- pemfitnah
            //sudah termaksud p* yang huruf keduanya konsonan misal proses <- pemproses
            if( getKata( input.substring(1) ) ){
                result.add("pem - "+input.substring(1));
                this.kataDasar = input.substring(1);
            }
            //untuk kasus /p*/ misal panggil <- pemanggil
            else if ( getKata("p"+input.substring(1)) ){
                result.add("pem - "+"p"+input.substring(1));
                this.kataDasar = "p"+input.substring(1);
            }
        }
        //untuk kasus kata awal /c/, /d/, /j/, /t*/ maka diubah menjadi pen-
        if(input.charAt(0)=='n'){
            //untuk kasus /c/, /d/, /j/ misal campur <- pencampur, duduk <- penduduk, jenguk <- penjenguk
            //sudah termaksud t* yang huruf keduanya konsonan misal transfer <- mentransfer
            if( getKata( input.substring(1) ) ){
                result.add("pen - "+input.substring(1));
                this.kataDasar = input.substring(1);
            }
            //untuk kasus /t*/ yang huruf keduanya vokal misal tari <- penari
            else if ( getKata("t"+input.substring(1)) ){
                result.add("pen - "+"t"+input.substring(1));
                this.kataDasar = "t"+input.substring(1);
            }
        }
        //untuk kasus kata awal /s*/ maka kata awal luluh dan diubah menjadi peny-
        if("ny".equals(input.substring(0,2))){
            //misal sontek <- penyontek
            if ( getKata("s"+input.substring(2)) ){
                result.add("peny - "+"s"+input.substring(2));
                this.kataDasar = "s"+input.substring(2);
            }
        }
        //untuk kasus  kata yang bersuku kata satu diubah menjadi menge-
        if("nge".equals(input.substring(0,3))){
            //misal bom <- pengebom | cat -> pengecat | klik -> pengeklik | lap -> pengelap | tik -> pengetik
            if ( getKata(input.substring(3)) ){
                result.add("penge - "+input.substring(3));
                this.kataDasar = input.substring(3);
            }
        }
        //Tidak dilebur jika kata dasar merupakan kata asing yang belum diserap secara sempurna. 
        //Contoh: me-hash me-transfer dsb.. (sudah ditangani tinggal dikamus saja ditambahin arti hash/transfer)
        
        //Aturan untuk imbuhan gabungan pe-an , misal perubahan
        if("an".equals(input.substring(input.length()-2)) && getKata(input.substring(0,input.length()-2))){
                    result.add("pe - "+ input.substring(0,input.length()-2) + " - an"); //tambahin space dan -
                    this.kataDasar = input.substring(0,input.length()-2);
        }
        //Aturan untuk imbuhan gabungan per-an, misal anak <- peranakan
        else if(input.charAt(0)=='r' && "an".equals(input.substring(input.length()-2)) && getKata(input.substring(1,input.length()-2))){
                result.add("per - "+ input.substring(1,input.length()-2) + " - an");
                this.kataDasar = input.substring(1,input.length()-2);
        }
        //Aturan untuk imbuhan gabungan pem-an, misal Pe–an + baru = Pembaruan Pe–an + pantau = Pemantauan
        else if(input.charAt(0)=='m' && "an".equals(input.substring(input.length()-2)) && getKata(input.substring(1,input.length()-2))){
                result.add("pem - "+ input.substring(1,input.length()-2) + " - an");
                this.kataDasar = input.substring(1,input.length()-2);
        }
        //Aturan untuk imbuhan gabungan pel-an, misal Per – an + ajar = Pelajaran
        else if(input.charAt(0)=='l' && "an".equals(input.substring(input.length()-2)) && getKata(input.substring(1,input.length()-2))){
                result.add("pel - "+ input.substring(1,input.length()-2) + " - an");
                this.kataDasar = input.substring(1,input.length()-2);
        }
        //Aturan untuk imbuhan gabungan peny-an, misal Pe–an + salur = Penyaluran,Pe–an + sampai = Penyampaian
        else if("ny".equals(input.substring(0,2)) && "an".equals(input.substring(input.length()-2)) && getKata(input.substring(2,input.length()-2))){
                result.add("peny - "+ input.substring(2,input.length()-2) + " - an");
                this.kataDasar = input.substring(2,input.length()-2);
        }
        else if("ny".equals(input.substring(0,2)) && "an".equals(input.substring(input.length()-2)) && getKata("s"+input.substring(2,input.length()-2))){
                result.add("peny - "+ "s"+input.substring(2,input.length()-2) + " - an");
                this.kataDasar = "s"+input.substring(2,input.length()-2);
        }
        //Aturan untuk imbuhan gabungan peng-an, misal Pe–an + asap = Pengasapan, Pe–an + hijau = Penghijauan
        else if("ng".equals(input.substring(0,2)) && "an".equals(input.substring(input.length()-2)) && getKata(input.substring(2,input.length()-2))){
                result.add("peng - "+ input.substring(2,input.length()-2) + " - an");
                this.kataDasar = input.substring(2,input.length()-2);
        }
        //Aturan untuk imbuhan gabungan penge-an, misal Pe–an + tahu = Pengetahuan Pe–an + bom = Pengeboman
        else if("nge".equals(input.substring(0,3)) && "an".equals(input.substring(input.length()-2)) && getKata(input.substring(3,input.length()-2))){
                result.add("penge - "+ input.substring(3,input.length()-2) + " - an");
                this.kataDasar = input.substring(3,input.length()-2);
        }
        
        //Aturan khusus
        if("merkosa".equals(input)){
            result.add("pe - "+"perkosa");
            this.kataDasar = "perkosa";
        }
        else if("merhati".equals(input)){
            result.add("pe - "+"perhati");
            this.kataDasar = "perhati";
        }
        else if("lajar".equals(input)){
            result.add("pel - "+"ajar");
            this.kataDasar = "ajar";
        }

        return result;
    }

    public ArrayList<String> aturanPer(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add("per - "+input);
            this.kataDasar = input;
        }
        //untuk kasus imbuhan gabungan per-an
        if("an".equals(input.substring(input.length()-2)) && getKata(input.substring(0,input.length()-2))){
                    result.add("per - "+ input.substring(0,input.length()-2) + " - an");
                    this.kataDasar = input.substring(0,input.length()-2);
        }

        return result;
    }
       
    public ArrayList<String> aturanSe(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add("se - "+input);
            this.kataDasar = input;
        }
        
        //untuk kasus imbuhan gabungan se-an
        if("an".equals(input.substring(input.length()-2)) && getKata(input.substring(0,input.length()-2))){
                    result.add("se - "+ input.substring(0,input.length()-2) + " an");
                    this.kataDasar = input.substring(0,input.length()-2);
        }

        return result;
    }
       
    /**
     *  Aturan-aturan imbuhan akhiran (Suffix)
     * @param input kata yang sudah dihilangkan suffixnya contoh, makanan yang dimasukan adalah makan
     * @return semua string yang valid
     */
    public ArrayList<String> akhiranKan(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add(input+" - kan");
            this.kataDasar = input;
        }

        return result;
    }
    
    public ArrayList<String> akhiranAn(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add(input+" - an");
            this.kataDasar =input;
        }

        return result;
    }
    
    public ArrayList<String> akhiranI(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add(input+" - i");
            this.kataDasar =input;
        }

        return result;
    }
    
    public ArrayList<String> akhiranLah(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add(input+" - lah");
            this.kataDasar = input;
        }

        return result;
    }
    
    public ArrayList<String> akhiranNya(String input){
        ArrayList<String> result = new ArrayList<>();
        if(getKata(input)){
            result.add(input+" - nya");
            this.kataDasar = input;
        }
        
        return result;
    }
    
    /**
     * Aturan-aturan sisipan
     * @param input string yang akan dihilangkan sisipannya. misal kinerja
     * @return 1 string saja yang sudah dihilangkan sisipannya. misal sisipan -in- pada kerja
     */
    public String sisipanIn(String input){
        String temp = input.replace("in",""); //menghilangkan sisipan -in-
        if(getKata(temp)){
            this.kataDasar = temp;
            return "sisipan -in- pada " + temp;
        }
        else{
            return null;
        }
    }
    public String sisipanEl(String input){
        String temp = input.replace("el",""); //menghilangkan sisipan -el-
        if(getKata(temp)){
            this.kataDasar = temp;
            return "sisipan -el- pada " + temp; //misalnya -el- + gembung = gelembung
        }
        else{
            return null;
        }
    }
    public String sisipanEm(String input){
        String temp = input.replace("em",""); //menghilangkan sisipan -em-
        if(getKata(temp)){
            this.kataDasar = temp;
            return "sisipan -em- pada " + temp; // -em- + gerlap = gemerlap
        }
        else{
            return null;
        }
    }
    public String sisipanEr(String input){
        String temp = input.replace("er",""); //menghilangkan sisipan -er-
        if(getKata(temp)){
            this.kataDasar = temp;
            return "sisipan -er- pada " + temp; // -er- + gigi = gerigi
        }
        else{
            return null;
        }
    }    
    public String sisipanAh(String input){
        String temp = input.replace("ah",""); //menghilangkan sisipan -ah-
        if(getKata(temp)){
            this.kataDasar = temp;
            return "sisipan -ah- pada " + temp; // -ah- + baru = baharu
        }
        else{
            return null;
        }
    }
    
    @Deprecated
    public boolean getKata(String input){
        return lexicon.searchInTree(input.toLowerCase());
    }
    
    /**
     * Seharunya ada lagi prefiks serapan asing tapi, sangat banyak kemungkinannya, jadi kelompok kami tidak
     * menangani kasus seperti itu Misalnya:
     * Bahasa Sanskerta/ Jawa Kuna / Melayu Kuna
        adi-, contoh: adikuasa
        aneka-, contoh: anekabahasa
        antar-, contoh: antarbangsa
        awa-, contoh: awanama
        catur-, contoh: caturwulan
        dasa-, contoh: dasawarsa
        dur-, contoh: durhaka
        dwi-, contoh: dwibahasa
        eka-, contoh: ekasuku
        lajak-, contoh: lajaklaku
        lewah-, contoh: lewahumur
        lir-, contoh: lirruang
        maha-, contoh: Mahakuasa
        nir-, contoh: nirkabel
        panca-, contoh: pancaragam
        pasca-, contoh: pascapanen
        pra-, contoh: prasejarah
        pramu-, contoh: pramusiwi
        purba-, contoh: purbawisesa
        purna-, contoh: purnawaktu
        su-, contoh: sujana
        swa-, contoh: swalayan
        tak-, contoh: taksa
        tan-, contoh: tansuara
        tata-, contoh: tatakrama.
        tri-, contoh: triunsur
        tuna-, contoh: tunadaksa

        * Bahasa Inggris
        a-, contoh: amoral
        ab-, contoh: abnormal
        abs-, contoh: abstrak
        ad-, contoh: adhesi
        ak-, contoh: akulturasi
        am-, contoh: amputasi
        amb-, contoh: ambivalensi
        an-, contoh: aneurisme
        ana-, contoh: anabolisme
        ant-, contoh: antasid
        ante-, contoh: anterior
        anti-, contoh: antiklinal
        apo-, contoh: apokromatik
        aut-, contoh: autarki
        auto-, contoh: autostrada
        bi-, contoh: bikonveks
        de-, contoh: dehidrasi
        di-, contoh: dikromatik
        dia-, contoh: diapositif
        dis-, contoh: disharmoni
        eko-, contoh: ekologi
        eks-, contoh: eksklusif
        ekso-, contoh: eksogami
        ekstra-, contoh: ekstradisi
        em-, contoh: empati
        en-, contoh: ensenfalitis
        endo-, contoh: endotermal
        epi-, contoh: epigon
        heksa-, contoh: heksagon
        hemi-, contoh: hemisfer
        hemo-, contoh: hemoglobin
        hepta-, contoh: heptarki
        hetero-, contoh: heterofil
        hipo-, contoh:
        il-, contoh: ilegal
        im-, contoh: imigrasi
        in-, contoh: induksi
        infra-, contoh: infrasonik
        inter-, contoh: interferensi
        intra-, contoh: intradermal
        intro-, contoh: introjeksi
        iso-, contoh: isoenzim
        kata-, contoh: katalis
        ko-, contoh: koordinasi
        kom-, contoh: komisi
        kon-, contoh: konsentrat
        kontra-, contoh: kontradiksi
        kuasi-, contoh: kuasilegislatif
        meta-, contoh: metamorfosis
        mono-, contoh: monodrama
        pan-, contoh: panasea
        pant-, contoh: pantisokrasi
        panto-, contoh: pantograf
        para-, contoh: paraldehida
        penta-, contoh: pentahedron
        peri-, contoh: perihelion
        poli-, contoh: polifagia
        pre-, contoh: prematur
        pro-, contoh: protoraks
        proto-, contoh: prototipe
        pseu-, contoh: pseudepigrafi
        pseudo-, contoh: pseudomorf
        re-, contoh: rehabilitasi
        retro-, contoh: retrofleks
        semi-, contoh: semipermanen
        sin-, contoh: sinestesia
        sub-, contoh: submukosa
        super-, contoh: supersonik
        supra-, contoh: suprasegmental
        tele-, contoh: teleskop
        trans-, contoh: transliterasi
        tri-, contoh: trikromat
        ultra-, contoh: ultramodern
        uni-, contoh: uniseluler
     */
    
    
    
}
