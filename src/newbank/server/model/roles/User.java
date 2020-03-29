package newbank.server.model.roles;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
  public String setNewPassword(String oldPassword, String newPassword) {
    if (checkPassword(oldPassword) ) {
      this.password = getMD5Hash(newPassword);
      return "Password Changed Successfully";
    } else {
      return "Password Change Failed - incorrect password given";
    }
  }

  /*
   * A function to check the password given is correct.
   * */
  public boolean checkPassword(String password) {
    return this.password.equals(getMD5Hash(password));
  }

  /*
   * A function to convert a password string to an MD5 hash so passwords are not stored in plain text.
   * */
  public static String getMD5Hash(String password) {
    try {
      // Create a new instance of MessageDigest using the MD5 hashing algorithm
      MessageDigest md = MessageDigest.getInstance("MD5");
      // Converting the password into bytes then getting the hashed bytes.
      byte[] hashBytes = md.digest(password.getBytes("UTF-8"));

      // Instantiating a string builder and looping through the hashed bytes to convert hashed password to a string.
      StringBuilder sb = new StringBuilder(2 * hashBytes.length);
      for(byte b : hashBytes) {
        sb.append(String.format("%02x", b&0xff));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      System.out.println("Password Checking failed: " + e);
      return null;
    }
  }

  public String getUserID() {
    return this.userID;
  }
}
