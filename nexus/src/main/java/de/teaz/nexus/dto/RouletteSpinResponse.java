package de.teaz.nexus.dto;

public record RouletteSpinResponse(
    int resultNumber,
    String resultColor,
    boolean won,
    int payout,
    int newBalance
) {}
