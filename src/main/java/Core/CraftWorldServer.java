package Core;

import Core.Mod.ModClassesLoader;
import Core.Mod.ModElementRegisterer;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog logger = new HLog(Thread.currentThread().getName());
        isRunning = true;
        logger.log(HELogLevel.FINEST, "Server Thread has started.");
        if (ModClassesLoader.loadClasses()) {
            HLog.logger(HELogLevel.BUG, "Mod Loading Error!");
            if (!CraftWorldClient.isRunning)
                return;
        }
        ModElementRegisterer.registerElements();
        logger.log(ModElementRegisterer.getAllElements());
        //TODO
        logger.log(HELogLevel.FINEST, "Server Thread exits.");
    }
}
