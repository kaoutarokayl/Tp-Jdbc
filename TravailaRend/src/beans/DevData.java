package beans;

public class DevData {
    private String developpeurs;
    private String jour;
    private int nbScripts;

    public DevData (String developpeurs, String jour, int nbScripts) {
        this.developpeurs = developpeurs;
        this.jour = jour;
        this.nbScripts = nbScripts;
    }

    // Getters et Setters
    public String getDeveloppeurs() {
        return developpeurs;
    }
    public void setDeveloppeurs(String developpeurs) {
        this.developpeurs = developpeurs;
    }

    public String getJour() {

        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public int getNbScripts() {
        return nbScripts;
    }

    public void setNbScripts(int nbScripts) {
        this.nbScripts = nbScripts;
    }
}