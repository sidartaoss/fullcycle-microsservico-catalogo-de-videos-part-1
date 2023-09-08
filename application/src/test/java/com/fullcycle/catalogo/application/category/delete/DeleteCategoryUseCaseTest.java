package com.fullcycle.catalogo.application.category.delete;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import com.fullcycle.catalogo.domain.category.Fixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Nested
    @DisplayName("Delete category with a valid identifier")
    class DeleteWithValidIdentifier {

        @Test
        void Given_a_valid_identifier_When_calls_delete_category_Then_should_delete_it() {
            // Given
            final var expectedId = Fixture.Categories.aulas().id();

            doNothing()
                    .when(categoryGateway).deleteById(anyString());

            // When
            Executable validMethodCall = () -> deleteCategoryUseCase.execute(expectedId);

            // Then
            assertDoesNotThrow(validMethodCall);
            verify(categoryGateway, times(1)).deleteById(expectedId);
        }
    }

    @Nested
    @DisplayName("Delete category with an invalid identifier")
    class DeleteWithInvalidIdentifier {

        @Test
        void Given_an_invalid_identifier_When_calls_delete_category_Then_should_be_ok() {
            // Given
            final var anInvalidEmptyId = " ";

            // When
            Executable validMethodCall = () -> deleteCategoryUseCase.execute(anInvalidEmptyId);

            // Then
            assertDoesNotThrow(validMethodCall);
            verify(categoryGateway, never()).deleteById(anyString());
        }
    }

}
