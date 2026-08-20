# NaPortaApp - V2

![alt text](logo.png)

- Software web/mobile de delivery interno para gestão/controle de pedidos dos usuários do hotel, permitindos que os hóspedes realizem a solicitação de comidas, bebidas e outros serviços através de um QRCode que identifica o quarto, e respectivamente, o usuário solicitante.

> 🚧 **Status:** Em planejamento

## Estrutura do Repositório

- **API**: Armazena o código backend da aplicação;
- **WEB**: Armazena a interface web do cliente;
- **MOBILE**: Armazena a interface mobile do cliente.

## Stack Ferramental

- **Backend:** Kotlin, Spring Boot;
- **Frontend:** React;
- **Mobile:** React Native;
- **Tooling:** TypeScript, ESLint, Prettier.

## Documentação de Negócio

## Arquitetura

- API REST utilizando Spring Boot
- Autenticação baseada em JWT
- Autenticação automática via Refresh Token (JWT)
- Controle de acesso baseado em papéis (RBAC)
- Persistência com PostgreSQL
- Cache e Rate Limiting com Redis
- Validação de entrada com Zod
- H2 Database Drive

### Requisitos Funcionais (RF)

- [ ] O usuário deve poder ler um QRCode que identifica-o, acessando a plataforma (login);
- [ ] O usuário deve poder realizar pedidos (deve ser encaminhado para o Gateway de pagamento);
- [ ] O usuário deve poder visualizar despesas agrupadas por data e valor;
- [ ] O usuário deve poder visualizar a quantidade de despesas por categoria;
- [ ] O usuário deve poder visualizar suas informações de perfil;
- [ ] O usuário deve poder visualizar o histórico de todas as suas despesas;
- [ ] O usuário deve poder visualizar a quantidade de refeições registradas;
- [ ] O usuário deve poder visualizar a quantidade de refeições registradas no dia;
- [ ] O usuário deve poder visualizar o valor gasto do mês;
- [ ] O usuário deve poder visualizar o valor gasto do dia;
- [ ] O usuário deve poder filtrar seu histórico de despesas por período e por categoria;
- [ ] O funcionário/administrador deve poder cadastrar refeições;
- [ ] O funcionário/administrador deve poder cadastrar categorias;
- [ ] O funcionário/administrador deve poder desabilitar qualquer usuário;
- [ ] O funcionário/administrador deve poder cadastrar um usuário (quarto, status, data_criação, start_checkout, end_checkout );
- [ ] O administrador deve poder gerenciar as permissões entre: "member", "employee", "admin";
- [ ] O administrador deve poder cadastrar refeições;
- [ ] O administrador deve poder trocar permissão do usuário;
- [ ] O administrador deve poder deletar refeições;
- [ ] O administrador deve poder deletar categorias;
- [ ] O administrador deve poder visualizar todos os usuários;
- [ ] O usuário deve poder interagir com chatbot com IA (leitura da base de dados);

### Requisitos Não-Funcionais (RNF)

- [ ] A senha do usuário precisa estar em formato hash;
- [ ] Os dados da aplicação precisam estar persistidos em um banco H2 Database;
- [ ] Todas as listas de dados precisam estar paginadas com 15 itens por página; 
- [ ] O banco de dados deve utilizar UUID v7 para performance e identificação;
- [ ] O usuário deve ser identificado por um JWT (JSON Web Token) entre as requisições;
- [ ] Todos os usuários devem ser identificados pela permissão de "membro", "funcionário" ou "admin";
- [ ] O Redis deve ser utilizado para cache e rate limiting;
- [ ] O sistema deve possuir tratamento centralizado de erros;
- [ ] O administrador não pode visualizar senhas dos usuários.
- [ ] Todas as rotas precisam estar documentadas utilizando o swagger;
- [ ] Os testes unitários devem ter cobertura de 70% (coverage);
- [ ] O sistema deve implementar refresh token para renovação de autenticação;
- [ ] A API deve registrar logs de erros e auditoria;

### Regras de Negócio (RN)

- [ ] O usuário cadastradado pelo func/admin não deve poder se cadastrar com e-mail duplicado;
- [ ] O administrador não deve poder cadastrar categorias com o mesmo título;
- [ ] O administrador não deve poder deletar uma categoria com gastos vinculados;
- [ ] O token de reset de senha deve expirar em 15 minutos e só pode ser usado uma vez;
- [ ] O usuário que solicitou renovação de senha não pode cadastrar a mesma senha novamente;
- [ ] Os usuários, por padrão, recebem o cargo (permissão) de "membro";
- [ ] O usuário não deve poder visualizar refeições de outros usuários;
- [ ] O administrador pode visualizar todos os usuários;
- [ ] Ao deletar uma conta, todas as despesas e categorias vinculadas devem ser deletadas em cascata.

### Funcionalidades (FT)

### Cobertura dos Testes Unitários

## Fluxograma de Desenvolvimento

## Estrutura do Banco de Dados

### Entidades

- [ ] Users

### Relacionamentos

- Um usuário possui um quarto e possui várias refeições
- Uma categoria pode possuir várias refeições
- Uma refeição pertence a uma categoria

## Comandos para Iniciar o Projeto

### API

#### Desenvolvimento

#### Start Frontend
- `pnpm run dev` — iniciar projeto para abrir no navegador
- `pnpm run build` — iniciar o build do projeto
- `pnpm run lint` — iniciar o lint pra corrigir o código

#### Testes
- `pnpm test` — executa os testes uma vez
- `pnpm test:watch` — executa os testes em modo watch
- `pnpm test:coverage` — executa os testes com cobertura
- `pnpm test:ui` — abre a interface visual dos testes

