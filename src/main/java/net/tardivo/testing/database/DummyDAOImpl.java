package net.tardivo.testing.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DummyDAOImpl implements DummyDAO {

	private Logger LOGGER = Logger.getLogger(DummyDAOImpl.class);

	@Autowired
	public DataSource dataSource;

	public List<Dummy> findAll() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select id, name from dummy order by name");

			List<Dummy> dummies = new ArrayList<Dummy>();
			while (rs.next()) {
				dummies.add(new Dummy(rs.getLong("id"), rs.getString("name")));
			}

			return dummies;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e);

		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	public Dummy getDummy(Long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn
					.prepareStatement("select id, name from dummy where id = ?");
			stmt.setLong(1, id);
			rs = stmt.executeQuery();

			Dummy dummy = null;
			if (rs.next()) {
				dummy = new Dummy(rs.getLong("id"), rs.getString("name"));
			}

			return dummy;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e);

		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	public void persist(Dummy dummy) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("insert into dummy (name) values (?)");
			stmt.setString(1, dummy.getName());
			stmt.executeUpdate();

		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e);

		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public Dummy findDummyByName(String name) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn
					.prepareStatement("select id, name from dummy where name = ?");
			stmt.setString(1, name);
			rs = stmt.executeQuery();

			if (rs.next()) {
				return new Dummy(rs.getLong("id"), rs.getString("name"));
			}

			throw new RuntimeException("Dummy not found");
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e);

		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
}
