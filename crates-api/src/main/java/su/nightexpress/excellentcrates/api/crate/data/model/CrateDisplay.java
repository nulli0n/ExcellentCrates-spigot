package su.nightexpress.excellentcrates.api.crate.data.model;

import java.util.List;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CrateDisplay {

    String getName();

    void setName(String name);

    List<String> getLore();

    void setLore(List<String> lore);
}
