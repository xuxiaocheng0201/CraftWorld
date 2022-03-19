package Core;

import Core.Event.EventSubscribe;
import HeadLibs.Configuration.HConfig;
import HeadLibs.Configuration.HConfigurations;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@EventSubscribe(eventBus = "*")
public class Craftworld {
    public static final String CURRENT_VERSION = "0.0.0";
    public static final String RUNTIME_PATH = HStringHelper.merge("Craftworld\\", CURRENT_VERSION, "\\");
    public static final String GLOBAL_CONFIGURATION_PATH = HStringHelper.merge(RUNTIME_PATH, "global.cfg");
    public static final String ASSETS_PATH = HStringHelper.merge(RUNTIME_PATH, "assets\\Craftworld\\");
    public static final String LOG_PATH;
    static {
        String log_path = HStringHelper.merge(RUNTIME_PATH, "log\\", HStringHelper.getDate("yyyy-MM-dd"), ".log");
        int i = 1;
        while ((new File(log_path)).exists())
            log_path = HStringHelper.merge(RUNTIME_PATH, "log\\", HStringHelper.getDate("yyyy-MM-dd"), "_", ++i, ".log");
        LOG_PATH = log_path;
        if (!(new File(Craftworld.ASSETS_PATH)).exists())
            if (System.console() != null)
                HFileHelper.extractFilesFromJar("assets", Craftworld.ASSETS_PATH);
            else
                try {
                    Files.copy((new File("../src/main/resources/assets")).getAbsoluteFile().toPath(),
                            (new File(Craftworld.ASSETS_PATH)).getParentFile().getAbsoluteFile().toPath());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
//        if (System.console() != null)
//            HFileHelper.extractFilesFromJar("natives", "natives");
    }

    public static boolean isClient = true;
    public static HConfigurations GLOBAL_CONFIGURATIONS;
    public static String CURRENT_LANGUAGE = "zh_cn";

    public static void main(String[] args)  {
        Thread.currentThread().setName("CraftWorldMain");
        HLog.logger(HELogLevel.INFO, "Hello Craftworld!");
        GetConfigurations();
        for (String i: args) {
            if (i == null)
                continue;
            if (i.equals("runClient"))
                isClient = true;
            if (i.equals("runServer"))
                isClient = false;
        }
        HLog.saveLogs(LOG_PATH);
        Runtime.getRuntime().addShutdownHook(new Thread(Thread.currentThread().getName()) {
            @Override
            public void run() {
                HLog.logger(HELogLevel.INFO, "Welcome to play again!");
                HLog.saveLogs(LOG_PATH);
            }
        });
        System.gc();
        try {
            if (isClient) {
                Thread client = new Thread(new CraftWorldClient());
                client.start();
                client.join();
            } else {
                Thread server = new Thread(new CraftWorldServer());
                server.start();
                server.join();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public static void GetConfigurations() {
        GLOBAL_CONFIGURATIONS = new HConfigurations(GLOBAL_CONFIGURATION_PATH);
        HConfig language = GLOBAL_CONFIGURATIONS.getByName("language");

        if (language == null)
            language = new HConfig("language", LanguageI18N.get("Core.configuration.language.name"), CURRENT_LANGUAGE);
        else
            language.setNote(LanguageI18N.get("Core.configuration.language.name"));
        CURRENT_LANGUAGE = language.getValue();

        GLOBAL_CONFIGURATIONS.clear();
        GLOBAL_CONFIGURATIONS.add(language);
        GLOBAL_CONFIGURATIONS.write();
    }

    @Subscribe
    public void onEvent(Object event) {
        HLog.logger(HELogLevel.DEBUG, "Posted Event: ", event.getClass().getName());
    }
}
