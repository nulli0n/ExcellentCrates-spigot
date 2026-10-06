package su.nightexpress.excellentcrates.crates.data.crate;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.data.model.ICrateDisplay;

@NullMarked
public class CrateDisplay implements ICrateDisplay {

    private String       name;
    private List<String> lore;

    public CrateDisplay(String name, List<String> lore) {
        this.name = name;
        this.lore = List.copyOf(lore);
    }

    public static CrateDisplay createDefault() {
        String name = "Crate";
        List<String> lore = List.of();

        return new CrateDisplay(name, lore);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getLore() {
        return this.lore;
    }

    @Override
    public void setLore(List<String> lore) {
        this.lore = List.copyOf(lore);
    }
}
