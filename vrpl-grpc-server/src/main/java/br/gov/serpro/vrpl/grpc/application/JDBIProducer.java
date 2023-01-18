package br.gov.serpro.vrpl.grpc.application;

import java.sql.SQLException;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.guava.GuavaPlugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class JDBIProducer {

	private static Jdbi jdbi;
	private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl( System.getProperty("DB_URL_CONNECTION_MANDATARIAS", "jdbc:postgresql://10.31.0.134:5432/dbdes_mandatarias_desenv") );
        config.setUsername( System.getProperty("DB_USER_MANDATARIAS", "usr_siconv_p") );
        config.setPassword( System.getProperty("DB_PASSWORD_MANDATARIAS","usr_siconv_p") );
        config.setMaximumPoolSize(Integer.parseInt(System.getProperty("DB_MAX_POOL_SIZE", "10")));
        ds = new HikariDataSource( config );
    }


	private static Jdbi createJdbi() {

		if (jdbi == null) {

			jdbi = Jdbi.create(ds);

			jdbi.installPlugin(new SqlObjectPlugin());
			jdbi.installPlugin(new GuavaPlugin());
			jdbi.installPlugin(new PostgresPlugin());
		}

		return jdbi;
	}


	public static Jdbi getJdbi() {
		return createJdbi();
	}


	public static Boolean hasValidConnection() throws SQLException {

		final int TIME_OUT_EM_SEGUNDOS = 15;

		return getJdbi().withHandle(new HandleCallback<Boolean, SQLException>() {

			@Override
			public Boolean withHandle(Handle handle) throws SQLException {

				return handle.getConnection().isValid(TIME_OUT_EM_SEGUNDOS);
			}

		});

	}

}
