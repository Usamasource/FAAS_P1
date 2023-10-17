import java.util.List;

public class RoundRobinPolicy extends PolicyManager {
    private int currentIndex = 0;

    @Override
    public Invoker selectInvoker(Controller controller, Action<?, ?> action, Object args) {
        int memoryRequirement = action.getMemoryRequirement();
        List<Invoker> invokers = controller.getInvokers();
        
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
