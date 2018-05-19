package game;


import java.util.List;
import java.util.ArrayList;

public class WorldRemoveCreate {
	private List<Creature> creatures;

	public List<Creature> getCreatures() {
		return creatures;
	}

	public void setCreatures(List<Creature> creatures) {
		this.creatures = creatures;
	}

	public void remove(Creature other) {
		creatures.remove(other);
	}

	public void update() {
		List<Creature> toUpdate = new ArrayList<>(creatures);
		for (Creature creature : toUpdate) {
			creature.update();
		}
	}
}