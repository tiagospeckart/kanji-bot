package kanjibot;

import org.junit.jupiter.api.*;
import org.testng.Assert;

public class BotTest {
    Bot testBot = new Bot();

    @Test
    public void testGetBotUsername() {
        Assert.assertEquals(testBot.getBotUsername(), "kanjidic_bot", "Bot Username is incorrect");
    }

    @Test
    public void testGetBotToken() {
        Assert.assertNotNull(testBot.getBotToken(),"No API Key provided");
    }

}