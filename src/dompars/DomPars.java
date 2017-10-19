/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dompars;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kaiser
 */
public class DomPars {

    /**
     * @param args the command line arguments
     */
    final static String ALBUMLIST = "http://www.ifpi.fi/tilastot/virallinen-lista/albumit";
    final static String SINGLELIST = "http://www.ifpi.fi/tilastot/virallinen-lista/singlet";
    final static String ROOTURL = "http://www.ifpi.fi/";

    public static void main(String[] args) {
//        System.out.println(splitSpecDivList(getHtml("http://www.ifpi.fi/tilastot/virallinen-lista/albumit")));
//        ArrayList<String> ab = splitLinks(splitSpecDivList(getHtml("http://www.ifpi.fi/tilastot/virallinen-lista/albumit")));
//        for (String a : ab) {
//            System.out.println(a);
//        }
//        ArrayList<String> ac = splitAlbums(getHtml("http://www.ifpi.fi/tilastot/virallinen-lista/albumit/1996/50"));
//        for (String a : ac) {
//            System.out.println(a);
//        }
        
        ArrayList<Week> listas = getWeekSingleList();
        for (Week w : listas) {
            w.getAllData();
        }
        StringBuilder sb = new StringBuilder();
        for (Week w : listas) {
            sb.append(w.toCSV());
            
        }
        System.out.println(sb.toString());
        
    }

    public static ArrayList<Week> getWeekList() {
        ArrayList<String> linkList = splitLinks(splitSpecDivList(getHtml(ALBUMLIST)));
        ArrayList<Week> ret = new ArrayList<>();
        for (String s : linkList) {
            ret.add(new Week(2, 2, getAlbumList(splitAlbums(getHtml(ROOTURL + s)))));
           // System.out.println(getHtml(ROOTURL + s));
        }

        return ret;
    }
    public static ArrayList<Week> getWeekSingleList() {
        ArrayList<String> linkList = splitLinks(splitSpecDivList(getHtml(SINGLELIST)));
        ArrayList<Week> ret = new ArrayList<>();
        Pattern pat = Pattern.compile(".*?(\\d{4})/(\\d{1,2})");
        Matcher mat;
        int year = 0;
        int week = 0;
        for (String s : linkList) {
            mat = pat.matcher(s);
            if (mat.matches()) {
                year = Integer.parseInt(mat.group(1));
                week = Integer.parseInt(mat.group(2));
                System.out.println(year + "_" + week);
            }
            ret.add(new Week(year, week, getAlbumList(splitAlbums(getHtml(ROOTURL + s)))));
           // System.out.println(getHtml(ROOTURL + s));
        }

        return ret;
    }

    public static ArrayList<String> splitLinks(String s) {
        Pattern pat = Pattern.compile("<a href=\"(.+?)\">");
        Matcher mat = pat.matcher(s);
        ArrayList<String> list = new ArrayList<>();
        while (mat.find()) {
            list.add(mat.group(1));
        }
        return list;
    }

    public static ArrayList<String> splitAlbums(String s) {
        Pattern pat = Pattern.compile("<div class=\"album-info\">(.+?)<div class=\"album-art\">", Pattern.DOTALL);
        Matcher mat = pat.matcher(s);
        ArrayList<String> list = new ArrayList<>();
        while (mat.find()) {
            list.add(mat.group());
        }
        return list;
    }

    public static ArrayList<Album> getAlbumList(ArrayList<String> lis) {
        ArrayList<Album> alblist = new ArrayList<Album>();

        for (String s : lis) {
            alblist.add(getAlbum(s));
        }
        return alblist;
    }

    public static Album getAlbum(String html) {
        String ahtml = html;
        Pattern arpat = Pattern.compile("<a class=\"artist\" href=.*?>(.*?)</a", Pattern.DOTALL);
        Matcher armat = arpat.matcher(html);
        Pattern tipat = Pattern.compile("<a class=\"title\" href=.*?>(.*?)</a", Pattern.DOTALL);
        Matcher timat = tipat.matcher(ahtml);
        Album album = new Album();
        if (timat.find() && armat.find()) {

            album = new Album(timat.group(1), armat.group(1));
            

        }
        //System.out.println(album.toString());
        return album;
    }

    public static String splitSpecDivList(String s) {
       
        String[] ss = s.split("<div id=\"virallinen-lista-arkisto\">");
        return ss[1].split("<div id=\"aside\">")[0];

//        Pattern pat = Pattern.compile("<html.*(.+?).*/html>", Pattern.MULTILINE);
////        Pattern pat = Pattern.compile("^<div id=\"virallinen-lista-arkisto\">.+?<div id=\"aside\">");
//        Matcher mat = pat.matcher(s);
//        if (mat.matches()) {
//         
//        return mat.group(0);   
//        }else{
//            return s + " Error";
//        }
    }
    public static String splitSpecDivSing(String s) {
       
        String[] ss = s.split("<div id=\"virallinen-lista-arkisto\">");
        return ss[1].split("<div id=\"aside\">")[0];

//        Pattern pat = Pattern.compile("<html.*(.+?).*/html>", Pattern.MULTILINE);
////        Pattern pat = Pattern.compile("^<div id=\"virallinen-lista-arkisto\">.+?<div id=\"aside\">");
//        Matcher mat = pat.matcher(s);
//        if (mat.matches()) {
//         
//        return mat.group(0);   
//        }else{
//            return s + " Error";
//        }
    }

    public static String getHtml(String s) {
        try {

            URL url = new URL(s);
            
            InputStream inp = url.openStream();
            int a = 0;
            StringBuffer ret = new StringBuffer();
            while ((a = inp.read()) != -1) {
                ret.append((char) a);
            }
            return new String(ret.toString().getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {
            return "Error from " + s;
        }
    }

}
