# Sistema de Locação de Veículos — API REST

**Disciplina:** PPGTI 1004 - Desenvolvimento Web II  
**Atividade:** 03 - Sistema de Persistência Híbrida com Segurança  
**Tecnologias:** Java 17, Spring Boot 3.5.14, Spring Data JPA, Spring Security 6, JWT, H2 Database

---

## 1. Domínio Escolhido

Sistema de Locação de Veículos para gerenciamento de frota, clientes e contratos de locação, com autenticação via JWT, controle de acesso por perfil de usuário e rastreabilidade completa via log de auditoria automático com identificação do usuário responsável.

O domínio foi mantido em continuidade com as Atividades 01 e 02, evoluindo agora com a adição da camada de segurança via Spring Security 6 e autenticação stateless com tokens JWT.

### Justificativa

O domínio de locação de veículos justifica naturalmente os três níveis de acesso exigidos pela atividade:

- O **MASTER** representa o administrador da locadora — responsável pelo cadastro da frota, clientes e configurações do sistema.
- O **CONTRIBUTOR** representa o atendente operacional — realiza locações, atualiza registros e gerencia o ciclo de atendimento.
- O **AUDITOR** representa o supervisor ou auditor de conformidade — visualiza todos os dados sem capacidade de alteração.

---

## 2. Arquitetura de Segurança

### 2.1 Roles do Domínio

| Role | Perfil | Capacidades |
|---|---|---|
| `ROLE_MASTER` | Administrador | Acesso total — cria, edita, exclui e visualiza tudo |
| `ROLE_CONTRIBUTOR` | Atendente / Operador | Cria e edita recursos operacionais — não cadastra frota nem clientes |
| `ROLE_AUDITOR` | Auditor / Supervisor | Somente leitura — visualiza tudo, não altera nada |

### 2.2 Matriz de Permissões

| Endpoint | Método | Nível | MASTER | CONTRIBUTOR | AUDITOR |
|---|---|---|---|---|---|
| `/info` | GET | Público | ✅ | ✅ | ✅ |
| `/auth/login` | POST | Público | ✅ | ✅ | ✅ |
| `/auth/registro` | POST | Público | ✅ | ✅ | ✅ |
| `/veiculos` | GET | 1 | ✅ | ✅ | ✅ |
| `/veiculos/{id}` | GET | 1 | ✅ | ✅ | ✅ |
| `/veiculos/disponiveis` | GET | 1 | ✅ | ✅ | ✅ |
| `/acessorios` | GET | 1 | ✅ | ✅ | ✅ |
| `/acessorios/{id}` | GET | 1 | ✅ | ✅ | ✅ |
| `/clientes` | GET | 1 | ✅ | ✅ | ✅ |
| `/clientes/{id}` | GET | 1 | ✅ | ✅ | ✅ |
| `/locacoes` | GET | 1 | ✅ | ✅ | ✅ |
| `/locacoes/{id}` | GET | 1 | ✅ | ✅ | ✅ |
| `/locacoes/cliente/{id}` | GET | 1 | ✅ | ✅ | ✅ |
| `/veiculos/{id}` | PUT | 2 | ✅ | ✅ | ❌ |
| `/acessorios/{id}` | PUT | 2 | ✅ | ✅ | ❌ |
| `/clientes/{id}` | PUT | 2 | ✅ | ✅ | ❌ |
| `/locacoes` | POST | 2 | ✅ | ✅ | ❌ |
| `/locacoes/{id}/encerrar` | DELETE | 2 | ✅ | ✅ | ❌ |
| `/veiculos` | POST | 3 | ✅ | ❌ | ❌ |
| `/veiculos/{id}` | DELETE | 3 | ✅ | ❌ | ❌ |
| `/acessorios` | POST | 3 | ✅ | ❌ | ❌ |
| `/acessorios/{id}` | DELETE | 3 | ✅ | ❌ | ❌ |
| `/clientes` | POST | 3 | ✅ | ❌ | ❌ |
| `/clientes/{id}` | DELETE | 3 | ✅ | ❌ | ❌ |

