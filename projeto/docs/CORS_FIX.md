## CORS / double-slash issue — diagnosis and fix

Resumo rápido
-------------

- Problema observado: requests do front-end (origem `https://...-5500.app.github.dev`) para o back-end (`https://...-8080.app.github.dev`) estavam falhando com erros CORS e retornando 404: "No static resource users."
- Causas principais encontradas:
  1. O front-end configurou `BASE_URL` com uma barra final (`...app.github.dev/`) e construiu URLs como `${BASE_URL}/users` — gerando `//users` (double-slash). Isso faz o servidor/proxy rotas se comportarem inesperadamente e resultou em um handler de recursos estáticos tentando servir `users` (404).
  2. Enquanto isso, o preflight CORS falhava porque a resposta 404 para a URL malformada não continha cabeçalhos CORS adequados — daí o navegador bloqueou a requisição.

O que consertei
----------------

1. Front-end: removi a barra final em `BASE_URL`.
   - Arquivo: `src/main/resources/static/index.html`
   - Antes: `const BASE_URL = 'https://...-8080.app.github.dev/';` (produzia //users)
   - Agora: `const BASE_URL = 'https://...-8080.app.github.dev';`

2. Back-end global CORS: tornei a configuração mais explícita e robusta para o cenário Codespaces / app.github.dev.
   - Arquivo: `src/main/java/com/nelson/projeto/config/CorsConfig.java`
   - O que foi atualizado: adicionadas regras `addAllowedOriginPattern` para `https://*.app.github.dev` e para alguns origins locais (localhost), permitidos headers e métodos comuns, habilitados credentials com `setAllowCredentials(true)` e aumento de `maxAge`.

Como testar localmente e em Codespaces
------------------------------------

1. Abra a aplicação back-end em Codespaces (ou seu ambiente). Garanta que o serviço esteja acessível (ex: `https://urban-cod-...-8080.app.github.dev`).
2. Abra a UI (front-end) no outro subdomínio Codespaces (ex: `https://urban-cod-...-5500.app.github.dev`) e use a aplicação normalmente.

Simular CORS usando curl
------------------------
Você pode simular uma preflight e a requisição com estas chamadas (ajuste a URL):

Preflight (OPTIONS):

```bash
curl -i -X OPTIONS \
  -H "Origin: https://urban-cod-r47rw7rwwr5jfv9j-5500.app.github.dev" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  https://urban-cod-r47rw7rwwr5jfv9j-8080.app.github.dev/users
```

Requisição normal (GET):

```bash
curl -i -H "Origin: https://urban-cod-r47rw7rwwr5jfv9j-5500.app.github.dev" \
  https://urban-cod-r47rw7rwwr5jfv9j-8080.app.github.dev/users
```

Se o back-end estiver configurado corretamente você verá nos headers de resposta `Access-Control-Allow-Origin` e possivelmente outros headers CORS (Access-Control-Allow-Methods, etc.).

Notas sobre testes de CI/local
-----------------------------
- Neste workspace, não consegui executar `mvn test` porque o ambiente de execução não tem Java 17 (erro: "release version 17 not supported"). Para executar os testes, rode o build em uma máquina com JDK 17 ou use o wrapper `./mvnw` com permissões corretas.

Próximos passos recomendados
---------------------------
- Se você usa cookies ou Authorization headers, verifique que os requests do front-end estejam enviando `credentials: 'include'` ao usar fetch e confirme no servidor que `setAllowCredentials(true)` está configurado (já está nesta correção).
- Para ambientes de produção, restrinja os origins explicitamente (evite usar padrões excessivamente permissivos em produção).

Se quiser, eu posso:
- Ajustar o front-end para concatenar URLs com segurança (evitar double slashes automaticamente),
- Rodar ou ajustar a configuração do container/CI para usar JDK 17 e então executar os testes.

---
arquivo adicionado pelo assistente para documentar as mudanças e testes.
