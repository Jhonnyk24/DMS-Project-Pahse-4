import java.sql.*;
import java.util.ArrayList;

public class MovieDatabaseManager {

    private Connection conn;

    // NEW Constructor takes the connection directly
    public MovieDatabaseManager(Connection connection) {
        this.conn = connection;
    }

    // ========== CRUD OPERATIONS ==========

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("year"),
                        rs.getString("director"),
                        rs.getDouble("rating"),
                        rs.getInt("runtimeMinutes"),
                        rs.getInt("votes"),
                        rs.getBoolean("watched")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public Movie getMovieById(int id) {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("year"),
                        rs.getString("director"),
                        rs.getDouble("rating"),
                        rs.getInt("runtimeMinutes"),
                        rs.getInt("votes"),
                        rs.getBoolean("watched")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addMovie(Movie m) {
        String sql = "INSERT INTO movies (title, year, director, rating, runtimeMinutes, votes, watched) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getTitle());
            stmt.setInt(2, m.getYear());
            stmt.setString(3, m.getDirector());
            stmt.setDouble(4, m.getRating());
            stmt.setInt(5, m.getRuntimeMinutes());
            stmt.setInt(6, m.getVotes());
            stmt.setBoolean(7, m.isWatched());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMovie(Movie m) {
        String sql = "UPDATE movies SET title = ?, year = ?, director = ?, rating = ?, runtimeMinutes = ?, votes = ?, watched = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getTitle());
            stmt.setInt(2, m.getYear());
            stmt.setString(3, m.getDirector());
            stmt.setDouble(4, m.getRating());
            stmt.setInt(5, m.getRuntimeMinutes());
            stmt.setInt(6, m.getVotes());
            stmt.setBoolean(7, m.isWatched());
            stmt.setInt(8, m.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(int id) {
        String sql = "DELETE FROM movies WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
