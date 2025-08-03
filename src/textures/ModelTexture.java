package textures;

public class ModelTexture {

    private int textureID;
    private int specularMap;

    public int getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(int normalMap) {
        this.normalMap = normalMap;
    }

    private int normalMap;

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    private float shineDamper = 1;
    private int numberOfRows = 1;

    public boolean isFakeLighting() {
        return fakeLighting;
    }

    public void setFakeLighting(boolean fakeLighting) {
        this.fakeLighting = fakeLighting;
    }

    private boolean fakeLighting = false;

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    private boolean hasTransparency = false;

    public int getSpecularMap() {
        return specularMap;
    }

    public boolean isHasSpecularMap() {
        return hasSpecularMap;
    }

    public void setSpecularMap(int specularMap) {
        this.specularMap = specularMap;
        this.hasSpecularMap = true;
    }


    private boolean hasSpecularMap = false;


    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    private float reflectivity = 0;

    public ModelTexture(int texture){
        this.textureID = texture;
    }

    public int getID(){
        return textureID;
    }

}
