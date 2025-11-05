import java.time.Year;

/**
 * Represents a single movie and contains validation and utility methods.
 */
public class Movie {

    // ===== New field for database primary key =====
    private int id; // Will match the `id` column in MySQL

    private String title;
    private int year;
    private String director;
    private double rating;         // 0.0 - 10.0
    private int runtimeMinutes;    // > 0
    private int votes;             // >= 0
    private boolean watched;

    // ===== Constructors =====

    /**
     * Constructor used when loading from the database (with ID).
     */
    public Movie(int id, String title, int year, String director, double rating, int runtimeMinutes, int votes, boolean watched) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.runtimeMinutes = runtimeMinutes;
        this.votes = votes;
        this.watched = watched;
    }

    /**
     * Constructor used when creating a NEW movie (no ID yet — DB auto-generates).
     */
    public Movie(String title, int year, String director, double rating, int runtimeMinutes, int votes, boolean watched) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.runtimeMinutes = runtimeMinutes;
        this.votes = votes;
        this.watched = watched;
    }

    public Movie() {}

    // ===== Getters =====
    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getDirector() { return director; }
    public double getRating() { return rating; }
    public int getRuntimeMinutes() { return runtimeMinutes; }
    public int getVotes() { return votes; }
    public boolean isWatched() { return watched; }

    // ===== Setters (needed for editing) =====
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setYear(int year) { this.year = year; }
    public void setDirector(String director) { this.director = director; }
    public void setRating(double rating) { this.rating = rating; }
    public void setRuntimeMinutes(int runtimeMinutes) { this.runtimeMinutes = runtimeMinutes; }
    public void setVotes(int votes) { this.votes = votes; }
    public void setWatched(boolean watched) { this.watched = watched; }

    // ===== Scariness Calculation =====
    public double getScariness() {
        double score = rating;
        score += Math.min(votes / 500000.0, 2);
        if (runtimeMinutes > 120) score += 1;
        if (watched) score -= 1;
        return Math.max(0, Math.min(10, score));
    }

    // ===== Display Formatting =====
    public String prettyPrint() {
        return String.format(
                "%s (%d)\nDirector: %s\nRating: %.1f\nRuntime: %d min\nVotes: %d\nWatched: %s",
                title, year, director, rating, runtimeMinutes, votes, watched ? "Yes" : "No"
        );
    }

    // ===== CSV Conversion (used only if needed for backup or export) =====
    @Override
    public String toString() {
        return String.format("%s,%d,%s,%.1f,%d,%d,%s",
                title, year, director, rating, runtimeMinutes, votes, watched);
    }

    /**
     * Parses movie from CSV — optional fallback/import.
     */
    public static Movie fromCSV(String line) {
        if (line == null) throw new IllegalArgumentException("Line is null");

        String[] parts = line.split(",", -1);
        if (parts.length != 7) throw new IllegalArgumentException("Expected 7 fields but found " + parts.length);

        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

        String title = parts[0];
        if (title.isEmpty()) throw new IllegalArgumentException("Title is empty");

        int year = Integer.parseInt(parts[1]);
        int currentYear = Year.now().getValue();
        if (year < 1888 || year > currentYear) throw new IllegalArgumentException("Invalid year");

        String director = parts[2];
        double rating = Double.parseDouble(parts[3]);
        int runtime = Integer.parseInt(parts[4]);
        int votes = Integer.parseInt(parts[5]);
        boolean watched = parts[6].equalsIgnoreCase("true") || parts[6].equalsIgnoreCase("yes");

        return new Movie(title, year, director, rating, runtime, votes, watched);
    }
}
