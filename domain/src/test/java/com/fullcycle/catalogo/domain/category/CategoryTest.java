package com.fullcycle.catalogo.domain.category;

import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CategoryTest extends UnitTest {

    @Nested
    class With {

        @Test
        void Given_valid_params_When_call_with_Then_should_instantiate_a_category() {
            // given
            final var expectedId = UUID.randomUUID().toString();
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedActivationStatus = ActivationStatus.ACTIVE;
            final var now = InstantUtils.now();
            final var expectedCreatedAt = now;
            final var expectedUpdatedAt = now;

            // when
            final var actualCategory = Category.with(
                    expectedId, expectedName, expectedDescription, expectedActivationStatus,
                    expectedCreatedAt, expectedUpdatedAt);
            // then
            assertNotNull(actualCategory);
            assertEquals(expectedId, actualCategory.id());
            assertEquals(expectedName, actualCategory.name());
            assertEquals(expectedDescription, actualCategory.description());
            assertEquals(expectedActivationStatus, actualCategory.activationStatus());
            assertEquals(expectedCreatedAt, actualCategory.createdAt());
            assertEquals(expectedUpdatedAt, actualCategory.updatedAt());
            assertNull(actualCategory.deletedAt());
        }

        @Test
        void Given_valid_params_When_call_with_category_Then_should_instantiate_a_category() {
            // given
            final var expectedId = UUID.randomUUID().toString();
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedActivationStatus = ActivationStatus.ACTIVE;
            final var now = InstantUtils.now();
            final var expectedCreatedAt = now;
            final var expectedUpdatedAt = now;

            final var aCategory = Category.with(
                    expectedId, expectedName, expectedDescription, expectedActivationStatus,
                    expectedCreatedAt, expectedUpdatedAt);

            // when
            final var actualCategory = Category.with(aCategory);

            // then
            assertNotNull(actualCategory);
            assertEquals(aCategory.id(), actualCategory.id());
            assertEquals(aCategory.name(), actualCategory.name());
            assertEquals(aCategory.description(), actualCategory.description());
            assertEquals(aCategory.activationStatus(), actualCategory.activationStatus());
            assertEquals(aCategory.createdAt(), actualCategory.createdAt());
            assertEquals(aCategory.updatedAt(), actualCategory.updatedAt());
            assertEquals(aCategory.deletedAt(), actualCategory.deletedAt());
        }
    }

    @Nested
    class Validate {

        @Test
        void Given_an_invalid_null_id_When_call_with_and_validate_Then_should_receive_an_error() {
            // given
            final var expectedErrorMessage = "'id' should not be null";
            final var expectedErrorCount = 1;

            final String expectedInvalidIdAsNull = null;
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedActivationStatus = ActivationStatus.ACTIVE;
            final var now = InstantUtils.now();
            final var expectedCreatedAt = now;
            final var expectedUpdatedAt = now;

            // when
            final var actualException = assertThrows(DomainException.class, () ->
                    Category.with(
                            expectedInvalidIdAsNull, expectedName, expectedDescription, expectedActivationStatus,
                            expectedCreatedAt, expectedUpdatedAt));
            // then
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        }

        @Test
        void Given_an_invalid_empty_id_When_call_with_and_validate_Then_should_receive_an_error() {
            // given
            final var expectedErrorMessage = "'id' should not be empty";
            final var expectedErrorCount = 1;

            final String expectedInvalidIdAsEmpty = " ";
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedActivationStatus = ActivationStatus.ACTIVE;
            final var now = InstantUtils.now();
            final var expectedCreatedAt = now;
            final var expectedUpdatedAt = now;

            // when
            final var actualException = assertThrows(DomainException.class, () ->
                    Category.with(
                            expectedInvalidIdAsEmpty, expectedName, expectedDescription, expectedActivationStatus,
                            expectedCreatedAt, expectedUpdatedAt));
            // then
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        }

        @Test
        void Given_an_invalid_null_name_When_calls_with_and_validate_Then_should_receive_an_error() {
            // given
            final var expectedErrorMessage = "'name' should not be null";
            final var expectedErrorCount = 1;

            final var expectedId = UUID.randomUUID().toString();
            final String expectedNameAsNull = null;
            final var expectedDescription = "A categoria mais assistida";
            final var expectedActivationStatus = ActivationStatus.ACTIVE;
            final var now = InstantUtils.now();
            final var expectedCreatedAt = now;
            final var expectedUpdatedAt = now;

            // when
            final var actualException = assertThrows(DomainException.class, () ->
                    Category.with(
                            expectedId, expectedNameAsNull, expectedDescription, expectedActivationStatus,
                            expectedCreatedAt, expectedUpdatedAt));
            // then
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        }

        @Test
        void Given_an_invalid_empty_name_When_calls_with_and_validate_Then_should_receive_an_error() {
            // given
            final var expectedErrorMessage = "'name' should not be empty";
            final var expectedErrorCount = 1;

            final var expectedId = UUID.randomUUID().toString();
            final String expectedNameAsEmpty = " ";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedActivationStatus = ActivationStatus.ACTIVE;
            final var now = InstantUtils.now();
            final var expectedCreatedAt = now;
            final var expectedUpdatedAt = now;

            // when
            final var actualException = assertThrows(DomainException.class, () ->
                    Category.with(
                            expectedId, expectedNameAsEmpty, expectedDescription, expectedActivationStatus,
                            expectedCreatedAt, expectedUpdatedAt));
            // then
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        }

        @Test
        void Given_a_valid_empty_description_When_calls_with_and_validate_then_should_not_throw_error() {
            // given
            final var expectedId = UUID.randomUUID().toString();
            final var expectedName = "Filmes";
            final var expectedDescriptionAsEmpty = " ";
            final var expectedActivationStatus = ActivationStatus.ACTIVE;
            final var now = InstantUtils.now();
            final var expectedCreatedAt = now;
            final var expectedUpdatedAt = now;
            // when
            Executable validMethodCall = () -> Category.with(
                    expectedId, expectedName, expectedDescriptionAsEmpty, expectedActivationStatus,
                    expectedCreatedAt, expectedUpdatedAt);
            // then
            assertDoesNotThrow(validMethodCall);
        }
    }
}