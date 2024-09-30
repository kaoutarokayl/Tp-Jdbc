
package beans;

public class Site {
    private int id;
    private String nom;

    // Constructeurs
    public Site() {
    }

    public Site(String nom) {
        this.nom = nom;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}