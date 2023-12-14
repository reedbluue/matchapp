FROM nginx:alpine
RUN apk add nodejs npm

COPY . .

RUN npm install && npm run build && cp -r dist/* /usr/share/nginx/html && \
    cp nginx.conf /etc/nginx/nginx.conf

EXPOSE ${PORT}