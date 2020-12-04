import jmcserver.Password;
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
public class PasswordTest {
    
    public PasswordTest() {
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
    public void testHashPasswordMethodReturnsHashedPassword() {
        Password password = new Password();
        String pword = "P@ssw0rd";
        String salt = password.generateSalt();
        String hashedPassword = password.hashPassword(pword, salt);
        System.out.println(hashedPassword);
        assertNotNull(hashedPassword);
    }
    
    @Test
    public void testGenerateSaltMethodReturnsSalt() {
        Password password = new Password();
        String salt = password.generateSalt();
        System.out.println(salt);
        assertNotNull(salt);
    }
}
