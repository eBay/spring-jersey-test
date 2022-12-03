package org.example.mockjaxrs2.app7;

import java.util.Objects;

public class Req {

    private String input;

    public Req() {
    }

    public Req(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Req)) {
            return false;
        }

        Req req = (Req) o;

        return Objects.equals(input, req.input);
    }

    @Override
    public int hashCode() {
        return input != null ? input.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Req{" +
            "input='" + input + '\'' +
            '}';
    }
}
