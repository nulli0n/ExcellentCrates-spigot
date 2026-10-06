package su.nightexpress.excellentcrates.keys.data.key;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.data.model.KeyDisplay;

@NullMarked
public class StandardKeyDisplay implements KeyDisplay {

    private String       name;
    private List<String> lore;

    public StandardKeyDisplay(String name, List<String> lore) {
        this.name = name;
        this.lore = List.copyOf(lore);
    }

    public static StandardKeyDisplay createDefault() {
        return new StandardKeyDisplay("A key", List.of("A key to open a crate."));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> lore) {
        this.lore = List.copyOf(lore);
    }
}

