package pw.nergon.licenseserver.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import pw.nergon.licenseserver.LicenseServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigHandler {

    private ConfigInfo loadedConfig;
    private File configFile;
    private Yaml yaml;

    public ConfigHandler() {
        Constructor constructor = new Constructor();
        PropertyUtils utils = constructor.getPropertyUtils();
        utils.setSkipMissingProperties(true);
        constructor.setPropertyUtils(utils);
        yaml = new Yaml(constructor);
        LicenseServer.instance.getLogger().log("Trying to open 'config.yml'");
        loadConfig();
    }

    private void loadConfig() {
        configFile = new File("config.yml");
        try (InputStream inputStream = new FileInputStream(configFile)) {
            loadedConfig = yaml.loadAs(inputStream, ConfigInfo.class);
            LicenseServer.instance.getLogger().logSuccess("Successfully opened file config.yml and loaded config");
        } catch (IOException e) {
            LicenseServer.instance.getLogger().logError("IOException when reading config.yml");
            e.printStackTrace();
        }
    }

    public ConfigInfo getLoadedConfig() {
        return loadedConfig;
    }
}
