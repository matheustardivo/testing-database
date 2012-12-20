package net.tardivo.testing.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-testing.xml")
public class DummyDAOTest extends DatabaseIntegration {

	@Autowired
	public DummyDAO dao;

	public DummyDAOTest() {
		super("dbunit/DummyDAOTest.xml");
	}

	@Test
	public void findAll() {
		List<Dummy> list = dao.findAll();
		assertEquals(4, list.size());
		assertEquals("Name 1", list.iterator().next().getName());
	}

	@Test
	public void getDummy() {
		Dummy dummy = dao.getDummy(4L);
		assertEquals("Name 4", dummy.getName());
	}

	@Test
	public void persist() {
		Dummy dummy = new Dummy(null, "Brand new dummy");
		dao.persist(dummy);

		List<Dummy> list = dao.findAll();
		assertEquals(5, list.size());
	}

	@Test
	public void findDummyByName() {
		Dummy dummy = dao.findDummyByName("Name 3");
		assertTrue(3 == dummy.getId());
		assertEquals("Name 3", dummy.getName());
	}

	@Test(expected = RuntimeException.class)
	public void findDummyByNonexistentName() {
		Dummy dummy = dao.findDummyByName("Brand new dummy");
		assertEquals("Brand new dummy", dummy.getName());
	}
}
