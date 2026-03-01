package es.tickethub.tickethub.services;

public class RecommendationLogic {
    public static final int MAX_PRICE = 500;
    public static final int MAX_AGE = 120;
    public static final int MAX_EVENT_TYPE = 3;
    /* COMMON LOGIC BETWEEN THE RECOMMENDATION STUFF */
    public static int categoryToNumber(String category) {
        return switch (category.toLowerCase()) {
            case "concierto" -> 0;
            case "festival" -> 1;
            case "teatro" -> 2;
            case "deporte" -> 3;
            default -> 0;
        };
    }

    // Normalization of the vector
    public static double normalize(double value, double min, double max) {
        if (max == min) {
            return 0.0;
        }
        return (value - min) / (max - min);
    }

}
