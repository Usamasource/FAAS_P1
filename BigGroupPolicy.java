import java.util.List;

public class BigGroupPolicy extends PolicyManager {
    private int currentIndex = 0;

    @Override
    public Invoker selectInvoker(Controller controller, Action<?, ?> action, Object args) {
        int memoryRequirement = action.getMemoryRequirement();
        List<Invoker> invokers = controller.getInvokers();

        // En lugar de seleccionar el siguiente invoker, intentaremos encontrar el invoker que tiene la mayoría de recursos disponibles
        Invoker bestInvoker = null;
        int maxAvailableMemory = 0;

        for (Invoker invoker : invokers) {
            int availableMemory = invoker.getCurrentMemoryLoad();
            if (availableMemory > maxAvailableMemory && availableMemory >= memoryRequirement) {
                bestInvoker = invoker;
                maxAvailableMemory = availableMemory;
            }
        }

        if (bestInvoker != null) {
            return bestInvoker;
        }

        // Si no encontramos un invoker con recursos disponibles, entonces aplicamos la lógica de RoundRobin
        int triedInvokers = 0;
        while (triedInvokers < invokers.size()) {
            Invoker selectedInvoker = invokers.get(currentIndex);
            currentIndex = (currentIndex + 1) % invokers.size();
            if (selectedInvoker.hasAvailableResources(memoryRequirement)) {
                return selectedInvoker;
            }
            triedInvokers++;
        }

        throw new RuntimeException("No invoker has available resources.");
    }
}
