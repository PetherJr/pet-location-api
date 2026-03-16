package com.petherson.petlocation.domain.model;

/**
 * Value Object representing a detailed address.
 */
public record Address(
    String country,
    String state,
    String city,
    String neighborhood,
    String streetAddress
) {
    public static Address ofLabel(String label) {
        // Fallback for mock or simple labels
        return new Address("Brasil", "Desconhecido", "Desconhecido", "Desconhecido", label);
    }
}
