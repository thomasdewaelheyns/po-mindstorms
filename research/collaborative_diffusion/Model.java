public interface Model {
  public Model setProcessor(ModelProcessor processor);
  public Model updateSensorValues(int[] values);
  public String explain();
}
