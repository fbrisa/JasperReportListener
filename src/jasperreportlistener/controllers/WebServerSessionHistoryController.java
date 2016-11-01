package jasperreportlistener.controllers;

import fi.iki.elonen.NanoHTTPD;
import jasperreportlistener.SessionHistory;
import nanohttpdwebserver.TwigWorker;
import java.io.File;
import java.util.Map;
import nanohttpdwebserver.controller.WebServerController;

public class WebServerSessionHistoryController extends WebServerController {    
    @Override
    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {
       
        context=ControllerUtilities.addDefaultData(context);
        
        context.put("sessionHistory",  SessionHistory.history);
        context.put("maxHistorySize",  SessionHistory.maxHistorySize);
        
        return TwigWorker.serveFile(uri, headers, session, new File("www/sessionHistory.html.twig"), null, context);
        
    }
}
