package Logic;

public class HaversinFormule implements IAfstandBerekeningFormule {
    // afstand wordt berekend op basis van de Haversine formule
    // https://en.wikipedia.org/wiki/Haversine_formula
    // er wordt van uitgegaan dat de aarde een perfect cirkel is, geen ellips

    //code: https://github.com/jasonwinn/haversine/blob/master/Haversine.java

    // static constanten
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    // instantie variabelen
    private double startBreedtegraad;
    private double startLengtegraad;
    private double eindBreedtegraad;
    private double eindLentegraad;

    // getters & setters
    public void setPunten(double startBreedtegraad, double startLengtegraad,
                          double eindBreedtegraad, double eindLentegraad) {
        this.startBreedtegraad = startBreedtegraad;
        this.startLengtegraad = startLengtegraad;
        this.eindBreedtegraad = eindBreedtegraad;
        this.eindLentegraad = eindLentegraad;
    }

    public double getAfstand() {

        double dLat  = Math.toRadians((eindBreedtegraad - startBreedtegraad));
        double dLong = Math.toRadians((eindLentegraad - startLengtegraad));

        startBreedtegraad = Math.toRadians(startBreedtegraad);
        eindBreedtegraad   = Math.toRadians(eindBreedtegraad);

        double a = haversin(dLat) + Math.cos(startBreedtegraad) * Math.cos(eindBreedtegraad) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    // private hulpmethoden
    private double haversin(double waarde) {
        return Math.pow(Math.sin(waarde / 2), 2);
    }
}
