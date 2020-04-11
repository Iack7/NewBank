package uk.ac.bath.newbank.database;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import uk.ac.bath.newbank.model.roles.Customer;
import uk.ac.bath.newbank.model.roles.User;

public class UserDBTest {

  private UserDB users;

  public UserDBTest() {
    users = new UserDB();
  }

  @Test
  public void testUserExists() {
    // Add user
    Customer testUser = new Customer("testUser", User.getMD5Hash("password"));
    users.addUser(testUser);

    assertTrue(users.userExists("testUser"));
    ;
  }

  @Test
  public void testUserDoesNotExist() {
    assertFalse(users.userExists("testUser"));
    ;
  }
}
