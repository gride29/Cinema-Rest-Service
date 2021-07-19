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
            String token = UUID.randomUUID().toString();
            ticketList.add(new Ticket(selectedSeat, token));
            return Map.of(
                    "token", token,
                    "ticket", selectedSeat
            );
        }
    }

    @PostMapping("/return")
    public Map<String, ?> refundTicket(@RequestBody Map<String, String> token) {
        String tokenString = token.get("token");
        Ticket returnedTicket = null;

        for (Ticket ticket: ticketList) {
            if (ticket.getToken().equals(tokenString)) {
                returnedTicket = ticket;
            }
        }

        if (returnedTicket == null) {
            throw new InvalidTokenException("Wrong token!");
        } else {
            ticketList.remove(returnedTicket);
            Seat seat = returnedTicket.getSeat();
            cinema.getAvailableSeats().add(seat);
            return Map.of(
                    "returned_ticket", seat
            );
        }
    }

    @PostMapping("/stats")
    public Map<String, ?> showStats(@RequestParam(required = false) String password) {
        if (!"super_secret".equals(password)) {
            throw new InvalidPasswordException("The password is wrong!");
        }

        int income = 0;
        for (Ticket ticket : ticketList) {
            income += ticket.getSeat().getPrice();
        }

        int availableSeats = cinema.getAvailableSeats().size();
        int purchasedTickets = ticketList.size();

        return Map.of(
                "current_income", income,
                "number_of_available_seats", availableSeats,
                "number_of_purchased_tickets", purchasedTickets
        );
    }
}
