package com.apiv1.ominiModa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OminiModaApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {
	}

	@Test
	void verifyDatabaseTablesExist() throws Exception {
		List<String> tables = new ArrayList<>();
		try (Connection connection = dataSource.getConnection()) {
			ResultSet rs = connection.getMetaData().getTables(null, "PUBLIC", null, new String[]{"TABLE"});
			while (rs.next()) {
				tables.add(rs.getString("TABLE_NAME").toUpperCase());
			}
		}
		System.out.println("Tablas en H2: " + tables);

		List<String> expectedTables = List.of(
			"CATEGORIAS",
			"CLIENTES",
			"DETALLE_VENTA",
			"ESTADOS",
			"PRODUCTOS",
			"PROMOCIONES",
			"PROVEEDORES",
			"PROVEEDORES_PRODUCTOS",
			"ROLES",
			"TIPO_CLIENTE",
			"USUARIO_ROL",
			"USUARIOS",
			"VENTAS"
		);

		for (String expected : expectedTables) {
			Assertions.assertTrue(tables.contains(expected), "Falta la tabla: " + expected);
		}
	}
}

