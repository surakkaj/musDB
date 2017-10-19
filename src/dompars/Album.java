/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dompars;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kaiser
 */
class Album {

    public String title = "";
    public String artist = "";
    public String data = "";
    public String genres = "";
    public String artistid = "";
    public String trackid = "";
    public String type = ""; //0 ALB 1 SING
    public float danceability = 0f;
    public float energy = 0f;
    public int key = -1;
    public float loudness = 0f;
    public int mode = -1;
    public float speechiness = 0f;
    public float acousticness = 0f;
    public float instrumentalness = 0f;
    public float liveness = 0f;
    public float valence = 0f;
    public float tempo = 0f;

    private static final String authTok = "BQCNFiO6SATrjSHJBkRQdE2woVpmkSKj1ntDRrwLjXUBB9WOY6Iavn3cUZ4lQKy3OgFCmVwzeyv0-GunTsj9bUtZZlMZWK2KtySfYacLdbkEuHFjnLO-ia9RgxKSBOvAe6Jq2Zhm3r1vwQo";

    public Album(String t, String a) {
        title = t;
        artist = a;
        //data = getData();

    }

    public Album(String title, String artist, String genres, String artistid, String trackid, String type, float dance, float energy, int key, float loudness, int mode, float spee, float acu, float ins, float live, float vale, float tempo) {

    }

    public Album() {

    }

    public void getAllData() {
        getArtistData();
        getTrackData();
        getAudioFeatures();
    }

    public String toCSV() {
        StringBuilder sb = new StringBuilder("");
        sb.append("\"" + title.toString() + "\";");
        sb.append("\"" + artist.toString() + "\";");
        sb.append("\"" + genres.toString() + "\";");
        sb.append("\"" + artistid.toString() + "\";");
        sb.append("\"" + trackid.toString() + "\";");
        sb.append("\"" + Float.toString(danceability) + "\";");
        sb.append("\"" + Float.toString(energy) + "\";");

        sb.append("\"" + Integer.toString(key) + "\";");
        sb.append("\"" + Float.toString(loudness) + "\";");

        sb.append("\"" + Integer.toString(mode) + "\";");
        sb.append("\"" + Float.toString(speechiness) + "\";");
        sb.append("\"" + Float.toString(acousticness) + "\";");
        sb.append("\"" + Float.toString(instrumentalness) + "\";");
        sb.append("\"" + Float.toString(liveness) + "\";");
        sb.append("\"" + Float.toString(valence) + "\";");
        sb.append("\"" + Float.toString(tempo) + "\";");
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        if (this == null) {
            return "NULL";
        }
        return title + "; " + artist;
    }

