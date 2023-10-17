import java.util.function.Function;

public class FunctionToActionAdapter<T, R> implements Action<T, R> {
    private Function<T, R> function;
    private int memoryRequirement;

    public FunctionToActionAdapter(Function<T, R> function, int memoryRequirement) {
        this.function = function;
        this.memoryRequirement = memoryRequirement;
    }

    @Override
    public R run(T arg) {
        return function.apply(arg);
    }

    @Override
    public int getMemoryRequirement() {
        return memoryRequirement;
    }
}
