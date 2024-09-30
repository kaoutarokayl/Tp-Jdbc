package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {

    // Méthode pour sauvegarder un site
    public static void save(Site s) {
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://localhost/db";
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // Étape 1 : Chargement du driver
            Class.forName("com.mysql.jdbc.Driver");
            // Étape 2 : Récupération de la connexion
            cn = DriverManager.getConnection(url, user, password);
            // Étape 3 : Création d'un statement
            st = cn.createStatement();

            // Vérifier si le site existe déjà
            String checkQuery = "SELECT COUNT(*) FROM site WHERE nom = '" + s.getNom() + "'";
            rs = st.executeQuery(checkQuery);
            rs.next();  // Aller à la première ligne du résultat

            // Si le site n'existe pas encore, l'insérer
            if (rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO site VALUES(null, '" + s.getNom() + "')";
                st.executeUpdate(insertQuery);
                System.out.println("Le site " + s.getNom() + " a été ajouté.");
            } else {
                System.out.println("Le site " + s.getNom() + " existe déjà.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Impossible de charger le driver : " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Impossible de libérer les ressources : " + ex.getMessage());
            }
        }
    }

    // Méthode pour charger et afficher tous les sites
    public static void load() {
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://localhost/db";
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // Étape 1 : Chargement du driver
            Class.forName("com.mysql.jdbc.Driver");
            // Étape 2 : Récupération de la connexion
            cn = DriverManager.getConnection(url, user, password);
            // Étape 3 : Création d'un statement
            st = cn.createStatement();
            String req = "SELECT * FROM site";
            // Étape 4 : Exécution de la requête
            rs = st.executeQuery(req);
            // Étape 5 : Parcours de ResultSet
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Impossible de charger le driver : " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Impossible de libérer les ressources : " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Insertion des données
        save(new Site("SAFI"));
        save(new Site("MARRAKECH"));
        save(new Site("EL JADIDA"));
        // Tentative de réinsertion de données existantes (vérification de redondance)
        save(new Site("SAFI"));
        save(new Site("MARRAKECH"));
        
        // Récupération et affichage des données
        load();
    }
}