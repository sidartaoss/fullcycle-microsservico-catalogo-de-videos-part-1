# Microsserviço de Catálogo de Vídeos - Parte I

O microsserviço de Catálogo de Vídeos é a aplicação _backend_ responsável por disponibilizar uma série de _APIs_ para a navegação e _playback_ dos vídeos.

A aplicação é um _Backend For Frontend_ (_BFF_), devendo trazer dados específicos de acordo com o tipo de _frontend_ que se conectar nela. Nesse sentido, faz uso do _GraphQL_, através do suporte que o _Spring_ oferece a essa tecnologia de linguagem de consultas.

A aplicação, inclusive, utiliza um banco de dados diferente do banco de dados da aplicação de Administração do Catálogo de Vídeos. Neste caso, é utilizado o _Elasticsearch_, de forma a aproveitar algumas funcionalidades de um banco de dados não-relacional orientado a documentos, como o _full-text search_, possibilitando acesso quase que em tempo real.

É interessante notar que o _design_ da aplicação segue com _Clean Architecture_, mas mais simplificado. Por quê? Porque a aplicação irá atuar apenas como um _query model_, onde os dados são replicados a partir da aplicação _Admin_ do Catálogo de Vídeos.

E como é feito a replicação? A replicação acontece com o _Kafka Connect_.

Assim, dentro da dinâmica do sistema:

1. O _Kafka Connect_ atua sobre o processo de _Change Data Capture_ (_CDC_), ou seja, para todas as atualizações na base de dados _MySQL_ da aplicação _Admin_ do Catálogo de Vídeos, o _Kafka Connect_ irá receber e propagá-las para o _Kafka_;
2. A _API_ do Catálogo de Vídeos será, então, notificada pelo _Kafka_ e fará um _Get_ na aplicação _Admin_ do Catálogo de Vídeos para obter o recurso e, a partir de então, indexá-lo junto ao _Elasticsearch_.

Estão envolvidas, nesta aplicação, tecnologias de:

- Backend

- Java (JDK 17)
- Spring Boot 3
- Gradle (gerenciador de dependências)
- Spring Data & JPA
- Elasticsearch
- Kafka (streaming de eventos)
- Kafka Connect (componente do Kafka)
- Testcontainers Elasticsearch (testes integrados)
- Docker (containers)
