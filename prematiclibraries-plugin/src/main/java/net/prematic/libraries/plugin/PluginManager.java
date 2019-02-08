package net.prematic.libraries.plugin;

import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.event.EventManager;
import net.prematic.libraries.tasking.TaskScheduler;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

public class PluginManager {


    private final File folder;
    private final List<Plugin> plugins;

    //optinal
    private CommandManager commandmanager;
    private EventManager eventmanager;
    private TaskScheduler scheduler;

    public PluginManager(File pluginfolder){
        this.folder = pluginfolder;
        this.folder.mkdirs();
        if(this.folder.isFile()) throw new IllegalArgumentException("Plugin folder can't be a file.");
        this.plugins = new LinkedList<>();
    }
    public File getFolder() {
        return this.folder;
    }
    public List<Plugin> getPlugins(){
        return this.plugins;
    }
    public Plugin getPlugin(String name){
        for(Plugin plugin : this.plugins) if(plugin.getDescription().getName().equalsIgnoreCase(name)) return plugin;
        return null;
    }
    public void setCommandManager(CommandManager commandmanager) {
        this.commandmanager = commandmanager;
    }
    public void setEventManager(EventManager eventmanager) {
        this.eventmanager = eventmanager;
    }
    public void setScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }
    public void loadPluginsInFolder(){
        for(PluginDescription description : getAvailablePlugins(this.folder)) enablePlugin(description);
    }
    public void enablePlugin(PluginDescription description){
        try{
            if(getPlugin(description.getName()) != null)
                throw new PluginLoadException("Plugin "+description.getName()+" is already installed");
            System.out.println("enabling plugin "+description.getName()+" "+description.getVersion());
            PluginClassloader loader = new PluginClassloader(new URL[] {description.getFile().toURI().toURL()});
            Class<?> main = loader.loadClass(description.getMain());
            Plugin plugin = (Plugin) main.newInstance();
            plugin.init(description,this,loader);
            this.plugins.add(plugin);
            plugin.bootstrap();
            System.out.println("enabled plugin "+description.getName()+" version "+description.getVersion()+" by "+description.getAuthor());
        }catch (Exception e){
            throw new PluginLoadException("Could not find main class");
        }
    }
    public void disablePlugin(Plugin plugin){
        try{
            System.out.println("disabling plugin "+plugin.getDescription().getAuthor()+" "+plugin.getDescription().getVersion());
            plugin.shutdown();
            onPluginShutdown(plugin);
            this.plugins.remove(plugin);
            PluginClassloader.loaders.remove(plugin.getLoader());
            plugin.getLoader().close();
            plugin.getExecutor().shutdown();

            /*
            if(this.commandmanager != null) this.commandmanager.unregisterCommand(plugin);
            if(this.eventmanager != null) this.eventmanager.unregisterListener(plugin);
            if(this.scheduler != null) this.scheduler.cancelTask(plugin);
             */

            System.out.println("disabled plugin "+plugin.getDescription().getName()
                    +" version "+plugin.getDescription().getVersion()+" by "+plugin.getDescription().getAuthor());
            plugin = null;
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public PluginDescription getPluginDescription(File file){
        try(JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry("plugin.properties");
            if(entry == null) throw new PluginLoadException("Could not find \"plugin.properties\" file");
            InputStreamReader reader = new InputStreamReader(jar.getInputStream(entry),StandardCharsets.UTF_8);
            Properties properties = new Properties();
            properties.load(reader);
            return new PluginDescription(file
                    ,properties.getProperty("main")
                    ,properties.getProperty("name")
                    ,properties.getProperty("description")
                    ,properties.getProperty("version")
                    ,properties.getProperty("author")
                    ,properties);
        }catch (Exception exception) {
            throw new PluginLoadException(exception);
        }
    }
    public List<PluginDescription> getAvailablePlugins(File dir) {
        List<PluginDescription> descriptions = new ArrayList<>();
        for(File file : Objects.requireNonNull(dir.listFiles(new PluginFileFilter()))) descriptions.add(getPluginDescription(file));
        return descriptions;
    }
    public Boolean isPluginValiad(Plugin plugin){
        return true;
    }
    public void shutdown(){
        for(Plugin plugin : this.plugins) disablePlugin(plugin);
    }
    public void onPluginShutdown(Plugin plugin){}
    private class PluginFileFilter implements FileFilter {
        public boolean accept(File pathname) {
            return pathname.exists() && pathname.isFile() && pathname.getName().endsWith(".jar");
        }
    }
}
