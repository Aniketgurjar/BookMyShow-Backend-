package Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Booking extends BaseModel {
    @ManyToOne
    private User bookedBy;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;

    @ManyToMany
    private List<ShowSeat> showSeats;

    private Date bookedAt;

    private int amount;

    @OneToMany
    private List<Payment> payments;
}