### 2.3 Mecanismo de Autenticação

A autenticação é realizada via JWT (JSON Web Token) stateless. O cliente realiza login via `POST /auth/login` e recebe um token que deve ser enviado no header `Authorization: Bearer <token>` em todas as requisições autenticadas. O token expira em 24 horas.

**Status HTTP de segurança:**

| Situação | Status |
|---|---|
| Token ausente ou inválido | 401 Unauthorized |
| Token válido mas role insuficiente | 403 Forbidden |

---

## 3. Entidades e Relacionamentos

### 3.1 Diagrama de Relacionamentos

![Diagrama de entidades](diagrama-entidades.svg)

### 3.2 Descrição dos Relacionamentos

A coluna **Cascade** indica quais operações JPA realizadas na entidade de origem são propagadas automaticamente para a entidade de destino.

| Relacionamento | Entidades | Tipo | Cascade |
|---|---|---|---|
| Um veículo possui muitos acessórios | Veiculo → Acessorio | Many-to-Many | PERSIST, MERGE |
| Um acessório pertence a muitos veículos | Acessorio → Veiculo | Many-to-Many (inverso) | — |
| Um veículo possui muitas locações | Veiculo → Locacao | One-to-Many | ALL + orphanRemoval |
| Uma locação pertence a um veículo | Locacao → Veiculo | Many-to-One | — |
| Um cliente possui muitas locações | Cliente → Locacao | One-to-Many | ALL + orphanRemoval |
| Uma locação pertence a um cliente | Locacao → Cliente | Many-to-One | — |

### 3.3 Atributos por Entidade

| Entidade | Atributos |
|---|---|
| Usuario | id, username, password (BCrypt), role (enum) |
| Veiculo | id, marca, modelo, placa (unique), categoria (enum), disponivel, valorDiaria |
| Acessorio | id, nome, descricao |
| Cliente | id, nome, cpf (unique), email (unique), telefone |
| Locacao | id, dataInicio, dataFim, valorTotal, cliente (FK), veiculo (FK) |
| LogAuditoria | id, entidade, entidadeId, operacao, usuarioResponsavel, dataHora |

---

## 4. Arquitetura de Persistência

### 4.1 Duas Bases de Dados (H2 em memória)

| Base | Nome | Entidades | Finalidade |
|---|---|---|---|
| Base A (primary) | baseA | Usuario, Veiculo, Acessorio, Cliente, Locacao | Dados principais |
| Base B (audit) | baseB | LogAuditoria | Rastreabilidade de operações |

A configuração das duas datasources está declarada explicitamente no arquivo `src/main/resources/application.properties`, com `EntityManagerFactory` e `TransactionManager` distintos para cada base.

### 4.2 Log de Auditoria Automático

Toda operação CREATE, UPDATE ou DELETE nas entidades principais dispara automaticamente uma entrada na Base B com os seguintes dados:

| Campo | Descrição |
|---|---|
| entidade | Nome da classe afetada (ex: Veiculo, Cliente) |
| entidadeId | ID do registro modificado |
| operacao | Tipo de operação: CREATE, UPDATE ou DELETE |
| usuarioResponsavel | Username extraído do token JWT via SecurityContextHolder |
| dataHora | Timestamp exato da operação |

---

## 5. Exemplos de JSON — Entidade vs DTO

Esta seção demonstra o desacoplamento entre a entidade de banco de dados e a interface da API.

### 5.1 Entidade `Usuario` (banco de dados — nunca exposta)

```json
{
  "id": 1,
  "username": "master",
  "password": "$2a$10$hashed...",
  "role": "ROLE_MASTER"
}
```

### 5.2 `UsuarioResponseDTO` (retornado pela API)

```json
{
  "id": 1,
  "username": "master",
  "role": "ROLE_MASTER"
}
```

> `password` omitido — dado sensível nunca exposto na saída.

