#  MotoApp API - Sistema de Gestão Financeira para Motoristas

## 📋 Sobre o Projeto
API RESTful desenvolvida em **Spring Boot** para gestão financeira e operacional de motoristas de aplicativo (Uber, 99, iFood, etc). O sistema permite registrar corridas, controlar gastos, calcular lucros automaticamente e gerar relatórios detalhados através de um dashboard completo.

**Objetivo:** Facilitar o controle financeiro de motoristas autônomos, fornecendo ferramentas profissionais para análise de rentabilidade e tomada de decisão.

## 🚀 Tecnologias
- **Java 17** com **Spring Boot 3.1.5**
- **Spring Data JPA** + **Hibernate**
- **MySQL 8.0** com armazenamento UUID como BINARY(16)
- **Lombok** para redução de boilerplate
- **Maven** para gerenciamento de dependências

## 📊 Modelo de Dados

### 🧑‍💼 **Usuario**
```java
@Id UUID usuarioId     // Identificador único
String nome            // Nome completo
String email           // Email (único)
String senha           // Hash bcrypt
```

### 📝 **Registro** (Corrida/Turno)
```java
@Id UUID registroId    // ID do registro
@ManyToOne Usuario usuario  // Motorista

// Informações básicas
LocalDate data
String plataforma      // Uber, 99, iFood, etc
String plataformaOutro // Se escolher "Outro"

// Métricas
int horasTrabalhadas
int corridasRealizadas
BigDecimal valorBruto

// Quilometragem
int kmInicial
int kmFinal            // Calcula kmRodados automaticamente

// Despesas
BigDecimal combustivel
BigDecimal alimentacao
BigDecimal gastosAdicionais

// Informações
String observacao
BigDecimal lucro       // Calculado automaticamente

// Método calculado
public int getKmRodados() {
    return kmFinal - kmInicial;
}
```

## 🔗 Endpoints da API

### 🔐 Autenticação
| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| `POST` | `/usuarios/login` | Login de usuário | `{email, senha}` |
| `POST` | `/usuarios/post` | Criar novo usuário | `{nome, email, senha}` |

### 👤 Usuários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/usuarios/{id}` | Buscar usuário por ID |
| `GET` | `/usuarios/TODOS` | Listar todos usuários |
| `PUT` | `/usuarios/alter/{id}` | Atualizar usuário |
| `DELETE` | `/usuarios/delete/{id}` | Deletar usuário |

### 📝 Registros (Corridas)
| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| `POST` | `/registros/{usuarioId}` | Criar novo registro | `Registro completo` |
| `GET` | `/registros/todos/{usuarioId}` | Listar registros do usuário | - |
| `GET` | `/registros` | Listar todos registros | - |
| `PUT` | `/registros/alter/{registroId}` | Atualizar registro | `Campos para atualizar` |
| `DELETE` | `/registros/delete/{registroId}` | Deletar registro | - |

### 📊 Dashboard & Análises
| Método | Endpoint | Descrição | Parâmetros |
|--------|----------|-----------|------------|
| `GET` | `/dashboard/estatisticas/{usuarioId}` | Cards com estatísticas | - |
| `GET` | `/dashboard/corridas-por-dia/{usuarioId}` | Gráfico de corridas por dia | `inicio, fim` (YYYY-MM-DD) |
| `GET` | `/dashboard/lucro-gastos/{usuarioId}` | Gráfico lucro vs gastos | `inicio, fim` (YYYY-MM-DD) |
| `GET` | `/dashboard/plataformas/{usuarioId}` | Estatísticas por plataforma | - |
| `GET` | `/dashboard/ultimos-registros/{usuarioId}` | Últimos registros | `limite` (opcional, padrão: 5) |

## 🧮 Cálculos Automáticos

### 💰 **Lucro**
```java
lucro = valorBruto - (combustivel + alimentacao + gastosAdicionais)
```
*Calculado automaticamente antes de salvar via `@PrePersist` e `@PreUpdate`*

### 🛣️ **Km Rodados**
```java
kmRodados = kmFinal - kmInicial
```
*Calculado sob demanda via método getter `getKmRodados()`*

## 📊 Exemplos de Uso

### 📝 Criar um Registro
**URL:** `POST http://localhost:8080/registros/{usuarioId}`

```json
{
  "data": "2024-01-15",
  "plataforma": "Uber",
  "horasTrabalhadas": 8,
  "corridasRealizadas": 12,
  "valorBruto": 250.00,
  "kmInicial": 10000,
  "kmFinal": 10120,
  "combustivel": 40.00,
  "alimentacao": 20.00,
  "gastosAdicionais": 10.00,
  "observacao": "Turno da manhã"
}
```

