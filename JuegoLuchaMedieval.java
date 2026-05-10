import java.util.Random;
import java.util.Scanner;

abstract class Personaje {
    protected String nombre;
    protected int puntosDeVida;
    protected int minDano;
    protected int maxDano;

    public Personaje(String nombre, int puntosDeVida, int minDano, int maxDano) {
        this.nombre = nombre;
        this.puntosDeVida = puntosDeVida;
        this.minDano = minDano;
        this.maxDano = maxDano;
    }

    public void atacar(Personaje oponente) {
        Random rand = new Random();
        int dano = rand.nextInt((maxDano - minDano) + 1) + minDano;
        oponente.recibirDano(dano);
        System.out.println(nombre + " ataca a " + oponente.getNombre()
                + " causando " + dano + " puntos de daño.");
    }

    public void recibirDano(int dano) {
        this.puntosDeVida -= dano;
        if (this.puntosDeVida < 0)
            this.puntosDeVida = 0;
    }

    public boolean estaVivo() {
        return puntosDeVida > 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosDeVida() {
        return puntosDeVida;
    }

    public abstract String getTipo();
}

class Guerrero extends Personaje {
    private int armadura; // reduce el daño recibido

    public Guerrero(String nombre) {
        super(nombre, 120, 15, 35); // más HP y más daño
        this.armadura = 5;
    }

    @Override
    public void atacar(Personaje oponente) {
        Random rand = new Random();
        int dano = rand.nextInt((maxDano - minDano) + 1) + minDano;
        // Bonus ocasional: golpe con escudo
        if (rand.nextInt(3) == 0) {
            dano += 10;
            System.out.println("[¡Golpe de escudo!]");
        }
        oponente.recibirDano(dano);
        System.out.println(nombre + " (Guerrero) ataca a " + oponente.getNombre()
                + " causando " + dano + " puntos de daño.");
    }

    @Override
    public void recibirDano(int dano) {
        int danoReal = Math.max(0, dano - armadura);
        super.recibirDano(danoReal);
        System.out.println("  [La armadura absorbe " + armadura + " puntos]");
    }

    @Override
    public String getTipo() {
        return "Guerrero ⚔️  (HP: 120 | Daño: 15–35 | Armadura: 5)";
    }
}

class Mago extends Personaje {
    private int mana;

    public Mago(String nombre) {
        super(nombre, 80, 10, 40); // menos HP pero más daño máximo
        this.mana = 100;
    }

    @Override
    public void atacar(Personaje oponente) {
        Random rand = new Random();
        int dano = rand.nextInt((maxDano - minDano) + 1) + minDano;
        // Hechizo especial si hay suficiente mana
        if (mana >= 20 && rand.nextBoolean()) {
            dano += 15;
            mana -= 20;
            System.out.println("[¡Hechizo potenciado! Mana restante: " + mana + "]");
        }
        oponente.recibirDano(dano);
        System.out.println(nombre + " (Mago) lanza un hechizo sobre " + oponente.getNombre()
                + " causando " + dano + " puntos de daño.");
    }

    @Override
    public String getTipo() {
        return "Mago 🔮  (HP: 80 | Daño: 10–40 | Mana: 100)";
    }
}

class Arquero extends Personaje {
    private int flechas;

    public Arquero(String nombre) {
        super(nombre, 100, 12, 28); // HP y daño equilibrados
        this.flechas = 30;
    }

    @Override
    public void atacar(Personaje oponente) {
        if (flechas == 0) {
            System.out.println(nombre + " no tiene flechas. Ataca con la daga (daño: 5).");
            oponente.recibirDano(5);
            return;
        }
        Random rand = new Random();
        int dano = rand.nextInt((maxDano - minDano) + 1) + minDano;
        // Disparo crítico con 20% de probabilidad
        if (rand.nextInt(5) == 0) {
            dano *= 2;
            System.out.println("[¡Disparo crítico!]");
        }
        flechas--;
        oponente.recibirDano(dano);
        System.out.println(nombre + " (Arquero) dispara a " + oponente.getNombre()
                + " causando " + dano + " puntos de daño. Flechas: " + flechas);
    }

