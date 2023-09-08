package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.category.ActivationStatus;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.domain.category.Fixture;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CategoryElasticsearchGateway categoryElasticsearchGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjection() {
        assertNotNull(categoryRepository);
        assertNotNull(categoryElasticsearchGateway);
    }

    @Nested
    @DisplayName("Save with valid category")
    class SaveWithValidCommand {

        @Test
        void Given_a_valid_category_When_calls_save_category_Then_should_persist_it() {
            // Given
            final var aulas = Fixture.Categories.aulas();
            // When
            final var actualOutput = categoryElasticsearchGateway.save(aulas);
            // Then
            assertEquals(aulas, actualOutput);

            categoryRepository.findById(aulas.id())
                    .ifPresent(persistedCategory -> {
                        assertEquals(aulas.name(), persistedCategory.getName());
                        assertEquals(aulas.description(), persistedCategory.getDescription());
                        assertEquals(isActive(aulas), persistedCategory.isActive());
                        assertEquals(aulas.createdAt(), persistedCategory.getCreatedAt());
                        assertEquals(aulas.updatedAt(), persistedCategory.getUpdatedAt());
                        assertEquals(aulas.deletedAt(), persistedCategory.getDeletedAt());
                    });
        }
    }

    @Nested
    @DisplayName("Delete with valid identifier")
    class DeleteWithValidIdentifier {

        @Test
        void Given_a_valid_identifier_When_calls_delete_category_Then_should_delete_it() {
            // Given
            final var aulas = Fixture.Categories.aulas();

            categoryRepository.save(CategoryDocument.from(aulas));

            final var expectedId = aulas.id();

            assertTrue(categoryRepository.existsById(
                    expectedId));

            // When
            categoryElasticsearchGateway.deleteById(expectedId);

            // Then
            assertFalse(categoryRepository.existsById(expectedId));
        }
    }

    @Nested
    @DisplayName("Delete with invalid identifier")
    class DeleteWithInvalidIdentifier {

        @Test
        void Given_an_invalid_identifier_When_calls_delete_category_Then_should_be_ok() {
            // Given
            final var expectedId = "an-invalid-identifier";

            // When
            Executable validMethodCall = () -> categoryElasticsearchGateway.deleteById(expectedId);

            // Then
            assertDoesNotThrow(validMethodCall);
        }
    }

    @Nested
    @DisplayName("Get with valid identifier")
    class GetWithValidIdentifier {

        @Test
        void Given_a_valid_identifier_When_calls_find_by_id_Then_should_retrieve_it() {
            // Given
            final var talks = Fixture.Categories.talks();

            categoryRepository.save(CategoryDocument.from(talks));

            final var expectedId = talks.id();

            assertTrue(categoryRepository.existsById(
                    expectedId));

            // When & Then
            categoryElasticsearchGateway.findById(expectedId)
                    .ifPresent(actualOutput -> {
                        assertEquals(talks.name(), actualOutput.name());
                        assertEquals(talks.description(), actualOutput.description());
                        assertEquals(talks.activationStatus(), actualOutput.activationStatus());
                        assertEquals(talks.createdAt(), actualOutput.createdAt());
                        assertEquals(talks.updatedAt(), actualOutput.updatedAt());
                        assertEquals(talks.deletedAt(), actualOutput.deletedAt());
                    });
        }
    }

    @Nested
    @DisplayName("Get with invalid identifier")
    class GetWithInvalidIdentifier {

        @Test
        void Given_an_invalid_identifier_When_calls_find_by_id_Then_should_return_empty() {
            // Given
            final var expectedId = "an-invalid-id";

            // When
            final var actualOutput = categoryElasticsearchGateway.findById(expectedId);

            // Then
            assertTrue(actualOutput.isEmpty());
        }
    }

    @Nested
    @DisplayName("List categories with a valid query")
    class ListWithAValidQuery {

        @Test
        void Given_a_valid_query_When_calls_list_cast_members_and_has_no_result_Then_should_return_empty_list() {
            // Given
            final var expectedPage = 0;
            final var expectedPerPage = 10;
            final var expectedTerms = "Algo";
            final var expectedSort = "name";
            final var expectedDirection = "asc";
            final var expectedTotal = 0;

            final var aQuery = new CategorySearchQuery(
                    expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

            // When
            final var actualOutput = categoryElasticsearchGateway.findAll(aQuery);

            // Then
            assertNotNull(actualOutput);
            assertEquals(expectedPage, actualOutput.meta().currentPage());
            assertEquals(expectedPerPage, actualOutput.meta().perPage());
            assertEquals(expectedTotal, actualOutput.meta().total());
            assertEquals(expectedTotal, actualOutput.data().size());
        }

        @ParameterizedTest
        @CsvSource({
                "aul,0,10,1,1,Aulas",
                "liv,0,10,1,1,Lives",
        })
        void Given_valid_terms_When_calls_findAll_Then_should_return_filtered(
                final String expectedTerms,
                final int expectedPage,
                final int expectedPerPage,
                final int expectedItemsCount,
                final long expectedTotal,
                final String expectedName
        ) {
            // Given
            mockCategories();

            final var sort = "name";
            final var direction = "asc";

            final var aQuery = new CategorySearchQuery(
                    expectedPage,
                    expectedPerPage,
                    expectedTerms,
                    sort,
                    direction
            );

            // When
            final var actualResult = categoryElasticsearchGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertFalse(actualResult.data().isEmpty());

            assertEquals(expectedPage, actualResult.meta().currentPage());
            assertEquals(expectedPerPage, actualResult.meta().perPage());
            assertEquals(expectedTotal, actualResult.meta().total());
            assertEquals(expectedItemsCount, actualResult.data().size());
            assertEquals(expectedName, actualResult.data().get(0).name());
        }

        @ParameterizedTest
        @CsvSource({
                "name,asc,0,10,3,3,Aulas",
                "name,desc,0,10,3,3,Talks",
                "created_at,asc,0,10,3,3,Aulas",
        })
        void Given_valid_sort_and_direction_When_calls_findAll_Then_should_return_sorted(
                final String expectedSort,
                final String expectedDirection,
                final int expectedPage,
                final int expectedPerPage,
                final int expectedItemsCount,
                final long expectedTotal,
                final String expectedName
        ) {
            // Given
            mockCategories();

            final String expectedTerms = "";

            final var aQuery = new CategorySearchQuery(
                    expectedPage,
                    expectedPerPage,
                    expectedTerms,
                    expectedSort,
                    expectedDirection
            );

            // When
            final var actualResult = categoryElasticsearchGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertFalse(actualResult.data().isEmpty());

            assertEquals(expectedPage, actualResult.meta().currentPage());
            assertEquals(expectedPerPage, actualResult.meta().perPage());
            assertEquals(expectedTotal, actualResult.meta().total());
            assertEquals(expectedItemsCount, actualResult.data().size());
            assertEquals(expectedName, actualResult.data().get(0).name());
        }

        @ParameterizedTest
        @CsvSource({
                "0,1,1,3,Aulas",
                "1,1,1,3,Lives",
                "2,1,1,3,Talks",
                "3,1,0,3,",
        })
        void Given_valid_page_When_calls_findAll_Then_should_return_paged(
                final int expectedPage,
                final int expectedPerPage,
                final int expectedItemsCount,
                final long expectedTotal,
                final String expectedName
        ) {
            // Given
            mockCategories();

            final String expectedTerms = "";
            final var expectedSort = "name";
            final var expectedDirection = "asc";

            final var aQuery = new CategorySearchQuery(
                    expectedPage,
                    expectedPerPage,
                    expectedTerms,
                    expectedSort,
                    expectedDirection
            );

            // When
            final var actualResult = categoryElasticsearchGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);

            assertEquals(expectedPage, actualResult.meta().currentPage());
            assertEquals(expectedPerPage, actualResult.meta().perPage());
            assertEquals(expectedTotal, actualResult.meta().total());
            assertEquals(expectedItemsCount, actualResult.data().size());

            if (expectedName != null && !expectedName.isBlank()) {
                assertEquals(expectedName, actualResult.data().get(0).name());
            }
        }
    }

    private boolean isActive(final Category aCategory) {
        return ActivationStatus.ACTIVE.equals(aCategory.activationStatus());
    }

    private void mockCategories() {
        categoryRepository.save(CategoryDocument.from(Fixture.Categories.aulas()));
        categoryRepository.save(CategoryDocument.from(Fixture.Categories.talks()));
        categoryRepository.save(CategoryDocument.from(Fixture.Categories.lives()));
    }
}