**Resposta:**
```json
{
  "registroId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "data": "2024-01-15",
  "plataforma": "Uber",
  "horasTrabalhadas": 8,
  "corridasRealizadas": 12,
  "valorBruto": 250.00,
  "kmInicial": 10000,
  "kmFinal": 10120,
  "combustivel": 40.00,
  "alimentacao": 20.00,
  "gastosAdicionais": 10.00,
  "observacao": "Turno da manhã",
  "lucro": 180.00,
  "kmRodados": 120
}
```

### 📊 Dashboard Estatísticas
**URL:** `GET http://localhost:8080/dashboard/estatisticas/{usuarioId}`

**Resposta:**
```json
{
  "totalCorridas": 32,
  "lucroTotal": "R$800.00",
  "gastosTotais": "R$122.00",
  "kmRodados": "142 km"
}
```

### 📅 Corridas por Dia
**URL:** `GET http://localhost:8080/dashboard/corridas-por-dia/{usuarioId}?inicio=2024-01-01&fim=2024-01-31`

**Resposta:**
```json
[
  {
    "data": "2024-01-15",
    "corridas": 12,
    "plataforma": "Uber"
  },
  {
    "data": "2024-01-16",
    "corridas": 8,
    "plataforma": "99"
  }
]
```

## ⚙️ Configuração

### 1. Pré-requisitos
- Java 17 ou superior
- MySQL 8.0+
- Maven 3.8+

### 2. Banco de Dados
```sql
CREATE DATABASE motoapp;
USE motoapp;

CREATE TABLE usuario (
    usuario_id BINARY(16) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE registro (
    registro_id BINARY(16) PRIMARY KEY,
    data DATE NOT NULL,
    plataforma VARCHAR(255) NOT NULL,
    plataforma_outro VARCHAR(255),
    horas_trabalhadas INT,
    corridas_realizadas INT,
    valor_bruto DECIMAL(10,2),
    km_inicial INT,
    km_final INT,
    combustivel DECIMAL(10,2),
    alimentacao DECIMAL(10,2),
    gastos_adicionais DECIMAL(10,2),
    observacao TEXT,
    lucro DECIMAL(10,2) NOT NULL,
    usuario_id BINARY(16) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE
);
```

### 3. Configuração da Aplicação
**Crie o arquivo `src/main/resources/application.yml`:**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/motoapp
    username: seu_usuario
    password: sua_senha
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect

server:
  port: 8080

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.springframework.web: INFO
```

### 4. Execução
```bash
# Clone o repositório
git clone https://github.com/felipeburidev/MotoAppAPI.git
cd MotoAppAPI

# Compile e execute
mvn clean package
java -jar target/MotoAppApi-0.0.1-SNAPSHOT.jar

# Ou execute diretamente
mvn spring-boot:run
```

## 📁 Estrutura do Projeto
```
MotoAppAPI/
├── src/main/java/MOTOAPPAPI/MotoAppApi/
│   ├── controller/          # Controladores REST
│   │   ├── UsuarioController.java
│   │   ├── RegistroController.java
│   │   └── DashboardController.java
│   ├── model/              # Entidades JPA
│   │   ├── Usuario.java
│   │   └── Registro.java
│   ├── repository/         # Interfaces Spring Data JPA
│   │   ├── UsuarioRepository.java
│   │   └── RegistroRepository.java
│   ├── service/           # Lógica de negócio
│   │   ├── UsuarioService.java
│   │   └── RegistroService.java
│   └── MotoAppApiApplication.java
├── src/main/resources/
│   ├── application.yml.example  # Template de configuração
│   └── application.yml          # Configuração local (não versionado)
├── pom.xml                     # Dependências Maven
└── README.md                  # Documentação
```

## 🔧 Funcionalidades Técnicas

### 🛡️ Segurança
- Hash de senhas com BCrypt
- Validação de email único
- Proteção contra injeção SQL
- .gitignore configurado para proteger credenciais

### ⚡ Performance
- UUID armazenado como BINARY(16) no MySQL
- Consultas otimizadas com índices
- Cálculos realizados no banco quando possível

### 🚨 Tratamento de Erros
- Mensagens de erro claras em português
- Códigos HTTP apropriados (200, 400, 404, 500)
- Validação de dados em tempo real

## 🌐 Frontend em Desenvolvimento
Um frontend responsivo está sendo desenvolvido para complementar esta API, proporcionando uma interface intuitiva para motoristas gerenciarem suas finanças.

## 🤝 Contribuição
1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request


## 👨‍💻 Autor
**Felipe Buri** - [@felipeburidev](https://github.com/felipeburidev)

## 🙏 Agradecimentos
- Equipe Spring pelo framework incrível
- Comunidade Java brasileira
- Todos os motoristas que testaram e deram feedback

---
✨ *"Organizando a vida financeira de motoristas, uma corrida por vez!"* ✨
