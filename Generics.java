
public <T> T mymethod(T type){
  return type;
}

public <T extends YourType> T mymethod(T type){
  // now you can use YourType methods
  return type;
}

