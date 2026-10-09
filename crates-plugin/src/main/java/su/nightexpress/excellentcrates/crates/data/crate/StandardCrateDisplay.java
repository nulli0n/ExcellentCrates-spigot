package su.nightexpress.excellentcrates.crates.data.crate;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.data.model.CrateDisplay;

@NullMarked
public class StandardCrateDisplay implements CrateDisplay {

    private String       name;
    private List<String> lore;

    public StandardCrateDisplay(String name, List<String> lore) {
        this.name = name;
        this.lore = List.copyOf(lore);
    }

    public static StandardCrateDisplay createDefault() {
        String name = "Crate";
        List<String> lore = List.of();

        return new StandardCrateDisplay(name, lore);
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