#### Banco de Dados

#### Qualidade de Código
- `pnpm lint` — verifica problemas de lint
- `pnpm lint:fix` — corrige problemas de lint automaticamente
- `pnpm format` — formata o código com Prettier

## Comandos de Desenvolvimento

### Backend

### Frontend

- pnpm add tailwindcss @tailwindcss/vite
  [instalação do tailwindcss no projeto, sendo necessário realizar modificações em vite.config.ts para aceitar "@" como raiz]

- pnpm i @types/node -D
  [para que o vite consiga trabalhar com aliases de importação no momento de build]

- pnpm dlx shadcn-ui@latest init
- npx shadcn@latest init
  [instalação da biblioteca de componentes shadcn-ui e uso do cli para iniciar arquivo de configurações]

- pnpm install localforage match-sorter sort-by
  [instalação do react router dom mais dependencias de desenvolvimento]

- pnpm create @eslint/config@latest
  [gera a base do arquivo de configuração e instala as dependências iniciais do ESLint e TypeScript]

- pnpm install -D prettier eslint-config-prettier prettier-plugin-tailwindcss eslint-plugin-react eslint-plugin-react-hooks eslint-plugin-react-refresh
  [instala apenas o que o CLI do ESLint não instala: Prettier, suporte para Tailwind e plugins específicos de React Hooks/Refresh]

- configurar "eslint.config.ts" com plugins de React e Prettier
  [é necessário configurar o arquivo (que pode ser mantido em .ts nas versões atuais) para injetar os plugins "react-hooks", "react-refresh" e o "eslint-config-prettier". Isso garante a aplicação das regras do React 19 e evita que o ESLint brigue com a formatação do Prettier]

```Typescript
import js from "@eslint/js";
import globals from "globals";
import tseslint from "typescript-eslint";
import pluginReact from "eslint-plugin-react";
import reactHooks from "eslint-plugin-react-hooks";
import reactRefresh from "eslint-plugin-react-refresh";
import prettierConfig from "eslint-config-prettier";

export default tseslint.config(
  { ignores: ["dist", "build", "node_modules"] },
  js.configs.recommended,
  ...tseslint.configs.recommended,
  {
    files: ["**/*.{ts,tsx}"],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
    plugins: {
      react: pluginReact,
      "react-hooks": reactHooks,
      "react-refresh": reactRefresh,
    },
    rules: {
      ...pluginReact.configs.flat.recommended.rules,
      ...reactHooks.configs.recommended.rules,
      "react/react-in-jsx-scope": "off",
      "react-refresh/only-export-components": [
        "warn",
        { allowConstantExport: true },
      ],
    },
  },
  prettierConfig,
);
```

- criar ".prettierrc.mjs" na raiz do projeto
  [arquivo de configuração do Prettier usando padrão ESM. Inclui o "prettier-plugin-tailwindcss" para habilitar a ordenação automática das classes do Tailwind no atributo className ao salvar o arquivo]

```Javascript
/** @type {import("prettier").Config} */
export default {
  semi: true,
  singleQuote: true,
  trailingComma: 'all',
  printWidth: 80,
  tabWidth: 2,
  plugins: ['prettier-plugin-tailwindcss'],
};
```

- criar ".prettierignore" na raiz do projeto
  [arquivo para o Prettier ignorar diretórios como "node_modules", "dist" e "build", evitando processamento desnecessário e lentidão durante a formatação automática]

```
node_modules
dist
build
```

- configurar "resolve.alias" no "vite.config.ts"
  [utiliza o módulo "path" do Node.js para mapear o caractere "@" diretamente para a pasta "src", permitindo resolver importações absolutas de forma manual sem dependências extras]

```Typescript
export default defineConfig({
  plugins: [
    tailwindcss(),
    react(),
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  }
});
```

- adicionar "baseUrl" e "paths" no "tsconfig.json"
  [configuração obrigatória para que o TypeScript e o VS Code reconheçam o alias "@", habilitando o preenchimento automático (autocomplete) e a navegação entre arquivos]

```Typescript
"compilerOptions": {
    "baseUrl": ".",
    "paths": {
      "@/*": [
        "./src/*"
      ]
    }
  }
```

- configurar ".vscode/settings.json" na pasta do projeto
  [configuração de workspace que define o Prettier como formatador padrão e ativa o "source.fixAll.eslint". Isso faz com que o VS Code corrija erros de lint e organize as classes do Tailwind automaticamente ao salvar (Ctrl + S)]

```Typescript
{
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": "explicit"
  },
  "eslint.validate": [
    "javascript",
    "javascriptreact",
    "typescript",
    "typescriptreact"
  ],
  "[typescriptreact]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  },
}
```

- pnpm i -D eslint-plugin-simple-import-sort
  [organiza importações dos arquivos no projeto]

```Typescript
  plugins: {
    react: pluginReact,
    "react-hooks": reactHooks,
    "react-refresh": reactRefresh,
    "simple-import-sort": simpleImportSort,
  },
  rules: {
      "simple-import-sort/imports": "error",
      "simple-import-sort/exports": "error",
    },
```

- pnpm i react-hook-form zod @hookform/resolvers
  [lidar com formulário no react utilizando validação com o zod e habilitando integração entre as ferramentas com resolvers]

- pnpm install sonner
  [biblioteca de componente toast já estilizado e pronto para uso]

- pnpm i recharts
  [biblioteca para construção de gráficos do dashboard]

- pnpm install axios
  [biblioteca base para realizar requisições]

- pnpm install zod
  [biblioteca para validar a entrada de dados na aplicação]

- pnpm install @tanstack/react-query
  [biblioteca para realizar requisições do front ao backend (api)]
