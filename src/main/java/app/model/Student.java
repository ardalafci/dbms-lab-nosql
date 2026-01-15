
package app.model;
// ekleme serileştirme
import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public String ogrenciNo;
    public String adSoyad;
    public String bolum;

    public Student() {
    }

    public Student(String ogrenciNo, String adSoyad, String bolum) {
        this.ogrenciNo = ogrenciNo;
        this.adSoyad = adSoyad;
        this.bolum = bolum;
    }
}
