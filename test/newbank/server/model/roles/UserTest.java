package newbank.server.model.roles;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    private String testPassword = "password";
    private String testNewPassword =  "newPassword";
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
