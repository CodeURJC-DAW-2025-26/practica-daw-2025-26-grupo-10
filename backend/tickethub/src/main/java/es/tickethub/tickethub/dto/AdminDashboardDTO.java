package es.tickethub.tickethub.dto;

public record AdminDashboardDTO(
    int activeEvents,
    long numberTickets,
    long numberUsers,
    long numberAdmins
) {}