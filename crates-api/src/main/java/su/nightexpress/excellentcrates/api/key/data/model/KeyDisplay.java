package su.nightexpress.excellentcrates.api.key.data.model;

import java.util.List;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface KeyDisplay {

    String getName();

    void setName(String name);

    List<String> getLore();

    void setLore(List<String> lore);
}