    public void getArtistData() {
        String ret = "";
        try {
            URL url = new URL("https://api.spotify.com/v1/search?q=" + getArtist() + "&type=artist&market=fi&limit=1");
            HttpURLConnection murl = (HttpURLConnection) url.openConnection();
            murl.setRequestMethod("GET");
            murl.setRequestProperty("Accept", "application/json");
            murl.setRequestProperty("Authorization", "Bearer " + authTok);
            murl.setUseCaches(false);
            murl.setDoInput(true);
            murl.setDoOutput(true);
            murl.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) murl.getContent(), "ISO-8859-1"));
            String inp = "";
            StringBuilder sb = new StringBuilder("");
            while ((inp = br.readLine()) != null) {
                sb.append(inp);
            }
            ret = sb.toString();

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        Pattern arpat = Pattern.compile("artists.*? \"id\" : \"(.*?)\"", Pattern.DOTALL);
        Matcher armat = arpat.matcher(ret);
        Pattern tipat = Pattern.compile("artists.*? \"genres\" : (.*?)],", Pattern.DOTALL);
        Matcher timat = tipat.matcher(ret);
        Pattern typat = Pattern.compile("\"album_type\" : \"(.*?)\",", Pattern.DOTALL);
        Matcher tymat = tipat.matcher(ret);
        if (timat.find() && armat.find()) {

            this.genres = timat.group(1);
            this.artistid = armat.group(1);

        }
        //System.out.println(this.genres + " " + this.artistid);

    }

    public void getAudioFeatures() {
        if (this.trackid.isEmpty()) {
            return;
        }
        String ret = "";
        try {
            URL url = new URL("https://api.spotify.com/v1/audio-features/" + this.trackid);
            HttpURLConnection murl = (HttpURLConnection) url.openConnection();
            murl.setRequestMethod("GET");
            murl.setRequestProperty("Accept", "application/json");
            murl.setRequestProperty("Authorization", "Bearer " + authTok);
            murl.setUseCaches(false);
            murl.setDoInput(true);
            murl.setDoOutput(true);
            murl.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) murl.getContent(), "ISO-8859-1"));
            String inp = "";
            StringBuilder sb = new StringBuilder("");
            while ((inp = br.readLine()) != null) {
                sb.append(inp);
            }
            ret = sb.toString();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
        // System.out.println(ret);
        Pattern dancep = Pattern.compile("\"danceability\" : (.*?),", Pattern.DOTALL);
        Matcher dancem = dancep.matcher(ret);

        Pattern energyp = Pattern.compile("\"energy\" : (.*?),", Pattern.DOTALL);
        Matcher energym = energyp.matcher(ret);

        Pattern keyt = Pattern.compile("\"key\" : (.*?),", Pattern.DOTALL);
        Matcher keym = keyt.matcher(ret);

        Pattern loudp = Pattern.compile("\"loudness\" : (.*?),", Pattern.DOTALL);
        Matcher loudm = loudp.matcher(ret);

        Pattern modep = Pattern.compile("\"mode\" : (.*?),", Pattern.DOTALL);
        Matcher modem = modep.matcher(ret);

        Pattern speecp = Pattern.compile("\"speechiness\" : (.*?),", Pattern.DOTALL);
        Matcher speecm = speecp.matcher(ret);

        Pattern acousp = Pattern.compile("\"acousticness\" : (.*?),", Pattern.DOTALL);
        Matcher acousm = acousp.matcher(ret);

        Pattern intsrup = Pattern.compile("\"instrumentalness\" : (.*?),", Pattern.DOTALL);
        Matcher instrum = intsrup.matcher(ret);

        Pattern livep = Pattern.compile("\"liveness\" : (.*?),", Pattern.DOTALL);
        Matcher livem = livep.matcher(ret);

        Pattern valenp = Pattern.compile("\"valence\" : (.*?),", Pattern.DOTALL);
        Matcher valenm = valenp.matcher(ret);

        Pattern tempop = Pattern.compile("\"tempo\" : (.*?),", Pattern.DOTALL);
        Matcher tempom = tempop.matcher(ret);
        if (dancem.find()) {
            this.danceability = Float.parseFloat(dancem.group(1));
        }
        if (energym.find()) {
            this.energy = Float.parseFloat(energym.group(1));
        }
        if (loudm.find()) {
            this.loudness = Float.parseFloat(loudm.group(1));
        }
        if (speecm.find()) {
            this.speechiness = Float.parseFloat(speecm.group(1));
        }
        if (acousm.find()) {
            this.acousticness = Float.parseFloat(acousm.group(1));
        }
        if (instrum.find()) {
            this.instrumentalness = Float.parseFloat(instrum.group(1));
        }
        if (livem.find()) {
            this.liveness = Float.parseFloat(livem.group(1));
        }
        if (valenm.find()) {
            this.valence = Float.parseFloat(valenm.group(1));
        }
        if (tempom.find()) {
            this.tempo = Float.parseFloat(tempom.group(1));
        }
        if (keym.find()) {
            this.key = Integer.parseInt(keym.group(1));
        }
        if (modem.find()) {
            this.mode = Integer.parseInt(modem.group(1));
        }
        // System.out.println("" +this.tempo + this.key + this.mode +this.danceability);

    }

    public void getTrackData() {
        String ret = "";
        try {
            URL url = new URL("https://api.spotify.com/v1/search?q=" + getArtist() + "+" + getTitle() + "&type=track&market=fi&limit=1");
            HttpURLConnection murl = (HttpURLConnection) url.openConnection();
            murl.setRequestMethod("GET");
            murl.setRequestProperty("Accept", "application/json");
            murl.setRequestProperty("Authorization", "Bearer " + authTok);
            murl.setUseCaches(false);
            murl.setDoInput(true);
            murl.setDoOutput(true);
            murl.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) murl.getContent(), "ISO-8859-1"));
            String inp = "";
            StringBuilder sb = new StringBuilder("");
            while ((inp = br.readLine()) != null) {
                sb.append(inp);
            }
            ret = sb.toString();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
        //System.out.println(ret);
        Pattern arpat = Pattern.compile("tracks.*?track:(.*?)\"", Pattern.DOTALL);
        Matcher armat = arpat.matcher(ret);
        if (armat.find()) {

            this.trackid = armat.group(1);
        }
        //System.out.println(this.trackid);

    }

    public String getData() {
        String ret = "";
        try {
            URL url = new URL("https://api.spotify.com/v1/search?q=" + getArtist() + "+" + getTitle() + "&type=artist,album&market=fi&limit=1");
            HttpURLConnection murl = (HttpURLConnection) url.openConnection();
            murl.setRequestMethod("GET");
            murl.setRequestProperty("Accept", "application/json");
            murl.setRequestProperty("Authorization", "Bearer " + authTok);
            murl.setUseCaches(false);
            murl.setDoInput(true);
            murl.setDoOutput(true);
            murl.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) murl.getContent(), "ISO-8859-1"));
            String inp = "";
            StringBuilder sb = new StringBuilder("");
            while ((inp = br.readLine()) != null) {
                sb.append(inp);
            }
            ret = sb.toString();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return ret;
    }

    public String getTitle() {
        return this.title.replaceAll(" ", "+").replaceAll("ä", "%C3%A4").replaceAll("ö", "%C3%B6").replaceAll("Ä", "%C3%84").replaceAll("Ö", "%C3%96").replaceAll("Å", "%C3%85").replaceAll("å", "%C3%A5");
    }

    public String getArtist() {
        return this.artist.replaceAll(" ", "+").replaceAll("ä", "%C3%A4").replaceAll("ö", "%C3%B6").replaceAll("Ä", "%C3%84").replaceAll("Ö", "%C3%96").replaceAll("Å", "%C3%85").replaceAll("å", "%C3%A5");
    }
}
/*
curl -X GET "https://api.spotify.com/v1/search?q=Antti+tuisku+&type=artist,album&market=fi&limit=1" -H "Accept: application/json" -H "Authorization: Bearer BQD1SnX4GhED1WLS7ieVjlH914WAktJoy_fz-GcjSHUeyGGgiEsX3dc31wbXTikbTVOHKrRXV3CPl6GsjzOooKjr5_HSSxKIjMKux5noWmxkLh5ymiD44jiZIyo0s9nsKLcLo_cvwyC0PKk"
*/
