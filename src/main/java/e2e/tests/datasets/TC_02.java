package e2e.tests.datasets;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_02 {


    @Test(groups = {"datasets"})
    public void TC_01_TEST() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getId()+" DATASETS_TC_02");
        Assert.assertEquals(true, false);
    }
}
