#!/bin/bash

# 在本地项目根执行mvn clean package -DskipTests
mvn clean package -DskipTests

# 切换到WEB模块
cd family-admin

# 执行docker打包
mvn docker:build