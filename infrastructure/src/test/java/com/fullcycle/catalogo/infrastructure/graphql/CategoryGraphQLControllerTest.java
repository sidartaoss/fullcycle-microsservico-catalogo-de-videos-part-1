package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import com.fullcycle.catalogo.application.category.list.ListCategoryUseCase;
import com.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import com.fullcycle.catalogo.domain.category.ActivationStatus;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.domain.category.Fixture;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import com.fullcycle.catalogo.GraphQLControllerTest;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@GraphQLControllerTest(controllers = CategoryGraphQLController.class)
public class CategoryGraphQLControllerTest {

    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @MockBean
    private SaveCategoryUseCase saveCategoryUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Nested
    @DisplayName("List categories with valid arguments")
    class ListCategoriesWithValidArguments {

        @Test
        void Given_default_arguments_When_calls_categories_Then_should_return_pagination() {
            // Given
            final var expectedCategories =
                    List.of(
                            ListCategoryOutput.from(Fixture.Categories.lives()),
                            ListCategoryOutput.from(Fixture.Categories.aulas()));

            final var expectedPage = 0;
            final var expectedPerPage = 10;
            final var expectedSearch = "";
            final var expectedSort = "name";
            final var expectedDirection = "asc";


            when(listCategoryUseCase.execute(any()))
                    .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedCategories.size(), expectedCategories));

            final var query = """
                    {
                        categories {
                            id
                            name
                        }
                    }
                    """;

            // When
            final var res = graphql.document(query).execute();
            final var actualCategories = res.path("categories")
                    .entityList(ListCategoryOutput.class)
                    .get();

            // Then
            assertTrue(
                    actualCategories.size() == expectedCategories.size()
                            && actualCategories.containsAll(expectedCategories));

            final var captor = ArgumentCaptor.forClass(CategorySearchQuery.class);
            verify(listCategoryUseCase, times(1)).execute(captor.capture());
            final CategorySearchQuery actualQuery = captor.getValue();

            assertEquals(expectedPage, actualQuery.page());
            assertEquals(expectedPerPage, actualQuery.perPage());
            assertEquals(expectedSort, actualQuery.sort());
            assertEquals(expectedDirection, actualQuery.direction());
            assertEquals(expectedSearch, actualQuery.terms());
        }

        @Test
        void Given_custom_arguments_When_calls_categories_Then_should_return_pagination() {
            // Given
            final var expectedCategories =
                    List.of(
                            ListCategoryOutput.from(Fixture.Categories.lives()),
                            ListCategoryOutput.from(Fixture.Categories.aulas()));

            final var expectedPage = 2;
            final var expectedPerPage = 15;
            final var expectedSort = "id";
            final var expectedDirection = "desc";
            final var expectedSearch = "asd";

            when(listCategoryUseCase.execute(any()))
                    .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedCategories.size(), expectedCategories));

            final var query = """
                    query AllCategories($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String) {
                        
                        categories(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction) {
                          id
                          name
                        }
                    }
                    """;

            // When
            final var res = graphql.document(query)
                    .variable("search", expectedSearch)
                    .variable("page", expectedPage)
                    .variable("perPage", expectedPerPage)
                    .variable("sort", expectedSort)
                    .variable("direction", expectedDirection)
                    .execute();
            final var actualCategories = res.path("categories")
                    .entityList(ListCategoryOutput.class)
                    .get();

            // Then
            assertTrue(
                    actualCategories.size() == expectedCategories.size()
                            && actualCategories.containsAll(expectedCategories));

            final var captor = ArgumentCaptor.forClass(CategorySearchQuery.class);
            verify(listCategoryUseCase, times(1)).execute(captor.capture());
            final CategorySearchQuery actualQuery = captor.getValue();

            assertEquals(expectedPage, actualQuery.page());
            assertEquals(expectedPerPage, actualQuery.perPage());
            assertEquals(expectedSort, actualQuery.sort());
            assertEquals(expectedDirection, actualQuery.direction());
            assertEquals(expectedSearch, actualQuery.terms());
        }
    }

    @Nested
    @DisplayName("Save with a valid input")
    class SaveWithValidInput {

        @Test
        void Given_a_valid_input_When_calls_save_category_mutation_Then_should_persist_and_return() {
            // given
            final var expectedId = IdUtils.uuid();
            final var expectedName = "Aulas";
            final var expectedDescription = "A melhor categoria";
            final var expectedIsActive = Boolean.FALSE;
            final var expectedNow = InstantUtils.now().toString();
            final var expectedCreatedAt = expectedNow;
            final var expectedUpdatedAt = expectedNow;
            final var expectedDeletedAt = expectedNow;

            final var input = Map.of(
                    "id", expectedId,
                    "name", expectedName,
                    "description", expectedDescription,
                    "active", expectedIsActive,
                    "createdAt", expectedCreatedAt,
                    "updatedAt", expectedUpdatedAt,
                    "deletedAt", expectedDeletedAt
            );

            final var query = """
                    mutation SaveCategory($input: CategoryInput!) {
                        category: saveCategory(input: $input) {
                            id
                            name
                            description
                        }
                    }
                    """;

            doAnswer(returnsFirstArg()).when(saveCategoryUseCase).execute(any());

            // when
            graphql.document(query)
                            .variable("input", input)
                            .execute()
                            .path("category.id").entity(String.class).isEqualTo(expectedId)
                            .path("category.name").entity(String.class).isEqualTo(expectedName)
                            .path("category.description").entity(String.class).isEqualTo(expectedDescription);
            // then
            final var captor = ArgumentCaptor.forClass(Category.class);
            verify(saveCategoryUseCase, times(1)).execute(captor.capture());
            final Category actualCategory = captor.getValue();

            assertEquals(expectedId, actualCategory.id());
            assertEquals(expectedName, actualCategory.name());
            assertEquals(expectedDescription, actualCategory.description());
            assertEquals(expectedIsActive, ActivationStatus.ACTIVE == actualCategory.activationStatus() ?
                    Boolean.TRUE : Boolean.FALSE);
            assertEquals(expectedCreatedAt, actualCategory.createdAt().toString());
            assertEquals(expectedUpdatedAt, actualCategory.updatedAt().toString());
            assertEquals(expectedDeletedAt, actualCategory.deletedAt().toString());
        }
    }
}
