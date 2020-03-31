package uk.ac.bath.newbank.model.roles;

public class Banker extends User {

  public Banker(String bankerID, String password) {
    super(bankerID, password);
  }

  @Override
  public String getUserType() {
    return "banker";
  }
}
