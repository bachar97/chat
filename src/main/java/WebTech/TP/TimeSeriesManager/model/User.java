package WebTech.TP.TimeSeriesManager.model;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EnableAutoConfiguration
public class User {
    @Id
    private String username;
    private String password;
}
