package com.learn;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.concurrent.TimeUnit;

@Slf4j
public class BookDAL {
    public static final String BOOKS_DB = "learn";
    public static final String BOOKS_COLLECTION = "books";

    MongoClient mongoClient;
    MongoCollection<Document> booksCollection;
    Document bookDoc;

    public BookDAL(MongoClient mongoClient, Document bookDocFields) {
        this.mongoClient = mongoClient;
        this.bookDoc = bookDocFields;

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
        Document newBook = new Document();
        ObjectId id = new ObjectId();
        newBook.append("_id", id)
                .append("bookId", bookId)
                .append("bookDetails", bookDoc);
        try {
            booksCollection.insertOne(newBook);
//            TimeUnit.MILLISECONDS.sleep(100);
        } catch (Exception e) {
//            Thread.currentThread().interrupt();
            log.error("Error inserting book :: {}", newBook);
        }
        return newBook;
    }
}
