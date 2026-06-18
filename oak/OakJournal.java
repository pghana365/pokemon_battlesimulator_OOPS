package oak;

import java.util.Scanner;
import pokemon.*;
import trainer.Trainer;

public class OakJournal {
    private Trainer[] trainers = new Trainer[10];
    private int trainerCount = 0;

    public void addTrainer(Trainer trainer) {
        if (trainerCount < 10) {
            trainers[trainerCount++] = trainer;
        }
    }

    public void runShowdown(Trainer t1, Trainer t2) {
        if (t1.getBadgeCount() <= 0 || t2.getBadgeCount() <= 0) {
            System.out.println("One or both trainers are eliminated!");
            return;
        }

        System.out.println("Showdown: " + t1.getName() + " vs. " + t2.getName());

        int wins1 = 0;
        int wins2 = 0;

        for (int i = 0; i < 6; i++) {
            Pokemon p1 = t1.getPokemon(i);
            Pokemon p2 = t2.getPokemon(i);

            if (p1 == null || p2 == null) {
                System.out.println("Round " + (i + 1) + " (Empty slot vs. Empty slot): Skip");
                continue;
            }

            String result = determineBattleWinner(p1, p2);

            if ("draw".equals(result)) {
                System.out.println("Round " + (i + 1) + " (" + p1.getName() + " vs. " + p2.getName() + "): It's a draw!");
            } else if ("t1".equals(result)) {
                System.out.println("Round " + (i + 1) + " (" + p1.getName() + " vs. " + p2.getName() + "): Winner is " + t1.getName() + "!");
                wins1++;
            } else {
                System.out.println("Round " + (i + 1) + " (" + p1.getName() + " vs. " + p2.getName() + "): Winner is " + t2.getName() + "!");
                wins2++;
            }
        }

        if (wins1 > wins2) {
            System.out.println("Final Winner: " + t1.getName());
            t1.incrementBadgeCount();
            t2.decrementBadgeCount();
        } else if (wins2 > wins1) {
            System.out.println("Final Winner: " + t2.getName());
            t2.incrementBadgeCount();
            t1.decrementBadgeCount();
        } else {
            System.out.println("Final Winner: It's a draw!");
        }
    }

    private String determineBattleWinner(Pokemon p1, Pokemon p2) {
        String type1 = p1.getType();
        String type2 = p2.getType();

        // Type advantages: t1 wins if...
        if (("Fire".equals(type1) && "Grass".equals(type2)) ||
            ("Grass".equals(type1) && "Water".equals(type2)) ||
            ("Water".equals(type1) && "Fire".equals(type2))) {
            return "t1";
        }
        // t2 wins if...
        if (("Fire".equals(type2) && "Grass".equals(type1)) ||
            ("Grass".equals(type2) && "Water".equals(type1)) ||
            ("Water".equals(type2) && "Fire".equals(type1))) {
            return "t2";
        }

        // No type advantage: compare health
        if (p1.getHealth() > p2.getHealth()) return "t1";
        if (p2.getHealth() > p1.getHealth()) return "t2";

        return "draw";
    }

    public Trainer[] getTrainers() {
        return trainers;
    }

    public void setTrainers(Trainer[] trainers) {
        this.trainers = trainers;
    }

    public int getTrainerCount() {
        return trainerCount;
    }

    public void setTrainerCount(int trainerCount) {
        this.trainerCount = trainerCount;
    }

