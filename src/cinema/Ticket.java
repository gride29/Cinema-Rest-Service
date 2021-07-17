package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket {

    @JsonProperty("seat")
    private Seat seat;

    public Ticket(Seat seat) {
        this.seat = seat;
    }

    public Ticket() {}

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
