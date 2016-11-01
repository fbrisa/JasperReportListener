/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasperreportlistener;

import jasperreportlistener.other.ApplicationClasspath;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francesco
 */
public class MyJarLoader {
    public static void loadJars(String folder) {
        
        try {
            ApplicationClasspath.addJars(folder);
        } catch (IOException ex) {
            Logger.getLogger(MyJarLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
