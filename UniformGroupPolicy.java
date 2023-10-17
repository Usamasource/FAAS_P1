import java.util.List;

public class UniformGroupPolicy extends PolicyManager {

    @Override
    public Invoker selectInvoker(Controller controller, Action<?, ?> action, Object args) {
        int memoryRequirement = action.getMemoryRequirement();
        List<Invoker> invokers = controller.getInvokers();
        
        // Vamos a encontrar el Invoker con la menor carga.
        Invoker leastLoadedInvoker = null;
        int minLoad = Integer.MAX_VALUE;

        for (Invoker invoker : invokers) {
            int currentLoad = invoker.getCurrentMemoryLoad();  // Método ficticio, puedes implementarlo en el Invoker para obtener la memoria actualmente en uso.
            if (currentLoad < minLoad && invoker.hasAvailableResources(memoryRequirement)) {
                minLoad = currentLoad;
                leastLoadedInvoker = invoker;
            }
        }

        if (leastLoadedInvoker == null) {
            throw new RuntimeException("No invoker has available resources.");
        }

        return leastLoadedInvoker;
    }
}
