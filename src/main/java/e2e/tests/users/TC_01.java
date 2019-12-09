package e2e.tests.users;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_01 {

    @Test(groups="users")
    public void TC_01_TEST() throws InterruptedException {
        Thread.sleep(1400);
        System.out.println(Thread.currentThread().getId()+" USERS_TC_01");
        Assert.assertEquals(true, false);
    }
}
