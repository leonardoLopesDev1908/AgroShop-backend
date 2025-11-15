AgroShop – Descrição do Back-end

O back-end do AgroShop foi desenvolvido em Java com Spring Boot, seguindo uma arquitetura limpa, modular e orientada a boas práticas. A API REST oferece endpoints bem estruturados, seguros e escaláveis, permitindo que o front-end consuma os dados de forma confiável.

A aplicação é organizada em camadas:

Model – Representa entidades como Usuário, Produto, Carrinho, Pedido e Avaliação.

Repository – Consulta e manipula tabelas no banco usando Spring Data JPA.

Service – Contém as regras de negócio e validações.

Controller – Exposição dos endpoints REST usados pelo front-end.

DTOs e Mappers – Transferência de dados otimizada, evitando exposição indevida de entidades.

O sistema oferece funcionalidades completas de um e-commerce:

Cadastro e gerenciamento de produtos, incluindo imagens, preço, estoque e categorias.

Autenticação e autorização com Spring Security, garantindo acesso seguro a funcionalidades protegidas.

Sistema de avaliações: usuários autenticados podem registrar título, nota, comentário e data; o produto exibe todas as avaliações recebidas.

Geração automática de código público para cada avaliação, facilitando buscas e organização.

Controle de usuários, com papéis como ADMIN e CLIENTE, permitindo recursos exclusivos para administradores.

Dashboard de vendas, expondo dados agregados para relatórios e estatísticas.

Persistência com banco relacional usando MySQL, com mapeamento correto via JPA.

Tratamento global de erros, garantindo respostas consistentes para o front-end.

Toda a API segue padrões REST e retorna dados em JSON. O uso de DTOs, MapStruct, Service Layer e validações com Bean Validation garantem um código limpo, seguro e fácil de manter.
O back-end foi pensado para ser escalável, robusto e pronto para suportar o crescimento natural de um sistema de vendas online.
