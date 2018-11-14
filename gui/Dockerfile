# STEP 1 build static website
FROM node:alpine as builder
RUN apk update && apk add --no-cache make git

WORKDIR /usr/src/app

## Setup arguments
ARG PORT
ENV PORT=$PORT

## Install angular CLI
RUN npm install -g @angular/cli

## Copy dependency information
COPY package.json package-lock.json /usr/src/app/

## Copy project files
COPY . /usr/src/app

## Install dependencies and compile angular project
RUN npm install && npm run build --prod

# STEP 2 build nginx with compiled project
FROM nginx:alpine

## Remove default nginx html files
RUN rm -rf /usr/share/nginx/html/*

## Copy from 'builder' to nginx html folder
COPY --from=builder /usr/src/app/dist/gui /usr/share/nginx/html
## Update nginx config (Prevents 404 errors, when there should not be any)
COPY --from=builder /usr/src/app/nginx.conf /etc/nginx/nginx.conf

EXPOSE $PORT
CMD ["nginx", "-g", "daemon off;"]
