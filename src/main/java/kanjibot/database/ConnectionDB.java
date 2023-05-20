package kanjibot.database;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ConnectionDB {
    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    public ConnectionDB() {
        this.mongoClient = openConnection();
    }

    private MongoClient openConnection() {
        Properties props = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream("environment.properties");
        if (is != null) {
            try {
                props.load(is);
                String mongoUser = props.getProperty("MONGO_USERNAME");
                String mongoPassword = props.getProperty("MONGO_PASSWORD");
                String mongoHost = props.getProperty("MONGO_HOST");
                String mongoPort = props.getProperty("MONGO_PORT");
                String mongoDatabaseName = props.getProperty("MONGO_DATABASE");
                String mongoCollectionName = props.getProperty("MONGO_COLLECTION");

                MongoCredential credential = MongoCredential.createCredential(mongoUser, mongoDatabaseName, mongoPassword.toCharArray());

                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(List.of(new ServerAddress(mongoHost, Integer.parseInt(mongoPort)))))
                        .credential(credential)
                        .build();

                mongoClient = MongoClients.create(settings);
                MongoDatabase database = mongoClient.getDatabase(mongoDatabaseName);
                collection = database.getCollection(mongoCollectionName);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load properties file.");
                return null;
            }
        } else {
            System.out.println("Properties file not found.");
            return null;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }));

        return mongoClient;
    }

    public MongoCollection<Document> getCollection() {
        return this.collection;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
