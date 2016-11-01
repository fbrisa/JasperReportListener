package jasperreportlistener.controllers;

import fi.iki.elonen.NanoHTTPD;
import jasperreportlistener.JasperReportListener;
import nanohttpdwebserver.TwigWorker;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import nanohttpdwebserver.controller.WebServerController;

public class WebServerIndexController extends WebServerController {    
    
    public static String computeDiffString(Map<TimeUnit,Long> diff) {
        String res="";
        if (diff.get(TimeUnit.DAYS)>0) {
            res=diff.get(TimeUnit.DAYS).toString()+" days, ";
        }
        res=res+diff.get(TimeUnit.HOURS).toString()+" hours, ";
        res=res+diff.get(TimeUnit.MINUTES).toString()+" minutes, ";
        res=res+diff.get(TimeUnit.SECONDS).toString()+" seconds";
        
        return res;
    }

    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }    
    
    @Override
    public NanoHTTPD.Response execMe(Map<String, String> headers, NanoHTTPD.IHTTPSession session, String uri,Map<String, Object> context) {
//        context.put("verboseLevel", JasperReportListener.verboseLevel);                

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy hh:mm:ss");
        

        context=ControllerUtilities.addDefaultData(context);
                
        Map<TimeUnit,Long> runningTime=computeDiff(JasperReportListener.runningSince,new Date());
        //Map:{DAYS=1, HOURS=3, MINUTES=46, SECONDS=40, MILLISECONDS=0, MICROSECONDS=0, NANOSECONDS=0}
        context.put("runningTime", computeDiffString(runningTime) );
        
        return TwigWorker.serveFile(uri, headers, session, new File("www/index.html.twig"), null, context);
        
    }
}
