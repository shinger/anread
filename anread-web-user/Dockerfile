FROM node:22 
WORKDIR /usr/local/anread/anread-web-user

COPY package.json ./
COPY lib ./lib
COPY src ./src
COPY public ./public
COPY index.html jsconfig.json vite.config.js ./

# 将本地包复制到 node_modules
RUN mkdir -p node_modules/epubjs node_modules/marks-pane && \
    cp -r lib/epubjs/* node_modules/epubjs/ && \
    cp -r lib/marks-pane/* node_modules/marks-pane/

# 替换 package.json 中的 file: 依赖，避免 npm 重新解析
RUN sed -i 's|"epubjs": "file:lib/epubjs"|"epubjs": "*"|g' package.json && \
    sed -i 's|"marks-pane": "file:lib/marks-pane"|"marks-pane": "*"|g' package.json

# 安装其余依赖
RUN rm -f package-lock.json && \
    npm install

EXPOSE 5173
CMD ["npm", "run", "dev"]