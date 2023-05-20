package kanjibot;

import com.mongodb.client.MongoCollection;
import kanjibot.database.ConnectionDB;
import org.bson.Document;
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
    private ConnectionDB dbConnection;

    public Bot() {
        Properties props = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream("environment.properties");
        if (is != null) {
            try {
                props.load(is);
                this.botUsername = props.getProperty("BOT_NAME");
                this.botToken = props.getProperty("BOT_TOKEN");

                this.dbConnection = new ConnectionDB();
                MongoCollection<Document> collection = dbConnection.getCollection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provide appropriate Bot API Token and Bot");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }));
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if(messageText.startsWith("/")) {
                handleCommand(update);
            } else {
                handleMessage(update);
            }
        }
    }

    private void handleCommand(Update update) {
        String command = update.getMessage().getText().substring(1); // removes the "/"
        SendMessage message = new SendMessage();

        // respond according to the command
        if (command.equals("start")) {
            message.setText("Welcome to KanjiBot!");
        } else if (command.equals("help")) {
            message.setText("Here is a list of commands...");
        } else {
            message.setText("Unknown command");
        }

        message.setChatId(update.getMessage().getChatId().toString());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Update update) {
        String messageText = update.getMessage().getText();
        SendMessage message = new SendMessage();

        // ... You can add your logic here ...

        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("You sent: " + messageText);

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
