package newbank.server.database;

import newbank.server.model.roles.Customer;
import newbank.server.model.roles.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDBTest {

    private UserDB users;
    public UserDBTest() {
        users = new UserDB();
    }

    @Test
    public void testUserExists() {
        //Add user
        Customer testUser = new Customer("testUser", User.getMD5Hash("password"));
        users.addUser(testUser);

        assertTrue(users.userExists("testUser"));;
    }

    @Test
    public void testUserDoesNotExist() {
        assertFalse(users.userExists("testUser"));;
    }
}