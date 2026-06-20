package pokemon;

public class Bulbasaur extends Grass {
    private int specialMoveDamage;

    public Bulbasaur() {
        super("Bulbasaur", 45);
        this.specialMoveDamage = 20;
    }

    public int getSpecialMoveDamage() {
        return specialMoveDamage;
    }
}
