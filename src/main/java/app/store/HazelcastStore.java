
package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import app.model.Student;

public class HazelcastStore {
    static HazelcastInstance hz;
    static IMap<String, Student> map;

    public static void init() {
        try {
            //ekleme docker baglantısı için
            String host = System.getenv().getOrDefault("HAZELCAST_HOST", "localhost");
            String port = System.getenv().getOrDefault("HAZELCAST_PORT", "5701");
            System.out.println("Connecting to Hazelcast at " + host + ":" + port);
            ClientConfig config = new ClientConfig();
            config.setClusterName("nosql-cluster");
            config.getNetworkConfig().addAddress(host + ":" + port);
            // ekleme timeout istemci server baslamadan baglanmasın diye
            config.getConnectionStrategyConfig().getConnectionRetryConfig().setClusterConnectTimeoutMillis(10000);
            hz = HazelcastClient.newHazelcastClient(config);
            System.out.println("Hazelcast connection successful");
            map = hz.getMap("ogrenciler");
            System.out.println("Inserting 10000 records to Hazelcast...");
            for (int i = 0; i < 10000; i++) {
                String id = "2025" + String.format("%06d", i);
                Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
                map.put(id, s);
                if (i % 1000 == 0) {
                    System.out.println("Hazelcast: Inserted " + i + " records");
                }
            }
            System.out.println("Hazelcast: All 10000 records inserted");
        } catch (Exception e) {
            System.err.println("Hazelcast initialization error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Student get(String id) {
        try {
            Student student = map.get(id);
            if (student == null) {
                System.out.println("Hazelcast: No data found for id: " + id);
            }
            return student;
        } catch (Exception e) {
            System.err.println("Hazelcast get error for id " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
