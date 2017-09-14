package jasperreportlistener.controllers;

import fi.iki.elonen.NanoHTTPD;
import jasperreportlistener.JasperReportListener;
import jasperreportlistener.RepositorySpecification;
import static jasperreportlistener.JasperReportListener.deleteOlderFiles;
import jasperreportlistener.SessionHistory;
import nanohttpdwebserver.TwigWorker;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import nanohttpdwebserver.controller.WebServerController;

public class WebServerJasperController extends WebServerController {    
    
    private static String getFileExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }
    
    
    @Override
    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {
       
        deleteOlderFiles();
        
        String out=session.getParms().getOrDefault("out","out.pdf");
        String jasperString=session.getParms().get("jasper");
        String repo=session.getParms().get("repo");
        
        
        File file = new File(out);
        if (! file.isAbsolute()) {
			if (! JasperReportListener.tmpFolder.exists()) {
				// potrebbe essere stata cancellata nel frattempo
				JasperReportListener.tmpFolder.mkdirs();
			}
            out= JasperReportListener.tmpFolder+"/"+file.getName();
			
        }
        
        
        String ext1 =  getFileExtension(jasperString); // retu
        if (ext1.isEmpty()) {
            // no extension, add .jasper
            jasperString=jasperString+".jasper";
        }
        
        // ogni caso cerco il file .jasper
        File jasper=new File(jasperString);
        if (! jasper.exists()) {
            // aggiungo il path
            if (RepositorySpecification.repositories.get(repo).getFolder()!=null) {
                jasper=new File(RepositorySpecification.repositories.get(repo).getFolder()+"/"+jasperString);                
            }
        }

        if (! jasper.exists()) {
            LOGGER.error("Non trovo il file report {}", jasperString);
            
            return TwigWorker.createResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json", "{ \"res\":\"File jasper not found\" }");
        }


        
        ArrayList<String> parametriReport=new ArrayList<>();
        
        session.getParms().keySet().stream().filter(
            (key) -> (! (key.equals("out") || key.equals("jasper") || key.equals("repo") || key.equals("folder")))
        ).forEachOrdered((key) -> {
            parametriReport.add("-P");
            parametriReport.add(key + "=" + session.getParms().get(key));
        });
        
        
        
        
        JasperReportListener.invokeByRepository(
            repo,
            jasper.getAbsolutePath(),
            out,
            parametriReport//new String[]{"COD="+session.getParms().get("COD")}
        );
        
        
        File outFile=new File(out);
        
        
        SessionHistory.add(session.getUri()+"?"+session.getQueryParameterString(), outFile);
        
        return TwigWorker.serveFile(uri, headers, session, outFile, "application/pdf", context);
        
        
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
