
package app.store;

import redis.clients.jedis.Jedis;
import app.model.Student;
import com.google.gson.Gson;

public class RedisStore {
    static Jedis jedis;
    static Gson gson = new Gson();

    public static void init() {
        try {
            //ekleme docker baglantısı için
            String host = System.getenv().getOrDefault("REDIS_HOST", "localhost");
            int port = Integer.parseInt(System.getenv().getOrDefault("REDIS_PORT", "6379"));
            System.out.println("Connecting to Redis at " + host + ":" + port);
            jedis = new Jedis(host, port);
            jedis.ping();
            System.out.println("Redis connection successful");
            System.out.println("Inserting 10000 records to Redis...");
            for (int i = 0; i < 10000; i++) {
                String id = "2025" + String.format("%06d", i);
                Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
                jedis.set(id, gson.toJson(s));
                if (i % 1000 == 0) {
                    System.out.println("Redis: Inserted " + i + " records");
                }
            }
            System.out.println("Redis: All 10000 records inserted");
        } catch (Exception e) {
            System.err.println("Redis initialization error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Student get(String id) {
        try {
            String json = jedis.get(id);
            if (json == null) {
                System.out.println("Redis: No data found for id: " + id);
                return null;
            }
            return gson.fromJson(json, Student.class);
        } catch (Exception e) {
            System.err.println("Redis get error for id " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
