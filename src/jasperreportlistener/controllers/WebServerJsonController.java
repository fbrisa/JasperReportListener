package jasperreportlistener.controllers;

import fi.iki.elonen.NanoHTTPD;
import jasperreportlistener.JasperReportListener;
import nanohttpdwebserver.TwigWorker;
import java.util.Map;
import nanohttpdwebserver.controller.WebServerController;

public class WebServerJsonController extends WebServerController {    
    @Override
    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {
       
        String k="";
        
        String newvl="";
        try  {
            newvl=session.getParms().getOrDefault("verboseLevel", Integer.toString(JasperReportListener.verboseLevel));
            JasperReportListener.verboseLevel=  Integer.parseInt(newvl);
            
            k=", \"verboseLevel\":"+newvl+" ";
            
        } catch (NumberFormatException e) {
            LOGGER.error("Impossibile assegnare nuovo verboseLevel:{} {}",newvl,e);
        }
        
        
        
        
        return  TwigWorker.createResponse(NanoHTTPD.Response.Status.OK, "application/json", "{ \"res\":1 "+k+" }");
        
        
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
