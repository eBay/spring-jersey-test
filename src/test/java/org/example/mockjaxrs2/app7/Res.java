package org.example.mockjaxrs2.app7;

import java.util.Objects;

public class Res {

    private String output;

    public Res() {
    }

    public Res(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Res)) {
            return false;
        }

        Res res = (Res) o;

        return Objects.equals(output, res.output);
    }

    @Override
    public int hashCode() {
        return output != null ? output.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Res{" +
            "output='" + output + '\'' +
            '}';
    }
}
