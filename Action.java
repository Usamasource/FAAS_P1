public interface Action<T, R> {
    R run(T input);
    int getMemoryRequirement();  // en megabytes, por ejemplo
}
