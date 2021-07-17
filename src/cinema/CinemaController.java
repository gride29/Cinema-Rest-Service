package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
public class CinemaController {

    private final Cinema cinema = new Cinema(9, 9);
    private final List<Ticket> ticketList = new ArrayList<>();

    @GetMapping("/seats")
    public Map<String, ?> getSeats() {
        return Map.of(
                "total_rows", cinema.getTotalRows(),
                "total_columns", cinema.getTotalColumns(),
                "available_seats", cinema.getAvailableSeats()
        );
    }

    @PostMapping("/purchase")
    public Map<String, ?> purchaseTicket(@RequestBody Seat seat) {
        int row = seat.getRow();
        int column = seat.getColumn();

        List<Seat> availableSeats = cinema.getAvailableSeats();

        if (row < 1 || row > 9 || column < 1 || column > 9) {
            throw new InvalidSeatException("The number of a row or a column is out of bounds!");
        }

        Seat selectedSeat = new Seat(row, column);

        if (!availableSeats.contains(selectedSeat)) {
            throw new InvalidSeatException("The ticket has been already purchased!");
        } else {
            availableSeats.remove(selectedSeat);
            ticketList.add(new Ticket(selectedSeat));
            return Map.of(
                    "row", row,
                    "column", column,
                    "price", selectedSeat.getPrice()
            );
        }
    }
}
