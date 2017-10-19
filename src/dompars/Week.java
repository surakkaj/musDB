/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dompars;

import java.util.ArrayList;

/**
 *
 * @author Kaiser
 */
public class Week {
    public int year = 0;
    public int week = 0;
    public ArrayList<Album> list = new ArrayList<Album>();
    public Album avg;
    
    public Week(int y, int w, ArrayList<Album> list){
        year = y;
        week = w;
        this.list = list;
    }
    public String toCSV(){
        StringBuilder sb = new StringBuilder("");
        sb.append(Integer.toString(year) +"/"+ Integer.toString(week)+"\n");
        for (Album a : list) {
            sb.append(a.toCSV());
        }
        return sb.toString();
    }
    @Override
    public String toString(){
        if (this == null) {
            return "NULL";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\"" + year + "/" + week + "\";\"\";\"\";\"\";\"\";\"\";\"0.0\";\"0.0\";\"-1\";\"0.0\";\"-1\";\"0.0\";\"0.0\";\"0.0\";\"0.0\";\"0.0\";\"0.0\";\n");
        for (Album a : list){
            sb.append("\n"+a.toString());
        }
        return sb.toString();
    }
    public void getAllData(){
        for(Album a : list){
            a.getAllData();
        }
    }
}
