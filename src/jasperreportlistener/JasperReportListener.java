package jasperreportlistener;

import jasperreportlistener.controllers.WebServerCmdController;
import jasperreportlistener.controllers.WebServerReloadController;
import jasperreportlistener.controllers.WebServerJasperController;
import jasperinvoker.JasperInvoker;
import jasperreportlistener.controllers.WebServerAboutController;
import jasperreportlistener.controllers.WebServerConfigController;
import jasperreportlistener.controllers.WebServerHelpController;
import jasperreportlistener.controllers.WebServerIndexController;
import jasperreportlistener.controllers.WebServerJsonController;
import jasperreportlistener.controllers.WebServerSessionHistoryController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import nanohttpdwebserver.NetworkTCPListener;
import nanohttpdwebserver.NetworkTCPListenerStarter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author francesco
 */
public class JasperReportListener {

    private static Logger LOGGER;

    public static final String VERSION = "1.1";
    public static int verboseLevel = 0;
    public static String configRepositoryPath = "config/repositories.yml";
    public static String jasperStarterFolder = null;

    public static final int DEFAULT_PORT = 8777;
    public static int port = DEFAULT_PORT;

    public static String webcmd = null;
    public static boolean vivi = true;

    public static File tmpFolder = new File("./");
    public static ArrayList<String> deletableExtensions = new ArrayList<>();
    public static int deleteTmpFilesOlder = -1;

    public static Date runningSince = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        runningSince = new Date();

        ArgumentParser parser = createArgumentParser();
        try {
            Namespace res = parser.parseArgs(args);

            verboseLevel = res.getInt("verbose");
            if (res.getString("repofile") != null) {
                configRepositoryPath = res.getString("repofile");//--repofile ./customrepositories.yml
            }

        } catch (ArgumentParserException ex) {
            parser.handleError(ex);
            System.exit(1);
        }

        File configLog4jFile = new File("./config/logback.xml");

        //Configurator.initialize("", "./config/log4j.xml");
        if (configLog4jFile.exists()) {
            System.setProperty("logback.configurationFile", configLog4jFile.getAbsolutePath());
            //Configurator.initialize("",null, configLog4jFile.toURI());
        }

        LOGGER = LoggerFactory.getLogger(JasperReportListener.class);
        if (verboseLevel > 0) {
            LOGGER.info("Avvio JasperReportListener");
        }

