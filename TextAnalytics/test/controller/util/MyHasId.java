package controller.util;

class MyHasId implements HasId {
	final Integer id;

	public MyHasId(int id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}