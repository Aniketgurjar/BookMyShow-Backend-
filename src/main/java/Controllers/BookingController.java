package Controllers;

import Dtos.BookMovieRequestDto;
import Dtos.BookMovieResponseDto;
import Services.BookingService;
import org.springframework.stereotype.Controller;

@Controller
public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public BookMovieResponseDto bookMovie(BookMovieRequestDto requestDto) {
        return null;
    }
}