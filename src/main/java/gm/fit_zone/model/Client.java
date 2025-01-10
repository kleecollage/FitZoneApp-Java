package gm.fit_zone.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data // Getters and Setters methods generated automatically
@AllArgsConstructor // Full Object Constructor
@NoArgsConstructor // Empty Constructor
// @ToString // Method added automatically
@EqualsAndHashCode // Method added automatically
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String lastname;
    private Integer membership;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getMembership() {
        return this.membership;
    }

    public void setMembership(Integer membership) {
        this.membership = membership;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", membership=" + membership +
                '}';
    }
}
