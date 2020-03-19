package newbank.server.model.roles;

public abstract class User {

  private String userID;
  private String password;

  public User(String userID, String password) {
    this.userID = userID.toLowerCase();
    this.password = password;
  }

  public abstract String getUserType();

  /*
   * A function to set a new password. Password change is only allowed, if the old password is provided.
   * */
  public boolean setNewPassword(String oldPassword, String newPassword) {
    if (this.password.equals(oldPassword)) {
      this.password = newPassword;
      return true;
    } else {
      return false;
    }
  }

  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }

  public String getUserID() {
    return this.userID;
  }
}
