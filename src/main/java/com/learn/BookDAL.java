package com.learn;

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

    public List<Document> getBooks() {
        List<Document> books = new ArrayList<>();
        try {
            Document book = booksCollection
                    .find()
                    .first();
            books.add(book);
        } catch (Exception e) {
            log.error("Error getting books!");
        }
        return books;
    }

    public Document addBook(String bookId, String bookName, String authorName) {
        ObjectId id = new ObjectId();
        Document bookDoc = new Document("_id", id)
                .append("bookId", bookId)
                .append("bookName", bookName)
                .append("authorName", authorName);

        try {
            booksCollection.insertOne(bookDoc);
        } catch (Exception e) {
            log.error("Error inserting book :: {}", bookDoc);
        }

        return bookDoc;
    }
}
