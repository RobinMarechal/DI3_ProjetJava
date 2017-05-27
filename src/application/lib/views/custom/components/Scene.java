package application.lib.views.custom.components;

import javafx.application.ConditionalFeature;
import javafx.beans.NamedArg;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

/**
 * Created by Robin on 25/04/2017.
 */
public class Scene extends javafx.scene.Scene
{
    /**
     * Creates a Scene for a specific root Node.
     *
     * @param root The root node of the scene graph
     * @throws NullPointerException if root is null
     */
    public Scene (@NamedArg ("root") Parent root)
    {
        super(root);
    }

    /**
     * Creates a Scene for a specific root Node with a specific size.
     *
     * @param root   The root node of the scene graph
     * @param width  The width of the scene
     * @param height The height of the scene
     * @throws NullPointerException if root is null
     */
    public Scene (@NamedArg ("root") Parent root, @NamedArg ("width") double width, @NamedArg ("height") double height)
    {
        super(root, width, height);
    }

    /**
     * Creates a Scene for a specific root Node with a fill.
     *
     * @param root The parent
     * @param fill The fill
     * @throws NullPointerException if root is null
     */
    public Scene (@NamedArg ("root") Parent root, @NamedArg (value = "fill", defaultValue = "WHITE") Paint fill)
    {
        super(root, fill);
    }

    /**
     * Creates a Scene for a specific root Node with a specific size and fill.
     *
     * @param root   The root node of the scene graph
     * @param width  The width of the scene
     * @param height The height of the scene
     * @param fill   The fill
     * @throws NullPointerException if root is null
     */
    public Scene (@NamedArg ("root") Parent root, @NamedArg ("width") double width, @NamedArg ("height") double height, @NamedArg (value = "fill", defaultValue = "WHITE") Paint fill)
    {
        super(root, width, height, fill);
    }

    /**
     * Constructs a scene consisting of a root, with a dimension of width and
     * height, and specifies whether a depth buffer is created for this scene.
     * <p>
     * A scene with only 2D shapes and without any 3D transforms does not need a
     * depth buffer. A scene containing 3D shapes or 2D shapes with 3D
     * transforms may use depth buffer support for proper depth sorted
     * rendering; to avoid depth fighting (also known as Z fighting), disable
     * depth testing on 2D shapes that have no 3D transforms. See
     * {@link Node#depthTestProperty depthTest} for more information.
     *
     * @param root        The root node of the scene graph
     * @param width       The width of the scene
     * @param height      The height of the scene
     * @param depthBuffer The depth buffer flag
     *                    <p>
     *                    The depthBuffer flag is a conditional feature and its default value is
     *                    false. See
     *                    {@link ConditionalFeature#SCENE3D ConditionalFeature.SCENE3D}
     *                    for more information.
     * @throws NullPointerException if root is null
     * @see Node#setDepthTest(DepthTest)
     */
    public Scene (@NamedArg ("root") Parent root, @NamedArg (value = "width", defaultValue = "-1") double width, @NamedArg (value = "height", defaultValue = "-1") double height, @NamedArg ("depthBuffer") boolean depthBuffer)
    {
        super(root, width, height, depthBuffer);
    }

    /**
     * Constructs a scene consisting of a root, with a dimension of width and
     * height, specifies whether a depth buffer is created for this scene and
     * specifies whether scene anti-aliasing is requested.
     * <p>
     * A scene with only 2D shapes and without any 3D transforms does not need a
     * depth buffer nor scene anti-aliasing support. A scene containing 3D
     * shapes or 2D shapes with 3D transforms may use depth buffer support for
     * proper depth sorted rendering; to avoid depth fighting (also known as Z
     * fighting), disable depth testing on 2D shapes that have no 3D transforms.
     * See {@link Node#depthTestProperty depthTest} for more information. A
     * scene with 3D shapes may enable scene anti-aliasing to improve its
     * rendering quality.
     * <p>
     * A Scene can be created and modified on any thread until it is attached to a {@code Stage} that is showing
     * ({@link javafx.stage.Window#isShowing()}. This does not mean the Scene is thread-safe,
     * so manipulation from multiple threads at the same time is illegal, may lead to unexpected results and must be avoided.
     *
     * @param root         The root node of the scene graph
     * @param width        The width of the scene
     * @param height       The height of the scene
     * @param depthBuffer  The depth buffer flag
     * @param antiAliasing The scene anti-aliasing attribute. A value of
     *                     {@code null} is treated as DISABLED.
     *                     <p>
     *                     The depthBuffer and antiAliasing are conditional features. With the
     *                     respective default values of: false and {@code SceneAntialiasing.DISABLED}. See
     *                     {@link ConditionalFeature#SCENE3D ConditionalFeature.SCENE3D}
     *                     for more information.
     * @throws NullPointerException if root is null
     * @see Node#setDepthTest(DepthTest)
     * @since JavaFX 8.0
     */
    public Scene (@NamedArg ("root") Parent root, @NamedArg (value = "width", defaultValue = "-1") double width, @NamedArg (value = "height", defaultValue = "-1") double height, @NamedArg ("depthBuffer") boolean depthBuffer, @NamedArg (value = "antiAliasing", defaultValue = "DISABLED") SceneAntialiasing antiAliasing)
    {
        super(root, width, height, depthBuffer, antiAliasing);
    }
}
