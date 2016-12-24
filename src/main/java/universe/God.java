package universe;

public enum God {
  ONE;

  private boolean godsWrath = false;
  private boolean oblivion = false;

  public boolean isGodsWrath() {
    return godsWrath;
  }

  public void visualizatingFinishNotify() {
    System.out.println("WRATH");
    System.out.println("WRATH");
    System.out.println("WRATH");
    godsWrath = true;
  }

  public void oblivion() {
    oblivion = true;
  }

  public boolean isOblivion() {
    return oblivion;
  }
}
