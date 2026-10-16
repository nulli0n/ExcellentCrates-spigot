package su.nightexpress.excellentcrates.api.reward.editor;

import java.util.Comparator;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.engine.text.StringRef;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardSorter extends Identifiable {

    StringRef getName();

    Comparator<Reward> getComparator(Crate crate);
}
