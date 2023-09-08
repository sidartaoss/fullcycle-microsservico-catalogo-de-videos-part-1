package com.fullcycle.catalogo.application.category.list;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import com.fullcycle.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.domain.category.Fixture;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ListCategoryUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private ListCategoryUseCase listCategoryUseCase;

    @Nested
    @DisplayName("List categories with a valid query")
    class ListWithAValidQuery {

        @Test
        void Given_a_valid_query_When_calls_list_categories_Then_should_return_categories() {
            // Given
            final var categories = List.of(
                    Fixture.Categories.lives(),
                    Fixture.Categories.aulas()
            );
            final var expectedItems = categories.stream()
                    .map(ListCategoryOutput::from)
                    .toList();

            final var expectedPage = 0;
            final var expectedPerPage = 10;
            final var expectedTerms = "Algo";
            final var expectedSort = "name";
            final var expectedDirection = "asc";
            final var expectedItemsCount = 2;

            final var aQuery = new CategorySearchQuery(
                    expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

            final var aPagination =
                    new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

            when(categoryGateway.findAll(any())).thenReturn(aPagination);

            // when
            final var actualOutput = listCategoryUseCase.execute(aQuery);

            // then
            assertEquals(expectedPage, actualOutput.meta().currentPage());
            assertEquals(expectedPerPage, actualOutput.meta().perPage());
            assertEquals(expectedItemsCount, actualOutput.meta().total());
            assertTrue(expectedItems.size() == actualOutput.data().size()
                    && expectedItems.containsAll(actualOutput.data()));
        }
    }
}
