package committee.nova.prioritytarget.api;

import committee.nova.prioritytarget.PriorityTarget;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public class PriorityTargetPredicates {
    private static final HashMap<Byte, BiPredicate<Mob, Player>> predicateOrList = new HashMap<>();
    private static final HashMap<Byte, BiPredicate<Mob, Player>> predicateAndList = new HashMap<>();

    public static void init() {
        addPredicateOr((byte) 0, (m, p) -> m.getTarget() != null && m.getTarget().is(p));
    }

    public static void addPredicateOr(byte priority, BiPredicate<Mob, Player> e) {
        predicateOrList.put(priority, e);
    }

    public static void addPredicateAnd(byte priority, BiPredicate<Mob, Player> e) {
        predicateAndList.put(priority, e);
    }

    public static void removeAllPredicateOr(String reason) {
        if (reason.replace(" ", "").isEmpty()) {
            PriorityTarget.LOGGER.warn("Someone was trying to remove all OR predicates with no reason, cancelled");
            return;
        }
        PriorityTarget.LOGGER.warn("All OR predicates removed, reason: " + reason);
        predicateOrList.forEach(predicateOrList::remove);
    }

    public static void removeAllPredicateAnd(String reason) {
        if (reason.replace(" ", "").isEmpty()) {
            PriorityTarget.LOGGER.warn("Someone was trying to remove all AND predicates with no reason, cancelled");
            return;
        }
        PriorityTarget.LOGGER.warn("All AND predicates removed, reason: " + reason);
        predicateAndList.forEach(predicateAndList::remove);
    }

    public static boolean isTargeting(Mob m, Player u) {
        byte or = -128;
        byte and = -127;
        for (final Map.Entry<Byte, BiPredicate<Mob, Player>> p : predicateOrList.entrySet()) {
            final byte k = p.getKey();
            if (p.getValue().test(m, u) && k > or) or = k;
        }
        for (final Map.Entry<Byte, BiPredicate<Mob, Player>> p : predicateAndList.entrySet()) {
            final byte k = p.getKey();
            if (!p.getValue().test(m, u) && k > and) and = k;
        }
        return or >= and;
    }
}
