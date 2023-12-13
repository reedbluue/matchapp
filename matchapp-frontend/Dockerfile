FROM alpine:latest
RUN apk add nodejs npm

WORKDIR /app

COPY . .

RUN npm install && npm run build && npx vite --host --port ${PORT}

EXPOSE ${PORT}