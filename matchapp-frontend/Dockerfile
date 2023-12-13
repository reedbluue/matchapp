FROM node:lts-alpine
WORKDIR /app
COPY . .
RUN npm install && npm install --global http-server
RUN npm run build
EXPOSE ${PORT}
CMD http-server -p 80 --proxy ${PROXY_URL}:80? ./dist