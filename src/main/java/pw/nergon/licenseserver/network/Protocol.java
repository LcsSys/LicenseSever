package pw.nergon.licenseserver.network;

public class Protocol {

    private String key;
    private boolean closeAfter;
    private int id;
    private String version;
    private String mac;

    public String getKey() {
        return key;
    }

    public boolean isCloseAfter() {
        return closeAfter;
    }

    public int getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getMac() {
        return mac;
    }
}
