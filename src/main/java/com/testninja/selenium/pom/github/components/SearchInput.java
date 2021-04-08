package com.testninja.selenium.pom.github.components;

import com.testninja.selenium.framework.pageobject.BaseWebComponent;

public class SearchInput extends BaseWebComponent {

    public void search(String keyword) {
        interactions.typeAndEnter(getWrappedWebElement(), keyword);
    }

}
