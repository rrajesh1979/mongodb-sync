package com.learn;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BookDAL {
    public static final String BOOKS_DB = "learn";
    public static final String BOOKS_COLLECTION = "books";

    MongoClient mongoClient;
    MongoCollection<Document> booksCollection;

    public BookDAL(MongoClient mongoClient) {
        this.mongoClient = mongoClient;

        try {
            booksCollection = this.mongoClient.getDatabase(BOOKS_DB).getCollection(BOOKS_COLLECTION);
        } catch (Exception e) {
            log.error("Error getting books DB/Collection!");
        }
    }

    public Document getBooks() {
        Document book = null;
        Document filter = new Document("bookId", "1000");
        try {
            book = booksCollection
                    .find(filter)
                    .first();
        } catch (Exception e) {
            log.error("Error getting books!");
        }
        return book;
    }

    public Document addBook(String bookId, String bookName, String authorName) {
        Faker faker = new Faker();
        ObjectId id = new ObjectId();
        Document bookDoc = new Document("_id", id)
                .append("bookId", bookId)
                .append("bookName", bookName)
                .append("authorName", authorName);

//        for(int i=0;i<100;i++) {
//            bookDoc.append(String.valueOf(i), faker.funnyName().toString());
//        }

        try {
            booksCollection.insertOne(bookDoc);
        } catch (Exception e) {
            log.error("Error inserting book :: {}", bookDoc);
        }

        return bookDoc;
    }
}
