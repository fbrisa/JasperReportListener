package jasperreportlistener;

import java.io.File;
import java.util.Date;

/**
 *
 * @author francesco
 */
public class SessionHistoryElement {
    private String url=null;
    private File outFile=null;
    private Date time=null;

    public SessionHistoryElement() {
        time=new Date();
    }

    
    
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the outFile
     */
    public File getOutFile() {
        return outFile;
    }

    /**
     * @param outFile the outFile to set
     */
    public void setOutFile(File outFile) {
        this.outFile = outFile;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }
    
    /**
     */
    public boolean getFileEsiste() {
        return this.outFile.exists();
    }

    
}
