/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webarch.nguyen;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tuandat95cbn
 */
public class Utils {
    public static void p(PrintWriter out, String s){
        out.println(s);
    }
    /**
     * Build p tag with red color
     * @param out
     * @param s 
     */
    public static void pMessageError(PrintWriter out, String s){
        out.println("<p style=\"color:red\">"+s+"</p>");
    }
    /**
     * Build a tag with session url rewrite
     * @param url
     * @param value
     * @param response
     * @return 
     */
    public static String buildATag(String url,String value,HttpServletResponse response){
        return "<a href = \"" + response.encodeURL(url) + "\">"+value+"</a>";
    }
    
}