### 5.3 `VeiculoRequestDTO` (entrada — POST/PUT)

```json
{
  "marca": "Toyota",
  "modelo": "Corolla",
  "placa": "ABC-1234",
  "categoria": "ECONOMICO",
  "valorDiaria": 80.0,
  "acessorioIds": [1, 2]
}
```

### 5.4 `VeiculoResponseDTO` (saída)

```json
{
  "id": 1,
  "marca": "Toyota",
  "modelo": "Corolla",
  "placa": "ABC-1234",
  "categoria": "ECONOMICO",
  "disponivel": true,
  "valorDiaria": 80.0,
  "valorDiariaComTaxa": 84.0,
  "acessorios": [
    { "id": 1, "nome": "GPS", "descricao": "Navegador satelital embutido" },
    { "id": 2, "nome": "Cadeirinha Infantil", "descricao": "Assento de segurança" }
  ]
}
```

> `valorDiariaComTaxa` é um campo calculado — não existe na entidade, gerado no DTO.

### 5.5 `LocacaoRequestDTO` (entrada)

```json
{
  "clienteId": 1,
  "veiculoId": 1,
  "dataInicio": "2026-05-10",
  "dataFim": "2026-05-15"
}
```

### 5.6 `LocacaoResponseDTO` (saída)

```json
{
  "id": 1,
  "dataInicio": "2026-05-10",
  "dataFim": "2026-05-15",
  "diasLocacao": 5,
  "valorTotal": 900.0,
  "nomeCliente": "João Silva",
  "modeloVeiculo": "Corolla",
  "placaVeiculo": "ABC-1234"
}
```

> `valorTotal` e `diasLocacao` são calculados pelo sistema — não informados pelo cliente.

---

## 6. Regras de Negócio

| Regra | Entidade | Comportamento |
|---|---|---|
| Valor mínimo por categoria | Veiculo | ECONOMICO ≥ R$ 50 / SUV ≥ R$ 150 / PREMIUM ≥ R$ 300 |
| Disponibilidade | Veiculo | Bloqueio de locação se `disponivel = false` |
| CPF único | Cliente | Rejeita cadastro com CPF já existente |
| Username único | Usuario | Rejeita registro com username já existente |
| Data de fim válida | Locacao | `dataFim` deve ser posterior a `dataInicio` |
| Valor calculado | Locacao | `valorTotal = dias × valorDiaria` (calculado pelo sistema) |
| Devolução | Locacao | Encerramento restaura `disponivel = true` no veículo |

---

## 7. Endpoints

### Autenticação (Público)

| Método | Rota | Ação |
|---|---|---|
| GET | /info | Informações do sistema |
| POST | /auth/login | Login — retorna token JWT |
| POST | /auth/registro | Registro de novo usuário |

### Acessórios

| Método | Rota | Ação | Nível |
|---|---|---|---|
| POST | /acessorios | Cadastrar | 3 |
| GET | /acessorios | Listar todos | 1 |
| GET | /acessorios/{id} | Buscar por ID | 1 |
| PUT | /acessorios/{id} | Atualizar | 2 |
| DELETE | /acessorios/{id} | Deletar | 3 |

### Veículos

| Método | Rota | Ação | Nível |
|---|---|---|---|
| POST | /veiculos | Cadastrar | 3 |
| GET | /veiculos | Listar todos | 1 |
| GET | /veiculos/{id} | Buscar por ID com acessórios (JOIN FETCH) | 1 |
| GET | /veiculos/disponiveis?categoria= | Filtrar disponíveis | 1 |
| PUT | /veiculos/{id} | Atualizar | 2 |
| DELETE | /veiculos/{id} | Deletar | 3 |

### Clientes

| Método | Rota | Ação | Nível |
|---|---|---|---|
| POST | /clientes | Cadastrar | 3 |
| GET | /clientes | Listar todos | 1 |
| GET | /clientes/{id} | Buscar por ID com locações (JOIN FETCH) | 1 |
| PUT | /clientes/{id} | Atualizar | 2 |
| DELETE | /clientes/{id} | Deletar | 3 |

