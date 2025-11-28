package guru.qa.niffler.api.core;

import java.net.*;
import java.util.List;

public enum ThreadSafeCookieStore implements CookieStore {
    INSTANCE;

    private final ThreadLocal<CookieStore> cookieStore = ThreadLocal.withInitial(
            ThreadSafeCookieStore::inMemoryCookieStore
    );

    @Override
    public void add(URI uri, HttpCookie cookie) {
        cookieStore.get().add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return cookieStore.get().get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return cookieStore.get().getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return cookieStore.get().getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return cookieStore.get().remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return cookieStore.get().removeAll();
    }

    public String xsrfCookie(){
        return cookieStore.get().getCookies()
                .stream()
                .filter(c -> c.getName().equals("XSRF-TOKEN"))
                .findFirst()
                .get()
                .getValue();
    }

    private static CookieStore inMemoryCookieStore() {
        return new CookieManager(null, CookiePolicy.ACCEPT_ALL).getCookieStore();
    }
}
