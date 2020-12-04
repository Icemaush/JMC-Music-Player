import jmcserver.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class UserTest {
    
    public UserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testVerifyPasswordSuccess() {
        String username = "Reece";
        String pword = "P@ssw0rd";
        User user = new User(username, pword);
        assertTrue(user.verifyPassword(pword));
    }
    
    @Test
    public void testVerifyPasswordFailure() {
        String username = "Reece";
        String pword = "P@ssw0rd";
        String incorrectPword = "1234";
        User user = new User(username, pword);
        assertFalse(user.verifyPassword(incorrectPword));
    }
}