### Locações

| Método | Rota | Ação | Nível |
|---|---|---|---|
| POST | /locacoes | Criar locação | 2 |
| GET | /locacoes | Listar todas | 1 |
| GET | /locacoes/{id} | Buscar por ID com cliente e veículo (JOIN FETCH) | 1 |
| GET | /locacoes/cliente/{clienteId} | Locações por cliente | 1 |
| DELETE | /locacoes/{id}/encerrar | Encerrar locação e liberar veículo | 2 |

> **Ausência do PUT em Locação:** uma locação é um contrato imutável após criação. A única operação de modificação válida é o encerramento via `DELETE /locacoes/{id}/encerrar`, que remove o registro e restaura a disponibilidade do veículo. Trata-se de uma decisão de domínio, não omissão técnica.

---

## 8. Como Executar

### Pré-requisitos

| Requisito | Versão mínima |
|---|---|
| Java | 17 |
| Maven | 3.8 |

### Passos

```bash
# Extrair o projeto e acessar o diretório
cd veiculos3

# Compilar e executar
./mvnw spring-boot:run
```

### Confirmação no console

```
==============================================
  USUÁRIOS INICIALIZADOS COM SUCESSO
==============================================
  MASTER      → usuario: master      | senha: master123
  CONTRIBUTOR → usuario: contributor | senha: contributor123
  AUDITOR     → usuario: auditor     | senha: auditor123
==============================================
Started Veiculos3Application on port 8080
```

### Acessos

| Recurso | URL | Observação |
|---|---|---|
| API | http://localhost:8080 | Porta padrão |
| H2 Console — Base A | http://localhost:8080/h2-console | JDBC URL: `jdbc:h2:mem:baseA` |
| H2 Console — Base B | http://localhost:8080/h2-console | JDBC URL: `jdbc:h2:mem:baseB` |

**Credenciais H2 (ambas as bases):** usuário `sa`, senha em branco.

---

## 9. Roteiro de Testes (Postman)

Importe o arquivo `locacao-veiculos3.postman_collection.json` no Postman. Configure um Environment com as variáveis `token_master`, `token_contributor` e `token_auditor` — preenchidas após os logins da Fase 2.

### Fase 1 — Endpoints Públicos

| # | Requisição | Token | Resultado esperado |
|---|---|---|---|
| 1 | Info do Sistema | Nenhum | 200 — dados do sistema sem autenticação |
| 25 | GET /veiculos sem token | Nenhum | 401 — token ausente |

### Fase 2 — Autenticação

| # | Requisição | Ação após Send |
|---|---|---|
| 2 | Login MASTER | Copiar token → salvar em `token_master` |
| 3 | Login CONTRIBUTOR | Copiar token → salvar em `token_contributor` |
| 4 | Login AUDITOR | Copiar token → salvar em `token_auditor` |
| 5 | Registro Novo Usuário | 201 — senha ausente no response |

### Fase 3 — Popular dados com MASTER

| # | Requisição | Token | Resultado esperado |
|---|---|---|---|
| 6 | Cadastrar Acessório GPS | MASTER | 201 — `id: 1` |
| 9 | Cadastrar Acessório Cadeirinha | MASTER | 201 — `id: 2` |
| 10 | Cadastrar Veículo Corolla (acessorioIds: [1,2]) | MASTER | 201 — acessórios vinculados |
| 12 | Cadastrar Veículo SUV | MASTER | 201 — `id: 2` |
| 17 | Cadastrar Cliente João | MASTER | 201 — `totalLocacoes: 0` |

### Fase 4 — Validar bloqueios 403

