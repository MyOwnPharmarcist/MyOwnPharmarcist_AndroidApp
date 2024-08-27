package teamyj.dev.hrd_final_project.layout;

public class Druglist {
    private String name;
    private String color;
    private String shape;
    private String texture;

    public Druglist(String name, String color, String shape, String texture) {
        this.name = name;
        this.color = color;
        this.shape = shape;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }

    public String getTexture() {
        return texture;
    }
}
