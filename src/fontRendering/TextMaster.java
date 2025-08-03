package fontRendering;

import RenderEngine.Loader;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

public class TextMaster {
    private static Loader loaderr;
    private static Map<FontType, List<GUIText>> texts = new HashMap<>();
    private static FontRenderer renderer;

    public static void init(Loader loader) {
        renderer = new FontRenderer();
        loaderr = new Loader();
    }

    public static void loadText(GUIText text) {
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = loaderr.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());
        List<GUIText> textBatch = texts.get(font);
        if(textBatch == null) {
            textBatch = new ArrayList<GUIText>();
            texts.put(font, textBatch);
        }
        textBatch.add(text);
    }

    public static void removeText(GUIText text) {
        List<GUIText> texBatch = texts.get(text.getFont());
        texBatch.remove(text);
        if(texBatch.isEmpty()) {
            texts.remove(text.getFont());
        }
    }

    public static void render() {
        renderer.renderer(texts);
    }

    public static void cleanUp() {
        renderer.cleanUp();
    }

}
