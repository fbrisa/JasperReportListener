package jasperreportlistener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author francesco
 */
public class RepositorySpecification {
    
    public static HashMap<String,RepositorySpecification> repositories=new HashMap<String,RepositorySpecification>();
    
    
    private String name="default";
    
    private String dstype="mysql";
    private String database_host="localhost";
    private int database_port=3306;
    private String database_name="";
    private String database_user="";
    private String database_password="";
    
    private String folder="";
    
    private LinkedHashMap<String,String> jasperParameter=new LinkedHashMap<>();
    
    private static String fixHomeFolderPath(String path) {
        return path.replaceFirst("^~",System.getProperty("user.home"));
    }
    
    
    
    public static HashMap<String,RepositorySpecification> loadFromYamlList(LinkedHashMap<String,Object> list) {
        repositories.clear();
        
        String jasperStarterFolder=((String) list.get("jasperStarterFolder"));
        JasperReportListener.jasperStarterFolder=fixHomeFolderPath(jasperStarterFolder);
        if (list.get("port")!=null) {
            JasperReportListener.port=(Integer) list.get("port");
        }
        
        
        
        LinkedHashMap tmpFolder=((LinkedHashMap) list.get("tmpFolder"));
        if (tmpFolder!=null) {
            String path=((String) tmpFolder.get("path"));
            if (path!=null) {
                JasperReportListener.tmpFolder=new File(fixHomeFolderPath(path));
                if (! JasperReportListener.tmpFolder.exists()) {
                    JasperReportListener.tmpFolder.mkdirs();
                }
            }

            if (tmpFolder.get("deletableExtensions")!=null) {
                JasperReportListener.deletableExtensions=((ArrayList<String>) tmpFolder.get("deletableExtensions"));            
            }
            
            Integer deleteTmpFilesOlder=(Integer) tmpFolder.get("deleteTmpFilesOlder");
            if (deleteTmpFilesOlder!=null) {
                JasperReportListener.deleteTmpFilesOlder=deleteTmpFilesOlder;
            }
        }
                        
        LinkedHashMap elenco=((LinkedHashMap) list.get("repositories"));
        
        Set<Map.Entry> keys=elenco.entrySet();
		
        keys.stream().forEach((k) -> {
            //System.out.println(k.getKey()+" -- "+k.getValue());
            
            
            RepositorySpecification repo=new RepositorySpecification();
            repo.setName(k.getKey().toString());
            
            LinkedHashMap para=(LinkedHashMap) k.getValue();
            
            LinkedHashMap<String,String> parameters=(LinkedHashMap<String,String>) para.get("parameters");
            repo.setDstype(parameters.get("dstype"));
            repo.setDatabase_host(parameters.get("database_host"));
            String port=(String)parameters.get("database_port");
            if (port!=null && (! port.equals("null")) && (! port.equals("~"))) {
                repo.setDatabase_port(new Integer(port));
            }
            repo.setDatabase_name(parameters.get("database_name"));
            repo.setDatabase_user(parameters.get("database_user"));
            repo.setDatabase_password(parameters.get("database_password"));            
            
            
            String historySize=(String)parameters.get("historySize");
            if (historySize!=null) {
                SessionHistory.maxHistorySize=new Integer(historySize);
            }
            
            
            
            String folder=(String) para.get("folder");
            repo.setFolder(folder);

            LinkedHashMap<String,String> jasperParameters_=(LinkedHashMap<String,String>) para.get("jasperParameters");
            if (jasperParameters_!=null) {
                repo.setJasperParameter(jasperParameters_);
            }
            
            
            RepositorySpecification.repositories.put(repo.getName(), repo);
        });
                
        
        return RepositorySpecification.repositories;
    }
    
    
    
    
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the database_host
     */
    public String getDatabase_host() {
        return database_host;
    }

    /**
     * @param database_host the database_host to set
     */
    public void setDatabase_host(String database_host) {
        this.database_host = database_host;
    }

    /**
     * @return the database_port
     */
    public int getDatabase_port() {
        return database_port;
    }

    /**
     * @param database_port the database_port to set
     */
    public void setDatabase_port(int database_port) {
        this.database_port = database_port;
    }

    /**
     * @return the database_name
     */
    public String getDatabase_name() {
        return database_name;
    }

    /**
     * @param database_name the database_name to set
     */
    public void setDatabase_name(String database_name) {
        this.database_name = database_name;
    }

    /**
     * @return the database_user
     */
    public String getDatabase_user() {
        return database_user;
    }

    /**
     * @param database_user the database_user to set
     */
    public void setDatabase_user(String database_user) {
        this.database_user = database_user;
    }

    /**
     * @return the database_password
     */
    public String getDatabase_password() {
        return database_password;
    }

    /**
     * @param database_password the database_password to set
     */
    public void setDatabase_password(String database_password) {
        this.database_password = database_password;
    }

    /**
     * @return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * @return the dstype
     */
    public String getDstype() {
        return dstype;
    }

    /**
     * @param dstype the dstype to set
     */
    public void setDstype(String dstype) {
        this.dstype = dstype;
    }

    /**
     * @return the jasperParameter
     */
    public LinkedHashMap<String,String> getJasperParameter() {
        return jasperParameter;
    }

    /**
     * @param jasperParameter the jasperParameter to set
     */
    public void setJasperParameter(LinkedHashMap<String,String> jasperParameter) {
        this.jasperParameter = jasperParameter;
    }
}
