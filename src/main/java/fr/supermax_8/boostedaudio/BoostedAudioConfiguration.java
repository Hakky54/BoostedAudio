package fr.supermax_8.boostedaudio;

import org.bukkit.configuration.file.FileConfiguration;

public class BoostedAudioConfiguration {

    private boolean debugMode;
    private String clientLink;
    private boolean autoHost;
    private int autoHostPort;
    private boolean ssl;
    private String keystorePassword;
    private String keystoreFileName;
    private int webSocketPort;
    private String webSocketHostName;
    private float maxVoiceDistance;
    private String connectionMessage;
    private String connectionHoverMessage;

    public BoostedAudioConfiguration() {
        load();
    }

    private void load() {
        BoostedAudio.getInstance().saveDefaultConfig();
        FileConfiguration config = BoostedAudio.getInstance().getConfig();

        debugMode = config.getBoolean("debugMode");

        clientLink = config.getString("client-link", "http://localhost:8080");
        autoHost = config.getBoolean("autoHost", true);
        autoHostPort = config.getInt("autoHostPort", 8080);

        ssl = config.getBoolean("ssl", false);
        keystorePassword = config.getString("keystorePassword", "YOUR_PASSWORD");
        keystoreFileName = config.getString("keystoreFileName", "keystore.jks");

        webSocketPort = config.getInt("webSocketPort", 8081);
        webSocketHostName = config.getString("webSocketHostName", "localhost");

        maxVoiceDistance = (float) config.getDouble("maxVoiceDistance", 30);

        connectionMessage = config.getString("connectionMessage", "§6Join the audio client by clicking here!");
        connectionHoverMessage = config.getString("connectionHoverMessage", "Click here");
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public String getClientLink() {
        return clientLink;
    }

    public boolean isAutoHost() {
        return autoHost;
    }

    public int getAutoHostPort() {
        return autoHostPort;
    }

    public boolean isSsl() {
        return ssl;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getKeystoreFileName() {
        return keystoreFileName;
    }

    public int getWebSocketPort() {
        return webSocketPort;
    }

    public String getWebSocketHostName() {
        return webSocketHostName;
    }

    public float getMaxVoiceDistance() {
        return maxVoiceDistance;
    }

    public String getConnectionMessage() {
        return connectionMessage;
    }

    public String getConnectionHoverMessage() {
        return connectionHoverMessage;
    }
}
