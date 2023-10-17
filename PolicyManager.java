import java.util.List;

public abstract class PolicyManager {
    
    public abstract Invoker selectInvoker(Controller controller, Action<?, ?> action, Object args);

    protected Invoker findAvailableInvoker(Controller controller, int memoryRequirement) {
        List<Invoker> invokers = controller.getInvokers();
        
        for (Invoker invoker : invokers) {
            if (invoker.hasAvailableResources(memoryRequirement)) {
                return invoker;
            }
        }
        
        return null;
    }
}
