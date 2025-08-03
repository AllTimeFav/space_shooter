package fontMeshCreator;

import fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GUIText {

	private String textString;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3f colour = new Vector3f(0f, 0f, 0f);

	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;
	private float width;
	private float edge;
	private float borderWidth;
	private float borderEdge;
	private Vector2f offset = new Vector2f(0.0f, 0.0f);
	private Vector3f outlineColor = new Vector3f(0.2f, 0.2f, 0.2f);

	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		TextMaster.loadText(this);
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setEdge(float edge) {
		this.edge = edge;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}

	public void setBorderEdge(float borderEdge) {
		this.borderEdge = borderEdge;
	}

	public void setOffset(float x, float y) {
		offset.set(x, y);
	}

	public void setOutlineColor(float r, float g, float b) {
		outlineColor.set(r, g, b);
	}

	public float getWidth() {
		return width;
	}

	public float getEdge() {
		return edge;
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public float getBorderEdge() {
		return borderEdge;
	}

	public Vector2f getOffset() {
		return offset;
	}

	public Vector3f getOutlineColor() {
		return outlineColor;
	}


	public void remove() {
		TextMaster.removeText(this);
	}

	public FontType getFont() {
		return font;
	}


	public void setColour(float r, float g, float b) {
		colour.set(r, g, b);
	}

	public Vector3f getColour() {
		return colour;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public Vector2f getPosition() {
		return position;
	}

	public int getMesh() {
		return textMeshVao;
	}


	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	protected float getFontSize() {
		return fontSize;
	}


	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	protected String getTextString() {
		return textString;
	}

}
