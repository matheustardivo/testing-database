package net.tardivo.testing.database;

import java.util.List;

public interface DummyDAO {

	public List<Dummy> findAll();

	public Dummy getDummy(Long id);

	public void persist(Dummy dummy);

	public Dummy findDummyByName(String name);
}
