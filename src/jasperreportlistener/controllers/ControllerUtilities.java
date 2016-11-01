package jasperreportlistener.controllers;

import jasperreportlistener.JasperReportListener;
import jasperreportlistener.RepositorySpecification;
import java.util.Map;

/**
 *
 * @author francesco
 */
public class ControllerUtilities {
    
    public static Map<String, Object>  addDefaultData(Map<String, Object> context) {
        context.put("websiteTitle",  "JasperReportListener");
        context.put("version",  jasperreportlistener.JasperReportListener.VERSION);
        context.put("runningSince",  JasperReportListener.runningSince);
        context.put("verboseLevel", JasperReportListener.verboseLevel);
        context.put("repositories",  RepositorySpecification.repositories);
        
        return context;
    }
    
}
