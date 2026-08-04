package de.teaz.nexus.dto;

public record DailyRewardStatus(
    boolean ready,
    String cooldownTimeLeft,
    int streak,
    int streakGoal
) {}
