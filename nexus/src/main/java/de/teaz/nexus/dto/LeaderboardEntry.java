package de.teaz.nexus.dto;

public record LeaderboardEntry(
        int rank,
        String name,
        int balance,
        String avatarUrl
) {}
