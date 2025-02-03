package su.nightexpress.excellentcrates.util.inspect;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.nightcore.util.placeholder.PlaceholderList;

import java.util.*;
import java.util.function.Consumer;

public class Inspector<T> {

    private static final String LOG_PREFIX = "-> ";

    private final Map<String, Inspection<T>> inspectionsMap;

    public Inspector() {
        this.inspectionsMap = new HashMap<>();
    }

    @NotNull
    public static <T> Inspector<T> create(@NotNull Consumer<Inspector<T>> consumer) {
        Inspector<T> inspector = new Inspector<>();
        consumer.accept(inspector);
        return inspector;
    }

//    @NotNull
//    public Inspector<T> addInspections(@NotNull Inspector<T> other) {
//        other.getInspections().forEach(this::addInspection);
//        return this;
//    }

    @NotNull
    public Inspector<T> addInspection(@NotNull Inspection<T> inspection) {
        this.inspectionsMap.put(inspection.name().toLowerCase(), inspection);
        return this;
    }

    public int countProblems(@NotNull T object) {
        return (int) this.getInspections().stream().map(inspection -> inspection.inspect(object)).filter(info -> info.getResult() != InspectionResult.GOOD).count();
    }

    public boolean hasProblems(@NotNull T object) {
        return this.countProblems(object) > 0;
    }

    public void addPlaceholders(@NotNull PlaceholderList<T> list) {
        this.inspectionsMap.forEach((type, inspection) -> {
            list.add(Placeholders.INSPECTION_TYPE.apply(inspection), source -> inspection.inspect(source).getColored());
        });

        list.add(Placeholders.INSPECTION_PROBLEMS, source -> {
            int problems = this.countProblems(source);
            String string = (problems > 0 ? Lang.INSPECTION_PROBLEMS : Lang.INSPECTION_NO_PROBLEMS).getString().replace(Placeholders.GENERIC_AMOUNT, String.valueOf(problems));
            return problems > 0 ? Lang.badEntry(string) : Lang.goodEntry(string);
        });
    }

    public void printConsole(@NotNull CratesPlugin plugin, @NotNull T object, @NotNull String header) {
        List<InspectionInfo> infos = new ArrayList<>();
        this.getInspections().forEach(inspection -> {
            InspectionInfo info = inspection.inspect(object);
            if (info.getResult() == InspectionResult.GOOD) return;

            infos.add(info);
        });
        if (infos.isEmpty()) return;

        plugin.warn(header);
        infos.forEach(info -> {
            String message = LOG_PREFIX + info.getMessage();

            switch (info.getResult()) {
                case BAD -> plugin.error(message);
                case WARN -> plugin.warn(message);
            }
        });
    }

//    @Nullable
//    public InspectionInfo getInspectionInfo(@NotNull InspectionType type, @NotNull T object) {
//        Inspection<T> inspection = this.inspectionsMap.get(type);
//        if (inspection == null) return null;
//
//        return inspection.inspect(object);
//    }
//
//    @NotNull
//    public String getInspectionMessage(@NotNull InspectionType type, @NotNull T object) {
//        InspectionInfo info = this.getInspectionInfo(type, object);
//        if (info == null) return "< No inspection >";
//
//        return info.getMessage();
//    }

    @NotNull
    public Map<String, Inspection<T>> getInspectionsMap() {
        return this.inspectionsMap;
    }

    @NotNull
    public Set<Inspection<T>> getInspections() {
        return new HashSet<>(this.inspectionsMap.values());
    }
}
