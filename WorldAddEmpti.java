package game;


import java.util.List;
import java.util.ArrayList;

public class WorldAddEmpti {
	private WorldRemoveCreate worldRemoveCreate = new WorldRemoveCreate();
	private Tile[][][] tiles;
	private int width;
	private int height;
	private int depth;

	public WorldRemoveCreate getWorldRemoveCreate() {
		return worldRemoveCreate;
	}

	public void setTiles(Tile[][][] tiles) {
		this.tiles = tiles;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Creature creature(int x, int y, int z) {
		for (Creature c : worldRemoveCreate.getCreatures()) {
			if (c.x == x && c.y == y && c.z == z)
				return c;
		}
		return null;
	}

	public void addAtEmptyLocation(Creature creature, int z) {
		int x;
		int y;
		do {
			x = (int) (Math.random() * width);
			y = (int) (Math.random() * height);
		} while (!tile(x, y, z).isGround() || creature(x, y, z) != null);
		creature.x = x;
		creature.y = y;
		creature.z = z;
		worldRemoveCreate.getCreatures().add(creature);
	}

	public void dig(int x, int y, int z) {
		if (tile(x, y, z).isDiggable())
			tiles[x][y][z] = Tile.FLOOR;
	}

	public Tile tile(int x, int y, int z) {
		if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth)
			return Tile.BOUNDS;
		else
			return tiles[x][y][z];
	}

	public void remove(Item item, Worlditem thisWorlditem) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < depth; z++) {
					if (thisWorlditem.getItems()[x][y][z] == item) {
						thisWorlditem.getItems()[x][y][z] = null;
						return;
					}
				}
			}
		}
	}

	public void addAtEmptyLocation(Item item, int depth, Worlditem thisWorlditem) {
		int x;
		int y;
		do {
			x = (int) (Math.random() * width);
			y = (int) (Math.random() * height);
		} while (!tile(x, y, depth).isGround() || thisWorlditem.item(x, y, depth) != null);
		thisWorlditem.getItems()[x][y][depth] = item;
	}

	public boolean addAtEmptySpace(Item item, int x, int y, int z, Worlditem thisWorlditem) {
		if (item == null)
			return true;
		List<Point> points = new ArrayList<>();
		List<Point> checked = new ArrayList<>();
		points.add(new Point(x, y, z));
		while (!points.isEmpty()) {
			Point p = points.remove(0);
			checked.add(p);
			if (!tile(p.x, p.y, p.z).isGround())
				continue;
			if (thisWorlditem.getItems()[p.x][p.y][p.z] == null) {
				thisWorlditem.getItems()[p.x][p.y][p.z] = item;
				Creature c = this.creature(p.x, p.y, p.z);
				if (c != null)
					c.notify("A %s lands between your feet.", c.nameOf(item));
				return true;
			} else {
				List<Point> neighbors = p.neighbors8();
				neighbors.removeAll(checked);
				points.addAll(neighbors);
			}
		}
		return false;
	}

	public boolean canEnter(int wx, int wy, int wz) {
		return tile(wx, wy, wz).isGround() && creature(wx, wy, wz) == null;
	}
}