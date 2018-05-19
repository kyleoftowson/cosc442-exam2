package game;


import java.util.List;
import java.util.ArrayList;

public class CreatureEffect {
	private List<Effect> effects;

	public List<Effect> getEffects() {
		return effects;
	}

	public void setEffects(List<Effect> effects) {
		this.effects = effects;
	}

	public void addEffect(Effect effect, Creature creature) {
		if (effect == null)
			return;
		effect.start(creature);
		effects.add(effect);
	}

	public void updateEffects(Creature creature) {
		List<Effect> done = new ArrayList<Effect>();
		for (Effect effect : effects) {
			effect.update(creature);
			if (effect.isDone()) {
				effect.end(creature);
				done.add(effect);
			}
		}
		effects.removeAll(done);
	}
}