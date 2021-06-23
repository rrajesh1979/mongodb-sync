package com.learn;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

@Slf4j
public class BookApplication {
    static final String VERSION = "0.0.1";
    private static String MONGODB_URI;

    public static void main(String[] args) {
        port(5000);

        log.info("MongoDB sync driver benchmark version :: {}", VERSION );

        //Initialize MongoDB Client
        MongoClient mongoClient = null;
        try {
            MONGODB_URI = System.getenv("MONGODB_URI");

            ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                    .maxSize(100)
                    .maxWaitTime(120000, TimeUnit.MILLISECONDS)
                    .build();

            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(MONGODB_URI))
                    .writeConcern(WriteConcern.MAJORITY)
                    .readConcern(ReadConcern.MAJORITY)
                    .retryWrites(true)
                    .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                    .build();
            mongoClient = MongoClients.create(mongoClientSettings);
            log.info(mongoClientSettings.getConnectionPoolSettings().toString());
        } catch (Exception e) {
            log.error("Error establishing MongoDB connection!");
            return;
        }

        APIRoutes apiRoutes = new APIRoutes(mongoClient);

        //curl -X GET http://localhost:5000/books
        get("/books", apiRoutes::getBooks);

        //curl -X POST http://localhost:5000/book/1000/HarryPotter/JKRowling
        post("/book/*/*/*", apiRoutes::addBook);

        after((req, res) -> res.type("application/json"));
    }
}
