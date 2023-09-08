package com.fullcycle.catalogo.application.category.save;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.category.ActivationStatus;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import com.fullcycle.catalogo.domain.category.Fixture;
import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SaveCategoryUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private SaveCategoryUseCase saveCategoryUseCase;

    @Nested
    @DisplayName("Save with valid command")
    class SaveWithValidCommand {

        @Test
        void Given_a_valid_command_When_calls_save_category_Then_should_persist_it() {
            // given
            final var aCategory = Fixture.Categories.aulas();

            when(categoryGateway.save(Mockito.any())).thenAnswer(returnsFirstArg());

            // when
            saveCategoryUseCase.execute(aCategory);

            // then
            verify(categoryGateway, times(1)).save(aCategory);
        }
    }

    @Nested
    @DisplayName("Save with invalid command")
    class SaveWithInvalidCommand {

        @Test
        void Given_an_invalid_name_When_calls_save_category_Then_should_return_an_error() {
            // given
            final var expectedErrorMessage = "'name' should not be empty";
            final var expectedErrorCount = 1;
            // when
            final var actualException = assertThrows(DomainException.class, () -> saveCategoryUseCase.execute(Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    " ",
                    "Conteúdo gravado",
                    ActivationStatus.ACTIVE,
                    InstantUtils.now(),
                    InstantUtils.now()
            )));
            // then
            verify(categoryGateway, never()).save(any());
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        }

        @Test
        void Given_an_invalid_id_When_calls_save_category_Then_should_return_an_error() {
            // given
            final var expectedErrorMessage = "'id' should not be empty";
            final var expectedErrorCount = 1;
            // when
            final var actualException = assertThrows(DomainException.class, () -> saveCategoryUseCase.execute(Category.with(
                    " ",
                    "Aulas",
                    "Conteúdo gravado",
                    ActivationStatus.ACTIVE,
                    InstantUtils.now(),
                    InstantUtils.now()
            )));
            // then
            verify(categoryGateway, never()).save(any());
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        }

        @Test
        void Given_an_invalid_null_category_When_calls_save_category_Then_should_return_an_error() {
            // given
            final var expectedErrorMessage = "'aCategory' should not be null";
            final var expectedErrorCount = 1;
            final Category anInvalidNullCategory = null;
            // when
            final var actualException = assertThrows(DomainException.class, () -> saveCategoryUseCase.execute(
                    anInvalidNullCategory));
            // then
            verify(categoryGateway, never()).save(any());
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        }
    }
}
