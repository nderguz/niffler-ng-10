package guru.qa.niffler.page;

import guru.qa.niffler.page.component.Header;

import javax.annotation.Nonnull;

public class AllPeoplePage {
    private final Header header = new Header();

    public @Nonnull Header getHeader(){
        return header;
    }
}
