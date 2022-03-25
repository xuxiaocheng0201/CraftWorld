package Core;

import Core.Events.ServerStoppingEvent;
import Core.Exceptions.ModRequirementsException;
import Core.Mod.ModClassesLoader;
import Core.Mod.ModLauncher;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftworldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldServer");
        HLog logger = new HLog(Thread.currentThread().getName());
        isRunning = true;
        logger.log(HELogLevel.FINEST, "Server Thread has started.");
        if (ModClassesLoader.loadClasses()) {
            HLog.logger(HELogLevel.BUG, "Mod Loading Error in loading! Server Thread exits.");
            isRunning = false;
            return;
        }
        ModClassesLoader.registerElements();
        ModLauncher.buildModContainer();
        ModLauncher.checkModContainer();
        if (!ModLauncher.getExceptions().isEmpty()) {
            logger.log(HELogLevel.BUG, "Mod Loading Error in checking requirements! Server Thread exits.");
            logger.log(HELogLevel.ERROR, ModLauncher.getExceptions());
            for (ModRequirementsException exception: ModLauncher.getExceptions())
                exception.printStackTrace();
            isRunning = false;
            return;
        }
        ModLauncher.toSimpleModContainer();
        System.gc();
        ModLauncher.sortMods();
        if (!ModLauncher.getExceptions().isEmpty()) {
            logger.log(HELogLevel.BUG, "Mod Loading Error in shorting! Server Thread exits.");
            logger.log(HELogLevel.ERROR, ModLauncher.getExceptions());
            for (ModRequirementsException exception: ModLauncher.getExceptions())
                exception.printStackTrace();
            isRunning = false;
            return;
        }
        logger.log(HELogLevel.FINEST, "Sorted Mod list: ", ModLauncher.getSortedMods());
        ModLauncher.launchMods();
        /* ********** Special Modifier ********** */
        CraftWorld.CraftWorld.getInstance().start();
        /* ********** \Special Modifier ********** */
        ModClassesLoader.getDefaultEventBus().post(new ServerStoppingEvent());
        isRunning = false;
        logger.log(HELogLevel.FINEST, "Server Thread exits.");
    }
}