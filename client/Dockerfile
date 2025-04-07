#A docker hub image which is used to run node related code
FROM node:17-alpine3.15

#Set working directory
WORKDIR /app

# copying from [host machine] [container] (first path is relative to the folder where docker is located and second path is the root folder inside the container)
COPY package*.json  ./
RUN npm install

#Copy source files
COPY . .

#Exposing vite's default port
EXPOSE 5173

#Start vite dev server
CMD ["npm","run","dev","--","--host"]