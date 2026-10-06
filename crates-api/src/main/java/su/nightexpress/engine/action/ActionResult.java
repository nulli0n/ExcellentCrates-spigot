package su.nightexpress.engine.action;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

@NullMarked
public record ActionResult(boolean result,
                           ActionReason reason,
                           @Nullable MessageLocale locale,
                           @Nullable PlaceholderApplier placeholders) {

    private static final PlaceholderApplier EMPTY_PLACEHOLDERS = PlaceholderApplier.empty();

    public static ActionResult of(boolean result) {
        return new ActionResult(result, result ? CommonReason.SUCCESS : CommonReason.FAILURE, null, null);
    }

    public static ActionResult of(boolean result, ActionReason reason) {
        return new ActionResult(result, reason, null, null);
    }

    public static ActionResult of(boolean result, MessageLocale locale) {
        return new ActionResult(result, CommonReason.SUCCESS, locale, null);
    }

    public static ActionResult of(boolean result, ActionReason reason, MessageLocale locale) {
        return new ActionResult(result, reason, locale, null);
    }

    public static ActionResult of(boolean result, MessageLocale locale, PlaceholderApplier placeholders) {
        return new ActionResult(result, CommonReason.SUCCESS, locale, placeholders);
    }

    public static ActionResult of(boolean result, ActionReason reason, MessageLocale locale,
                                  PlaceholderApplier placeholders) {
        return new ActionResult(result, reason, locale, placeholders);
    }

    public static ActionResult ok() {
        return new ActionResult(true, CommonReason.SUCCESS, null, null);
    }

    public static ActionResult ok(ActionReason reason) {
        return new ActionResult(true, reason, null, null);
    }

    public static ActionResult ok(MessageLocale locale) {
        return new ActionResult(true, CommonReason.SUCCESS, locale, null);
    }

    public static ActionResult ok(ActionReason reason, MessageLocale locale) {
        return new ActionResult(true, reason, locale, null);
    }

    public static ActionResult ok(MessageLocale locale, PlaceholderApplier placeholders) {
        return new ActionResult(true, CommonReason.SUCCESS, locale, placeholders);
    }

    public static ActionResult ok(ActionReason reason, MessageLocale locale,
                                  PlaceholderApplier placeholders) {
        return new ActionResult(true, reason, locale, placeholders);
    }

    public static ActionResult fail() {
        return new ActionResult(false, CommonReason.FAILURE, null, null);
    }

    public static ActionResult fail(ActionReason reason) {
        return new ActionResult(false, reason, null, null);
    }

    public static ActionResult fail(MessageLocale locale) {
        return new ActionResult(false, CommonReason.FAILURE, locale, null);
    }

    public static ActionResult fail(ActionReason reason, MessageLocale locale) {
        return new ActionResult(false, reason, locale, null);
    }

    public static ActionResult fail(MessageLocale locale, PlaceholderApplier placeholders) {
        return new ActionResult(false, CommonReason.FAILURE, locale, placeholders);
    }

    public static ActionResult fail(ActionReason reason, MessageLocale locale, PlaceholderApplier placeholders) {
        return new ActionResult(false, reason, locale, placeholders);
    }

    public Optional<MessageLocale> feedback() {
        return Optional.ofNullable(this.locale);
    }

    /* public void addPlaceholders(PlaceholderContext.Builder builder) {
        if (this.placeholders != null) {
            this.placeholders.accept(builder);
        }
    } */

    public boolean handleFeedback(Consumer<MessageLocale> consumer) {
        if (this.locale != null) {
            consumer.accept(this.locale);
        }
        return this.result;
    }

    public boolean handleFeedback(BiConsumer<MessageLocale, PlaceholderApplier> consumer) {
        if (this.locale != null) {
            PlaceholderApplier contextPlaceholders;
            if (this.placeholders == null) {
                contextPlaceholders = EMPTY_PLACEHOLDERS;
            }
            else contextPlaceholders = this.placeholders;

            consumer.accept(this.locale, contextPlaceholders);
        }
        return this.result;
    }

    public boolean is(ActionReason reason) {
        return this.reason == reason;
    }

    public boolean success() {
        return this.result;
    }

    public boolean failure() {
        return !this.result;
    }
}
