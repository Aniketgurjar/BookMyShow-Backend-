package Models;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Region extends BaseModel { // CITY
    private String name;
//    private List<Theatre> theatres;
}