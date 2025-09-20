# =======================================================
# ESTÁGIO 1: O "BUILDER" - Constrói nossa aplicação React
# =======================================================
# Usamos uma imagem Node.js leve (baseada em Alpine Linux)
FROM node:20-alpine AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Otimização de cache do Docker:
# Copia primeiro os arquivos de dependência e as instala.
# Esta camada só será refeita se o package.json mudar.
COPY package.json package-lock.json ./
RUN npm install

# Agora copia o resto do código-fonte da aplicação.
COPY . .

# Roda o script de build para gerar os arquivos estáticos de produção.
RUN npm run build

# =======================================================
# ESTÁGIO 2: O "SERVER" - Serve os arquivos estáticos
# =======================================================
# Usamos uma imagem Nginx super leve para servir nosso site.
FROM nginx:stable-alpine

# Remove a configuração padrão do Nginx.
RUN rm /etc/nginx/conf.d/default.conf

# Copia nossa configuração customizada do Nginx (que criaremos a seguir).
COPY nginx.conf /etc/nginx/conf.d/default.conf

# A mágica do multi-stage: Copia APENAS os arquivos de produção
# do estágio 'builder' para o diretório padrão do Nginx.
COPY --from=builder /app/dist /usr/share/nginx/html

# Expõe a porta 80, que é a porta padrão do Nginx.
EXPOSE 80

# O comando para iniciar o servidor Nginx.
CMD ["nginx", "-g", "daemon off;"]