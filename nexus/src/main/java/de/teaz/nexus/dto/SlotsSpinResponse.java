package de.teaz.nexus.dto;

public record SlotsSpinResponse(
    int[] symbolIndexes,
    boolean won,
    int payout,
    int newBalance
) {}
