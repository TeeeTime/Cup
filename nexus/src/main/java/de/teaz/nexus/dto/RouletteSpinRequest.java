package de.teaz.nexus.dto;

public record RouletteSpinRequest(
    String betType,
    int betAmount
) {}
