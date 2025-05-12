package Dtos;


import Models.Booking;
import Models.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookMovieResponseDto {
    private Booking booking;
    private BookingStatus bookingStatus;
}