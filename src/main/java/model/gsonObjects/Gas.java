package model.gsonObjects;


public class Gas extends GsonObject{


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Gas(String name, int id){
        this.setName(name);
        this.setId(id);
    }
}
