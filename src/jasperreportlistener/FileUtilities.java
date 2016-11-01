package jasperreportlistener;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author francesco
 */
public class FileUtilities {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtilities.class);
    
    public static void deleteFilesOlderThanNdays(int daysBack, File directory,String... estensioni) {
        
        if (JasperReportListener.verboseLevel>0) {
            LOGGER.info("deleteFilesOlderThanNdays...");
            if (JasperReportListener.verboseLevel>1) {
                LOGGER.info("days:"+daysBack+" dir:"+directory.getAbsolutePath()+" estensioni:"+ estensioni.length);
            }
        }
        
        
        if (directory.exists()) {

            File[] listFiles = directory.listFiles((File dir, String name) -> {
                for(String estensione:estensioni) {
                    if (name.toLowerCase().endsWith("."+estensione.toLowerCase())) {
                        return true;
                    }
                }
                
                return false;
            });

            long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);
            for (File listFile : listFiles) {
                if (listFile.lastModified() < purgeTime) {
                    if (!listFile.delete()) {
                        LOGGER.error("Unable to delete file: " + listFile);
                    } else {
                        LOGGER.info("Deleted file:" + listFile);
                    }
                }
            }
        }
    }
}
