package net.catenax.semantics.adapter.test;

import net.catenax.semantics.framework.IdsConnector;
import net.catenax.semantics.framework.config.*;
import net.catenax.semantics.framework.dsc.DsConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests setting up a default adapter wo special config
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AdapterConfiguration.class })
class AdapterConfigurationTest {

	/**
	 * the actual test code is rather small ;-)
	 * @param context created spring context
	 */
	@Test
	void contextLoads(ApplicationContext context) {
		assertThat(context).isNotNull();
		assertThat(context.getBean(IdsConnector.class)).isNotNull();
		assertThat(context.getBean(IdsConnector.class).getClass()).isEqualTo(DsConnector.class);
	}

}

