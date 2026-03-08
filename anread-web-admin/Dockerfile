FROM node:22 
WORKDIR /usr/local/anread/anread-web-admin

COPY ./package.json ./
RUN npm install
COPY ./index.html ./vite.config.js ./
COPY ./src ./src
COPY ./public ./public

EXPOSE 5174
CMD ["npm", "run", "dev"]
