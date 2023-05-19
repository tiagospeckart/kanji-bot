package kanjibot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Bot extends TelegramLongPollingBot {
    private String botUsername;
    private String botToken;
    public Bot() {
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

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}