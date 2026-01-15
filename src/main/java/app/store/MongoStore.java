
package app.store;

import com.mongodb.client.*;
import org.bson.Document;
import app.model.Student;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class MongoStore {
    static MongoClient client;
    static MongoCollection<Document> collection;
    static Gson gson = new Gson();

    public static void init() {
        try {
            //ekleme docker baglantısı için
            String host = System.getenv().getOrDefault("MONGO_HOST", "localhost");
            String port = System.getenv().getOrDefault("MONGO_PORT", "27017");
            String uri = String.format("mongodb://%s:%s", host, port);
            System.out.println("Connecting to MongoDB at " + uri);
            client = MongoClients.create(uri);
            collection = client.getDatabase("nosqllab").getCollection("ogrenciler");
            System.out.println("MongoDB connection successful");
            collection.drop();
            System.out.println("Inserting 10000 records to MongoDB...");
            //ekleme veriler listeye eklendi
            List<Document> batch = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                String id = "2025" + String.format("%06d", i);
                Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
                batch.add(Document.parse(gson.toJson(s)));
                //1000lik gruplar halinde ekleme
                if (batch.size() == 1000 || i == 9999) {
                    collection.insertMany(batch);
                    System.out.println("MongoDB: Inserted " + (i + 1) + " records");
                    batch.clear();
                }
            }
            System.out.println("MongoDB: All 10000 records inserted");
        } catch (Exception e) {
            System.err.println("MongoDB initialization error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Student get(String id) {
        try {
            Document doc = collection.find(new Document("ogrenciNo", id)).first();
            if (doc == null) {
                System.out.println("MongoDB: No data found for id: " + id);
                return null;
            }
            return gson.fromJson(doc.toJson(), Student.class);
        } catch (Exception e) {
            System.err.println("MongoDB get error for id " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
