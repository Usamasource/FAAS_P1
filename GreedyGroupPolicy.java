public class GreedyGroupPolicy extends PolicyManager {

    @Override
    public Invoker selectInvoker(Controller controller, Action<?, ?> action, Object args) {
        int memoryRequirement = action.getMemoryRequirement();
        
        Invoker selectedInvoker = findAvailableInvoker(controller, memoryRequirement);
        if (selectedInvoker != null) {
            return selectedInvoker;
        }

        throw new RuntimeException("No invoker has available resources.");
    }
}

// ... Aquí puedes agregar más políticas siguiendo el mismo patrón ...
