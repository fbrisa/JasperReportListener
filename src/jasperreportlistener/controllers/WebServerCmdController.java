package jasperreportlistener.controllers;

import fi.iki.elonen.NanoHTTPD;
import jasperreportlistener.JasperReportListener;
import nanohttpdwebserver.TwigWorker;
import java.io.File;
import java.util.Map;
import nanohttpdwebserver.controller.WebServerController;

public class WebServerCmdController extends WebServerController {    
    @Override
    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {

        context=ControllerUtilities.addDefaultData(context);
        
        context.put("cmd", session.getParms().get("cmd"));
        JasperReportListener.webcmd=session.getParms().get("cmd");
       
        return TwigWorker.serveFile(uri, headers, session, new File("www/cmd.html.twig"), null, context);
        
        
//        context.put("cod", session.getParms().get("COD"));
//        context.put("out", session.getParms().get("out"));
//        return TwigWorker.serveFile(uri, headers, session, new File("www/jasper.html.twig"), null, context);
    }
//    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {
//        
//        context.put("list", "lisssssssta");
//        
//        context.put("cod", session.getParms().get("cod"));
//        
//        return TwigWorker.serveFile(uri, headers, session, new File("www/jasper.html.twig"), null, context);
//    }
}
