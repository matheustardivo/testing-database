package net.tardivo.testing.database;

import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class DatabaseIntegration {
	private String filename;

	@Autowired
	public DataSource dataSource;

	public DatabaseIntegration(String filename) {
		this.filename = filename;
	}

	@Before
	public void setupDB() throws Exception {
		Connection connection = DataSourceUtils.getConnection(dataSource);
		IDatabaseConnection dbUnitConnection = new DatabaseConnection(
				connection);

		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);

		IDataSet dataSet = builder.build(getClass().getClassLoader()
				.getResourceAsStream(filename));

		try {
			DatabaseOperation.REFRESH.execute(dbUnitConnection, dataSet);
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
		}
	}

	@After
	public void cleanDB() throws Exception {
		Connection connection = DataSourceUtils.getConnection(dataSource);
		IDatabaseConnection dbUnitConnection = new DatabaseConnection(
				connection);

		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);

		IDataSet dataSet = builder.build(getClass().getClassLoader()
				.getResourceAsStream(filename));

		try {
			DatabaseOperation.TRUNCATE_TABLE.execute(dbUnitConnection, dataSet);
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
		}
	}
}
