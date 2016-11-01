package jasperreportlistener;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author francesco
 */
public class SessionHistory {
    public static ArrayList<SessionHistoryElement> history=new ArrayList<>();    
    public static int maxHistorySize=100;
    
    public static SessionHistoryElement add(String url,File fileout) {
        SessionHistoryElement s=new SessionHistoryElement();
        
        s.setOutFile(fileout);
        s.setUrl(url);
        
        if (history.size()>=maxHistorySize) {
            history.remove(0);            
        }
        
        history.add(s);
        
        return s;
    }
}
