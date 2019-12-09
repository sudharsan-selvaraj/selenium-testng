package framework.core;

import framework.testng.SuiteInfo;

public interface ILaunchListener {

    SuiteInfo transformSuite(SuiteInfo suite);

}
