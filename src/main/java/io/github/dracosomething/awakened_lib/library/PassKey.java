package io.github.dracosomething.awakened_lib.library;

import java.util.Objects;
import java.util.Random;

public class PassKey {
    private String stringKey;
    private long longKey;

    private PassKey(String stringKey, long longKey) {
        this.stringKey = stringKey;
        this.longKey = longKey;
    }

    public static PassKey create(String string, long longKey) {
        return new PassKey(string, longKey);
    }

    public static PassKey create(String string) {
        Random random = new Random();
        return create(string, random.nextLong());
    }

    public static PassKey create(long longKey) {
        return create("key", longKey);
    }

    public boolean correctInput(String string) {
        String key = this.parse();
        return Objects.equals(string, key);
    }

    public String parse() {
        return this.stringKey + ":" + Long.toString(longKey);
    }
}
