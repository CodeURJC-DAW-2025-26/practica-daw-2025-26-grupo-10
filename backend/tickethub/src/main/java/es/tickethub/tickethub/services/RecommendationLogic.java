package es.tickethub.tickethub.services;

public class RecommendationLogic {
/* COMMON LOGIC BETWEEN THE RECOMMENDATION STUFF */
    public static int categoryToNumber(String category) {
        return switch(category.toLowerCase()) {
            case "concierto" -> 0;
            case "festival" -> 1;
            case "teatro" -> 2;
            case "deporte" -> 3;
            default -> 0;
        };
    }
    //Normalization of the vector
    public static double normalize(double value, double min, double max) {
        if (max == min) {return 0.0;}
        return (value - min) / (max - min);
    }

    
}
