import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Main {

    private static final List<Invoker> invokers = Arrays.asList(
            new Invoker(1000),
            new Invoker(1000),
            new Invoker(500)
    );

    private static Controller controller; // Se asignará según el policy manager seleccionado

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nSeleccione una prueba:");
            System.out.println("1. Suma");
            System.out.println("2. Sleep");
            System.out.println("3. Policy Manager");
            System.out.println("4. Salir");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    testSuma();
                    break;
                case 2:
                    testSleep();
                    break;
                case 3:
                    testPolicyManager();
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    public static void testSuma() {
        Function<Map<String, Integer>, Integer> addAction = args -> args.get("x") + args.get("y");
        controller = new Controller(invokers, new RoundRobinPolicy());
        controller.registerAction("add", addAction);
        
        // Aquí puedes agregar la lógica de la prueba para la suma...
    }

    public static void testSleep() {
        Action<Integer, String> sleepAction = s -> {
            try {
                TimeUnit.SECONDS.sleep(s);
                return "Done!";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        controller = new Controller(invokers, new RoundRobinPolicy());
        controller.registerAction("sleep", sleepAction);
        
        // Aquí puedes agregar la lógica de la prueba para el sleep...
    }

    public static void testPolicyManager() {
        Action<Map<String, Integer>, Integer> addAction = args -> args.get("x") + args.get("y");

        System.out.println("1. Round Robin");
        System.out.println("2. Greedy Group");
        System.out.println("3. Uniform Group");
        System.out.println("4. Big Group");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                controller = new Controller(invokers, new RoundRobinPolicy());
                break;
            case 2:
                controller = new Controller(invokers, new GreedyGroupPolicy());
                break;
            case 3:
                controller = new Controller(invokers, new UniformGroupPolicy());
                break;
            case 4:
                controller = new Controller(invokers, new BigGroupPolicy());
                break;
            default:
                System.out.println("Opción no válida.");
                return;
        }

        controller.registerAction("add", addAction);
        
        // Aquí puedes agregar la lógica de la prueba para el PolicyManager...
    }
}
