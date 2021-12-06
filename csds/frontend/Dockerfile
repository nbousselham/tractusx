#### Stage 1: Build the vue application
FROM node:lts-alpine as build

# Configure the main working directory inside the docker image. 
# This is the base directory used in any further RUN, COPY, and ENTRYPOINT 
# commands.

WORKDIR /app

# Copy the package.json as well as the package-lock.json and install 
# the dependencies. This is a separate step so the dependencies 
# will be cached unless changes to one of those two files 
# are made.              
COPY package.json ./
RUN npm install

# Copy the main application
COPY ./ .

# Arguments
#ARG VUE_APP_API_HOST_URL
#ENV VUE_APP_API_HOST_URL=${VUE_APP_API_HOST_URL}

# Build the application
RUN npm run build

#### Stage 2: Serve the application from Nginx 
FROM nginx:1.19.7-alpine 

RUN mkdir /app

# Copy the vue build from Stage 1
COPY --from=build /app/dist /app

# Copy our custom nginx config
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

ENTRYPOINT ["nginx","-g","daemon off;"]
