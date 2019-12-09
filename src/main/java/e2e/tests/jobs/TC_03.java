package e2e.tests.jobs;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_03 {

    @Test(groups = {"jobs"})
    public void TC_03_TEST() throws InterruptedException{
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getId()+" JOBS_TC_03");
        Assert.assertEquals(true, false);
    }
}
