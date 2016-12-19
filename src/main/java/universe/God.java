package universe;

public enum God {
  ONE;

  private boolean godsWrath = false;

  public boolean isGodsWrath() {
    return godsWrath;
  }

  public void visualizatingFinishNotify() {
    godsWrath = true;
  }
}
