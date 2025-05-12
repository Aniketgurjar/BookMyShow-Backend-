package Services;

import Models.Show;
import Models.ShowSeat;
import Models.ShowSeatType;
import Repositories.ShowSeatTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceCalculationService {
    private ShowSeatTypeRepository showSeatTypeRepository;

    public PriceCalculationService(ShowSeatTypeRepository showSeatTypeRepository) {
        this.showSeatTypeRepository = showSeatTypeRepository;
    }


    public int calculatePrice(List<ShowSeat> showSeats, Show show) {
        //Get the ShowSeatType for show
        List<ShowSeatType> showSeatTypes = showSeatTypeRepository.findAllByShow(show);

        int amount = 0;
        for (ShowSeat showSeat : showSeats) {
            for (ShowSeatType showSeatType : showSeatTypes) {
                if (showSeat.getSeat().getSeatType().equals(showSeatType.getSeatType())) {
                    amount += showSeatType.getPrice();
                    break;
                }
            }
        }

        return amount;
    }
}