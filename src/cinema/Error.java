package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty("error")
    private String error;

    public Error() {}

    public Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