    public static void main(String[] args) {
        OakJournal oj = new OakJournal();

        // Hardcoded sample trainers for demonstration
        Trainer ash = new Trainer("Ash");
        ash.addPokemon(0, new Charmander());
        ash.addPokemon(1, new Squirtle());
        ash.addPokemon(2, new Bulbasaur());
        ash.addPokemon(3, new Vulpix());
        ash.addPokemon(4, new Psyduck());
        ash.addPokemon(5, new Chikorita());
        oj.addTrainer(ash);

        Trainer misty = new Trainer("Misty");
        misty.addPokemon(0, new Psyduck());
        misty.addPokemon(1, new Greninja());
        misty.addPokemon(2, new Chikorita());
        misty.addPokemon(3, new Squirtle());
        misty.addPokemon(4, new Bulbasaur());
        misty.addPokemon(5, new Pikachu());
        oj.addTrainer(misty);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Trainer");
            System.out.println("2. Add Pokemon to Trainer");
            System.out.println("3. Start Battle (Showdown)");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter trainer name: ");
                    String trainerName = scanner.nextLine();
                    Trainer newTrainer = new Trainer(trainerName);
                    oj.addTrainer(newTrainer);
                    System.out.println("Trainer added.");
                    break;

                case 2:
                    System.out.println("Available Trainers:");
                    for (int i = 0; i < oj.getTrainerCount(); i++) {
                        System.out.println(i + ": " + oj.getTrainers()[i].getName());
                    }
                    System.out.print("Select trainer index: ");
                    int trainerIdx = scanner.nextInt();
                    if (trainerIdx < 0 || trainerIdx >= oj.getTrainerCount()) {
                        System.out.println("Invalid trainer index.");
                        break;
                    }
                    Trainer selectedTrainer = oj.getTrainers()[trainerIdx];

                    // Loop to add multiple Pokémon easily
                    while (true) {
                        System.out.println("\nCurrent team for " + selectedTrainer.getName() + ":");
                        for (int j = 0; j < 6; j++) {
                            Pokemon p = selectedTrainer.getPokemon(j);
                            if (p != null) {
                                System.out.println("Slot " + j + ": " + p.getName() + " (" + p.getType() + ")");
                            } else {
                                System.out.println("Slot " + j + ": Empty");
                            }
                        }

                        System.out.print("Enter slot (0-5, or -1 to stop adding): ");
                        int slot = scanner.nextInt();
                        if (slot == -1) {
                            break;
                        }
                        if (slot < 0 || slot > 5) {
                            System.out.println("Invalid slot.");
                            continue;
                        }

                        // Check if slot is occupied
                        if (selectedTrainer.getPokemon(slot) != null) {
                            System.out.println("Slot " + slot + " is already occupied. Skip or choose another.");
                            continue;
                        }

                        System.out.println("Available Pokemon:");
                        System.out.println("1: Charmander (Fire)");
                        System.out.println("2: Vulpix (Fire)");
                        System.out.println("3: Squirtle (Water)");
                        System.out.println("4: Psyduck (Water)");
                        System.out.println("5: Greninja (Water)");
                        System.out.println("6: Bulbasaur (Grass)");
                        System.out.println("7: Chikorita (Grass)");
                        System.out.println("8: Pikachu (Electric)");
                        System.out.print("Select Pokemon: ");
                        int pokemonChoice = scanner.nextInt();

                        Pokemon selectedPokemon = null;
                        switch (pokemonChoice) {
                            case 1: selectedPokemon = new Charmander(); break;
                            case 2: selectedPokemon = new Vulpix(); break;
                            case 3: selectedPokemon = new Squirtle(); break;
                            case 4: selectedPokemon = new Psyduck(); break;
                            case 5: selectedPokemon = new Greninja(); break;
                            case 6: selectedPokemon = new Bulbasaur(); break;
                            case 7: selectedPokemon = new Chikorita(); break;
                            case 8: selectedPokemon = new Pikachu(); break;
                            default:
                                System.out.println("Invalid Pokemon choice.");
                                break;
                        }
                        if (selectedPokemon != null) {
                            selectedTrainer.addPokemon(slot, selectedPokemon);
                            System.out.println("Pokemon added to slot " + slot + ".");
                        }
                    }
                    System.out.println("Finished adding Pokémon to " + selectedTrainer.getName() + ".");
                    break;

                case 3:
                    System.out.println("Available Trainers:");
                    for (int i = 0; i < oj.getTrainerCount(); i++) {
                        System.out.println(i + ": " + oj.getTrainers()[i].getName() + " (Badges: " + oj.getTrainers()[i].getBadgeCount() + ")");
                    }
                    System.out.print("Select Trainer 1 index: ");
                    int t1Idx = scanner.nextInt();
                    System.out.print("Select Trainer 2 index: ");
                    int t2Idx = scanner.nextInt();

                    if (t1Idx < 0 || t1Idx >= oj.getTrainerCount() || t2Idx < 0 || t2Idx >= oj.getTrainerCount()) {
                        System.out.println("Invalid trainer indices.");
                        break;
                    }

                    Trainer battleT1 = oj.getTrainers()[t1Idx];
                    Trainer battleT2 = oj.getTrainers()[t2Idx];
                    oj.runShowdown(battleT1, battleT2);
                    break;

                case 4:
                    System.out.println("Exiting.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
