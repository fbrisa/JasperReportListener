package jasperreportlistener.controllers;

import fi.iki.elonen.NanoHTTPD;
import jasperreportlistener.JasperReportListener;
import nanohttpdwebserver.TwigWorker;
import java.io.File;
import java.util.Map;
import nanohttpdwebserver.controller.WebServerController;

public class WebServerReloadController extends WebServerController {    
    @Override
    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {
       
        
                
        boolean res=JasperReportListener.booleanloadRepos();

        context=ControllerUtilities.addDefaultData(context);
        
        context.put("res", res);
        
        return TwigWorker.serveFile(uri, headers, session, new File("www/reload.html.twig"), null, context);
        
    }
}
