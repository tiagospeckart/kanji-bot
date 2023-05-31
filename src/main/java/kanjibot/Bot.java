package kanjibot;

import com.mongodb.client.MongoCollection;
import kanjibot.database.ConnectionDB;
import org.bson.Document;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private final BotProperties botProperties;

    public Bot() {
        this.botProperties = new BotProperties();
        ConnectionDB dbConnection = new ConnectionDB();
        MongoCollection<Document> collection = dbConnection.getCollection();
        Runtime.getRuntime().addShutdownHook(new Thread(dbConnection::closeConnection));
    }

    @Override
    public String getBotUsername() {
        return botProperties.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botProperties.getBotToken();
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
