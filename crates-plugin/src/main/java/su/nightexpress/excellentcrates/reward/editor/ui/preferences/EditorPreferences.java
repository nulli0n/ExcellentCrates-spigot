package su.nightexpress.excellentcrates.reward.editor.ui.preferences;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class EditorPreferences {

    private boolean fastMode;

    public EditorPreferences(boolean fastMode) {
        this.fastMode = fastMode;
    }

    public boolean isFastMode() {
        return fastMode;
    }

    public void setFastMode(boolean fastMode) {
        this.fastMode = fastMode;
    }
}
