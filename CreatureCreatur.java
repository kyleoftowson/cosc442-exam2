package game;


public class CreatureCreatur {
	private CreatureAi ai;
	private int detectCreatures;

	public CreatureAi getAi() {
		return ai;
	}

	public void setAi(CreatureAi ai) {
		this.ai = ai;
	}

	public int getDetectCreatures() {
		return detectCreatures;
	}

	public void setDetectCreatures(int detectCreatures) {
		this.detectCreatures = detectCreatures;
	}

	public boolean canSee(int wx, int wy, int wz, World thisWorld) {
		return (detectCreatures > 0 && thisWorld.creature(wx, wy, wz) != null || ai.canSee(wx, wy, wz));
	}

	public Creature creature(int wx, int wy, int wz, World thisWorld) {
		if (canSee(wx, wy, wz, thisWorld))
			return thisWorld.creature(wx, wy, wz);
		else
			return null;
	}

	public Tile tile(int wx, int wy, int wz, World thisWorld) {
		if (canSee(wx, wy, wz, thisWorld))
			return thisWorld.tile(wx, wy, wz);
		else
			return ai.rememberedTile(wx, wy, wz);
	}

	public Item item(int wx, int wy, int wz, World thisWorld) {
		if (canSee(wx, wy, wz, thisWorld))
			return thisWorld.item(wx, wy, wz);
		else
			return null;
	}
}