import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Main {

    private static final List<Invoker> invokers = Arrays.asList(
            new Invoker(1000),
            new Invoker(1000),
            new Invoker(500));

    static Function<Map<String, Integer>, Integer> addAction = args -> args.get("x") + args.get("y");

    private static Controller controller;
    static Function<Integer, String> sleepFunction = s -> {
        try {
            TimeUnit.SECONDS.sleep(s);
            return "Done!";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        controller = new Controller(invokers, new RoundRobinPolicy());
        controller.registerAction("add", addAction, 10);
        controller.registerAction("sleep", new FunctionToActionAdapter<>(sleepFunction, 10));

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

    public static void testSuma() throws Exception {
        // Testear la acción
        Map<String, Integer> testData = Map.of("x", 5, "y", 7);
        Object result = controller.invoke("add", testData);

        if (result instanceof Integer && (int) result == 12) {
            System.out.println("La acción 'add' funcionó correctamente!");
        } else {
            System.out.println("Error en la acción 'add'. Resultado esperado: 12. Resultado obtenido: " + result);
        }
    }

    public static void testSleep() throws Exception {
        // Testear la acción
        long startTime = System.currentTimeMillis();
        Object result = controller.invoke("sleep", 3); // Sleep durante 3 segundos
        long elapsedTime = System.currentTimeMillis() - startTime;

        if (result.equals("Done!") && elapsedTime >= 3000) {
            System.out.println("La acción 'sleep' funcionó correctamente!");
        } else {
            System.out
                    .println("Error en la acción 'sleep'. El tiempo no fue el esperado o el resultado fue incorrecto.");
        }
    }

    public static void testPolicyManager() {
        List<PolicyManager> policies = Arrays.asList(
                new RoundRobinPolicy(),
                new GreedyGroupPolicy(),
                new UniformGroupPolicy(),
                new BigGroupPolicy());
        for (PolicyManager policy : policies) {
            controller = new Controller(invokers, policy);
            controller.registerAction("add", new FunctionToActionAdapter<>(addAction, 10));

            // Realizar N invocaciones para comparar el rendimiento de cada política
            int N = 1000;
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < N; i++) {
                Map<String, Integer> testData = Map.of("x", i, "y", i + 1);
                controller.invoke("add", testData, 10);
            }
            long elapsedTime = System.currentTimeMillis() - startTime;

            System.out.println("Tiempo total con " + policy.getClass().getSimpleName() + ": " + elapsedTime + "ms");
        }
    }

}
