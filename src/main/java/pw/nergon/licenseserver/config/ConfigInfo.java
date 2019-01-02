package pw.nergon.licenseserver.config;

public class ConfigInfo {

    private String dbHost, dbName, dbUser, dbPass, pathToKeyStore, keyStorePass, pathToTrustStore, trustStorePass;
    private boolean useSSL;

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getPathToKeyStore() {
        return pathToKeyStore;
    }

    public void setPathToKeyStore(String pathToKeyStore) {
        this.pathToKeyStore = pathToKeyStore;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public String getPathToTrustStore() {
        return pathToTrustStore;
    }

    public void setPathToTrustStore(String pathToTrustStore) {
        this.pathToTrustStore = pathToTrustStore;
    }

    public String getTrustStorePass() {
        return trustStorePass;
    }

    public void setTrustStorePass(String trustStorePass) {
        this.trustStorePass = trustStorePass;
    }
}
