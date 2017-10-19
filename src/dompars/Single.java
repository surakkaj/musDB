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

/**
 *
 * @author Kaiser
 */
public class Single extends Album{
    
    public String title;
    public String artist;
    private static final String authTok ="BQBkJ4zxGrXz8tEdZO7cerxAjSVMJFw9MF13Z5DDkFfN0jrQKecBtFXgHdXqZ1eSuLkCHa0KnF5oskTle4jsafAXf_5qbn1fhXc3tk43WUIUl0l0CJaKEkBpOMmC4ccKVaSLiQt7KRy3aLM";

    public Single(String t, String a) {
        super(t, a);
    }
     @Override
    public String toString(){
        if (this == null) {
            return "NULL";
        }
        return title + "; " + artist;
    }
    
}
