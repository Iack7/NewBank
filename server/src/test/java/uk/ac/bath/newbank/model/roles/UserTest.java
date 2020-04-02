package uk.ac.bath.newbank.model.roles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserTest {

  private String testPassword = "password";
  private String testNewPassword = "newPassword";
  private String testWrongPassword = "wrongPassword";
  private String testPasswordHash = "5f4dcc3b5aa765d61d8327deb882cf99";

  // Test user created with a password of 'password'
  private Customer testCustomer = new Customer("test", testPasswordHash);

  @Test
  public void testSetNewPasswordWithCorrectOldPassword() {
    testCustomer.setNewPassword(testPassword, testNewPassword);
    assertTrue(testCustomer.checkPassword(testNewPassword));
    assertFalse(testCustomer.checkPassword(testPassword));
  }

  @Test
  public void testSetNewPasswordWithIncorrectOldPassword() {
    testCustomer.setNewPassword(testWrongPassword, testNewPassword);
    assertTrue(testCustomer.checkPassword(testPassword));
    assertFalse(testCustomer.checkPassword(testNewPassword));
  }

  @Test
  public void testCheckPasswordWithCorrectPassword() {
    assertTrue(testCustomer.checkPassword(testPassword));
  }

  @Test
  public void testCheckPasswordWithIncorrectPassword() {
    assertTrue(testCustomer.checkPassword(testPassword));
  }

  @Test
  public void testGetMD5Hash() {
    assertEquals(testPasswordHash, testCustomer.getMD5Hash(testPassword));
  }
}
