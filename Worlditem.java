package game;


public class Worlditem {
	private Item[][][] items;

	public Item[][][] getItems() {
		return items;
	}

	public void setItems(Item[][][] items) {
		this.items = items;
	}

	public Item item(int x, int y, int z) {
		return items[x][y][z];
	}

	public void remove(int x, int y, int z) {
		items[x][y][z] = null;
	}
}