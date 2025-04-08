public class Element {
    String ime, simbol;
    int atomskiBroj, atomskaMasa, brojProtona, brojIzotopa, vrstaId;

    public Element(String ime, String simbol, int atomskiBroj, int atomskaMasa, int brojProtona, int brojIzotopa, int vrstaId) {
        this.ime = ime;
        this.simbol = simbol;
        this.atomskiBroj = atomskiBroj;
        this.atomskaMasa = atomskaMasa;
        this.brojProtona = brojProtona;
        this.brojIzotopa = brojIzotopa;
        this.vrstaId = vrstaId;
    }
}
