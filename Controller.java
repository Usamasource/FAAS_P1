import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;

public class Controller {
    private final Map<String, Action<?, ?>> actions = new HashMap<>();
    private final List<Invoker> invokers;
    private final PolicyManager policyManager;

    public Controller(List<Invoker> invokers, PolicyManager policyManager) {
        this.invokers = invokers;
        this.policyManager = policyManager;
    }
    public void registerAction(String id, Action action) {
        actions.put(id, action);
    }

    // Sobrecarga para admitir una Function
    public <T, R> void registerAction(String id, Function<T, R> function, int memoryRequirement) {
        actions.put(id, new FunctionToActionAdapter<>(function, memoryRequirement));
    }

    public <T, R> R invoke(String actionId, T args) throws Exception {
        Action<T, R> action = (Action<T, R>) actions.get(actionId);
        if (action == null) {
            throw new IllegalArgumentException("Action not found: " + actionId);
        }

        Invoker selectedInvoker = policyManager.selectInvoker(this, action, args);
        
        if (selectedInvoker == null) {
            throw new RuntimeException("No invoker has available resources for the action.");
        }

        return selectedInvoker.invoke(action, args);
    }

    public <T, R> Future<R> invokeAsync(String actionId, T args) {
        Action<T, R> action = (Action<T, R>) actions.get(actionId);
        if (action == null) {
            throw new IllegalArgumentException("Action not found: " + actionId);
        }
        Invoker selectedInvoker = policyManager.selectInvoker(this, action, args);
        return selectedInvoker.invokeAsync(action, args);
    }

    public List<Invoker> getInvokers() {
        return invokers;
    }
    public void invoke(String string, Map<String, Integer> testData, int i) {
    }
}