    @Override
    public String getTipo() {
        return "Arquero 🏹  (HP: 100 | Daño: 12–28 | Flechas: 30)";
    }
}

abstract class PersonajeFactory {
    // Factory Method: cada subclase decide qué tipo de Personaje crear
    public abstract Personaje crearPersonaje(String nombre);
}

class GuerreroFactory extends PersonajeFactory {
    @Override
    public Personaje crearPersonaje(String nombre) {
        return new Guerrero(nombre);
    }
}

class MagoFactory extends PersonajeFactory {
    @Override
    public Personaje crearPersonaje(String nombre) {
        return new Mago(nombre);
    }
}

class ArqueroFactory extends PersonajeFactory {
    @Override
    public Personaje crearPersonaje(String nombre) {
        return new Arquero(nombre);
    }
}

class JuegoLucha {
    private Personaje jugador1;
    private Personaje jugador2;

    public JuegoLucha(Personaje jugador1, Personaje jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    public void iniciarPelea() {
        System.out.println("\n========================================");
        System.out.println("  ¡Comienza el combate medieval!");
        System.out.println("  " + jugador1.getNombre() + " vs " + jugador2.getNombre());
        System.out.println("========================================\n");

        int turno = 1;
        while (jugador1.estaVivo() && jugador2.estaVivo()) {
            System.out.println("--- Turno " + turno + " ---");
            ejecutarTurno(jugador1, jugador2);
            if (jugador2.estaVivo()) {
                ejecutarTurno(jugador2, jugador1);
            }
            turno++;
            System.out.println();
        }

        System.out.println("========================================");
        if (jugador1.estaVivo()) {
            System.out.println("  🏆 " + jugador1.getNombre() + " ha ganado la pelea!");
        } else {
            System.out.println("  🏆 " + jugador2.getNombre() + " ha ganado la pelea!");
        }
        System.out.println("========================================");
    }

    private void ejecutarTurno(Personaje atacante, Personaje defensor) {
        System.out.println("Turno de " + atacante.getNombre()
                + " | HP defensor (" + defensor.getNombre() + "): "
                + defensor.getPuntosDeVida());
        atacante.atacar(defensor);
        System.out.println(defensor.getNombre() + " ahora tiene "
                + defensor.getPuntosDeVida() + " HP.");
    }
}

public class JuegoLuchaMedieval {

    // Retorna la fábrica según la elección del usuario
    private static PersonajeFactory elegirFactory(int opcion) {
        switch (opcion) {
            case 1:
                return new GuerreroFactory();
            case 2:
                return new MagoFactory();
            case 3:
                return new ArqueroFactory();
            default:
                return new GuerreroFactory();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== JUEGO DE LUCHA MEDIEVAL ===\n");
        System.out.println("Tipos de personaje:");
        System.out.println("  1. Guerrero ⚔️   HP: 120 | Daño: 15–35 | Tiene armadura");
        System.out.println("  2. Mago     🔮   HP:  80 | Daño: 10–40 | Hechizos especiales");
        System.out.println("  3. Arquero  🏹   HP: 100 | Daño: 12–28 | Disparo crítico");

        // Jugador 1
        System.out.print("\nJugador 1 – Introduce tu nombre: ");
        String nombre1 = scanner.nextLine();
        System.out.print("Jugador 1 – Elige tipo (1/2/3): ");
        int tipo1 = scanner.nextInt();
        scanner.nextLine();

        // Jugador 2
        System.out.print("\nJugador 2 – Introduce tu nombre: ");
        String nombre2 = scanner.nextLine();
        System.out.print("Jugador 2 – Elige tipo (1/2/3): ");
        int tipo2 = scanner.nextInt();
        scanner.nextLine();

        // Crear personajes usando Factory Method
        PersonajeFactory factory1 = elegirFactory(tipo1);
        PersonajeFactory factory2 = elegirFactory(tipo2);

        Personaje p1 = factory1.crearPersonaje(nombre1);
        Personaje p2 = factory2.crearPersonaje(nombre2);

        System.out.println("\nPersonaje 1: " + p1.getNombre() + " – " + p1.getTipo());
        System.out.println("Personaje 2: " + p2.getNombre() + " – " + p2.getTipo());

        // Iniciar la pelea
        JuegoLucha juego = new JuegoLucha(p1, p2);
        juego.iniciarPelea();

        scanner.close();
    }
}
