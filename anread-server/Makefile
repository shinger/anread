# 生成接口文档

html: anread-server-user@html-doc anread-server-admin@html-doc anread-server-book@html-doc anread-server-filesystem@html-doc
torna: anread-server-user@torna anread-server-admin@torna anread-server-book@torna anread-server-filesystem@torna


# 生成HTML文档
anread-server-user@html-doc:
	mvn smart-doc:html -Dfile.encoding=UTF-8  -pl :anread-server-user -am

anread-server-admin@html-doc:
	mvn smart-doc:html -Dfile.encoding=UTF-8  -pl :anread-server-admin -am

anread-server-book@html-doc:
	mvn smart-doc:html -Dfile.encoding=UTF-8  -pl :anread-server-book -am

anread-server-filesystem@html-doc:
	mvn smart-doc:html -Dfile.encoding=UTF-8  -pl :anread-server-filesystem -am


# 推送到Torna
anread-server-user@torna:
	mvn smart-doc:torna-rest -Dfile.encoding=UTF-8  -pl :anread-server-user -am

anread-server-admin@torna:
	mvn smart-doc:torna-rest -Dfile.encoding=UTF-8  -pl :anread-server-admin -am

anread-server-book@torna:
	mvn smart-doc:torna-rest -Dfile.encoding=UTF-8  -pl :anread-server-book -am

anread-server-filesystem@torna:
	mvn smart-doc:torna-rest -Dfile.encoding=UTF-8  -pl :anread-server-filesystem -am


