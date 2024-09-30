package beans;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ExoJDBC {

    public static void main(String[] args) {
     String url = "jdbc:mysql://localhost/dbb";  
        String username = "root";
        String password = "";
        Connection connection = null;
        Statement statement = null;

        try {
            // Charger le driver JDBC
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Driver JDBC chargé avec succès.");
            } catch (ClassNotFoundException e) {
                System.out.println("Le driver est introuvable. Vérifiez si le driver est correctement configuré.");
                return; // Exit if the driver is not found
            }

             // Créer la table et insérer les données
            try {
                connection  = DriverManager.getConnection(url, username, password);
                System.out.println("Connexion réussie à la base de données !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
                return; // Exit if the connection fails
            }

            // Creer le statement
            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la création de l'instruction : " + e.getMessage());
                return; // Exit if statement creation fails
            }

            // Méthode pour créer la table DevData
            try ( // Scanner for user input
                    Scanner scanner = new Scanner(System.in)) {
                // Création de la table DevData
                String createTableSQL = "CREATE TABLE IF NOT EXISTS DevData (" +
                        "Developpeurs VARCHAR(32), " +
                        "Jour CHAR(11), " +
                        "NbScripts INTEGER" +
                        ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("Table 'DevData' créée ou déjà existante.");
                
                // Vider la table avant d'insérer
                statement.executeUpdate("DELETE FROM DevData");
                
                // Insertion des données
                String[] inserts = {
                    "INSERT INTO DevData VALUES ('ALAMI', 'Lundi', 1)",
                    "INSERT INTO DevData VALUES ('WAFI', 'Lundi', 2)",
                    "INSERT INTO DevData VALUES ('SLAMI', 'Mardi', 9)",
                    "INSERT INTO DevData VALUES ('SAFI', 'Mardi', 2)",
                    "INSERT INTO DevData VALUES ('ALAMI', 'Mardi', 2)",
                    "INSERT INTO DevData VALUES ('SEBIHI', 'Mercredi', 2)",
                    "INSERT INTO DevData VALUES ('WAFI', 'Jeudi', 3)",
                    "INSERT INTO DevData VALUES ('ALAOUI', 'Vendredi', 9)",
                    "INSERT INTO DevData VALUES ('WAFI', 'Vendredi', 3)",
                    "INSERT INTO DevData VALUES ('SEBIHI', 'Vendredi', 4)"
                };
                
                for (String insert : inserts) {
                    statement.executeUpdate(insert);
                }
                System.out.println("Données insérées avec succès dans la table 'DevData'.");
                
                //  Afficher la personne ayant réalisé le nombre maximum de scripts en une journée
                String maxScriptsQuery = "SELECT d.Developpeurs, d.Jour, d.NbScripts " +
                        "FROM DevData d " +
                        "INNER JOIN (" +
                        "    SELECT Jour, MAX(NbScripts) AS MaxScripts " +
                        "    FROM DevData " +
                        "    GROUP BY Jour" +
                        ") m ON d.Jour = m.Jour AND d.NbScripts = m.MaxScripts";
                
                ResultSet maxScriptsResultSet = statement.executeQuery(maxScriptsQuery);
                
                System.out.println("\nPersonne ayant réalisé le nombre maximum de scripts en une journée :");
                System.out.println("---------------------------------------------------------------");
                System.out.printf("%-15s %-10s %-10s%n", "Developpeurs", "Jour", "NbScripts");
                System.out.println("---------------------------------------------------------------");
                
                while (maxScriptsResultSet.next()) {
                    String developpeur = maxScriptsResultSet.getString("Developpeurs");
                    String jour = maxScriptsResultSet.getString("Jour");
                    int nbScripts = maxScriptsResultSet.getInt("NbScripts");
                    
                    System.out.printf("%-15s %-10s %-10d%n", developpeur, jour, nbScripts);
                }
                
                // Afficher la liste des développeurs triée par le nombre total de scripts
                String totalScriptsQuery = "SELECT Developpeurs, SUM(NbScripts) AS TotalScripts " +
                        "FROM DevData " +
                        "GROUP BY Developpeurs " +
                        "ORDER BY TotalScripts DESC";
                
                ResultSet totalScriptsResultSet = statement.executeQuery(totalScriptsQuery);
                
                System.out.println("\nListe des développeurs triée par le nombre total de scripts :");
                System.out.println("---------------------------------------------------------------");
                System.out.printf("%-15s %-10s%n", "Developpeurs", "Total Scripts");
                System.out.println("---------------------------------------------------------------");
                
                while (totalScriptsResultSet.next()) {
                    String developpeur = totalScriptsResultSet.getString("Developpeurs");
                    int totalScripts = totalScriptsResultSet.getInt("TotalScripts");
                    
                    System.out.printf("%-15s %-10d%n", developpeur, totalScripts);
                }
                
                //  Calculer et afficher le nombre total de scripts réalisés en une semaine
                String totalWeeklyScriptsQuery = "SELECT SUM(NbScripts) AS TotalScriptsSemaine FROM DevData";
                
                ResultSet weeklyScriptsResultSet = statement.executeQuery(totalWeeklyScriptsQuery);
                
                if (weeklyScriptsResultSet.next()) {
                    int totalWeeklyScripts = weeklyScriptsResultSet.getInt("TotalScriptsSemaine");
                    System.out.println("\nNombre total de scripts réalisés en une semaine : " + totalWeeklyScripts);
                }
                
                //  Calculer le nombre total de scripts réalisés par un programmeur donné
                System.out.print("\nEntrez le nom du programmeur pour voir le nombre total de scripts : ");
                String developpeurInput = scanner.nextLine(); // Lire le nom du programmeur
                
                String totalScriptsByDeveloperQuery = "SELECT SUM(NbScripts) AS TotalScripts " +
                        "FROM DevData " +
                        "WHERE Developpeurs = '" + developpeurInput + "'";
                
                ResultSet developerScriptsResultSet = statement.executeQuery(totalScriptsByDeveloperQuery);
                
                if (developerScriptsResultSet.next()) {
                    int totalScriptsByDeveloper = developerScriptsResultSet.getInt("TotalScripts");
                    System.out.println("Total de scripts réalisés par " + developpeurInput + " : " + totalScriptsByDeveloper);
                } else {
                    System.out.println("Aucun script trouvé pour le programmeur : " + developpeurInput);
                }
                
                // Demander à l'utilisateur d'entrer une requête SQL
                System.out.print("\nEntrez une requête SQL : ");
                String userQuery = scanner.nextLine(); // Lire la requête SQL
                
                // Exécuter la requête
                if (userQuery.trim().toUpperCase().startsWith("SELECT")) {
                    try (ResultSet resultSet = statement.executeQuery(userQuery)) {
                        // Récupérer les métadonnées
                        ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        
                        // Afficher le nombre de colonnes
                        System.out.println("Nombre de colonnes : " + columnCount);
                        
                        // Afficher le nom et le type de chaque colonne
                        System.out.println("\nInformations sur les colonnes :");
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.printf("Colonne %d: %s (%s)%n", i, metaData.getColumnName(i), metaData.getColumnTypeName(i));
                        }

                        // Afficher le contenu de la table ligne par ligne
                        System.out.println("\nContenu de la table :");
                        while (resultSet.next()) {
                            StringBuilder row = new StringBuilder();
                            for (int i = 1; i <= columnCount; i++) {
                                row.append(resultSet.getString(i)).append(" | ");
                            }
                            System.out.println(row.toString());
                        }
                    }
                } else {
                    // Pour les requêtes autres que SELECT
                    int rowsAffected = statement.executeUpdate(userQuery);
                    System.out.println("Nombre de lignes modifiées : " + rowsAffected);
                }
                
                // Fermer les ressources après utilisation
                statement.close();
                connection.close();
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } finally {
             // S'assurer que les ressources sont fermées même en cas d'exception

        try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
    }
}