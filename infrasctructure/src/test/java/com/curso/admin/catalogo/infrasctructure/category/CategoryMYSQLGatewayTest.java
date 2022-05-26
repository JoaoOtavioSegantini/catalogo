package com.curso.admin.catalogo.infrasctructure.category;

import com.curso.admin.catalogo.infrasctructure.MySQLGatewayTest;
import com.curso.admin.catalogo.infrasctructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

@MySQLGatewayTest
public class CategoryMYSQLGatewayTest {

    @Autowired
    private CategoryMYSQLGateway categoryMYSQLGateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    public void testInjectedDependencies(){
        Assertions.assertNotNull(categoryMYSQLGateway);
        Assertions.assertNotNull(repository);
    }


   static class cleanUpExtension implements BeforeEachCallback {

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            final var repositories = SpringExtension
                    .getApplicationContext(context)
                    .getBeansOfType(CrudRepository.class)
                    .values();

            cleanUp(repositories);
        }

        private void cleanUp (final Collection<CrudRepository> repositories) {
            repositories.forEach(CrudRepository::deleteAll);
        }
    }


}
