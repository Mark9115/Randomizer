package mark.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "student_id")
    private int studentId;
    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;
    @Column(name = "rating")
    private int record;

    public Rating() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public Rating(int id, int studentId, LocalDate date, int record) {
        this.id = id;
        this.studentId = studentId;
        this.date = date;
        this.record = record;
    }
}
