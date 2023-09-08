package com.fullcycle.catalogo.domain.category;

import com.fullcycle.catalogo.domain.utils.InstantUtils;
import net.datafaker.Faker;

import java.util.UUID;

public final class Fixture {

    private Fixture() {
    }

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
        );
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static final class Categories {
        private static final Category AULAS =
                Category.with(
                        UUID.randomUUID().toString().replace("-", ""),
                        "Aulas",
                        "Conteúdo gravado",
                        ActivationStatus.ACTIVE,
                        InstantUtils.now(),
                        InstantUtils.now()
                );
        private static final Category LIVES =
                Category.with(
                        UUID.randomUUID().toString().replace("-", ""),
                        "Lives",
                        "Conteúdo ao vivo",
                        ActivationStatus.ACTIVE,
                        InstantUtils.now(),
                        InstantUtils.now()
                );

        private static final Category TALKS =
                Category.with(
                        UUID.randomUUID().toString().replace("-", ""),
                        "Talks",
                        "Conteúdo ao vivo",
                        ActivationStatus.INACTIVE,
                        InstantUtils.now(),
                        InstantUtils.now(),
                        InstantUtils.now()
                );

        public static Category aulas() {
            return Category.with(AULAS);
        }

        public static Category lives() {
            return Category.with(LIVES);
        }

        public static Category talks() {
            return Category.with(TALKS);
        }
    }
}
