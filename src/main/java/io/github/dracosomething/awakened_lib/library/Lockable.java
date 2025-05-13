package io.github.dracosomething.awakened_lib.library;

public class Lockable<T> {
    private T object;
    private PassKey key;
    private boolean locked;

    private Lockable (T object, PassKey key) {
        this.key = key;
        this.object = object;
        this.locked = false;
    }

    public static <T> Lockable<T> of (T object, PassKey key) {
        return new Lockable<>(object, key);
    }

    public boolean lock() {
        if (this.locked) return false;
        this.locked = true;
        return true;
    }

    public boolean tryUnlock(String key) {
        this.locked = this.key.correctInput(key);
        return this.locked;
    }

    public T get() {
        if (this.locked) return null;
        return this.object;
    }

    public boolean isUnlocked() {
        return !this.locked;
    }

    public void ifUnlocked(Runnable task) {
        if (isUnlocked()) {
            task.run();
        }
    }

    public boolean set (T value) {
        if (this.isUnlocked()) this.object = value;
        return this.object == value;
    }

    public boolean changeKey(PassKey key) {
        if (this.isUnlocked()) this.key = key;
        return this.key == key;
    }
}
