
public <T> T mymethod(T type){
    return type;
}

public <T extends YourType> T mymethod(T type){
    // now you can use YourType methods
    return type;
}

public static <T extends Comparable<T>> int compare(final T name1, final T name2) {
    if (name1 == null) return name2 == null ? 0 : -1;
    if (name2 == null) return 1;
    return name1.compareTo(name2);
}
