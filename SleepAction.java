public class SleepAction implements Action<Integer, String> {
    @Override
    public String run(Integer durationInSeconds) {
        try {
            Thread.sleep(durationInSeconds * 1000);
            return "Dormí por " + durationInSeconds + " segundos.";
        } catch (InterruptedException e) {
            return "Fui interrumpido durante mi siesta!";
        }
    }

    @Override
    public int getMemoryRequirement() {
        return 5; // Esto es arbitrario, digamos que necesita 5 MB para ejecutar.
    }
}
