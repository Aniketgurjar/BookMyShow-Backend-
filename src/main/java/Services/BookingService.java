package Services;
import Exception.InvalidShowException;
import Exception.InvalidUserIdException;
import Exception.ShowSeatNotAvailableException;
import Models.*;
import Repositories.BookingRepository;
import Repositories.ShowRepository;
import Repositories.ShowSeatRepository;
import Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private UserRepository userRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;
    private PriceCalculationService priceCalculationService;
    private BookingRepository bookingRepository;

    public BookingService(UserRepository userRepository,
                          ShowRepository showRepository,
                          ShowSeatRepository showSeatRepository,
                          PriceCalculationService priceCalculationService,
                          BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.priceCalculationService = priceCalculationService;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking bookMovie(Long userId, Long showId, List<Long> showSeatIds) throws InvalidUserIdException, InvalidShowException, ShowSeatNotAvailableException {
        /*
        1. Get the user from the userId.
        2. Get the show from the showId.
        3. Get the list of show Seats from the list of showSeatIds.
        4. Check if all the seats are available or not.
        ------TAKE A LOCK-------
        5. If no, throw an exception.
        6. If yes, mark the seat status as BLOCKED.
        7. Save the status in the DB.
        -------RELEASE THE LOCK-------
        8. Create the booking object with PENDING status.
        9. Move to the Payment page.
         */

        //1. Get the user from the userId.
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new InvalidUserIdException("User with id: " + userId + " doesn't exist");
        }

        User user = userOptional.get();

        //2. Get the show from the showId.
        Optional<Show> showOptional = showRepository.findById(showId);

        if (showOptional.isEmpty()) {
            throw new InvalidShowException("Show with id: " + showId + " doesn't exist");
        }

        Show show = showOptional.get();

        //3. Get the list of show Seats from the list of showSeatIds.
        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatIds);

        //4. Check if all the seats are available or not.
        //5. If no, throw an exception.
        for (ShowSeat showSeat : showSeats) {
            if (!showSeat.getShowSeatStatus().equals(ShowSeatStatus.AVAILABLE)) {
                throw new ShowSeatNotAvailableException("ShowSeat with id: " + showSeat.getId() + " isn't available.");
            }
        }

        List<ShowSeat> savedShowSeats = new ArrayList<>();

        //6. If yes, mark the seat status as BLOCKED.
        for (ShowSeat showSeat : showSeats) {
            showSeat.setShowSeatStatus(ShowSeatStatus.BLOCKED);
            //7. Save the status in the DB.
            savedShowSeats.add(showSeatRepository.save(showSeat));
        }

        //8. Create the booking object with PENDING status.
        Booking booking = new Booking();
        booking.setBookedBy(user);
        booking.setBookedAt(new Date());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setShowSeats(savedShowSeats);
        booking.setAmount(priceCalculationService.calculatePrice(savedShowSeats, show));

        //Call the PaymentService to complete the payment for this booking.

        return bookingRepository.save(booking);
    }
}