        if (booleanloadRepos()) {
            //logger.info(list.toString());
            if (verboseLevel > 2) {
                LOGGER.info("Loading jars");
            }
            MyJarLoader.loadJars(JasperReportListener.jasperStarterFolder + "/lib");
            MyJarLoader.loadJars(JasperReportListener.jasperStarterFolder + "/jdbc");
            if (verboseLevel > 2) {
                LOGGER.info("OK");
            }

            deleteOlderFiles();

            //http://127.0.0.1:8777/?repo=locale&jasper=example1.jasper&out=/tmp/aaa.pdf&COD=68
            //            logger.info("Test 1");
            //            JasperReportListener.invokeByRepository(
            //                "locale",
            //                RepositorySpecification.repositories.get("locale").getFolder()+"/example1.jasper",
            //                "/tmp/out1",
            //                new String[]{"COD=69"}
            //            );
            //            JasperReportListener.invokeByRepository(
            //                "locale",
            //                RepositorySpecification.repositories.get("locale").getFolder()+"/example1.jasper",
            //                "/tmp/out2",
            //                new String[]{"COD=70"}
            //            );
            //            logger.info("OK");
            //            
            NetworkTCPListener networkTCPListener = (new NetworkTCPListenerStarter()).startListeningOnPort(JasperReportListener.port);
            //networkTCPListener.controllers.put("/", new WebServerControllerDefault());        
            //networkTCPListener.controllers.put("/index.html.twig", new WebServerControllerDefault());        
            networkTCPListener.controllers.put("/jasper", new WebServerJasperController());
            networkTCPListener.controllers.put("/cmd", new WebServerCmdController());
            networkTCPListener.controllers.put("/json", new WebServerJsonController());
            networkTCPListener.controllers.put("/reload", new WebServerReloadController());
            networkTCPListener.controllers.put("/about", new WebServerAboutController());
            networkTCPListener.controllers.put("/help", new WebServerHelpController());
            networkTCPListener.controllers.put("/sessionHistory", new WebServerSessionHistoryController());
            networkTCPListener.controllers.put("/config", new WebServerConfigController());
            networkTCPListener.controllers.put("", new WebServerIndexController());

            if (verboseLevel > 1) {
                String k = "";
                if (port != DEFAULT_PORT) {
                    k = " on port " + port;
                }
                LOGGER.info("JasperReportListener up and running" + k);
            }

            while (vivi) {
                try {
                    Thread.sleep(10000);

                    if (webcmd != null) {
                        if (webcmd.equals("shutdown")) {
                            //http://127.0.0.1:8777/cmd/?cmd=shutdown

                            Thread t = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException ex) { }
                                    vivi = false;
                                }

                            };
                            t.start();

                            if (verboseLevel > 0) {
                                LOGGER.info("Shutdown da web.");
                            }
                        }
                        
                        if (webcmd.equals("vrbP")) {
                            verboseLevel++;
                        }
                        if (webcmd.equals("vrbM")) {
                            if (verboseLevel>0) {
                                verboseLevel--;
                            }
                        }
                        
                        webcmd="";
                    }

                } catch (InterruptedException ex) {
                }
            }

        } else {
            LOGGER.error("esco in quanto repository file {} non trovato", configRepositoryPath);
        }

        if (verboseLevel > 1) {
            LOGGER.info("Fine programma");
        }
        
    }

    public static void deleteOlderFiles() {
        String[] exts = new String[deletableExtensions.size()];
        exts = deletableExtensions.toArray(exts);

        if (deleteTmpFilesOlder >= 0) {
            FileUtilities.deleteFilesOlderThanNdays(deleteTmpFilesOlder, tmpFolder, exts);
        }
    }

    public static void invokeByRepository(String repoName, String jasperFile, String output, ArrayList<String> parametersArr) {
        RepositorySpecification repo = RepositorySpecification.repositories.get(repoName);

        
        repo.getJasperParameter().keySet().forEach((key) -> {
            parametersArr.add("-P");
            parametersArr.add(key + "=" + repo.getJasperParameter().get(key));
        });
        
        String[] parametriReportString = new String[parametersArr.size()];
        parametriReportString = parametersArr.toArray(parametriReportString);
        
        
        JasperInvoker.invoke(
            repo.getFolder(),
            jasperFile,
            repo.getDstype(),
            repo.getDatabase_host(),
            repo.getDatabase_user(),
            repo.getDatabase_password(),
            repo.getDatabase_name(),
            output,
            parametriReportString
        );
    }

    private static ArgumentParser createArgumentParser() {
        ArgumentParser parser= ArgumentParsers.newArgumentParser("jasperreportlistener", false).version(VERSION);

        parser.addArgument("-h", "--help").action(Arguments.help()).help("show this help message and exit");
        parser.addArgument("--verbose", "-v").action(Arguments.count());
        parser.addArgument("-r", "--repofile").nargs("?").help("chose a custom repository file");

        return parser;
    }

    public static boolean booleanloadRepos() {

        Yaml yaml = new Yaml();
        try {
            File configRepository = new File(configRepositoryPath);
            if (configRepository.exists()) {
                LinkedHashMap<String, Object> list = (LinkedHashMap<String, Object>) yaml.load(new FileInputStream(configRepository));
                RepositorySpecification.loadFromYamlList(list);
                return true;
            } else {
                LOGGER.error("Config repository file:" + configRepositoryPath + " not found.");
            }

        } catch (FileNotFoundException ex) {
            LOGGER.error("configRepositoryPath non trovato: {}", ex);
        }

        return false;
    }

}
