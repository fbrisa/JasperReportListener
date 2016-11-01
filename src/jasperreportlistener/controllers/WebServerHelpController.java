package jasperreportlistener.controllers;

import fi.iki.elonen.NanoHTTPD;
import nanohttpdwebserver.TwigWorker;
import java.io.File;
import java.util.Map;
import nanohttpdwebserver.controller.WebServerController;

public class WebServerHelpController extends WebServerController {    
    @Override
    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {
//        context.put("verboseLevel", JasperReportListener.verboseLevel);                
//        context.put("version",  JasperReportListener.VERSION);
        context=ControllerUtilities.addDefaultData(context);
        
        
        return TwigWorker.serveFile(uri, headers, session, new File("www/help.html.twig"), null, context);
        
    }
}
