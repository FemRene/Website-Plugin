package me.femrene.website;

import org.bukkit.plugin.java.JavaPlugin;

public final class Website extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        JavalinServer.start(8006);
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        JavalinServer.getJavalin().stop();
    }
}
