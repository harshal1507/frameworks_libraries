package com.practice.java;

import java.util.Optional;

public class OptionalFeatures {
    public static void main(String[] args) {

        Guitar guitar = new Guitar();
        Optional<Guitar> guitarOptional = Optional.of(guitar);

    }
}
