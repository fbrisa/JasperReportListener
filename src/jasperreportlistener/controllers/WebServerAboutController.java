package jasperreportlistener.controllers;

import fi.iki.elonen.NanoHTTPD;
import jasperreportlistener.JasperReportListener;
import nanohttpdwebserver.TwigWorker;
import java.io.File;
import java.util.Map;
import nanohttpdwebserver.controller.WebServerController;

public class WebServerAboutController extends WebServerController {    
    @Override
    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {
       
        String newvl="";
        try  {
            newvl=session.getParms().getOrDefault("verboseLevel", Integer.toString(JasperReportListener.verboseLevel));
            JasperReportListener.verboseLevel=  Integer.parseInt(newvl);
        } catch (NumberFormatException e) {
            LOGGER.error("Impossibile assegnare nuovo verboseLevel:{} {}",newvl,e);
        }
        
        context=ControllerUtilities.addDefaultData(context);
        
        return TwigWorker.serveFile(uri, headers, session, new File("www/about.html.twig"), null, context);
        
    }
}
