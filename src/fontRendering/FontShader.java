package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/fontRendering/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "src/fontRendering/fontFragment.glsl";

	private int location_color;
	private int location_translation;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_offset;
	private int location_outlineColor;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_translation = super.getUniformLocation("translation");
		location_color = super.getUniformLocation("color");
		location_width = super.getUniformLocation("width");
		location_edge = super.getUniformLocation("edge");
		location_borderWidth = super.getUniformLocation("borderWidth");
		location_borderEdge = super.getUniformLocation("borderEdge");
		location_offset = super.getUniformLocation("offset");
		location_outlineColor = super.getUniformLocation("outlineColor");
	}

	protected void loadWidth(float width) {
		super.loadFloat(location_width, width);
	}
	protected void loadBorder(float border) {
		super.loadFloat(location_width, border);
	}
	protected void loadBorderWidth(float BorderWidth) {
		super.loadFloat(location_width, BorderWidth);
	}
	protected void loadBorderEdge(float BorderEdge) {
		super.loadFloat(location_width, BorderEdge);
	}
	public void loadOutlineColor(Vector3f outlineColor){
		super.loadVector(location_outlineColor, outlineColor);
	}
	public void loadOffset(Vector2f offset){
		super.load2DVector(location_offset, offset);
	}
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void  loadColor(Vector3f color) {
		super.loadVector(location_color, color);
	}

	protected void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}


}
