package net.demilich.metastone.game.spells;

import java.util.List;
import java.util.function.Predicate;

import net.demilich.metastone.game.GameContext;
import net.demilich.metastone.game.Player;
import net.demilich.metastone.game.entities.Entity;
import net.demilich.metastone.game.spells.desc.SpellArg;
import net.demilich.metastone.game.spells.desc.SpellDesc;

public abstract class Spell {

	public void cast(GameContext context, Player player, SpellDesc desc, List<Entity> targets) {
		if (targets == null) {
			onCast(context, player, desc, null);
			return;
		}
		
		@SuppressWarnings("unchecked")
		Predicate<Entity> targetFilter = (Predicate<Entity>) desc.get(SpellArg.ENTITY_FILTER);
		List<Entity> validTargets = SpellUtils.getValidTargets(targets, targetFilter);
		if (validTargets.size() > 0 && desc.getBool(SpellArg.RANDOM_TARGET)) {
			Entity target = SpellUtils.getRandomTarget(validTargets);
			onCast(context, player, desc, target);
		} else {
			for (Entity target : validTargets) {
				onCast(context, player, desc, target);
			}
		}
	}

	protected abstract void onCast(GameContext context, Player player, SpellDesc desc, Entity target);

	@Override
	public String toString() {
		return "[SPELL " + getClass().getSimpleName() + "]";
	}

}