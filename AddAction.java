import java.util.Map;

public class AddAction implements Action<Map<String, Integer>, Integer> {
    @Override
    public Integer run(Map<String, Integer> args) {
        return args.get("x") + args.get("y");
    }

    @Override
    public int getMemoryRequirement() {
        return 10; // Esto es arbitrario, digamos que necesita 10 MB para ejecutar.
    }
}