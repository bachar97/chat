package WebTech.TP.TimeSeriesManager.model;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EnableAutoConfiguration
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String timeSeriesId;
    private String tag;
}
