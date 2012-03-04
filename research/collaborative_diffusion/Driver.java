public interface Driver {
  public Driver useModel(Model model);
  public Driver useRobotAPI(RobotAPI api);

  public boolean isBusy();

  public void step();

  public void perform(int action);
}
