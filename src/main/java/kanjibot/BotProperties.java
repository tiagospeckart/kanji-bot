package kanjibot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotProperties {
    private String botUsername;
    private String botToken;

    public BotProperties() {
        Properties props = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream("environment.properties");
        if (is != null) {
            try {
                props.load(is);
                this.botUsername = props.getProperty("BOT_NAME");
                this.botToken = props.getProperty("BOT_TOKEN");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provide appropriate Bot API Token and Bot");
        }
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
