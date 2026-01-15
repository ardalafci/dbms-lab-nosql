
package app;

import static spark.Spark.*;
import com.google.gson.Gson;
import app.store.*;

public class Main {
    public static void main(String[] args) {
        port(8080);
        Gson gson = new Gson();

        System.out.println("Initializing stores...");
        RedisStore.init();
        System.out.println("Redis initialized");
        HazelcastStore.init();
        System.out.println("Hazelcast initialized");
        MongoStore.init();
        System.out.println("MongoDB initialized");
        System.out.println("Server is ready on port 8080");

        // ekleme key=value seklinde parametre alması için :param kullanıldı
        get("/nosql-lab-rd/:param", (req, res) -> {
            try {
                res.type("application/json");
                String param = req.params(":param");
                System.out.println("Redis request: " + param);
                String[] parts = param.split("=");
                if (parts.length != 2) {
                    res.status(400);
                    return "{\"error\": \"Invalid parameter format\"}";
                }
                String id = parts[1];
                return gson.toJson(RedisStore.get(id));
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"error\": \"" + e.getMessage() + "\"}";
            }
        });

        get("/nosql-lab-hz/:param", (req, res) -> {
            try {
                res.type("application/json");
                String param = req.params(":param");
                System.out.println("Hazelcast request: " + param);
                String[] parts = param.split("=");
                if (parts.length != 2) {
                    res.status(400);
                    return "{\"error\": \"Invalid parameter format\"}";
                }
                String id = parts[1];
                return gson.toJson(HazelcastStore.get(id));
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"error\": \"" + e.getMessage() + "\"}";
            }
        });

        get("/nosql-lab-mon/:param", (req, res) -> {
            try {
                res.type("application/json");
                String param = req.params(":param");
                System.out.println("MongoDB request: " + param);
                String[] parts = param.split("=");
                if (parts.length != 2) {
                    res.status(400);
                    return "{\"error\": \"Invalid parameter format\"}";
                }
                String id = parts[1];
                return gson.toJson(MongoStore.get(id));
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"error\": \"" + e.getMessage() + "\"}";
            }
        });
    }
}
