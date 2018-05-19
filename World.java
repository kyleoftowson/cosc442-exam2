package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class World {
	private WorldAddEmpti worldAddEmpti = new WorldAddEmpti();
	private Worlditem worlditem = new Worlditem();
	public int width() { return worldAddEmpti.getWidth(); }
	
	public int height() { return worldAddEmpti.getHeight(); }

	public int depth() { return worldAddEmpti.getDepth(); }
	
	public World(Tile[][][] tiles){
		worldAddEmpti.setTiles(tiles);
		worldAddEmpti.setWidth(tiles.length);
		worldAddEmpti.setHeight(tiles[0].length);
		worldAddEmpti.setDepth(tiles[0][0].length);
		worldAddEmpti.getWorldRemoveCreate().setCreatures(new ArrayList<Creature>());
		worlditem.setItems(new Item[worldAddEmpti.getWidth()][worldAddEmpti.getHeight()][worldAddEmpti.getDepth()]);
	}

	public Creature creature(int x, int y, int z){
		return worldAddEmpti.creature(x, y, z);
	}
	
	public Tile tile(int x, int y, int z){
		return worldAddEmpti.tile(x, y, z);
	}
	
	public char glyph(int x, int y, int z){
		Creature creature = worldAddEmpti.creature(x, y, z);
		if (creature != null)
			return creature.glyph();
		
		if (worlditem.item(x,y,z) != null)
			return worlditem.item(x,y,z).glyph();
		
		return worldAddEmpti.tile(x, y, z).glyph();
	}
	
	public Color color(int x, int y, int z){
		Creature creature = worldAddEmpti.creature(x, y, z);
		if (creature != null)
			return creature.color();
		
		if (worlditem.item(x,y,z) != null)
			return worlditem.item(x,y,z).color();
		
		return worldAddEmpti.tile(x, y, z).color();
	}

	public void dig(int x, int y, int z) {
		worldAddEmpti.dig(x, y, z);
	}
	
	public void addAtEmptyLocation(Creature creature, int z){
		worldAddEmpti.addAtEmptyLocation(creature, z);
	}
	
	public void update(){
		worldAddEmpti.getWorldRemoveCreate().update();
	}

	public void remove(Creature other) {
		worldAddEmpti.getWorldRemoveCreate().remove(other);
	}
	
	public void remove(Item item) {
		worldAddEmpti.remove(item, this.worlditem);
	}
	
	public Item item(int x, int y, int z){
		return worlditem.item(x, y, z);
	}
	
	public void addAtEmptyLocation(Item item, int depth) {
		worldAddEmpti.addAtEmptyLocation(item, depth, this.worlditem);
	}

	public void remove(int x, int y, int z) {
		worlditem.remove(x, y, z);
	}

	public boolean addAtEmptySpace(Item item, int x, int y, int z){
		return worldAddEmpti.addAtEmptySpace(item, x, y, z, this.worlditem);
	}

	public void add(Creature pet) {
		worldAddEmpti.getWorldRemoveCreate().getCreatures().add(pet);
	}

	public boolean canEnter(int wx, int wy, int wz) {
		return worldAddEmpti.canEnter(wx, wy, wz);
	}
}