| # | Requisição | Token | Resultado esperado |
|---|---|---|---|
| 7 | Cadastrar Acessório | CONTRIBUTOR | 403 — role insuficiente |
| 11 | Cadastrar Veículo | CONTRIBUTOR | 403 — role insuficiente |
| 15 | Atualizar Veículo | AUDITOR | 403 — role insuficiente |
| 16 | Deletar Veículo | AUDITOR | 403 — role insuficiente |
| 18 | Cadastrar Cliente | CONTRIBUTOR | 403 — role insuficiente |
| 21 | Criar Locação | AUDITOR | 403 — role insuficiente |
| 24 | Encerrar Locação | AUDITOR | 403 — role insuficiente |

### Fase 5 — Validar acessos permitidos por nível

| # | Requisição | Token | Resultado esperado |
|---|---|---|---|
| 8 | Listar Acessórios | AUDITOR | 200 — leitura permitida |
| 13 | Listar Veículos | AUDITOR | 200 — leitura permitida |
| 14 | Atualizar Veículo | CONTRIBUTOR | 200 — atualização permitida |
| 19 | Listar Clientes | CONTRIBUTOR | 200 — leitura permitida |
| 20 | Criar Locação (cliente 1, veículo 1) | CONTRIBUTOR | 201 — `valorTotal` calculado |
| 22 | Listar Locações | AUDITOR | 200 — leitura permitida |
| 23 | Encerrar Locação | CONTRIBUTOR | 204 — veículo liberado |

### Fase 6 — Verificar auditoria (Base B)

Acesse o H2 Console com JDBC URL `jdbc:h2:mem:baseB` e execute:

```sql
SELECT * FROM LOG_AUDITORIA ORDER BY DATA_HORA DESC;
```

Resultado esperado — todas as operações registradas com o username responsável:

| ENTIDADE | OPERACAO | USUARIO_RESPONSAVEL |
|---|---|---|
| Locacao | DELETE | contributor |
| Locacao | CREATE | contributor |
| Cliente | CREATE | master |
| Veiculo | UPDATE | contributor |
| Veiculo | CREATE | master |
| Acessorio | CREATE | master |

---

## 10. Arquivos da Entrega

| Arquivo | Descrição |
|---|---|
| `src/` | Código-fonte completo organizado por camadas |
| `pom.xml` | Dependências do projeto (inclui jjwt 0.12.3) |
| `src/main/resources/application.properties` | Configuração das duas datasources e JWT |
| `locacao-veiculos3.postman_collection.json` | Collection para teste dos endpoints |
| `diagrama-entidades.svg` | Diagrama de entidades e relacionamentos |
| `README.md` | Este documento |

---

## 11. Estrutura do Projeto

```
src/main/java/com/locacao/veiculos3/
├── config/         # DataSourceConfig (primary/audit), SecurityConfig, DataInitializer
├── controller/     # InfoController, AuthController, VeiculoController,
│                     AcessorioController, ClienteController, LocacaoController
├── dto/
│   ├── auth/       # LoginRequestDTO, LoginResponseDTO,
│   │                 RegistroRequestDTO, UsuarioResponseDTO
│   └── (raiz)      # VeiculoRequestDTO, VeiculoResponseDTO,
│                     ClienteRequestDTO, ClienteResponseDTO,
│                     AcessorioRequestDTO, AcessorioResponseDTO,
│                     LocacaoRequestDTO, LocacaoResponseDTO
├── enums/          # CategoriaVeiculo, RoleUsuario
├── exception/      # RegraNegocioException, RecursoNaoEncontradoException,
│                     GlobalExceptionHandler
├── model/
│   ├── primary/    # Usuario, Veiculo, Acessorio, Cliente, Locacao
│   └── audit/      # LogAuditoria
├── repository/
│   ├── primary/    # UsuarioRepository, VeiculoRepository,
│   │                 AcessorioRepository, ClienteRepository, LocacaoRepository
│   └── audit/      # LogAuditoriaRepository
├── security/       # JwtUtil, JwtFilter, UserDetailsServiceImpl
└── service/        # AuthService, AuditoriaService, VeiculoService,
                      AcessorioService, ClienteService, LocacaoService
```
